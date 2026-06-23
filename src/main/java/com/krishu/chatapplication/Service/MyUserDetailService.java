package com.krishu.chatapplication.Service;

import com.krishu.chatapplication.Model.User;
import com.krishu.chatapplication.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailService(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.getUserByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        return org.springframework.security.core.userdetails
                .User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
