package org.whatsfordinner.whatsfordinner.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allergies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
