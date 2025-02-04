package com.backend.senior_backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.senior_backend.service.UsersService;

@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/a")
    public String postMethodName() {
        return "Hello, world";
    }

    @GetMapping("/getinfo")
    public ResponseEntity<?> getProfile() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.getProfile(phone);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.deleteUser(phone);
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordMap) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.changePassword(phone, passwordMap);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String response = usersService.signOut(token);

        return ResponseEntity.ok(response);
    }
}
