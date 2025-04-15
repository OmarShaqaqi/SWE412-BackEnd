package com.backend.senior_backend.service;

import com.backend.senior_backend.models.Complian;
import com.backend.senior_backend.repositories.ComplianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplainsService {

    @Autowired
    private ComplianRepository complianRepository;

    // Retrieve all complaints
    public List<Complian> getAllComplains() {
        return complianRepository.findAll();
    }

    // Create a new complaint
    public Complian createComplian(Complian complian) {
        return complianRepository.save(complian);
    }
}