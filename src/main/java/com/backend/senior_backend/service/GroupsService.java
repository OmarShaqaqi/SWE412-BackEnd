package com.backend.senior_backend.service;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.dto.GroupNameWithBudget;
import com.backend.senior_backend.dto.GroupWithRoleDTO;
import com.backend.senior_backend.dto.budgetAndExpensesDTO;
import com.backend.senior_backend.models.Categories;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.repositories.CategoriesRepository;
import com.backend.senior_backend.repositories.ExpenseRepository;
import com.backend.senior_backend.repositories.GroupsRepository;
import com.backend.senior_backend.repositories.ParticipantsRepository;

import jakarta.transaction.Transactional;

@Service
public class GroupsService {

    @Autowired
    private  GroupsRepository groupsRepository;

    @Autowired
    private ParticipantsService participantsService;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private ExpenseRepository expensesRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;
    
    public Groups addGroup(Groups group, String phone, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        groupsRepository.save(group);
        participantsService.addLeader(group.getId(), phone, true);
        return group;
    }

    public List<Groups> getGroups(String phone) {
        List<Groups> groups = participantsService.findAllGroups(phone);
        return groups;
    }
    public List<GroupWithRoleDTO> getGroupsWithRoles(String phone) {
        List<GroupWithRoleDTO> groups = participantsService.findAllGroupsWithRoles(phone);
        return groups;
    }

     // New method to get the user's role in a specific group
     public Boolean getUserRoleInGroup(String phone, Long groupId) {
        
        // Find the participant record for this group and user
        Boolean participantRole = participantsService.getUserRoleInGroup(phone, groupId);
        
        return participantRole;
    }

    public budgetAndExpensesDTO getPersonalBudgetAndExpenses(String phone) {

        List<Long> groupIds = participantsRepository.findGroupIdsByUserPhone(phone);
        Groups group = getCorrectGroup(groupIds, "personal");

        int budget = group.getBudget();
        Double expenses = expensesService.getTotalExpenses(group.getId(),phone);
        budgetAndExpensesDTO budgetAndExpenses = new budgetAndExpensesDTO(budget, expenses);
        return budgetAndExpenses;
    }

    public Groups getCorrectGroup(List<Long> groupIds,String name) {
        for (Long groupId : groupIds) {
            Groups group = groupsRepository.findById(groupId).orElse(null);
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    public budgetAndExpensesDTO getGroupBudgetAndExpenses(String phone, Long groupId) {

        Groups group = groupsRepository.findById(groupId).orElse(null);
        System.out.println("group name:" + group.getName());

        int budget = group.getBudget();
        Double expenses = expensesService.getTotalExpensesAllUsers(groupId);
        budgetAndExpensesDTO budgetAndExpenses = new budgetAndExpensesDTO(budget, expenses);
        return budgetAndExpenses;
    }


    public String deleteGroup(Long groupId) {
        // Ensure the group exists
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            return "Group not found!";
        }

        // Optional: Delete associated expenses
        List<Expenses> expenses = expensesRepository.findAllByCategoryIdGroupId(groupId);
        if (!expenses.isEmpty()) {
            expensesRepository.deleteAll(expenses);
        }
        List<Participants> participants = participantsRepository.findAllByGroupId(groupId.intValue());
        if (!participants.isEmpty()){
            participantsRepository.deleteAll(participants);
        }
        List<Categories> categories = categoriesRepository.findAllByGroupId(groupId);
        if(!categories.isEmpty()){
            categoriesRepository.deleteAll(categories);
        }
        groupsRepository.delete(group);

        return "Group deleted successfully";
    }

    

    public String updateGroup(Long groupId, GroupNameWithBudget updatedGroup) {
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            return "Group not found"; // Handle the case when the group doesn't exist
        }
    
        // Only update if the value is not null
        if (updatedGroup.getGroupName() != null) {
            group.setName(updatedGroup.getGroupName());
        }
        if (updatedGroup.getBudget() != null) {
            group.setBudget(updatedGroup.getBudget());
        }
    
        groupsRepository.save(group);
        return "Group updated successfully";
    }
    


    
}
