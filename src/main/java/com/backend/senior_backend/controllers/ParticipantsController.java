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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




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

    @GetMapping("/get/{groupId}")
    public ResponseEntity<?> getParticipants(@PathVariable("groupId") int groupId) {
        return ResponseEntity.ok(participantsService.findAllParticipantsWithRolesAndExpenses(groupId));
    }
    
    

}
