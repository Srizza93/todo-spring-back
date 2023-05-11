package com.todo.back.repository.user;

import com.todo.back.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByUsername(String username);
    Boolean existsByUsername(String username);

    UserProfile findUserByEmail(String email);
    Boolean existsByEmail(String email);

    public long count();
}
