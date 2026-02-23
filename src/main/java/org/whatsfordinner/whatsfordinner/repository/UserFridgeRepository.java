package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserFridge;

import java.util.List;

public interface UserFridgeRepository extends JpaRepository<UserFridge, Long> {
    List<UserFridge> findByUser(User user);
}
