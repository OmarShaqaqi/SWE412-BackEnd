package com.backend.senior_backend.service;


import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.protocol.types.Field.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.senior_backend.dto.GroupWithRoleDTO;
import com.backend.senior_backend.dto.ParticipantExpenseDTO;
import com.backend.senior_backend.dto.ParticipantInformationDTO;
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

    @Autowired
    private ExpensesService expensesService;

    public String addLeader(Long groupId, String phone, boolean isLeader) {
        if (isLeader) {
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
        else {
            if (!usersRepository.findByPhone(phone).isPresent()) {
                return "❌ User not found!";
            }
            else if(participantsRepository.findAllByUserPhone(phone).stream().anyMatch(p -> p.getGroup().getId() == groupId)) {
                return "❌ User already in group!";
            }
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

    public List<GroupWithRoleDTO> findAllGroupsWithRoles(String phone) {
        List<Participants> participants = participantsRepository.findAllByUserPhone(phone);
        return participants.stream()
                .map(participant -> new GroupWithRoleDTO(participant.getGroup(), participant.isLeader()))
                .toList();
    }

    public List<ParticipantExpenseDTO> findAllParticipantsWithRolesAndExpenses(int groupId) {
        List<Participants> participants = participantsRepository.findAllByGroupId(groupId);

        List<ParticipantExpenseDTO> participantsWithExpenses = participants.stream()
        .map(participant -> new ParticipantExpenseDTO(participant.getUser().getPhone(),
        expensesService.getTotalExpenses(participant.getGroup().getId(),
        participant.getUser().getPhone()), participant.isLeader())).toList();

        return participantsWithExpenses;
    }

    public List<Groups> findAllGroups(String phone) {
        List<Participants> participants = participantsRepository.findAllByUserPhone(phone);
        return participants.stream().map(Participants::getGroup).toList();
    }

    public boolean isGroupLeader(Long groupId, String phone) {
        Optional<Participants> participantOpt = participantsRepository.findById(new ParticipantsId(groupId, phone));
        
        return participantOpt.isPresent() && participantOpt.get().isLeader();
    }



    public String addParticipant(Long groupId, String username) {

        if (!usersRepository.findByUsername(username).isPresent()) {
            return "❌ User not found!";
        }
        else if(participantsRepository.findAllByUserPhone(username).stream().anyMatch(p -> p.getGroup().getId() == groupId)) {
            return "❌ User already in group!";
        }
        Optional<Groups> groupOpt = groupsRepository.findById(groupId);
        Optional<Users> userOpt = usersRepository.findByUsername(username);
        
        ParticipantsId participantsId = new ParticipantsId(groupId,userOpt.get().getPhone());
        Participants participant = new Participants();
        participant.setId(participantsId);
        participant.setGroup(groupOpt.get());
        participant.setUser(userOpt.get());
        participant.setLeader(false);
        participantsRepository.save(participant);

        return "✅ Participant added successfully!";
        
    }
    
    public Boolean getUserRoleInGroup(String phone, Long groupId) {
        // Fetch the group and participant from the database
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            return null;  // If the group does not exist
        }

        // Find the participant record for this group and user
        Participants participant = participantsRepository.findByGroupIdAndUserPhone(groupId, phone);
        
        if (participant == null) {
            return null;  // If no participant record exists, return null
        }

        // Return the role of the participant (e.g., "leader" or "participant")
        return participant.isLeader();
    }


    public ParticipantInformationDTO getParticipantInformation(Long groupId, String phone) {
        // Fetch the group and participant from the database
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            return null;  // If the group does not exist
        }

        // Find the participant record for this group and user
        Participants participant = participantsRepository.findByGroupIdAndUserPhone(groupId, phone);
        
        if (participant == null) {
            return null;  // If no participant record exists, return null
        }

        // Return the role of the participant (e.g., "leader" or "participant")
        return new ParticipantInformationDTO(participant.getUser().getPhone(),
                                            participant.getUser().getUsername(),
                                            expensesService.getTotalExpenses(groupId, phone));
    }

   
}
