package com.backend.senior_backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.backend.senior_backend.models.Expenses;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.models.ParticipantsId;
import com.backend.senior_backend.models.Users;
import com.backend.senior_backend.repositories.ExpenseRepository;
import com.backend.senior_backend.repositories.ParticipantsRepository;
import com.backend.senior_backend.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantsService {

    private final ParticipantsRepository participantsRepository;
    private final UsersRepository usersRepository;
    private final ExpenseRepository expenseRepository;

    // ✅ Add a participant to a group
    public String addParticipant(String userPhone, Long groupId, boolean isLeader) {
        Optional<Users> userOpt = usersRepository.findById(userPhone);

        if (userOpt.isEmpty()) {
            return "User not found!";
        }

        ParticipantsId participantsId = new ParticipantsId(groupId, userPhone);
        if (participantsRepository.existsById(participantsId)) {
            return "User is already a participant in this group!";
        }

        Participants participant = new Participants(participantsId, null, userOpt.get(), isLeader);
        participantsRepository.save(participant);
        return "✅ Participant added successfully!";
    }

    // ✅ Get all participants in a group
    public List<Participants> getParticipantsByGroup(Long groupId) {
        return participantsRepository.findByGroup_Id(groupId);
    }

    // ✅ Remove a participant from a group
    public String removeParticipant(String userPhone, Long groupId) {
        ParticipantsId participantsId = new ParticipantsId(groupId, userPhone);
        Optional<Participants> participantOpt = participantsRepository.findById(participantsId);

        if (participantOpt.isEmpty()) {
            return "❌ Participant not found in this group!";
        }

        participantsRepository.deleteById(participantsId);
        return "✅ Participant removed successfully!";
    }

    // ✅ Get all expenses of a user in a group
    public List<Expenses> getUserExpensesInGroup(String userPhone, Long groupId) {
        return expenseRepository.findByUser_PhoneAndCategory_Group_Id(userPhone, groupId);
    }
}
