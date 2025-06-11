package com.lavacaapi.lavaca.votes;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "withdrawal_id", nullable = false)
    private UUID withdrawalId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "voto", nullable = false)
    private String voto; // aprobar / rechazar / abstenerse

    @Column(name = "razon")
    private String razon;

    @Column(name = "fecha_voto", nullable = false)
    private Timestamp fechaVoto;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getWithdrawalId() { return withdrawalId; }
    public void setWithdrawalId(UUID withdrawalId) { this.withdrawalId = withdrawalId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getVoto() { return voto; }
    public void setVoto(String voto) { this.voto = voto; }
    public String getRazon() { return razon; }
    public void setRazon(String razon) { this.razon = razon; }
    public Timestamp getFechaVoto() { return fechaVoto; }
    public void setFechaVoto(Timestamp fechaVoto) { this.fechaVoto = fechaVoto; }
}

