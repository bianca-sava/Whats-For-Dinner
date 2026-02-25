package org.whatsfordinner.whatsfordinner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatsfordinner.whatsfordinner.dto.AddAllergyRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.AllergyResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.UserPreferencesDTO;
import org.whatsfordinner.whatsfordinner.dto.UserResponseDTO;
import org.whatsfordinner.whatsfordinner.service.UserProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(userProfileService.getMe());
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferencesDTO> getPreferences() {
        return ResponseEntity.ok(userProfileService.getPreferences());
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserPreferencesDTO> updatePreferences(@RequestBody UserPreferencesDTO dto) {
        return ResponseEntity.ok(userProfileService.updatePreferences(dto));
    }

    @GetMapping("/allergies")
    public ResponseEntity<List<AllergyResponseDTO>> getAllergies() {
        return ResponseEntity.ok(userProfileService.getAllergies());
    }

    @PostMapping("/allergies")
    public ResponseEntity<AllergyResponseDTO> addAllergy(@Valid @RequestBody AddAllergyRequestDTO request) {
        return ResponseEntity.ok(userProfileService.addAllergy(request));
    }

    @DeleteMapping("/allergies/{allergyId}")
    public ResponseEntity<Void> removeAllergy(@PathVariable Long allergyId) {
        userProfileService.removeAllergy(allergyId);
        return ResponseEntity.noContent().build();
    }
}