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

/**
 * Servicio para manejar todas las operaciones relacionadas con transacciones (aportes y retiros)
 * en el sistema de Vacas.
 */
@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionRepository;

    @Autowired
    private TransactionLogsRepository transactionLogRepository;

    @Autowired
    private VacasRepository vacasRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    /**
     * Registra un nuevo aporte o retiro en una vaca
     *
     * @param transaction datos de la transacción
     * @return la transacción creada
     */
    @Transactional
    public Transactions createTransaction(Transactions transaction) {
        // Validamos que la vaca existe
        Vacas vaca = vacasRepository.findById(transaction.getVacaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + transaction.getVacaId()));

        // Validamos que el participante existe en la vaca
        Participants participant = participantsRepository.findById(transaction.getParticipantId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante con ID: " + transaction.getParticipantId()));

        // Verificamos que el participante pertenezca a la vaca
        if (!participant.getVacaId().equals(vaca.getId())) {
            throw new IllegalArgumentException("El participante no pertenece a esta vaca");
        }

        // Validación de tipo y monto
        if ("DEPOSIT".equals(transaction.getType()) && transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto del depósito debe ser mayor a 0");
        }
        if ("WITHDRAWAL".equals(transaction.getType()) && transaction.getAmount() >= 0) {
            throw new IllegalArgumentException("El monto del retiro debe ser menor a 0");
        }

        // Lógica de actualización de saldo
        BigDecimal currentAmount = vaca.getCurrent();
        BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getAmount());

        if ("WITHDRAWAL".equals(transaction.getType())) {
            // Para retiros, verificamos que haya saldo suficiente
            if (currentAmount.add(transactionAmount).compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente para el retiro");
            }
        }

        // Actualizamos el saldo de la vaca
        vaca.setCurrent(currentAmount.add(transactionAmount));

        // Si la vaca alcanza o supera su meta, actualizamos su estado
        if (vaca.getCurrent().compareTo(vaca.getGoal()) >= 0) {
            vaca.setStatus("COMPLETED");
        }

        vacasRepository.save(vaca);

        // Preparamos la transacción
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }

        if (transaction.getDate() == null) {
            transaction.setDate(new Timestamp(System.currentTimeMillis()));
        }

        // Guardamos la transacción
        Transactions savedTransaction = transactionRepository.save(transaction);

        // Registramos el log de la transacción
        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(savedTransaction.getId());
        log.setUserId(savedTransaction.getUserId());
        log.setAction("CREATE");
        log.setOld_data(null);
        log.setNew_data(String.format("{\"amount\": %d, \"type\": \"%s\", \"description\": \"%s\"}",
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription() != null ? savedTransaction.getDescription() : ""));
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionLogRepository.save(log);

        return savedTransaction;
    }

    /**
     * Obtiene todas las transacciones
     * @return lista de transacciones
     */
    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Obtiene una transacción por su ID
     * @param id ID de la transacción
     * @return transacción encontrada o empty si no existe
     */
    public Optional<Transactions> getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }

    /**
     * Obtiene todas las transacciones relacionadas con una vaca
     * @param vacaId ID de la vaca
     * @return lista de transacciones de la vaca
     */
    public List<Transactions> getTransactionsByVaca(UUID vacaId) {
        // Verificar que la vaca existe
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return transactionRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todas las transacciones realizadas por un usuario
     * @param userId ID del usuario
     * @return lista de transacciones del usuario
     */
    public List<Transactions> getTransactionsByUser(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Obtiene todas las transacciones de un participante
     * @param participantId ID del participante
     * @return lista de transacciones del participante
     */
    public List<Transactions> getTransactionsByParticipant(UUID participantId) {
        // Verificar que el participante existe
        if (!participantsRepository.existsById(participantId)) {
            throw new EntityNotFoundException("No se encontró el participante con ID: " + participantId);
        }
        return transactionRepository.findByParticipantId(participantId);
    }

    /**
     * Actualiza una transacción existente
     * @param id ID de la transacción a actualizar
     * @param transaction Datos actualizados
     * @return transacción actualizada
     */
    @Transactional
    public Transactions updateTransaction(UUID id, Transactions transaction) {
        Transactions existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la transacción con ID: " + id));

        // Guardar el estado anterior para el log
        String oldData = String.format("{\"amount\": %d, \"type\": \"%s\", \"description\": \"%s\"}",
                existingTransaction.getAmount(),
                existingTransaction.getType(),
                existingTransaction.getDescription() != null ? existingTransaction.getDescription() : "");

        // No permitir cambiar el ID, vacaId o participantId
        transaction.setId(id);
        transaction.setVacaId(existingTransaction.getVacaId());
        transaction.setParticipantId(existingTransaction.getParticipantId());

        // Si hay cambio en el monto, actualizar el saldo de la vaca
        if (existingTransaction.getAmount() != transaction.getAmount()) {
            Vacas vaca = vacasRepository.findById(existingTransaction.getVacaId())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca"));

            // Revertir el monto anterior
            BigDecimal currentAmount = vaca.getCurrent();
            BigDecimal oldAmount = BigDecimal.valueOf(existingTransaction.getAmount());
            BigDecimal newAmount = BigDecimal.valueOf(transaction.getAmount());

            // Restar el monto anterior y sumar el nuevo
            vaca.setCurrent(currentAmount.subtract(oldAmount).add(newAmount));

            // Verificar si el nuevo saldo es suficiente (para evitar saldos negativos)
            if (vaca.getCurrent().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("La actualización resultaría en un saldo negativo");
            }

            // Actualizar estado de la vaca si es necesario
            if (vaca.getCurrent().compareTo(vaca.getGoal()) >= 0) {
                vaca.setStatus("COMPLETED");
            } else if ("COMPLETED".equals(vaca.getStatus())) {
                vaca.setStatus("ACTIVE");
            }

            vacasRepository.save(vaca);
        }

        // Actualizar y guardar la transacción
        Transactions updatedTransaction = transactionRepository.save(transaction);

        // Registrar log de la actualización
        String newData = String.format("{\"amount\": %d, \"type\": \"%s\", \"description\": \"%s\"}",
                updatedTransaction.getAmount(),
                updatedTransaction.getType(),
                updatedTransaction.getDescription() != null ? updatedTransaction.getDescription() : "");

        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(id);
        log.setUserId(transaction.getUserId());
        log.setAction("UPDATE");
        log.setOld_data(oldData);
        log.setNew_data(newData);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionLogRepository.save(log);

        return updatedTransaction;
    }

    /**
     * Elimina una transacción
     * @param id ID de la transacción a eliminar
     */
    @Transactional
    public void deleteTransaction(UUID id) {
        Transactions transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la transacción con ID: " + id));

        // Revertir el monto en la vaca
        Vacas vaca = vacasRepository.findById(transaction.getVacaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca"));

        BigDecimal currentAmount = vaca.getCurrent();
        BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getAmount());

        // Restar el monto de la transacción del saldo actual
        vaca.setCurrent(currentAmount.subtract(transactionAmount));

        // Verificar si el nuevo saldo es suficiente (para evitar saldos negativos)
        if (vaca.getCurrent().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La eliminación resultaría en un saldo negativo");
        }

        // Actualizar estado de la vaca si es necesario
        if (vaca.getCurrent().compareTo(vaca.getGoal()) < 0 && "COMPLETED".equals(vaca.getStatus())) {
            vaca.setStatus("ACTIVE");
        }

        vacasRepository.save(vaca);

        // Registrar log de la eliminación
        String oldData = String.format("{\"amount\": %d, \"type\": \"%s\", \"description\": \"%s\"}",
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription() != null ? transaction.getDescription() : "");

        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(id);
        log.setUserId(transaction.getUserId());
        log.setAction("DELETE");
        log.setOld_data(oldData);
        log.setNew_data(null);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionLogRepository.save(log);

        // Eliminar la transacción
        transactionRepository.deleteById(id);
    }

    /**
     * Obtiene estadísticas de transacciones para una vaca
     * @param vacaId ID de la vaca
     * @return Mapa con estadísticas (total aportado, promedio, mayor aporte, etc)
     */
    public Map<String, Object> getTransactionStats(UUID vacaId) {
        // Verificar que la vaca existe
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }

        List<Transactions> transactions = transactionRepository.findByVacaId(vacaId);

        int totalDeposits = 0;
        int totalWithdrawals = 0;
        int largestDeposit = 0;
        int largestWithdrawal = 0;
        int depositCount = 0;
        int withdrawalCount = 0;

        for (Transactions t : transactions) {
            if ("DEPOSIT".equals(t.getType())) {
                totalDeposits += t.getAmount();
                depositCount++;
                if (t.getAmount() > largestDeposit) {
                    largestDeposit = t.getAmount();
                }
            } else if ("WITHDRAWAL".equals(t.getType())) {
                totalWithdrawals += Math.abs(t.getAmount());
                withdrawalCount++;
                if (Math.abs(t.getAmount()) > largestWithdrawal) {
                    largestWithdrawal = Math.abs(t.getAmount());
                }
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("transactionCount", transactions.size());
        stats.put("depositCount", depositCount);
        stats.put("withdrawalCount", withdrawalCount);
        stats.put("totalDeposits", totalDeposits);
        stats.put("totalWithdrawals", totalWithdrawals);
        stats.put("netAmount", totalDeposits - totalWithdrawals);
        stats.put("largestDeposit", largestDeposit);
        stats.put("largestWithdrawal", largestWithdrawal);
        stats.put("averageDeposit", depositCount > 0 ? totalDeposits / depositCount : 0);
        stats.put("averageWithdrawal", withdrawalCount > 0 ? totalWithdrawals / withdrawalCount : 0);

        return stats;
    }

    /**
     * Obtiene las transacciones más recientes
     * @param limit cantidad de transacciones a obtener
     * @return lista de transacciones
     */
    public List<Transactions> getRecentTransactions(int limit) {
        List<Transactions> allTransactions = transactionRepository.findAll();
        allTransactions.sort(Comparator.comparing(Transactions::getDate).reversed());

        if (limit > 0 && limit < allTransactions.size()) {
            return allTransactions.subList(0, limit);
        }

        return allTransactions;
    }

    /**
     * Aprueba una transacción
     * @param id ID de la transacción
     * @param approvedBy ID del usuario que aprueba
     * @return transacción aprobada
     */
    @Transactional
    public Transactions approveTransaction(UUID id, UUID approvedBy) {
        Transactions transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la transacción con ID: " + id));

        // Guardar el estado anterior para el log
        String oldData = String.format("{\"approved_by\": \"%s\"}",
                transaction.getApprovedBy() != null ? transaction.getApprovedBy() : "null");

        // Actualizar el campo approvedBy
        transaction.setApprovedBy(approvedBy);

        // Guardar la transacción
        Transactions updatedTransaction = transactionRepository.save(transaction);

        // Registrar log de la aprobación
        String newData = String.format("{\"approved_by\": \"%s\"}", approvedBy);

        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(id);
        log.setUserId(approvedBy);
        log.setAction("APPROVE");
        log.setOld_data(oldData);
        log.setNew_data(newData);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionLogRepository.save(log);

        return updatedTransaction;
    }

    /**
     * Obtiene las transacciones pendientes de aprobación de una vaca
     * @param vacaId ID de la vaca
     * @return lista de transacciones pendientes
     */
    public List<Transactions> getPendingTransactionsByVaca(UUID vacaId) {
        // Consideramos
        return transactionRepository.findByVacaId(vacaId).stream()
                .filter(t -> t.getApprovedBy() == null)
                .toList();
    }

    /**
     * Rechaza una transacción
     * @param id ID de la transacción
     * @param rejectedBy ID del usuario que rech
