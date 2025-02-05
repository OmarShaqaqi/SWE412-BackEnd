package com.backend.senior_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.senior_backend.data.UserRepository;
import com.backend.senior_backend.utils.JwtUtils;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


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

        return ResponseEntity.ok(Map.of("status", 200));
    }
    public ResponseEntity<?> getProfile(String phone) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        Users userDetails = user.get();
        Map<String, Object> response = new HashMap<>();
        response.put("phone", userDetails.getPhone());
        response.put("email", userDetails.getEmail());
        response.put("username", userDetails.getUsername());
        response.put("fname", userDetails.getFname());
        response.put("lname", userDetails.getLname());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteUser(String phone) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        usersRepository.delete(user.get());
        return ResponseEntity.ok("✅ User deleted successfully!");
    }

    public ResponseEntity<?> changePassword(String phone, Map<String, String> passwordMap) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        Users userDetails = user.get();
        String newPassword = passwordMap.get("newPassword");
        userDetails.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(userDetails);

        return ResponseEntity.ok("✅ Password changed successfully!");
    }

    public String signOut(String token) {
        jwtUtils.invalidateToken(token);
        return "✅ User signed out successfully!";
    }
} 


