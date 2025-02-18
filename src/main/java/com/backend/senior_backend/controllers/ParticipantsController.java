package com.backend.senior_backend.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.service.ParticipantsService;
import com.backend.senior_backend.dto.ParticipantRequestDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantsController {

    private final ParticipantsService participantsService;

    // ✅ Get all participants in a group
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Participants>> getParticipantsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(participantsService.getParticipantsByGroup(groupId));
    }

    // ✅ Add a participant to a group (Changed to accept JSON)
    @PostMapping("/add")
    public ResponseEntity<String> addParticipant(@RequestBody ParticipantRequestDTO request) {
        if (request.getUserPhone() == null || request.getGroupId() == null) {
            return ResponseEntity.badRequest().body("❌ Missing required fields!");
        }
        String response = participantsService.addParticipant(request.getUserPhone(), request.getGroupId(), request.isLeader());
        return ResponseEntity.ok(response);
    }

    // ✅ Remove a participant from a group
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeParticipant(@RequestParam String userPhone,
                                                    @RequestParam Long groupId) {
        String response = participantsService.removeParticipant(userPhone, groupId);
        return ResponseEntity.ok(response);
    }

    // ✅ Get all expenses of a user in a group
    @GetMapping("/expenses")
    public ResponseEntity<List<Expenses>> getUserExpensesInGroup(@RequestParam String userPhone,
                                                                @RequestParam Long groupId) {
        List<Expenses> expenses = participantsService.getUserExpensesInGroup(userPhone, groupId);
        return ResponseEntity.ok(expenses);
    }
}
