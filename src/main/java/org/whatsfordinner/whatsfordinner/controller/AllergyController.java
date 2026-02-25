package org.whatsfordinner.whatsfordinner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatsfordinner.whatsfordinner.model.Allergy;
import org.whatsfordinner.whatsfordinner.repository.AllergyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/allergies")
@RequiredArgsConstructor
public class AllergyController {

    private final AllergyRepository allergyRepository;

    @GetMapping
    public ResponseEntity<List<Allergy>> getAllAllergies() {
        return ResponseEntity.ok(allergyRepository.findAll());
    }
}