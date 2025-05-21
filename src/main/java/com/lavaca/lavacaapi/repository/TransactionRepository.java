package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Buscar por ID de vaca
    List<Transaction> findByVacaId(UUID vacaId);

    // Buscar por ID de usuario
    List<Transaction> findByUserId(UUID userId);

    // Buscar por ID de participante
    List<Transaction> findByParticipantId(UUID participantId);

    // Buscar por tipo de transacción
    List<Transaction> findByType(String type);

    // Buscar por método de pago
    List<Transaction> findByPaymentMethod(String paymentMethod);

    // Buscar transacciones dentro de un rango de fechas
    List<Transaction> findByDateBetween(Timestamp startDate, Timestamp endDate);

    // Buscar transacciones por monto mayor a un valor
    List<Transaction> findByAmountGreaterThan(int amount);

    // Búsqueda combinada: por vacaId y tipo
    List<Transaction> findByVacaIdAndType(UUID vacaId, String type);
}
