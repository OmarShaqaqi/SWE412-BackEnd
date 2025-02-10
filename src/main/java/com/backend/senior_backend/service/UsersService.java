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
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.regex.Pattern.matches;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.senior_backend.Entity.EmailDetails;
import com.backend.senior_backend.data.UserRepository;
import com.backend.senior_backend.dto.LoginRequestDTO;
import com.backend.senior_backend.utils.JwtUtils;

import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // In-memory store for reset PINs
    private final Map<String, String> resetPins = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;


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

        public Map<String, String> loginUser(LoginRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        Optional<Users> userOptional = usersRepository.findByPhone(request.getPhone());

        if (userOptional.isEmpty()) {
            response.put("error", "User not found!");
            return response;
        }

        Users user = userOptional.get();

        if (!matches(request.getPassword(), user.getPassword())) {
            response.put("error", "Invalid password!");
            return response;
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getPhone());
        response.put("token", token);
        return response;
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

    public ResponseEntity<?> deleteUser(String phone,String password) {
        System.out.println("✅ Authenticated user phone: " + phone);

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        Users userDetails = user.get();
        if (!userDetails.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Wrong password!");
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
        userDetails.setPassword(newPassword);
        usersRepository.save(userDetails);

        return ResponseEntity.ok("✅ Password changed successfully!: "+newPassword);
    }

    public ResponseEntity<?> updateUserDetails(String phone, Map<String, String> userDetailsMap) {
        System.out.println("✅ Authenticated user phone: " + phone);
    
        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }
    
        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }
    
        Users userDetails = user.get();
        if (userDetailsMap.containsKey("username")) {
            userDetails.setUsername(userDetailsMap.get("username"));
        }
        if (userDetailsMap.containsKey("email")) {
            userDetails.setEmail(userDetailsMap.get("email"));
        }
    
        usersRepository.save(userDetails);
        return ResponseEntity.ok("✅ User details updated successfully!");
    }
    

    // //Email ---------------------
    public ResponseEntity<?> resetRequest(String email) {
        Optional<Users> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User with this email not found!");
        }

        // Generate a reset PIN
        String resetPin = generateResetPin();
        Users user = userOptional.get();
        String phone = user.getPhone();  // Get user phone to store the PIN
        
        // Store PIN in HashMap with expiration logic
        resetPins.put(phone, resetPin);
        expireResetPin(phone, 10);  // Set expiration time for 10 minutes

        // Send PIN via email
        EmailDetails details = new EmailDetails();
        details.setRecipient(user.getEmail());
        details.setSubject("Password Reset Request");
        details.setMsgBody("Your reset PIN is: " + resetPin);
        
        emailService.sendSimpleMail(details);

        return ResponseEntity.ok("✅ Reset PIN sent to your email.");
    }

    // Generate a random 6-digit PIN
    private String generateResetPin() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  // Ensures a 6-digit format
    }

    // Expire PIN after a set time (10 minutes)
    private void expireResetPin(String phone, int minutes) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resetPins.remove(phone);
            }
        }, TimeUnit.MINUTES.toMillis(minutes));
    }

    public ResponseEntity<?> validateResetPin(String phone, String pin) {
        String storedPin = resetPins.get(phone);

        if (storedPin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ No PIN found or it has expired!");
        }

        if (!storedPin.equals(pin)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Invalid PIN!");
        }

        // Clear the PIN after successful validation
        resetPins.remove(phone);

        return ResponseEntity.ok("✅ PIN validated successfully!");
    }

    public String signOut(String token) {
        jwtUtils.invalidateToken(token);
        return "✅ User signed out successfully!";
    }
} 


