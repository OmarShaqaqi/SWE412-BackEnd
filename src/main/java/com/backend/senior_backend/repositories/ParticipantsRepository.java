package com.backend.senior_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.models.ParticipantsId;

public interface ParticipantsRepository extends JpaRepository<Participants, ParticipantsId>{
    List<Participants> findAllByUserPhone(String phone);
}
