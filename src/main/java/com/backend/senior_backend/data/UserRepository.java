package com.backend.senior_backend.data;

import com.backend.senior_backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByPhone(String phone);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
}
