package com.backend.senior_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantRequest {
    private Long group_id;
    private String username;
}
