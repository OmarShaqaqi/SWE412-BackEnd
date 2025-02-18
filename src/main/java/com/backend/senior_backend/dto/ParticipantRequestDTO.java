package com.backend.senior_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantRequestDTO {
    private String userPhone;
    private Long groupId;
    private boolean isLeader;
}
