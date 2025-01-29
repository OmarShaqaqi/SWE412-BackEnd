package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.backend.senior_backend.models.Groups;
import org.springframework.web.bind.annotation.GetMapping;


// API Layer
@RestController
public class GroupController {

    @GetMapping("/group")
    public String postMethodName() {

        return "Hello, world";
    }
    
    
}