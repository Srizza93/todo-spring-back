package com.todo.back.repository.user;

import com.todo.back.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<UserProfile, String> {

    @Query("{email:'?0'}")
    UserProfile findUserByEmail(String email);

    @Query("{name:'?0'}")
    List<UserProfile> findAll(String category);

    public long count();
}
