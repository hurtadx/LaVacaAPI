package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {

    // Buscar logs por ID de transacción
    List<TransactionLog> findByTransactionId(UUID transactionId);

    // Buscar logs generados por un usuario específico
    List<TransactionLog> findByUserId(UUID userId);

    // Filtrar por tipo de acción (create, update, delete, etc.)
    List<TransactionLog> findByAction(String action);

    // Buscar logs por rango de fechas
    List<TransactionLog> findByTimestampBetween(Timestamp startDate, Timestamp endDate);

    // Buscar logs por usuario y acción
    List<TransactionLog> findByUserIdAndAction(UUID userId, String action);

    // Buscar logs por transacción y acción
    List<TransactionLog> findByTransactionIdAndAction(UUID transactionId, String action);

    // Encontrar los logs más recientes (útil para auditorías)
    List<TransactionLog> findTop20ByOrderByTimestampDesc();

    // Encontrar los últimos cambios a una transacción específica
    List<TransactionLog> findByTransactionIdOrderByTimestampDesc(UUID transactionId);

    // Encontrar logs recientes por usuario
    List<TransactionLog> findTop10ByUserIdOrderByTimestampDesc(UUID userId);

    // Contar logs por tipo de acción (estadísticas)
    @Query("SELECT t.action, COUNT(t) FROM TransactionLog t GROUP BY t.action")
    List<Object[]> countLogsByAction();

    // Buscar logs que contengan ciertos datos (útil para búsquedas en campos JSON)
    @Query("SELECT t FROM TransactionLog t WHERE t.old_data LIKE %:searchTerm% OR t.new_data LIKE %:searchTerm%")
    List<TransactionLog> findByDataContaining(@Param("searchTerm") String searchTerm);

    // Buscar logs para múltiples transacciones (útil para análisis por lotes)
    List<TransactionLog> findByTransactionIdIn(List<UUID> transactionIds);

    // Verificar si existe algún log para una transacción con una acción específica
    boolean existsByTransactionIdAndAction(UUID transactionId, String action);

    boolean existsByTransactionId(UUID transactionId, String cancel);

}
