package com.krishu.chatapplication.Config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.beans.JavaBean;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtfilter;

    public SecurityConfiguration(JwtFilter jwtfilter){
        this.jwtfilter=jwtfilter;
    }

    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain getSecurity(HttpSecurity http, JwtFilter jwtFilter){
        http.csrf(csrf->csrf.disable());
        http.authorizeHttpRequests(request->request.requestMatchers("/register","/loginUser","/chat/**")
                .permitAll().anyRequest().authenticated());
        http.sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
