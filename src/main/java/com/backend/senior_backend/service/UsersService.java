package com.backend.senior_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;


    public ResponseEntity<?> newUser(Users user, BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        if (bindingResult.hasErrors()) { //check if there are errors
            bindingResult.getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()) 
            );
            errors.put("status", 400);
            return ResponseEntity.badRequest().body(errors);
        }
        else if (usersRepository.findById(user.getPhone()).isPresent()){ //check if phone already exists
            errors.put("status", 400);
            errors.put("message", "Phone already exists");
            return ResponseEntity.badRequest().body(errors);
        }
         else if (usersRepository.findByEmail(user.getEmail()).isPresent() ){ //check if email already exists
            errors.put("status", 400);
            errors.put("message", "email already exists");
            return ResponseEntity.badRequest().body(errors);
         }
         else if (usersRepository.findByUsername(user.getUsername()).isPresent()) { //check if username already exists
            errors.put("status", 400);
            errors.put("message", "username already exists");
            return ResponseEntity.badRequest().body(errors);
        }

        usersRepository.save(user); //save user

        return ResponseEntity.ok(Map.of("status", 200)); // no errors
    }
}
