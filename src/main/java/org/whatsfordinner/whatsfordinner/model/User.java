package org.whatsfordinner.whatsfordinner.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    @Builder.Default
    private Integer defaultServings = 2;

    @Column(nullable = false)
    @Builder.Default
    private Boolean hasCompletedOnboarding = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPreferences preferences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAllergy> allergies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFridge> fridgeItems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}