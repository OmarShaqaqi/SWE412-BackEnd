package com.backend.senior_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.transaction.annotation.Transactional;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.UsersRepository;
import com.backend.senior_backend.dto.profilePictureDTO;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.backend.senior_backend.dto.LoginRequestDTO;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private GroupsService groupsService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Long getUsersCount() {
        return usersRepository.count();
    }

    public ResponseEntity<?> newUser(Users user, Double budget, BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        if (bindingResult.hasErrors()) { // check if there are errors
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            errors.put("status", 400);
            return ResponseEntity.badRequest().body(errors);
        } else if (usersRepository.findById(user.getPhone()).isPresent()) { // check if phone already exists
            errors.put("status", 400);
            errors.put("message", "Phone already exists");
            return ResponseEntity.badRequest().body(errors);
        } else if (usersRepository.findByEmail(user.getEmail()).isPresent()) { // check if email already exists
            errors.put("status", 400);
            errors.put("message", "email already exists");
            return ResponseEntity.badRequest().body(errors);
        } else if (usersRepository.findByUsername(user.getUsername()).isPresent()) { // check if username already exists
            errors.put("status", 400);
            errors.put("message", "username already exists");
            return ResponseEntity.badRequest().body(errors);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        usersRepository.save(user); // save user

        Groups group = new Groups();
        group.setBudget(budget.intValue());
        group.setName("personal");
        group.setIconName("Wallet");

        groupsService.addGroup(group, user.getPhone(), bindingResult);

        return ResponseEntity.ok(Map.of("status", 200));
    }

    public Map<String, String> loginUser(LoginRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        Optional<Users> userOptional = usersRepository.findByPhone(request.getPhone());

        if (userOptional.isEmpty()) {
            response.put("error", "User not found!");
            return response;
        }

        Users user = userOptional.get();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            response.put("error", "Invalid password!");
            return response;
        }

        String token = jwtService.generateToken(user.getPhone());
        response.put("token", token);
        return response;

    }

    // admin login
    public Map<String, String> loginAdmin(LoginRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        Optional<Users> userOptional = usersRepository.findByPhone(request.getPhone());

        if (userOptional.isEmpty()) {
            response.put("error", "User not found!");
            return response;
        }

        Users user = userOptional.get();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            response.put("error", "Invalid password!");
            return response;
        }

        String token = jwtService.generateToken(user.getPhone());
        response.put("token", token);
        return response;

    }

    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    public ResponseEntity<?> getProfile(String phone) {

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

    public ResponseEntity<?> deleteUser(String phone, String password) {

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        if (!encoder.matches(password, user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Wrong password!");
        }

        usersRepository.delete(user.get());
        return ResponseEntity.ok("✅ User deleted successfully!");
    }

    public ResponseEntity<?> changePassword(String phone, Map<String, String> passwordMap) {

        if (phone == null || phone.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Unauthorized access!");
        }

        Optional<Users> user = usersRepository.findById(phone);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }

        Users userDetails = user.get();
        String newPassword = passwordMap.get("newPassword");
        userDetails.setPassword(encoder.encode(newPassword));
        usersRepository.save(userDetails);

        return ResponseEntity.ok("✅ Password changed successfully!: " + newPassword);
    }

    public ResponseEntity<?> updateUserDetails(String phone, Map<String, String> userDetailsMap) {

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
        // if (userDetailsMap.containsKey("email")) {
        // userDetails.setEmail(userDetailsMap.get("email"));
        // }

        usersRepository.save(userDetails);
        return ResponseEntity.ok("✅ User details updated successfully!");
    }

    public String signOut(String token) {
        jwtService.invalidateToken(token);
        return "✅ User signed out successfully!";
    }

    public Boolean isUserAvailable(String username) {

        if (usersRepository.findByUsername(username).isPresent()) {
            return true;
        }
        return false;
    }

    @Transactional
    public void uploadProfilePicture(String phone, profilePictureDTO image_encode) {
        Users user = usersRepository.findById(phone).get();
        String image = image_encode.getImage();
        user.setImage(image);
        usersRepository.save(user);
    }

    @Transactional
    public profilePictureDTO getProfilePicture(String phone) {
        Users user = usersRepository.findById(phone).get();
        profilePictureDTO image = new profilePictureDTO(user.getImage());
        return image;
    }
}
