package com.todo.back.repository;

import com.todo.back.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    public long count();
}
