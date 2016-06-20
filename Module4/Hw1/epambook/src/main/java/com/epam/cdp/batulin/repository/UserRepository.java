package com.epam.cdp.batulin.repository;

import com.epam.cdp.batulin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
