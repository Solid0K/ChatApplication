package com.krishu.chatapplication.Service;

import com.krishu.chatapplication.Model.Message;
import com.krishu.chatapplication.Repository.MessageRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepo messagerepo;

    public MessageService(MessageRepo messagerepo){
        this.messagerepo=messagerepo;
    }

    public void addMessage(String sender, String message){
        messagerepo.save(new Message(sender,message, LocalDateTime.now()));
    }

    public List<Message> getSomeMessages() {
        return messagerepo.findTop50ByOrderByTimestampDesc();
    }
}
