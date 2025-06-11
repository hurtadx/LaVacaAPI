package com.lavacaapi.lavaca.withdrawals.dto;

public class WithdrawalResultDTO {
    private String estado;
    private String motivo;
    private boolean veto;

    public WithdrawalResultDTO(String estado, String motivo, boolean veto) {
        this.estado = estado;
        this.motivo = motivo;
        this.veto = veto;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public boolean isVeto() { return veto; }
    public void setVeto(boolean veto) { this.veto = veto; }
}
