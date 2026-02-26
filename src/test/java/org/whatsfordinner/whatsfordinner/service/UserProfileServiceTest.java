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
import org.whatsfordinner.whatsfordinner.dto.*;
import org.whatsfordinner.whatsfordinner.exception.NotFoundException;
import org.whatsfordinner.whatsfordinner.model.*;
import org.whatsfordinner.whatsfordinner.repository.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserPreferencesRepository userPreferencesRepository;
    @Mock private AllergyRepository allergyRepository;
    @Mock private UserAllergyRepository userAllergyRepository;

    @InjectMocks private UserProfileService userProfileService;

    private User user;

    @BeforeEach
    void setUp() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        user = User.builder()
                .email("test@test.com")
                .firstName("Ana")
                .defaultServings(2)
                .hasCompletedOnboarding(true)
                .build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
    }

    //getMe

    @Test
    void getMe_returnsCorrectUserInfo() {
        UserResponseDTO result = userProfileService.getMe();

        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getFirstName()).isEqualTo("Ana");
        assertThat(result.getDefaultServings()).isEqualTo(2);
        assertThat(result.getHasCompletedOnboarding()).isTrue();
    }

    // getPreferences

    @Test
    void getPreferences_returnsPreferences_whenExist() {
        UserPreferences prefs = UserPreferences.builder()
                .user(user).isVegetarian(true).isVegan(false).build();
        when(userPreferencesRepository.findByUser(user)).thenReturn(Optional.of(prefs));

        UserPreferencesDTO result = userProfileService.getPreferences();

        assertThat(result.getIsVegetarian()).isTrue();
        assertThat(result.getIsVegan()).isFalse();
    }

    @Test
    void getPreferences_returnsDefaults_whenNoPreferencesExist() {
        when(userPreferencesRepository.findByUser(user)).thenReturn(Optional.empty());

        UserPreferencesDTO result = userProfileService.getPreferences();

        assertThat(result.getIsVegetarian()).isFalse();
        assertThat(result.getIsVegan()).isFalse();
    }

    // updatePreferences

    @Test
    void updatePreferences_savesAndReturnsUpdatedPreferences() {
        when(userPreferencesRepository.findByUser(user)).thenReturn(Optional.empty());
        when(userPreferencesRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserPreferencesDTO dto = UserPreferencesDTO.builder()
                .isVegetarian(true).isVegan(true).build();

        UserPreferencesDTO result = userProfileService.updatePreferences(dto);

        assertThat(result.getIsVegetarian()).isTrue();
        assertThat(result.getIsVegan()).isTrue();
        verify(userPreferencesRepository).save(any());
    }

    //getAllergies

    @Test
    void getAllergies_returnsUserAllergies() {
        Allergy gluten = Allergy.builder().id(1L).name("gluten").build();
        UserAllergy ua = UserAllergy.builder().user(user).allergy(gluten).build();
        when(userAllergyRepository.findByUser(user)).thenReturn(List.of(ua));

        List<AllergyResponseDTO> result = userProfileService.getAllergies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("gluten");
    }

    @Test
    void getAllergies_returnsEmpty_whenNoAllergies() {
        when(userAllergyRepository.findByUser(user)).thenReturn(List.of());

        List<AllergyResponseDTO> result = userProfileService.getAllergies();

        assertThat(result).isEmpty();
    }

    //addAllergy

    @Test
    void addAllergy_savesAndReturnsAllergy() {
        Allergy nuts = Allergy.builder().id(2L).name("nuts").build();
        when(allergyRepository.findById(2L)).thenReturn(Optional.of(nuts));
        when(userAllergyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AllergyResponseDTO result = userProfileService.addAllergy(
                AddAllergyRequestDTO.builder().allergyId(2L).build());

        assertThat(result.getName()).isEqualTo("nuts");
        assertThat(result.getId()).isEqualTo(2L);
    }

    @Test
    void addAllergy_throwsNotFoundException_whenAllergyNotFound() {
        when(allergyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.addAllergy(
                AddAllergyRequestDTO.builder().allergyId(99L).build()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Allergy not found");
    }

    //removeAllergy

    @Test
    void removeAllergy_deletesAllergy_whenExists() {
        Allergy nuts = Allergy.builder().id(2L).name("nuts").build();
        UserAllergy ua = UserAllergy.builder().user(user).allergy(nuts).build();
        when(userAllergyRepository.findByUserAndAllergyId(user, 2L))
                .thenReturn(Optional.of(ua));

        userProfileService.removeAllergy(2L);

        verify(userAllergyRepository).delete(ua);
    }

    @Test
    void removeAllergy_doesNothing_whenAllergyNotAssigned() {
        when(userAllergyRepository.findByUserAndAllergyId(user, 99L))
                .thenReturn(Optional.empty());

        userProfileService.removeAllergy(99L);

        verify(userAllergyRepository, never()).delete(any());
    }
}