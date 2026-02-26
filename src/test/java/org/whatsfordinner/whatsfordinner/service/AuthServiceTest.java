package org.whatsfordinner.whatsfordinner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.whatsfordinner.whatsfordinner.dto.LoginRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.RegisterRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.UserResponseDTO;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.repository.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AllergyRepository allergyRepository;
    @Mock private UserAllergyRepository userAllergyRepository;
    @Mock private UserPreferencesRepository userPreferencesRepository;

    @InjectMocks private AuthService authService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .email("existing@test.com")
                .passwordHash("hashed_password")
                .hasCompletedOnboarding(false)
                .build();
    }

    // Register

    @Test
    void register_success_returnsUserResponseDTO() {
        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        User saved = User.builder().email("new@test.com").passwordHash("hashed")
                .hasCompletedOnboarding(false).build();
        when(userRepository.save(any())).thenReturn(saved);

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .email("new@test.com").password("password123").build();

        UserResponseDTO result = authService.register(request);

        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getHasCompletedOnboarding()).isFalse();
    }

    @Test
    void register_throwsException_whenEmailAlreadyInUse() {
        when(userRepository.findByEmail("existing@test.com"))
                .thenReturn(Optional.of(existingUser));

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .email("existing@test.com").password("password123").build();

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already in use");
    }

    @Test
    void register_encodesPassword() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("mypassword")).thenReturn("encoded_password");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        authService.register(RegisterRequestDTO.builder()
                .email("a@b.com").password("mypassword").build());

        verify(passwordEncoder).encode("mypassword");
    }

    // Login

    @Test
    void login_success_returnsTokenAndOnboardingStatus() {
        when(userRepository.findByEmail("existing@test.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken("existing@test.com")).thenReturn("jwt_token");

        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("existing@test.com").password("password123").build();

        Map<String, Object> result = authService.login(request);

        assertThat(result.get("token")).isEqualTo("jwt_token");
        assertThat(result.get("hasCompletedOnboarding")).isEqualTo(false);
    }

    @Test
    void login_throwsException_whenUserNotFound() {
        when(userRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(
                LoginRequestDTO.builder().email("nobody@test.com").password("pass").build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void login_throwsException_whenPasswordIncorrect() {
        when(userRepository.findByEmail("existing@test.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(
                LoginRequestDTO.builder().email("existing@test.com").password("wrongpassword").build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }

    @Test
    void login_returnsHasCompletedOnboardingTrue_whenUserCompletedOnboarding() {
        User completedUser = User.builder()
                .email("done@test.com").passwordHash("hash")
                .hasCompletedOnboarding(true).build();
        when(userRepository.findByEmail("done@test.com")).thenReturn(Optional.of(completedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("token");

        Map<String, Object> result = authService.login(
                LoginRequestDTO.builder().email("done@test.com").password("pass").build());

        assertThat(result.get("hasCompletedOnboarding")).isEqualTo(true);
    }
}