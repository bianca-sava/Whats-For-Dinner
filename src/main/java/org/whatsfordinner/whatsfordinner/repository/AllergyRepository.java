package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.Allergy;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
}
