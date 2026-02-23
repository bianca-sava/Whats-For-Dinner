package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.UserFridge;

public interface UserFridgeRepository extends JpaRepository<UserFridge, Long> {
}
