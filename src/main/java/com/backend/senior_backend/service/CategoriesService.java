package com.backend.senior_backend.service;

import com.backend.senior_backend.models.*;
import com.backend.senior_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    // public String addCategory(Long groupId, String categoryName, String userPhone) {
    //     Optional<Groups> groupOpt = groupsRepository.findById(groupId);
    //     if (groupOpt.isEmpty()) {
    //         return "❌ Group not found!";
    //     }

    //     Optional<Participants> participantOpt = participantsRepository.findById(new ParticipantsId(groupId, userPhone));
    //     if (participantOpt.isEmpty() || !participantOpt.get().isLeader()) {
    //         return "❌ Only group leaders can add categories!";
    //     }

    //     CategoriesId categoriesId = new CategoriesId(groupId, categoryName);
    //     Categories category = new Categories();
    //     category.setId(categoriesId);
    //     category.setGroup(groupOpt.get());

    //     categoriesRepository.save(category);
    //     return "✅ Category added successfully!";
    // }
    public String addCategory(Long groupId, String categoryName) {
        Optional<Groups> groupOpt = groupsRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            return "❌ Group not found!";
        }

        CategoriesId categoriesId = new CategoriesId(groupId, categoryName);
        Categories category = new Categories();
        category.setId(categoriesId);
        category.setGroup(groupOpt.get());

        categoriesRepository.save(category);
        return "✅ Category added successfully!";
    }

    public List<Categories> getCategories(Long groupId) {
        return categoriesRepository.findAllByGroupId(groupId);
    }

    public String deleteCategory(Long groupId, String categoryName, String userPhone) {
        Optional<Participants> participantOpt = participantsRepository.findById(new ParticipantsId(groupId, userPhone));
        if (participantOpt.isEmpty() || !participantOpt.get().isLeader()) {
            return "❌ Only group leaders can delete categories!";
        }
        
        CategoriesId categoriesId = new CategoriesId(groupId, categoryName);
        Optional<Categories> categoryOpt = categoriesRepository.findById(categoriesId);
        if (categoryOpt.isEmpty()) {
            return "❌ Category not found!";
        }
        categoriesRepository.delete(categoryOpt.get());
        return "✅ Category deleted successfully!";
    }
}
