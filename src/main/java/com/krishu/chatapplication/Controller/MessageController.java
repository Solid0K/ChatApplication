package com.krishu.chatapplication.Controller;

import com.krishu.chatapplication.Model.Message;
import com.krishu.chatapplication.Repository.MessageRepo;
import com.krishu.chatapplication.Service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/getMessages")
public class MessageController {
    private final MessageService messageservice;

    public MessageController(MessageService messageservice){
        this.messageservice=messageservice;
    }

    @GetMapping
    public List<Message> getSomeMessage(){
        return messageservice.getSomeMessages();
    }
}
