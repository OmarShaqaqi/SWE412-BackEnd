package com.backend.senior_backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.senior_backend.models.Groups;

public interface GroupRepo extends JpaRepository<Groups, Integer>{

}
