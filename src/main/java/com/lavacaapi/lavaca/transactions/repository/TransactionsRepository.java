package com.lavacaapi.lavaca.transactions.repository;

import com.lavacaapi.lavaca.transactions.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<Transactions, UUID> {

    // Buscar por ID de vaca
    List<Transactions> findByVacaId(UUID vacaId);

    // Buscar por ID de usuario
    List<Transactions> findByUserId(UUID userId);

    // Buscar por ID de participante
    List<Transactions> findByParticipantId(UUID participantId);

    // Buscar por tipo de transacción
    List<Transactions> findByType(String type);

    // Buscar por método de pago
    List<Transactions> findByPaymentMethod(String paymentMethod);
}
