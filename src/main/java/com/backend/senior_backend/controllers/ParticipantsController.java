package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.backend.senior_backend.dto.ParticipantRequest;
import com.backend.senior_backend.service.ParticipantsService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addLeader(@RequestBody ParticipantRequest request) {

        // Extract data from JSON request
        Long groupId = request.getGroup_id();
        String username = request.getUsername();

        String status = participantsService.addParticipant(groupId, username);
        if (status.equals("✅ Participant added successfully!")) {
            return ResponseEntity.ok(status);
        }
        else {
            return ResponseEntity.status(Response.SC_CONFLICT).body(status);
        }
    }
    

}
