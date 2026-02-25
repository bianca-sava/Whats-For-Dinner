package org.whatsfordinner.whatsfordinner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatsfordinner.whatsfordinner.dto.LoginRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.OnboardingRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.RegisterRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.UserResponseDTO;
import org.whatsfordinner.whatsfordinner.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/onboarding")
    public ResponseEntity<Void> completeOnboarding(@Valid @RequestBody OnboardingRequestDTO request) {
        authService.completeOnboarding(request);
        return ResponseEntity.ok().build();
    }
}