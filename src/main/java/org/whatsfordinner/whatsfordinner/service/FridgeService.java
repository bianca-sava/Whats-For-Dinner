package org.whatsfordinner.whatsfordinner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.whatsfordinner.whatsfordinner.dto.AddToFridgeRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.FridgeItemResponseDTO;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserFridge;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;
import org.whatsfordinner.whatsfordinner.repository.UserFridgeRepository;
import org.whatsfordinner.whatsfordinner.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final UserFridgeRepository userFridgeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public FridgeItemResponseDTO addToFridge(AddToFridgeRequestDTO request) {
        User user = getCurrentUser();
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        UserFridge fridgeItem = UserFridge.builder()
                .user(user)
                .ingredient(ingredient)
                .quantity(request.getQuantity())
                .build();

        UserFridge saved = userFridgeRepository.save(fridgeItem);
        return mapToDTO(saved);
    }

    public List<FridgeItemResponseDTO> getFridgeItems() {
        User user = getCurrentUser();
        return userFridgeRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public void removeFromFridge(Long fridgeItemId) {
        User user = getCurrentUser();
        UserFridge fridgeItem = userFridgeRepository.findById(fridgeItemId)
                .orElseThrow(() -> new RuntimeException("Fridge item not found"));

        if (!fridgeItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        userFridgeRepository.deleteById(fridgeItemId);
    }

    private FridgeItemResponseDTO mapToDTO(UserFridge fridgeItem) {
        return FridgeItemResponseDTO.builder()
                .id(fridgeItem.getId())
                .ingredientName(fridgeItem.getIngredient().getName())
                .category(fridgeItem.getIngredient().getCategory())
                .unit(fridgeItem.getIngredient().getDefaultUnit())
                .quantity(fridgeItem.getQuantity())
                .build();
    }
}