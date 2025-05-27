package com.lavacaapi.lavaca.transaction_logs.repository;

import com.lavacaapi.lavaca.transaction_logs.TransactionLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, UUID> {

    // Buscar logs por ID de transacción
    List<TransactionLogs> findByTransactionId(UUID transactionId);

    // Buscar logs por ID de usuario
    List<TransactionLogs> findByUserId(UUID userId);

    // Buscar logs por tipo de acción
    List<TransactionLogs> findByAction(String action);

    // Buscar logs por transacción y acción
    boolean existsByTransactionIdAndAction(UUID transactionId, String action);

    // Buscar logs ordenados por fecha
    List<TransactionLogs> findByTransactionIdOrderByTimestampDesc(UUID transactionId);

    // Buscar logs por fecha
    List<TransactionLogs> findByTimestampAfter(Timestamp timestamp);
    List<TransactionLogs> findByTimestampBefore(Timestamp timestamp);
    List<TransactionLogs> findByTimestampBetween(Timestamp startDate, Timestamp endDate);

    // Encontrar logs más recientes
    List<TransactionLogs> findTop10ByOrderByTimestampDesc();

    // Buscar con paginación para optimizar consultas de grandes volúmenes
    Page<TransactionLogs> findByTransactionId(UUID transactionId, Pageable pageable);
    Page<TransactionLogs> findByUserId(UUID userId, Pageable pageable);
    Page<TransactionLogs> findByAction(String action, Pageable pageable);

    // Métodos de conteo
    @Query("SELECT COUNT(t) FROM TransactionLogs t WHERE t.action = :action")
    long countByAction(@Param("action") String action);

    @Query("SELECT COUNT(t) FROM TransactionLogs t WHERE t.transactionId = :transactionId")
    long countByTransactionId(@Param("transactionId") UUID transactionId);

    // Buscar por múltiples acciones
    List<TransactionLogs> findByActionIn(List<String> actions);

    // Eliminar logs antiguos (útil para mantenimiento)
    void deleteByTimestampBefore(Timestamp timestamp);
}
