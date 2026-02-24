package org.whatsfordinner.whatsfordinner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.whatsfordinner.whatsfordinner.dto.ScannedIngredientDTO;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiReceiptScannerService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final IngredientRepository ingredientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ScannedIngredientDTO> scanReceipt(String base64Image) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        StringBuilder ingredientsList = new StringBuilder();
        for (Ingredient ing : allIngredients) {
            ingredientsList.append("ID: ").append(ing.getId()).append(" | Name: ").append(ing.getName()).append("\n");
        }

        String promptText = "You are an assistant that reads shopping receipts. Items may be in Romanian. " +
                "Allowed ingredients from my database:\n" + ingredientsList.toString() +
                "\nTask: Read receipt, ignore non-food. Map food items to my allowed list. " +
                "Return ONLY a JSON array: [{\"ingredientId\": ID, \"receiptName\": \"Name on receipt\", \"mappedName\": \"Name from my list\"}]";

        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("text", promptText),
                                Map.of("inline_data", Map.of(
                                        "mime_type", "image/jpeg",
                                        "data", base64Image
                                ))
                        )
                ))
        );

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + geminiApiKey;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return parseGeminiResponse(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("AI processing failed: " + e.getMessage());
        }
    }

    private List<ScannedIngredientDTO> parseGeminiResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        String text = root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

        String cleanJson = text.replace("```json", "").replace("```", "").trim();

        JsonNode itemsArray = objectMapper.readTree(cleanJson);
        List<ScannedIngredientDTO> result = new ArrayList<>();
        for (JsonNode item : itemsArray) {
            result.add(ScannedIngredientDTO.builder()
                    .ingredientId(item.get("ingredientId").asLong())
                    .receiptName(item.get("receiptName").asText())
                    .mappedName(item.get("mappedName").asText())
                    .build());
        }
        return result;
    }
}