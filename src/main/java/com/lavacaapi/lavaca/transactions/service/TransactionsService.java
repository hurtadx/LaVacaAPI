package com.lavacaapi.lavaca.transactions.service;

import com.lavacaapi.lavaca.transactions.Transactions;
import com.lavacaapi.lavaca.transaction_logs.TransactionLogs;
import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.transactions.repository.TransactionsRepository;
import com.lavacaapi.lavaca.transaction_logs.repository.TransactionLogsRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.participants.repository.ParticipantsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.lavacaapi.lavaca.transactions.dto.AporteRequestDTO;
import com.lavacaapi.lavaca.transactions.dto.AporteResponseDTO;
import com.lavacaapi.lavaca.profiles.repository.ProfilesRepository;
import com.lavacaapi.lavaca.profiles.Profiles;

@Service
public class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private VacasRepository vacasRepository;
    @Autowired
    private ParticipantsRepository participantsRepository;
    @Autowired
    private ProfilesRepository profilesRepository;

    @Transactional
    public AporteResponseDTO createAporte(AporteRequestDTO aporteRequestDTO) {
        System.out.println("[APORTE] Payload recibido en backend: " + aporteRequestDTO);
        // Validar existencia de la vaca
        Vacas vaca = vacasRepository.findById(aporteRequestDTO.getVacaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + aporteRequestDTO.getVacaId()));
        // Validar existencia del participante
        Participants participant = participantsRepository.findById(aporteRequestDTO.getParticipantId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante con ID: " + aporteRequestDTO.getParticipantId()));
        if (!participant.getVacaId().equals(vaca.getId())) {
            throw new IllegalArgumentException("El participante no pertenece a esta vaca");
        }
        if (aporteRequestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto del aporte debe ser mayor a 0");
        }
        // Actualizar saldo de la vaca
        BigDecimal currentAmount = vaca.getCurrent();
        BigDecimal transactionAmount = BigDecimal.valueOf(aporteRequestDTO.getAmount());
        vaca.setCurrent(currentAmount.add(transactionAmount));
        if (vaca.getCurrent().compareTo(vaca.getGoal()) >= 0) {
            vaca.setStatus("COMPLETED");
        }
        vacasRepository.save(vaca);
        // Crear y guardar la transacción
        Transactions transaction = new Transactions();
        transaction.setId(UUID.randomUUID());
        transaction.setVacaId(vaca.getId());
        transaction.setParticipantId(participant.getId());
        transaction.setAmount(aporteRequestDTO.getAmount());
        transaction.setDescription(aporteRequestDTO.getDescription());
        transaction.setType(aporteRequestDTO.getType());
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        transaction.setUserId(aporteRequestDTO.getUserId());
        Transactions savedTransaction = transactionsRepository.save(transaction);
        // Obtener perfil del usuario
        Profiles profile = profilesRepository.findByUserId(aporteRequestDTO.getUserId())
                .orElse(null);
        String username = profile != null ? profile.getUsername() : null;
        String email = profile != null ? profile.getEmail() : null;
        double newTotal = vaca.getCurrent().doubleValue();
        return new AporteResponseDTO(savedTransaction, newTotal, username, email);
    }

    public List<Transactions> getTransactionsByVaca(UUID vacaId) {
        return transactionsRepository.findByVacaId(vacaId);
    }

    public Transactions createTransaction(Transactions transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        return transactionsRepository.save(transaction);
    }

    public List<Transactions> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    public Optional<Transactions> getTransactionById(UUID id) {
        return transactionsRepository.findById(id);
    }
}
