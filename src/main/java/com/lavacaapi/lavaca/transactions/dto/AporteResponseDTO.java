package com.lavacaapi.lavaca.transactions.dto;

import com.lavacaapi.lavaca.transactions.Transactions;

public class AporteResponseDTO {
    private Transactions transaction;
    private double newTotal;
    private String username;
    private String email;

    public AporteResponseDTO(Transactions transaction, double newTotal, String username, String email) {
        this.transaction = transaction;
        this.newTotal = newTotal;
        this.username = username;
        this.email = email;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }

    public double getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(double newTotal) {
        this.newTotal = newTotal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

