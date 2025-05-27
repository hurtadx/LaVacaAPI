package com.lavacaapi.lavaca.votes.repository;

import com.lavacaapi.lavaca.votes.Votes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface VotesRepository extends JpaRepository<Votes, UUID> {

    // Buscar votos por vacaId
    List<Votes> findByVacaId(UUID vacaId);

    // Buscar votos por userId
    List<Votes> findByUserId(UUID userId);

    // Buscar votos por transactionId
    List<Votes> findByTransactionId(UUID transactionId);

    // Buscar por tipo de voto (approve/reject)
    List<Votes> findByVote(String vote);

    // Buscar votos combinando vacaId y tipo de voto
    List<Votes> findByVacaIdAndVote(UUID vacaId, String vote);

    // Buscar votos de un usuario en una vaca específica
    List<Votes> findByUserIdAndVacaId(UUID userId, UUID vacaId);

    // Buscar por fecha de creación
    List<Votes> findByCreatedAtAfter(Timestamp date);

    List<Votes> findByCreatedAtBefore(Timestamp date);
}

