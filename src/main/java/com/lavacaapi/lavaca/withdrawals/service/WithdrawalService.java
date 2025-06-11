package com.lavacaapi.lavaca.withdrawals.service;

import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.votes.Vote;
import com.lavacaapi.lavaca.votes.repository.VoteRepository;
import com.lavacaapi.lavaca.withdrawals.Withdrawal;
import com.lavacaapi.lavaca.withdrawals.dto.WithdrawalResultDTO;
import com.lavacaapi.lavaca.withdrawals.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VacasRepository vacasRepository;

    public Withdrawal createWithdrawal(Withdrawal withdrawal) {
        return withdrawalRepository.save(withdrawal);
    }

    public List<Withdrawal> getWithdrawalsByVacaId(UUID vacaId) {
        return withdrawalRepository.findByVacaId(vacaId);
    }

    public Optional<Withdrawal> getWithdrawalById(UUID id) {
        return withdrawalRepository.findById(id);
    }

    public WithdrawalResultDTO getWithdrawalResult(UUID withdrawalId) {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new RuntimeException("Retiro no encontrado"));
        Vacas vaca = vacasRepository.findById(withdrawal.getVacaId())
                .orElseThrow(() -> new RuntimeException("Vaca no encontrada"));
        // Reglas de votación desde la vaca
        String tipoRegla = vaca.getTipoRegla(); // mayoría_simple, 75, unanimidad
        int tiempoLimite = vaca.getTiempoLimiteVotacion();
        boolean poderVeto = vaca.isPoderVeto();
        double porcentajeVeto = vaca.getPorcentajeVeto();
        // Obtener votos
        var votos = voteRepository.findByWithdrawalId(withdrawalId);
        int totalVotos = votos.size();
        int aprobar = (int) votos.stream().filter(v -> "aprobar".equalsIgnoreCase(v.getVoto())).count();
        int rechazar = (int) votos.stream().filter(v -> "rechazar".equalsIgnoreCase(v.getVoto())).count();
        int abstener = (int) votos.stream().filter(v -> "abstenerse".equalsIgnoreCase(v.getVoto())).count();
        // Lógica de veto: si alguien con >70% rechaza, veto
        boolean veto = false;
        String motivo = null;
        if (poderVeto) {
            // Aquí deberías calcular el porcentaje de participación de cada usuario
            // Por simplicidad, asumimos que si hay un voto de rechazo, se revisa el veto
            for (Vote v : votos) {
                if ("rechazar".equalsIgnoreCase(v.getVoto())) {
                    // TODO: calcular porcentaje real de participación
                    double porcentajeParticipacion = 100.0; // Simulación
                    if (porcentajeParticipacion >= porcentajeVeto) {
                        veto = true;
                        motivo = "Veto aplicado por usuario con participación >= " + porcentajeVeto + "%";
                        break;
                    }
                }
            }
        }
        String estado = "pendiente";
        if (veto) {
            estado = "rechazado";
        } else if ("mayoría_simple".equalsIgnoreCase(tipoRegla)) {
            if (aprobar > rechazar) estado = "aprobado";
            else if (rechazar >= aprobar && rechazar > 0) estado = "rechazado";
        } else if ("75".equalsIgnoreCase(tipoRegla)) {
            if (totalVotos > 0 && (aprobar * 100.0 / totalVotos) >= 75) estado = "aprobado";
            else if (rechazar > 0) estado = "rechazado";
        } else if ("unanimidad".equalsIgnoreCase(tipoRegla)) {
            if (rechazar == 0 && aprobar == totalVotos && totalVotos > 0) estado = "aprobado";
            else if (rechazar > 0) estado = "rechazado";
        }
        if (estado.equals("aprobado")) motivo = "Retiro aprobado por votación";
        else if (estado.equals("rechazado") && motivo == null) motivo = "Retiro rechazado por votación";
        return new WithdrawalResultDTO(estado, motivo, veto);
    }
}
