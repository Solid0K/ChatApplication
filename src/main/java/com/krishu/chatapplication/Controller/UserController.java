package com.krishu.chatapplication.Controller;

import com.krishu.chatapplication.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userservice;

    public UserController(UserService userservice){
        this.userservice=userservice;
    }

    @GetMapping("/online")
    public Set<String> getOnlineUser(){
        return userservice.getOnlineUsers();
    }
}
