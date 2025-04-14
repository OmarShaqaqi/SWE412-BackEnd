package com.backend.senior_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.backend.senior_backend.dto.GroupWithRoleDTO;
import com.backend.senior_backend.dto.budgetAndExpensesDTO;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.service.GroupsService;

import jakarta.validation.Valid;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




// API Layer
@RestController
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @PostMapping("/groups/add")
    public ResponseEntity<Groups> addGroup(@Valid @RequestBody Groups group, BindingResult bindingResult) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("This is the binding results");
        System.out.println(bindingResult);

        final Groups new_group = groupsService.addGroup(group,phone,bindingResult);

        return ResponseEntity.ok(new_group);
    }


    @GetMapping("/groups/get")
    public ResponseEntity<List<Groups>> getGroups() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Groups> groups = groupsService.getGroups(phone);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/groups/{groupId}/user-role")
    public ResponseEntity<Boolean> getUserRoleInGroup(@PathVariable Long groupId) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();  // Get the current user's phone from JWT
        Boolean role = groupsService.getUserRoleInGroup(phone, groupId);  // Get the user's role from the service layer
        
        if (role != null) {
            return ResponseEntity.ok(role);  // Return the role (e.g., "leader" or "participant")
        } else {
            return ResponseEntity.status(404).body(null);  // Return an error if the role is not found
        }
    }

    // @GetMapping("/groups/personal")
    // public budgetAndExpensesDTO getPersonalBudgetAndExpenses() {
    //     String phone = SecurityContextHolder.getContext().getAuthentication().getName();  
        
    // }
    

    
    
    
    
}