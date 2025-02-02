package com.backend.senior_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.senior_backend.models.Users;

public interface UsersRepository  extends JpaRepository<Users, String>{

}
