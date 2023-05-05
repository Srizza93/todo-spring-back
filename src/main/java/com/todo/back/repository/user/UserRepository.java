package com.todo.back.repository.user;

import com.todo.back.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserProfile, String> {

    UserProfile findUserByEmail(String email);

    public long count();
}
