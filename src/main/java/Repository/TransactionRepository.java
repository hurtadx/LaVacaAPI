package Repository;

import Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Buscar por ID de vaca
    List<Transaction> findByVaca_id(String vacaId);

    // Buscar por ID de usuario
    List<Transaction> findByUser_id(UUID userId);

    // Buscar por ID de participante
    List<Transaction> findByParticipant_id(UUID participantId);

    // Buscar por tipo de transacción
    List<Transaction> findByType(String type);

    // Buscar por método de pago
    List<Transaction> findByPayment_method(String paymentMethod);

    // Buscar transacciones dentro de un rango de fechas
    List<Transaction> findByDateBetween(Timestamp startDate, Timestamp endDate);

    // Buscar transacciones por monto mayor a un valor
    List<Transaction> findByAmountGreaterThan(int amount);

    // Búsqueda combinada: por vaca_id y tipo
    List<Transaction> findByVaca_idAndType(String vacaId, String type);

    // Consulta personalizada para encontrar las transacciones más recientes para una vaca específica
    @Query("SELECT t FROM Transaction t WHERE t.vaca_id = :vacaId ORDER BY t.date DESC")
    List<Transaction> findRecentTransactionsByVacaId(@Param("vacaId") String vacaId);

    // Consulta para suma total de transacciones por vaca_id
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.vaca_id = :vacaId")
    Integer getTotalAmountByVacaId(@Param("vacaId") String vacaId);


}