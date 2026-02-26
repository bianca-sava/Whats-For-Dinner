package org.whatsfordinner.whatsfordinner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whatsfordinner.whatsfordinner.dto.LoginRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.OnboardingRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.RegisterRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.UserResponseDTO;
import org.whatsfordinner.whatsfordinner.exception.NotFoundException;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserAllergy;
import org.whatsfordinner.whatsfordinner.model.UserPreferences;
import org.whatsfordinner.whatsfordinner.repository.AllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.UserAllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.UserPreferencesRepository;
import org.whatsfordinner.whatsfordinner.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AllergyRepository allergyRepository;
    private final UserAllergyRepository userAllergyRepository;
    private final UserPreferencesRepository userPreferencesRepository;

    public UserResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .hasCompletedOnboarding(false)
                .build();

        User saved = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .hasCompletedOnboarding(saved.getHasCompletedOnboarding())
                .build();
    }

    public Map<String, Object> login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return Map.of(
                "token", token,
                "hasCompletedOnboarding", Boolean.TRUE.equals(user.getHasCompletedOnboarding())
        );
    }

    @Transactional
    public void completeOnboarding(OnboardingRequestDTO request) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDefaultServings(request.getDefaultServings());
        user.setHasCompletedOnboarding(true);
        userRepository.save(user);

        UserPreferences preferences = userPreferencesRepository.findByUser(user)
                .orElse(UserPreferences.builder().user(user).build());
        preferences.setIsVegetarian(request.getIsVegetarian());
        preferences.setIsVegan(request.getIsVegan());
        userPreferencesRepository.save(preferences);

        userAllergyRepository.deleteAll(userAllergyRepository.findByUser(user));

        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            List<UserAllergy> userAllergies = request.getAllergyIds().stream()
                    .map(id -> allergyRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Allergy not found: " + id)))
                    .map(allergy -> UserAllergy.builder()
                            .user(user)
                            .allergy(allergy)
                            .build())
                    .toList();
            userAllergyRepository.saveAll(userAllergies);
        }
    }
}