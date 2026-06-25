package com.krishu.chatapplication.Controller;

import com.krishu.chatapplication.DTO.LoginRequest;
import com.krishu.chatapplication.DTO.RegisterRequest;
import com.krishu.chatapplication.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final UserService service;

    public AuthController(UserService service){
        this.service=service;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request){
        service.registerUser(request);
    }

    @PostMapping("/loginUser")
    public String login(@Valid @RequestBody LoginRequest request){
        return service.verify(request);
    }

    @GetMapping("/test")
    public String test(){
        return "its just a test";
    }
}
