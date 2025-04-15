package com.backend.senior_backend.repositories;

import com.backend.senior_backend.models.Complian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianRepository extends JpaRepository<Complian, Long> {
}
