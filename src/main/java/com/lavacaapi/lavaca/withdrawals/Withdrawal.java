package com.lavacaapi.lavaca.withdrawals;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "withdrawals")
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "vaca_id", nullable = false)
    private UUID vacaId;

    @Column(name = "solicitante_id", nullable = false)
    private UUID solicitanteId;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo", nullable = false)
    private String tipo; // personal / comunitario

    @Column(name = "estado", nullable = false)
    private String estado; // pendiente / aprobado / rechazado / cancelado

    @Column(name = "fecha_solicitud", nullable = false)
    private Timestamp fechaSolicitud;

    @Column(name = "fecha_resolucion")
    private Timestamp fechaResolucion;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getVacaId() { return vacaId; }
    public void setVacaId(UUID vacaId) { this.vacaId = vacaId; }
    public UUID getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(UUID solicitanteId) { this.solicitanteId = solicitanteId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Timestamp getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Timestamp fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public Timestamp getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(Timestamp fechaResolucion) { this.fechaResolucion = fechaResolucion; }
}

