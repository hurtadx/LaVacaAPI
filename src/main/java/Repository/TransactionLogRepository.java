package Repository;

import Model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {

    // Buscar logs por ID de transacción
    List<TransactionLog> findByTransaction_id(UUID transactionId);

    // Buscar logs generados por un usuario específico
    List<TransactionLog> findByUser_id(UUID userId);

    // Filtrar por tipo de acción (create, update, delete, etc.)
    List<TransactionLog> findByAction(String action);

    // Buscar logs por rango de fechas
    List<TransactionLog> findByTimestampBetween(Timestamp startDate, Timestamp endDate);

    // Buscar logs por usuario y acción
    List<TransactionLog> findByUser_idAndAction(UUID userId, String action);

    // Buscar logs por transacción y acción
    List<TransactionLog> findByTransaction_idAndAction(UUID transactionId, String action);

    // Encontrar los logs más recientes (útil para auditorías)
    List<TransactionLog> findTop20ByOrderByTimestampDesc();

    // Encontrar los últimos cambios a una transacción específica
    List<TransactionLog> findByTransaction_idOrderByTimestampDesc(UUID transactionId);

    // Encontrar logs recientes por usuario
    List<TransactionLog> findTop10ByUser_idOrderByTimestampDesc(UUID userId);

    // Contar logs por tipo de acción (estadísticas)
    @Query("SELECT t.action, COUNT(t) FROM TransactionLog t GROUP BY t.action")
    List<Object[]> countLogsByAction();

    // Buscar logs que contengan ciertos datos (útil para búsquedas en campos JSON)
    @Query("SELECT t FROM TransactionLog t WHERE t.old_data LIKE %:searchTerm% OR t.new_data LIKE %:searchTerm%")
    List<TransactionLog> findByDataContaining(@Param("searchTerm") String searchTerm);

    // Buscar logs para múltiples transacciones (útil para análisis por lotes)
    List<TransactionLog> findByTransaction_idIn(List<UUID> transactionIds);

    // Verificar si existe algún log para una transacción
    boolean existsByTransaction_id(UUID transactionId);

    // Contar número de cambios por transacción
    @Query("SELECT COUNT(t) FROM TransactionLog t WHERE t.transaction_id = :transactionId")
    Long countChangesByTransaction(@Param("transactionId") UUID transactionId);

    //estos metodos pueden servir para:

}