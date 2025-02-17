package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.service.GroupsService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



// API Layer
@RestController
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @PostMapping("/add")
    public String addGroup(@Valid @RequestBody Groups group, BindingResult bindingResult) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("This is the binding results");
        System.out.println(bindingResult);

        final String status = groupsService.addGroup(group,phone,bindingResult);

        return status;
    }
    
    
}