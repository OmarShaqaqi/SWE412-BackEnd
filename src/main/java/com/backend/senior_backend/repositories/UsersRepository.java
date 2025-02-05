package com.backend.senior_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.senior_backend.models.Users;

public interface UsersRepository  extends JpaRepository<Users, String>{

      Optional<Users> findByEmail(String email);
      Optional<Users>findByUsername(String username);
      Optional<Users> findByPhone(String phone);

}
