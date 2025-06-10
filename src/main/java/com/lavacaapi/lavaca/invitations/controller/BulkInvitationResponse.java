package com.lavacaapi.lavaca.invitations.controller;

import java.util.List;
import java.util.UUID;

public class BulkInvitationResponse {
    public static class InvitationInfo {
        private UUID userId;
        private String username;
        private String message;

        public InvitationInfo(UUID userId, String username, String message) {
            this.userId = userId;
            this.username = username;
            this.message = message;
        }

        public UUID getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }
    }

    private List<InvitationInfo> invitations;
    private SenderInfo sender;
    private VacaInfo vaca;

    public BulkInvitationResponse(List<InvitationInfo> invitations, SenderInfo sender, VacaInfo vaca) {
        this.invitations = invitations;
        this.sender = sender;
        this.vaca = vaca;
    }

    public List<InvitationInfo> getInvitations() {
        return invitations;
    }

    public SenderInfo getSender() {
        return sender;
    }

    public VacaInfo getVaca() {
        return vaca;
    }

    public static class SenderInfo {
        private UUID userId;
        private String username;

        public SenderInfo(UUID userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public UUID getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class VacaInfo {
        private UUID vacaId;
        private String vacaName;

        public VacaInfo(UUID vacaId, String vacaName) {
            this.vacaId = vacaId;
            this.vacaName = vacaName;
        }

        public UUID getVacaId() {
            return vacaId;
        }

        public String getVacaName() {
            return vacaName;
        }
    }
}

