package com.lavacaapi.lavaca.invitations.controller.dto;

import java.util.List;
import java.util.UUID;

public class InvitationRequestDTO {
    private UUID vaca_id;
    private List<UUID> user_ids;
    private UUID sender_id;

    public UUID getVaca_id() { return vaca_id; }
    public void setVaca_id(UUID vaca_id) { this.vaca_id = vaca_id; }
    public List<UUID> getUser_ids() { return user_ids; }
    public void setUser_ids(List<UUID> user_ids) { this.user_ids = user_ids; }
    public UUID getSender_id() { return sender_id; }
    public void setSender_id(UUID sender_id) { this.sender_id = sender_id; }
}
