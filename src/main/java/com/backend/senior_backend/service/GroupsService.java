package com.backend.senior_backend.service;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.dto.GroupWithRoleDTO;
import com.backend.senior_backend.dto.budgetAndExpensesDTO;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.repositories.GroupsRepository;

@Service
public class GroupsService {

    @Autowired
    private  GroupsRepository groupsRepository;

    @Autowired
    private ParticipantsService participantsService;
    
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

        
    }

    
}
