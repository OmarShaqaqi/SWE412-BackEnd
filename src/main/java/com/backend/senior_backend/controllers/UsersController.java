package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import java.util.Map;
import org.springframework.http.HttpStatus;
import com.backend.senior_backend.service.UsersService;
import jakarta.validation.Valid;
import com.backend.senior_backend.dto.LoginRequestDTO;
import com.backend.senior_backend.dto.UserBudegtDTO;
import com.backend.senior_backend.models.Users;

@RestController
@CrossOrigin
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserBudegtDTO user, BindingResult bindingResult) {

        Users new_user = new Users(user.getPhone(), user.getEmail(), user.getPassword(), user.getUsername(),
                user.getFname(), user.getLname());
        Double budget = user.getBudget();

        return usersService.newUser(new_user, budget, bindingResult);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request) {
        Map<String, String> response = usersService.loginUser(request);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getinfo")
    public ResponseEntity<?> getProfile() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        return usersService.getProfile(phone);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.ok(usersService.getUsers());
    }

    @PostMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> requestMap) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        String password = requestMap.get("password");
        return usersService.deleteUser(phone, password);
    }

    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUserDetails(@RequestBody Map<String, String> userDetailsMap) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.updateUserDetails(phone, userDetailsMap);
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordMap) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.changePassword(phone, passwordMap);
    }

    @GetMapping("/isuseravailable/{username}")
    public ResponseEntity<Boolean> isUserAvailable(@PathVariable String username) {
        System.out.println("Username: " + username);
        return ResponseEntity.ok(usersService.isUserAvailable(username));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String response = usersService.signOut(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getallusers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/usersCount")
    public ResponseEntity<Long> getUsersCount() {
        Long count = usersService.getUsersCount();
        return ResponseEntity.ok(count);
    }

}
