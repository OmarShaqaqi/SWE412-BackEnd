// package com.backend.senior_backend.controllers;

// import com.backend.senior_backend.dto.SignupRequestDTO;
// import com.backend.senior_backend.dto.LoginRequestDTO;
// import com.backend.senior_backend.service.AuthService;

// import jakarta.validation.Valid;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     @Autowired
//     private AuthService authService;

//     @PostMapping("/signup")
//     public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequestDTO request) {
//         Map<String, String> response = authService.registerUser(request);

//         if (response.containsKey("error")) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//         }

//         return ResponseEntity.ok(response);
//     }

//     @PostMapping("/login")
//     public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request) {
//         Map<String, String> response = authService.loginUser(request);

//         if (response.containsKey("error")) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//         }

//         return ResponseEntity.ok(response);
//     }
// }
