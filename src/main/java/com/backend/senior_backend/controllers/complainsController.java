package com.backend.senior_backend.controllers;

import com.backend.senior_backend.models.Complian;
import com.backend.senior_backend.service.ComplainsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/complains")
public class complainsController {

    @Autowired
    private ComplainsService complainsService;

    // Endpoint to retrieve all complaints
    @GetMapping
    public ResponseEntity<List<Complian>> getAllComplains() {
        List<Complian> complains = complainsService.getAllComplains();
        return ResponseEntity.ok(complains);
    }

    // Endpoint to post a new complaint
    @PostMapping
    public ResponseEntity<Complian> createComplian(@RequestBody Complian complian) {
        Complian savedComplian = complainsService.createComplian(complian);
        return ResponseEntity.ok(savedComplian);
    }

    // Endpoint to delete a complaint by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplian(@PathVariable Long id) {
        try {
            complainsService.deleteComplianById(id);
            return ResponseEntity.ok("Complaint with ID " + id + " deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}