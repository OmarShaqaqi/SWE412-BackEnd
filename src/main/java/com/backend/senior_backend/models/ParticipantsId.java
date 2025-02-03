package com.backend.senior_backend.models;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ParticipantsId implements Serializable {
    private Long groupId;
    private String userPhone;
}
