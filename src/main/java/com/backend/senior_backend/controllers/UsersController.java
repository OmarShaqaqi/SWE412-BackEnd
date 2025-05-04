package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;


import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import com.backend.senior_backend.service.UsersService;
import jakarta.validation.Valid;
import com.backend.senior_backend.dto.LoginRequestDTO;
import com.backend.senior_backend.dto.UserBudegtDTO;
import com.backend.senior_backend.models.Users;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;



@RestController
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

   @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserBudegtDTO user, BindingResult bindingResult) {

        Users new_user = new Users(user.getPhone(),user.getEmail(),user.getPassword(),user.getUsername(),user.getFname(),user.getLname());
        Double budget = user.getBudget();

        return usersService.newUser(new_user,budget,bindingResult);
        
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

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> getProfilePicture(@RequestParam("file") MultipartFile file)  {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = usersService.saveUserProfilePicture(phone,file);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getProfilePicture")
    public ResponseEntity<byte[]> getProfilePicture() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        byte[] profilePicture = usersService.getProfilePicture(phone);

        // If the profile picture doesn't exist, return 404
        if (profilePicture == null || profilePicture.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Determine the content type (you can dynamically detect the image type if needed)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);  // Or use MediaType.IMAGE_PNG for PNG files

        // Return the profile picture as byte array with the appropriate content type
        return new ResponseEntity<>(profilePicture, headers, HttpStatus.OK);
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
