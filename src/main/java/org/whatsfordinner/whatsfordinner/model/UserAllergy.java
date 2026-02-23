package org.whatsfordinner.whatsfordinner.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "user_allergies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "allergy_id", nullable = false)
    private Allergy allergy;
}
