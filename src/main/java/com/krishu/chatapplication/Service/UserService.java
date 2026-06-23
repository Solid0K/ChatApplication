package com.krishu.chatapplication.Service;

import com.krishu.chatapplication.DTO.LoginRequest;
import com.krishu.chatapplication.DTO.RegisterRequest;
import com.krishu.chatapplication.Model.User;
import com.krishu.chatapplication.Repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo user_repo;
    private final BCryptPasswordEncoder encoder;
    private final JWTService jwtService;

    public UserService(UserRepo user_repo,BCryptPasswordEncoder encoder,JWTService jwtService){
        this.user_repo=user_repo;
        this.encoder=encoder;
        this.jwtService=jwtService;
    }


    public void registerUser(RegisterRequest request) {
        System.out.println("Username = " + request.getUsername());
        System.out.println("Password = " + request.getPassword());
        if(user_repo.getUserByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username is already exist");
        }
        User user=new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user_repo.save(user);
    }

    public String verify(@Valid LoginRequest request) {
        User user=user_repo.getUserByUsername(request.getUsername()).
                orElseThrow(()->new RuntimeException("User not found"));
        if(!encoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Password is incorrect");
        }
        return jwtService.getToken(user.getUsername());
    }
}
