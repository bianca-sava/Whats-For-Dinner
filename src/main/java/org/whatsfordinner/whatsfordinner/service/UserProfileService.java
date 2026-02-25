package org.whatsfordinner.whatsfordinner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.whatsfordinner.whatsfordinner.dto.AddAllergyRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.UserResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.AllergyResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.UserPreferencesDTO;
import org.whatsfordinner.whatsfordinner.model.Allergy;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserAllergy;
import org.whatsfordinner.whatsfordinner.model.UserPreferences;
import org.whatsfordinner.whatsfordinner.repository.AllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.UserAllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.UserPreferencesRepository;
import org.whatsfordinner.whatsfordinner.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final AllergyRepository allergyRepository;
    private final UserAllergyRepository userAllergyRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponseDTO getMe() {
        User user = getCurrentUser();
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .hasCompletedOnboarding(user.getHasCompletedOnboarding())
                .firstName(user.getFirstName())
                .build();
    }

    public UserPreferencesDTO getPreferences() {
        User user = getCurrentUser();
        UserPreferences preferences = userPreferencesRepository.findByUser(user)
                .orElse(UserPreferences.builder()
                        .user(user)
                        .isVegetarian(false)
                        .isVegan(false)
                        .build());

        return UserPreferencesDTO.builder()
                .isVegetarian(preferences.getIsVegetarian())
                .isVegan(preferences.getIsVegan())
                .build();
    }

    public UserPreferencesDTO updatePreferences(UserPreferencesDTO dto) {
        User user = getCurrentUser();
        UserPreferences preferences = userPreferencesRepository.findByUser(user)
                .orElse(UserPreferences.builder()
                        .user(user)
                        .build());

        preferences.setIsVegetarian(dto.getIsVegetarian());
        preferences.setIsVegan(dto.getIsVegan());
        userPreferencesRepository.save(preferences);

        return dto;
    }

    public List<AllergyResponseDTO> getAllergies() {
        User user = getCurrentUser();
        return userAllergyRepository.findByUser(user)
                .stream()
                .map(ua -> AllergyResponseDTO.builder()
                        .id(ua.getAllergy().getId())
                        .name(ua.getAllergy().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public AllergyResponseDTO addAllergy(AddAllergyRequestDTO request) {
        User user = getCurrentUser();
        Allergy allergy = allergyRepository.findById(request.getAllergyId())
                .orElseThrow(() -> new RuntimeException("Allergy not found"));

        UserAllergy userAllergy = UserAllergy.builder()
                .user(user)
                .allergy(allergy)
                .build();

        userAllergyRepository.save(userAllergy);

        return AllergyResponseDTO.builder()
                .id(allergy.getId())
                .name(allergy.getName())
                .build();
    }

    public void removeAllergy(Long allergyId) {
        User user = getCurrentUser();
        userAllergyRepository.findByUserAndAllergyId(user, allergyId)
                .ifPresent(userAllergyRepository::delete);
    }
}