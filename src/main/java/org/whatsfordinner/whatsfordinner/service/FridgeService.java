package org.whatsfordinner.whatsfordinner.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.whatsfordinner.whatsfordinner.dto.AddToFridgeRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.FridgeItemResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.ReceiptScanRequestDTO;
import org.whatsfordinner.whatsfordinner.dto.ScannedIngredientDTO;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserFridge;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;
import org.whatsfordinner.whatsfordinner.repository.UserFridgeRepository;
import org.whatsfordinner.whatsfordinner.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeService {

    private final UserFridgeRepository userFridgeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final AiReceiptScannerService aiReceiptScannerService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<ScannedIngredientDTO> scanReceipt(ReceiptScanRequestDTO request) {
        return aiReceiptScannerService.scanReceipt(request.getBase64Image());
    }

    public FridgeItemResponseDTO addToFridge(AddToFridgeRequestDTO request) {
        User user = getCurrentUser();
        Optional<UserFridge> existingItem = userFridgeRepository.findByUserAndIngredientId(user, request.getIngredientId());

        if (existingItem.isPresent()) {
            return mapToDTO(existingItem.get());
        }

        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        UserFridge fridgeItem = UserFridge.builder().user(user).ingredient(ingredient).build();
        UserFridge saved = userFridgeRepository.save(fridgeItem);
        return mapToDTO(saved);
    }


    public List<FridgeItemResponseDTO> getFridgeItems() {
        User user = getCurrentUser();
        return userFridgeRepository.findByUser(user)
                .stream()
                .filter(item -> item.getIngredient() != null)
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
                .ingredientId(fridgeItem.getIngredient().getId())
                .ingredientName(fridgeItem.getIngredient().getName())
                .category(fridgeItem.getIngredient().getCategory())
                .isPantryItem(fridgeItem.getIngredient().getIsPantryItem())
                .build();
    }

    public List<FridgeItemResponseDTO> addMultipleToFridge(List<Long> ingredientIds) {
        User user = getCurrentUser();

        return ingredientIds.stream()
                .map(id -> {
                    Optional<UserFridge> existing = userFridgeRepository.findByUserAndIngredientId(user, id);
                    if (existing.isPresent()) return existing.get();

                    Ingredient ing = ingredientRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));

                    UserFridge newItem = UserFridge.builder()
                            .user(user)
                            .ingredient(ing)
                            .build();
                    return userFridgeRepository.save(newItem);
                })
                .map(this::mapToDTO)
                .toList();
    }
}