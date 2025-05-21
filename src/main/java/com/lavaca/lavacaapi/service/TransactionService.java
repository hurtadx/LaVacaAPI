package com.lavaca.lavacaapi.service;

import com.lavaca.lavacaapi.dto.TransactionDTO;
import com.lavaca.lavacaapi.model.Transaction;
import com.lavaca.lavacaapi.model.TransactionLog;
import com.lavaca.lavacaapi.model.Vaca;
import com.lavaca.lavacaapi.model.Participant;
import com.lavaca.lavacaapi.repository.TransactionRepository;
import com.lavaca.lavacaapi.repository.TransactionLogRepository;
import com.lavaca.lavacaapi.repository.VacaRepository;
import com.lavaca.lavacaapi.repository.ParticipantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio para manejar todas las operaciones relacionadas con transacciones (aportes y retiros)
 * en el sistema de Vacas.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private VacaRepository vacaRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * Registra un nuevo aporte o retiro en una vaca
     *
     * @param data datos de la transacción
     * @return la transacción creada
     */
    @Transactional
    public Transaction createTransaction(TransactionDTO data) {
        // Validamos que la vaca existe
        Vaca vaca = vacaRepository.findById(data.getVacaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + data.getVacaId()));

        // Validamos que el participante existe en la vaca
        Participant participant = participantRepository.findById(data.getParticipantId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante"));

        if (!participant.getVacaId().equals(vaca.getId())) {
            throw new IllegalArgumentException("El participante no pertenece a esta vaca");
        }

        // Validamos el monto según el tipo de transacción
        if ("DEPOSIT".equals(data.getType()) && data.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto del aporte debe ser mayor a cero");
        }

        if ("WITHDRAWAL".equals(data.getType()) && data.getAmount() >= 0) {
            throw new IllegalArgumentException("El monto del retiro debe ser negativo");
        }

        // Si es un retiro, validamos que haya fondos suficientes
        if ("WITHDRAWAL".equals(data.getType())) {
            BigDecimal currentAmount = vaca.getCurrent();
            BigDecimal withdrawalAmount = BigDecimal.valueOf(Math.abs(data.getAmount()));

            if (currentAmount.compareTo(withdrawalAmount) < 0) {
                throw new IllegalStateException("Fondos insuficientes para realizar el retiro");
            }
        }

        // Creamos la nueva transacción
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setVacaId(data.getVacaId());
        transaction.setParticipantId(data.getParticipantId());
        transaction.setAmount(data.getAmount());
        transaction.setDescription(data.getDescription());
        transaction.setDate(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setUserId(data.getUserId());
        transaction.setType(data.getType());
        transaction.setPaymentMethod(data.getPaymentMethod());

        // Guardamos la transacción
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Actualizamos el monto actual de la vaca
        updateVacaAmount(vaca, data.getAmount());

        // Registramos el log de la transacción
        logTransactionActivity(savedTransaction, "CREATE", null, transaction);

        return savedTransaction;
    }

    /**
     * Obtiene todas las transacciones de una vaca
     *
     * @param vacaId ID de la vaca
     * @return lista de transacciones
     */
    public List<Transaction> getTransactionsByVaca(UUID vacaId) {
        // Primero comprobamos que la vaca existe
        if (!vacaRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }

        return transactionRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todas las transacciones realizadas por un usuario
     *
     * @param userId ID del usuario
     * @return lista de transacciones
     */
    public List<Transaction> getTransactionsByUser(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Obtiene los detalles de una transacción específica
     *
     * @param transactionId ID de la transacción
     * @return detalles de la transacción
     */
    public Transaction getTransactionDetails(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la transacción con ID: " + transactionId));
    }

    /**
     * Calcula el porcentaje de participación de un usuario en una vaca
     *
     * @param userId ID del usuario
     * @param vacaId ID de la vaca
     * @return mapa con datos de participación
     */
    public Map<String, Object> getUserParticipationPercentage(UUID userId, UUID vacaId) {
        // Validamos que la vaca existe
        Vaca vaca = vacaRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));

        // Validamos que el usuario es participante de la vaca
        if (!participantRepository.existsByVacaIdAndUserId(vacaId, userId)) {
            throw new IllegalArgumentException("El usuario no es participante de esta vaca");
        }

        // Obtenemos el participante del usuario en esta vaca
        Participant participant = participantRepository.findByVacaIdAndUserId(vacaId, userId)
                .orElseThrow(() -> new IllegalStateException("Error al obtener datos del participante"));

        // Calculamos el total aportado por el usuario
        List<Transaction> userTransactions = transactionRepository.findByVacaIdAndType(vacaId, "DEPOSIT");
        int userTotalAmount = userTransactions.stream()
                .filter(t -> t.getParticipantId().equals(participant.getId())) // Filtramos por el participante específico
                .mapToInt(Transaction::getAmount)
                .sum();

        // Obtenemos el monto actual de la vaca
        BigDecimal vacaTotal = vaca.getCurrent();

        // Calculamos el porcentaje
        BigDecimal percentage = BigDecimal.ZERO;
        if (vacaTotal.compareTo(BigDecimal.ZERO) > 0) {
            percentage = BigDecimal.valueOf(userTotalAmount)
                    .divide(vacaTotal, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // Preparamos la respuesta
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("vacaId", vacaId);
        result.put("participantId", participant.getId());
        result.put("username", participant.getName());
        result.put("totalAmount", userTotalAmount);
        result.put("vacaTotal", vacaTotal);
        result.put("percentage", percentage);

        return result;
    }

    /**
     * Cancela o revierte una transacción
     *
     * @param transactionId ID de la transacción
     * @param userId ID del usuario que realiza la cancelación
     */
    @Transactional
    public void cancelTransaction(UUID transactionId, UUID userId) {
        // Obtenemos la transacción
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la transacción con ID: " + transactionId));

        // Verificamos que la transacción esté en un estado que permita cancelación
        if (transactionLogRepository.existsByTransactionId(transactionId, "CANCEL")) {
            throw new IllegalStateException("Esta transacción ya ha sido cancelada");
        }

        // Revertimos el monto en la vaca
        Vaca vaca = vacaRepository.findById(transaction.getVacaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca"));

        // Si era un depósito, restamos; si era un retiro, sumamos
        int amountToReverse = -transaction.getAmount();  // Invertimos el signo

        updateVacaAmount(vaca, amountToReverse);

        // Registramos la cancelación en el log
        logTransactionActivity(transaction, "CANCEL", userId, null);
    }

    /**
     * Obtiene el historial de cambios de una transacción
     *
     * @param transactionId ID de la transacción
     * @return lista de logs de la transacción
     */
    public List<TransactionLog> getTransactionHistory(UUID transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new EntityNotFoundException("No se encontró la transacción con ID: " + transactionId);
        }

        return transactionLogRepository.findByTransactionIdOrderByTimestampDesc(transactionId);
    }

    /**
     * Actualiza el monto actual de una vaca
     *
     * @param vaca entidad vaca
     * @param amount monto a sumar/restar
     */
    private void updateVacaAmount(Vaca vaca, int amount) {
        BigDecimal newAmount = vaca.getCurrent().add(BigDecimal.valueOf(amount));

        // Aseguramos que el monto no sea negativo
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("El monto resultante no puede ser negativo");
        }

        vaca.setCurrent(newAmount);

        // Si se alcanzó o superó la meta, actualizamos el estado
        if (newAmount.compareTo(vaca.getGoal()) >= 0) {
            vaca.setStatus("COMPLETED");
        }

        vacaRepository.save(vaca);
    }

    /**
     * Registra una actividad en el log de transacciones
     *
     * @param transaction transacción afectada
     * @param action tipo de acción realizada
     * @param performedBy usuario que realiza la acción
     * @param newData nuevos datos (si aplica)
     */
    private void logTransactionActivity(Transaction transaction, String action, UUID performedBy, Transaction newData) {
        TransactionLog log = new TransactionLog();
        log.setId(UUID.randomUUID());
        log.setTransactionId(transaction.getId());
        log.setUserId(performedBy != null ? performedBy : transaction.getUserId());
        log.setAction(action);
        log.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        // Convertimos los datos a formato JSON (aquí usamos un enfoque simple)
        String oldDataStr = String.format(
                "{\"amount\":%d,\"type\":\"%s\",\"description\":\"%s\"}",
                transaction.getAmount(), transaction.getType(),
                transaction.getDescription() != null ? transaction.getDescription() : "");
        log.setOld_data(oldDataStr);

        if (newData != null) {
            String newDataStr = String.format(
                    "{\"amount\":%d,\"type\":\"%s\",\"description\":\"%s\"}",
                    newData.getAmount(), newData.getType(),
                    newData.getDescription() != null ? newData.getDescription() : "");
            log.setNew_data(newDataStr);
        }

        transactionLogRepository.save(log);
    }

    /**
     * Obtiene las transacciones recientes de todas las vacas
     *
     * @param limit número máximo de transacciones a devolver
     * @return lista de transacciones
     */
    public List<Transaction> getRecentTransactions(int limit) {
        // Buscamos las transacciones ordenadas por fecha (la más reciente primero)
        List<Transaction> allTransactions = transactionRepository.findAll();

        // Ordenamos por fecha de manera descendente
        allTransactions.sort(Comparator.comparing(Transaction::getDate).reversed());

        // Limitamos el número de resultados
        return allTransactions.stream()
                .limit(limit)
                .toList();
    }

    /**
     * Calcula estadísticas de transacciones para una vaca
     *
     * @param vacaId ID de la vaca
     * @return mapa con estadísticas
     */
    public Map<String, Object> getTransactionStats(UUID vacaId) {
        // Validamos que la vaca existe
        Vaca vaca = vacaRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));

        List<Transaction> transactions = transactionRepository.findByVacaId(vacaId);

        // Total de transacciones
        long totalCount = transactions.size();

        // Total de aportes
        long depositsCount = transactions.stream()
                .filter(t -> "DEPOSIT".equals(t.getType()))
                .count();

        // Total de retiros
        long withdrawalsCount = transactions.stream()
                .filter(t -> "WITHDRAWAL".equals(t.getType()))
                .count();

        // Monto total depositado
        int totalDeposited = transactions.stream()
                .filter(t -> "DEPOSIT".equals(t.getType()))
                .mapToInt(Transaction::getAmount)
                .sum();

        // Monto total retirado
        int totalWithdrawn = transactions.stream()
                .filter(t -> "WITHDRAWAL".equals(t.getType()))
                .mapToInt(Transaction::getAmount)
                .sum();

        // Preparamos la respuesta
        Map<String, Object> stats = new HashMap<>();
        stats.put("vacaId", vacaId);
        stats.put("vacaName", vaca.getName());
        stats.put("totalTransactionsCount", totalCount);
        stats.put("depositsCount", depositsCount);
        stats.put("withdrawalsCount", withdrawalsCount);
        stats.put("totalDeposited", totalDeposited);
        stats.put("totalWithdrawn", Math.abs(totalWithdrawn));  // Valor absoluto para mejor legibilidad
        stats.put("currentAmount", vaca.getCurrent());
        stats.put("goal", vaca.getGoal());

        // Calculamos el porcentaje de avance
        BigDecimal progressPercentage = BigDecimal.ZERO;
        if (vaca.getGoal().compareTo(BigDecimal.ZERO) > 0) {
            progressPercentage = vaca.getCurrent()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(vaca.getGoal(), 2, RoundingMode.HALF_UP);
        }
        stats.put("progressPercentage", progressPercentage);

        return stats;
    }
}
