package com.backend.senior_backend.service;

import com.backend.senior_backend.dto.ExpensesResponse;
import com.backend.senior_backend.models.*;
import com.backend.senior_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.StringCharacterIterator;
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

    @Autowired
    private ExpenseRepository expenseRepository;

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

    public String deleteCategory(Long groupId, String categoryName) {


    Optional<Categories> category = categoriesRepository.findById(new CategoriesId(groupId,categoryName));

    if (category == null){
        return "category is not available";
    }

    List<Expenses> expenses = expenseRepository.findAllByCategoryId(new CategoriesId(groupId,categoryName));

    if (!expenses.isEmpty()) {
        expenseRepository.deleteAll(expenses);
    }

    category.ifPresent(categoriesRepository::delete);

    return "category was deleted successfully";
        
        
    }

    public String modifyCategory(Long groupId, String categoryName, String newCategoryName) {

        Optional<Categories> category = categoriesRepository.findById(new CategoriesId(groupId,categoryName));
        if (category == null){
            return "category is not available";
        }
        category.ifPresent(c -> {
            c.setId(new CategoriesId(groupId,newCategoryName));  // Set the new category name
            categoriesRepository.save(c);  // Save the updated entity
        });

        return "Category name was changed";


        
    }
}
