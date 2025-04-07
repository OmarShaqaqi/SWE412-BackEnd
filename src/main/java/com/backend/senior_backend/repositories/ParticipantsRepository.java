package com.backend.senior_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.senior_backend.models.Participants;
import com.backend.senior_backend.models.ParticipantsId;

public interface ParticipantsRepository extends JpaRepository<Participants, ParticipantsId>{
    List<Participants> findAllByUserPhone(String phone);
    Participants findByGroupIdAndUserPhone(Long groupId, String userPhone);
    List<Participants> findAllByGroupId(int groupId);
    @Query("SELECT p.id.groupId FROM Participants p WHERE p.id.userPhone = :phone")
    List<Long> findGroupIdsByUserPhone(@Param("phone") String phone);

}
