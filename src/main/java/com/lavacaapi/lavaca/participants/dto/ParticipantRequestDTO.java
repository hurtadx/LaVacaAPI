package com.lavacaapi.lavaca.participants.dto;

import java.util.UUID;

public class ParticipantRequestDTO {
    private UUID user_id;
    private String name;
    private String email;

    public UUID getUser_id() { return user_id; }
    public void setUser_id(UUID user_id) { this.user_id = user_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

