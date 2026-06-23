package com.krishu.chatapplication.Repository;

import com.krishu.chatapplication.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    Optional<User> getUserByUsername(String username);
}
