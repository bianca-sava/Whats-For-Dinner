package org.whatsfordinner.whatsfordinner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatsfordinner.whatsfordinner.dto.RecipeResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.RecipeSearchRequestDTO;
import org.whatsfordinner.whatsfordinner.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/search")
    public ResponseEntity<List<RecipeResponseDTO>> searchRecipes(@RequestBody RecipeSearchRequestDTO request) {
        return ResponseEntity.ok(recipeService.searchRecipes(request));
    }
}