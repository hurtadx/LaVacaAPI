package com.lavacaapi.lavaca.invitations.controller;

import java.util.List;
import java.util.UUID;

public class BulkInvitationRequest {
    private UUID vacaId;
    private UUID senderId;
    private List<UUID> userIds;

    public UUID getVacaId() {
        return vacaId;
    }

    public void setVacaId(UUID vacaId) {
        this.vacaId = vacaId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public List<UUID> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<UUID> userIds) {
        this.userIds = userIds;
    }
}

