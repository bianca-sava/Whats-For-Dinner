package org.whatsfordinner.whatsfordinner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.whatsfordinner.whatsfordinner.dto.AddToFridgeRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.FridgeItemResponseDTO;
import org.whatsfordinner.whatsfordinner.exception.NotFoundException;
import org.whatsfordinner.whatsfordinner.model.*;
import org.whatsfordinner.whatsfordinner.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {

    @Mock private UserFridgeRepository userFridgeRepository;
    @Mock private UserRepository userRepository;
    @Mock private IngredientRepository ingredientRepository;
    @Mock private AiReceiptScannerService aiReceiptScannerService;

    @InjectMocks private FridgeService fridgeService;

    private UUID userId;
    private User user;
    private Ingredient pasta;

    @BeforeEach
    void setUp() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        userId = UUID.randomUUID();
        // Build user AND manually set id (Lombok Builder doesn't trigger @GeneratedValue)
        user = User.builder().email("test@test.com").build();
        user.setId(userId);

        pasta = Ingredient.builder().id(1L).name("pasta")
                .category(Ingredient.Category.GRAIN)
                .defaultUnit(Ingredient.Unit.GRAMS)
                .isPantryItem(false).build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
    }

    @Test
    void addToFridge_success_savesAndReturnsItem() {
        when(userFridgeRepository.findByUserAndIngredientId(user, 1L)).thenReturn(Optional.empty());
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(pasta));
        UserFridge saved = UserFridge.builder().id(10L).user(user).ingredient(pasta).build();
        when(userFridgeRepository.save(any())).thenReturn(saved);

        FridgeItemResponseDTO result = fridgeService.addToFridge(
                AddToFridgeRequestDTO.builder().ingredientId(1L).build());

        assertThat(result.getIngredientId()).isEqualTo(1L);
        assertThat(result.getIngredientName()).isEqualTo("pasta");
    }

    @Test
    void addToFridge_returnsExistingItem_whenAlreadyInFridge() {
        UserFridge existing = UserFridge.builder().id(5L).user(user).ingredient(pasta).build();
        when(userFridgeRepository.findByUserAndIngredientId(user, 1L))
                .thenReturn(Optional.of(existing));

        FridgeItemResponseDTO result = fridgeService.addToFridge(
                AddToFridgeRequestDTO.builder().ingredientId(1L).build());

        assertThat(result.getId()).isEqualTo(5L);
        verify(userFridgeRepository, never()).save(any());
    }

    @Test
    void addToFridge_throwsNotFoundException_whenIngredientNotFound() {
        when(userFridgeRepository.findByUserAndIngredientId(user, 99L))
                .thenReturn(Optional.empty());
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fridgeService.addToFridge(
                AddToFridgeRequestDTO.builder().ingredientId(99L).build()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Ingredient not found");
    }

    @Test
    void getFridgeItems_returnsAllItemsForUser() {
        UserFridge item = UserFridge.builder().id(1L).user(user).ingredient(pasta).build();
        when(userFridgeRepository.findByUser(user)).thenReturn(List.of(item));

        List<FridgeItemResponseDTO> result = fridgeService.getFridgeItems();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIngredientName()).isEqualTo("pasta");
    }

    @Test
    void getFridgeItems_filtersOutNullIngredients() {
        UserFridge validItem = UserFridge.builder().id(1L).user(user).ingredient(pasta).build();
        UserFridge nullItem = UserFridge.builder().id(2L).user(user).ingredient(null).build();
        when(userFridgeRepository.findByUser(user)).thenReturn(List.of(validItem, nullItem));

        List<FridgeItemResponseDTO> result = fridgeService.getFridgeItems();

        assertThat(result).hasSize(1);
    }

    @Test
    void removeFromFridge_success_deletesItem() {
        // fridgeItem belongs to the same user (same object reference → same UUID)
        UserFridge item = UserFridge.builder().id(1L).user(user).ingredient(pasta).build();
        when(userFridgeRepository.findById(1L)).thenReturn(Optional.of(item));

        fridgeService.removeFromFridge(1L);

        verify(userFridgeRepository).deleteById(1L);
    }

    @Test
    void removeFromFridge_throwsNotFoundException_whenItemNotFound() {
        when(userFridgeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fridgeService.removeFromFridge(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Fridge item not found");
    }

    @Test
    void removeFromFridge_throwsException_whenItemBelongsToAnotherUser() {
        // otherUser must also have a UUID set explicitly, different from userId
        User otherUser = User.builder().email("other@test.com").build();
        otherUser.setId(UUID.randomUUID()); // different UUID → .equals() returns false

        UserFridge item = UserFridge.builder().id(1L).user(otherUser).ingredient(pasta).build();
        when(userFridgeRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> fridgeService.removeFromFridge(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unauthorized");
    }

    @Test
    void addMultipleToFridge_addsOnlyNewIngredients() {
        Ingredient egg = Ingredient.builder().id(2L).name("egg")
                .category(Ingredient.Category.OTHER)
                .defaultUnit(Ingredient.Unit.PIECES)
                .isPantryItem(false).build();

        UserFridge existingPasta = UserFridge.builder().id(1L).user(user).ingredient(pasta).build();
        when(userFridgeRepository.findByUserAndIngredientId(user, 1L))
                .thenReturn(Optional.of(existingPasta));
        when(userFridgeRepository.findByUserAndIngredientId(user, 2L))
                .thenReturn(Optional.empty());
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(egg));
        UserFridge savedEgg = UserFridge.builder().id(2L).user(user).ingredient(egg).build();
        when(userFridgeRepository.save(any())).thenReturn(savedEgg);

        List<FridgeItemResponseDTO> result = fridgeService.addMultipleToFridge(List.of(1L, 2L));

        assertThat(result).hasSize(2);
        // pasta already existed — save called only once for egg
        verify(userFridgeRepository, times(1)).save(any());
    }
}