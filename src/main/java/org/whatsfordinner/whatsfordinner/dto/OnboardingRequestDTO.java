package org.whatsfordinner.whatsfordinner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingRequestDTO {

    @NotBlank
    private String firstName;

    private String lastName;

    @NotNull
    private Boolean isVegetarian;

    @NotNull
    private Boolean isVegan;

    @NotNull
    @Min(1) @Max(20)
    private Integer defaultServings;

    private List<Long> allergyIds;
}