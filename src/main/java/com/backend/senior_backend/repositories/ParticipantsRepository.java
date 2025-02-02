package com.backend.senior_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.models.ParticipantsId;

public interface ParticipantsRepository extends JpaRepository<Participants, ParticipantsId>{

}
