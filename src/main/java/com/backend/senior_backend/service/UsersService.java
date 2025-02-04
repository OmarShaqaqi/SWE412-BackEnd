package com.backend.senior_backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.senior_backend.data.UserRepository;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.utils.JwtUtils;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> getProfile(String phone) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = userRepository.findByPhone(phone);
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

        Optional<Users> user = userRepository.findByPhone(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        userRepository.delete(user.get());
        return ResponseEntity.ok("✅ User deleted successfully!");
    }

    public ResponseEntity<?> changePassword(String phone, Map<String, String> passwordMap) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = userRepository.findByPhone(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        Users userDetails = user.get();
        String newPassword = passwordMap.get("newPassword");
        userDetails.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userDetails);

        return ResponseEntity.ok("✅ Password changed successfully!");
    }

    public String signOut(String token) {
        jwtUtils.invalidateToken(token);
        return "✅ User signed out successfully!";
    }
}
