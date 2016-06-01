package com.epam.cdp.repository;

import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserImpl, Long > {
    User findByEmail(String email);

    List<User> findByNameLike(String name, Pageable pageable);
}
