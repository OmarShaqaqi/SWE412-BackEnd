package com.backend.senior_backend.service;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.senior_backend.repositories.GroupsRepository;
import com.backend.senior_backend.repositories.ParticipantsRepository;
import com.backend.senior_backend.repositories.UsersRepository;
import com.backend.senior_backend.models.Groups;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.models.ParticipantsId;
import com.backend.senior_backend.models.Users;

@Service
public class ParticipantsService {

    @Autowired
    private  ParticipantsRepository participantsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    public String addParticipant(Long groupId, String phone, boolean isLeader) {
        Optional<Groups> groupOpt = groupsRepository.findById(groupId);
        Optional<Users> userOpt = usersRepository.findByPhone(phone);
        
        ParticipantsId participantsId = new ParticipantsId(groupId, phone);
        Participants participant = new Participants();
        participant.setId(participantsId);
        participant.setGroup(groupOpt.get());
        participant.setUser(userOpt.get());
        participant.setLeader(isLeader);

        participantsRepository.save(participant);
        return "✅ Participant added successfully!";
    }
    
   
}
