package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    // Buscar votos por vacaId
    List<Vote> findByVacaId(UUID vacaId);

    // Buscar votos por userId
    List<Vote> findByUserId(UUID userId);

    // Buscar votos por transactionId
    List<Vote> findByTransactionId(UUID transactionId);

    // Buscar por tipo de voto (approve/reject)
    List<Vote> findByVote(String vote);

    // Buscar votos combinando vacaId y tipo de voto
    List<Vote> findByVacaIdAndVote(UUID vacaId, String vote);

    // Buscar votos de un usuario en una vaca específica
    List<Vote> findByUserIdAndVacaId(UUID userId, UUID vacaId);

    // Buscar por fecha de creación
    List<Vote> findByCreatedAtAfter(Timestamp date);

    List<Vote> findByCreatedAtBefore(Timestamp date);
}