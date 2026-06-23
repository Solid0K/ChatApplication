package com.krishu.chatapplication.Repository;

import com.krishu.chatapplication.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User,String> {
    Optional<User> getUserByUsername(String username);
}
