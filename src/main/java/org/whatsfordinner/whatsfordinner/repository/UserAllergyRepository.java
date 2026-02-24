package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserAllergy;

import java.util.List;
import java.util.Optional;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
    List<UserAllergy> findByUser(User user);
    Optional<UserAllergy> findByUserAndAllergyId(User user, Long allergyId);
}
