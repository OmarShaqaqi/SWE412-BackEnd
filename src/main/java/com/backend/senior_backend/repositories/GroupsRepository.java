package com.backend.senior_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.senior_backend.models.Groups;

public interface GroupsRepository extends JpaRepository<Groups, Long>{
}
