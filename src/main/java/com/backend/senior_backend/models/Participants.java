package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participants {

    @EmbeddedId
    private ParticipantsId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @ManyToOne
    @MapsId("userPhone")
    @JoinColumn(name = "user_phone", nullable = false)
    private Users user;

    @Column(nullable = false, insertable = false)
    private boolean isLeader;
}
