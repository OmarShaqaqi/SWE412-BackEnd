package com.backend.senior_backend.service;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.repositories.GroupsRepository;

@Service
public class GroupsService {

    @Autowired
    private  GroupsRepository groupsRepository;

    @Autowired
    private ParticipantsService participantsService;
    
    public String addGroup(Groups group, String phone, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "❌ Error adding group: " + bindingResult.getFieldError("name").getDefaultMessage();
        }
        groupsRepository.save(group);
        participantsService.addParticipant(group.getId(), phone, true);
        return "✅ Group added successfully!";
    }

    public List<Groups> getGroups(String phone) {
        List<Groups> groups = participantsService.findAllGroups(phone);
        return groups;
    }
    
}
