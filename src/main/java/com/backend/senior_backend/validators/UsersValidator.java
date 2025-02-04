package com.backend.senior_backend.validators;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.backend.senior_backend.models.Users;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.Collections;


@Component
public class UsersValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


    public Set<String> validateSignUp(Users user) {
        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        if(!violations.isEmpty()){
            return violations
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

}
