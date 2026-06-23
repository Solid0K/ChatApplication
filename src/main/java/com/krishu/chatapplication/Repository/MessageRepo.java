package com.krishu.chatapplication.Repository;

import com.krishu.chatapplication.Model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends MongoRepository<Message,String> {
    List<Message> findTop50ByOrderByTimestampDesc();
}
