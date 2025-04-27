package com.backend.senior_backend.dto;

import com.backend.senior_backend.models.Groups;

public class GroupWithRoleDTO {
    private Groups group;
    private boolean isLeader;

    public GroupWithRoleDTO(Groups group, boolean isLeader) {
        this.group = group;
        this.isLeader = isLeader;
    }

    // Getters and Setters
    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
