package com.epam.cdp.batulin.cqrs.repository;

import com.epam.cdp.batulin.cqrs.event.store.History;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEventRepository extends MongoRepository<History, String> {
}
