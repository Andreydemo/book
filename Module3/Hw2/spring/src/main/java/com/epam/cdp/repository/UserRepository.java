package com.epam.cdp.repository;

import com.epam.cdp.model.impl.UserImpl;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserImpl, Long> {
}
