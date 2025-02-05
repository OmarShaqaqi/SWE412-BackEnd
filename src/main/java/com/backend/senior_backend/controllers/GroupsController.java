package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


// API Layer
@RestController
public class GroupsController {

    @GetMapping("/group")
    public String postMethodName() {

        return "Hello, world";
    }
    
    
}