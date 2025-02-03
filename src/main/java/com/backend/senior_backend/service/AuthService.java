package com.backend.senior_backend.service;

import com.backend.senior_backend.dto.SignupRequestDTO;
import com.backend.senior_backend.dto.LoginRequestDTO;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.data.UserRepository;
import com.backend.senior_backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    public Map<String, String> requestPasswordReset(String phone) {
        Optional<Users> userOptional = userRepository.findByPhone(phone);

        Map<String, String> response = new HashMap<>();
        if (userOptional.isEmpty()) {
            response.put("error", "User not found!");
            return response;
        }

        Users user = userOptional.get();
        String resetPin = generateResetPin();
        user.setResetPin(resetPin);
        user.setPinExpiration(LocalDateTime.now().plusMinutes(10)); // Set expiration time (10 min)
        userRepository.save(user);

        // Send PIN via email
        emailService.sendResetPinEmail(user.getEmail(), resetPin);

        response.put("message", "Security PIN sent to your email.");
        return response;
    }

    public Map<String, String> resetPassword(String phone, String pin, String newPassword) {
        Optional<Users> userOptional = userRepository.findByPhone(phone);

        Map<String, String> response = new HashMap<>();
        if (userOptional.isEmpty()) {
            response.put("error", "User not found!");
            return response;
        }

        Users user = userOptional.get();

        if (user.getResetPin() == null || !user.getResetPin().equals(pin)) {
            response.put("error", "Invalid or expired security PIN.");
            return response;
        }

        if (user.getPinExpiration().isBefore(LocalDateTime.now())) {
            response.put("error", "Security PIN has expired. Request a new one.");
            return response;
        }

        user.setPassword(passwordEncoder.encode(newPassword)); // Hash new password
        user.setResetPin(null); // Clear the reset PIN after use
        user.setPinExpiration(null);
        userRepository.save(user);

        response.put("message", "Password reset successful.");
        return response;
    }

    // Generate a random 6-digit PIN
    private String generateResetPin() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // Ensures a 6-digit format
    }

    //registerUser method
    public Map<String, String> registerUser(SignupRequestDTO request) {
        Map<String, String> response = new HashMap<>();
    
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            response.put("error", "Phone number already exists!");
            return response;
        }

        if (!isStrongPassword(request.getPassword())) {
            response.put("error","Weak password! Must be at least 8 characters with uppercase, lowercase, number, and special character.");
            return response;
        }
    
        String hashedPassword = passwordEncoder.encode(request.getPassword());
    
        Users user = new Users(
                request.getPhone(),
                request.getFname(),
                request.getLname(),
                request.getEmail(),
                request.getUsername(),
                hashedPassword
        );
    
        userRepository.save(user);
    
        response.put("success", "User registered successfully!");
        return response;
    }
    // Password strength validator
    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(regex).matcher(password).matches();
    }

    //loginUser method
    public Map<String, String> loginUser(LoginRequestDTO request) {
    Map<String, String> response = new HashMap<>();
    
    Optional<Users> userOptional = userRepository.findByPhone(request.getPhone());

    if (userOptional.isEmpty()) {
        response.put("error", "User not found!");
        return response;
    }

    Users user = userOptional.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        response.put("error", "Invalid password!");
        return response;
    }

    // Generate JWT token
    String token = jwtUtils.generateToken(user.getPhone());
    response.put("token", token);
    return response;
}

}
