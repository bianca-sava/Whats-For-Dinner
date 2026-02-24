package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserPreferences;
import java.util.Optional;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    Optional<UserPreferences> findByUser(User user);
}
