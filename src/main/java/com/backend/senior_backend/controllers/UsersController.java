package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;
import com.backend.senior_backend.service.UsersService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:52239")
@RestController
public class UsersController {

    private final UsersService usersService;
    private final UsersRepository usersRepository;

    public UsersController(UsersService usersService, UsersRepository usersRepository) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
    }

   @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody Users user, BindingResult bindingResult) {

        return usersService.newUser(user,bindingResult);
        
    }

    // @PostMapping("/email")
    // public Users postMethodName(@RequestBody Users entity) {

    //    final Optional<Users> user =  usersRepository.findByEmail(entity.getEmail());
    //    System.out.println(user.getPassword());
        
        
    //     return user;
    // }
    
    
    
}