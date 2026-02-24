package org.whatsfordinner.whatsfordinner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatsfordinner.whatsfordinner.dto.AddToFridgeRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.FridgeItemResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.ReceiptScanRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.ScannedIngredientDTO;
import org.whatsfordinner.whatsfordinner.service.FridgeService;

import java.util.List;

@RestController
@RequestMapping("/api/fridge")
@RequiredArgsConstructor
public class FridgeController {

    private final FridgeService fridgeService;

    @PostMapping("/scan")
    public ResponseEntity<List<ScannedIngredientDTO>> scanReceipt(@Valid @RequestBody ReceiptScanRequestDTO request) {
        return ResponseEntity.ok(fridgeService.scanReceipt(request));
    }

    @PostMapping
    public ResponseEntity<FridgeItemResponseDTO> addToFridge(@Valid @RequestBody AddToFridgeRequestDTO request) {
        return ResponseEntity.ok(fridgeService.addToFridge(request));
    }

    @GetMapping
    public ResponseEntity<List<FridgeItemResponseDTO>> getFridgeItems() {
        return ResponseEntity.ok(fridgeService.getFridgeItems());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromFridge(@PathVariable Long id) {
        fridgeService.removeFromFridge(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<FridgeItemResponseDTO>> addMultiple(@RequestBody List<Long> ingredientIds) {
        return ResponseEntity.ok(fridgeService.addMultipleToFridge(ingredientIds));
    }

}