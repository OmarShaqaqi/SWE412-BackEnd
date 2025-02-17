package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.backend.senior_backend.dto.ParticipantRequest;
import com.backend.senior_backend.service.ParticipantsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/participants")
public class ParticipantsController {

    @Autowired
    private ParticipantsService participantsService;

    @PostMapping("/add")
    public String addParticipant(@RequestBody ParticipantRequest request) {
        // Extract authenticated user's phone number
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract data from JSON request
        Long groupId = request.getGroup_id();
        String username = request.getUsername();

        participantsService.addParticipant(groupId, phone, false);

        return "Received group_id: " + groupId + ", username: " + username + ", phone: " + phone;
    }
    

}
