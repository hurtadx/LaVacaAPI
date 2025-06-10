package com.lavacaapi.lavaca.invitations.controller;

import java.util.UUID;

public class InvitationActionResponse {
    private Data data;

    public InvitationActionResponse(UUID invitationId, String status, UUID vacaId, String vacaName, UUID senderId, String senderUsername) {
        this.data = new Data(invitationId, status, vacaId, vacaName, senderId, senderUsername);
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private UUID id;
        private String status;
        private Vaca vaca;
        private Sender sender;

        public Data(UUID invitationId, String status, UUID vacaId, String vacaName, UUID senderId, String senderUsername) {
            this.id = invitationId;
            this.status = status;
            this.vaca = new Vaca(vacaId, vacaName);
            this.sender = new Sender(senderId, senderUsername);
        }

        public UUID getId() { return id; }
        public String getStatus() { return status; }
        public Vaca getVaca() { return vaca; }
        public Sender getSender() { return sender; }
    }

    public static class Vaca {
        private UUID id;
        private String name;
        public Vaca(UUID id, String name) {
            this.id = id;
            this.name = name;
        }
        public UUID getId() { return id; }
        public String getName() { return name; }
    }

    public static class Sender {
        private UUID id;
        private String username;
        public Sender(UUID id, String username) {
            this.id = id;
            this.username = username;
        }
        public UUID getId() { return id; }
        public String getUsername() { return username; }
    }
}

