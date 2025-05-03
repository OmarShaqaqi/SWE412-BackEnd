package com.backend.senior_backend.controllers;

import com.backend.senior_backend.dto.CategoryRequest;
import com.backend.senior_backend.dto.CategoryResponseDTO;
import com.backend.senior_backend.models.*;
import com.backend.senior_backend.service.CategoriesService;
import com.backend.senior_backend.service.ParticipantsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.StringCharacterIterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoriesService categoryService;

    @Autowired
    private ParticipantsService participantsService;


    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest request) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!participantsService.isGroupLeader(request.getGroupId(), phone)) {
            return ResponseEntity.status(403).body("❌ Only group leaders can add categories!");
        }

        // Ensure only two arguments are passed
        String response = categoryService.addCategory(request.getGroupId(), request.getCategoryName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories(@RequestParam Long groupId) {
        List<Categories> categories = categoryService.getCategories(groupId);

        // Map Categories to CategoryResponseDTO
        List<CategoryResponseDTO> response = categories.stream()
            .map(category -> new CategoryResponseDTO(
                category.getId().getGroupId(),
                category.getId().getName()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam Long groupId, @RequestParam String categoryName) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        Boolean isLeader = participantsService.isGroupLeader(groupId, phone);
        if (!isLeader) {
            return ResponseEntity.status(403).body("❌ Only group leaders can delete groups!");
        }
        String response = categoryService.deleteCategory(groupId, categoryName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/modify")
    public ResponseEntity<String> modifyCategory(@RequestParam Long groupId, @RequestParam String categoryName, @RequestParam String newCategoryName) {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        Boolean isLeader = participantsService.isGroupLeader(groupId, phone);
        if (!isLeader) {
            return ResponseEntity.status(403).body("❌ Only group leaders can delete groups!");
        }
        String response = categoryService.modifyCategory(groupId,categoryName,newCategoryName);
        return ResponseEntity.ok(response);


    }

   
    

}
