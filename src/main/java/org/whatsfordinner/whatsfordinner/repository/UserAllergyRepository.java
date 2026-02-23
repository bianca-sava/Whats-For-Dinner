package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.UserAllergy;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
}
