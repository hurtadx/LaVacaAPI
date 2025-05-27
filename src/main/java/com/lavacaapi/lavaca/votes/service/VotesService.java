package com.lavacaapi.lavaca.votes.service;

import com.lavacaapi.lavaca.votes.Votes;
import com.lavacaapi.lavaca.votes.repository.VotesRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VotesService {

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private VacasRepository vacasRepository;

    /**
     * Crea un nuevo voto
     * @param vote voto a crear
     * @return voto creado
     */
    @Transactional
    public Votes createVote(Votes vote) {
        // Validar que los IDs necesarios existan
        if (vote.getVacaId() == null) {
            throw new IllegalArgumentException("El ID de la vaca es obligatorio");
        }

        if (vote.getUserId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }

        if (!vacasRepository.existsById(vote.getVacaId())) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vote.getVacaId());
        }

        // Validar el tipo de voto
        if (vote.getVote() == null || (!vote.getVote().equals("approve") && !vote.getVote().equals("reject"))) {
            throw new IllegalArgumentException("El tipo de voto debe ser 'approve' o 'reject'");
        }

        // Generar ID si no se proporciona
        if (vote.getId() == null) {
            vote.setId(UUID.randomUUID());
        }

        // Establecer fecha de creación si no se proporciona
        if (vote.getCreatedAt() == null) {
            vote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        return votesRepository.save(vote);
    }

    /**
     * Obtiene todos los votos
     * @return lista de votos
     */
    public List<Votes> getAllVotes() {
        return votesRepository.findAll();
    }

    /**
     * Obtiene un voto por su ID
     * @param id ID del voto
     * @return voto encontrado o empty si no existe
     */
    public Optional<Votes> getVoteById(UUID id) {
        return votesRepository.findById(id);
    }

    /**
     * Obtiene todos los votos relacionados con una vaca
     * @param vacaId ID de la vaca
     * @return lista de votos de la vaca
     */
    public List<Votes> getVotesByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return votesRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todos los votos de un usuario
     * @param userId ID del usuario
     * @return lista de votos del usuario
     */
    public List<Votes> getVotesByUserId(UUID userId) {
        return votesRepository.findByUserId(userId);
    }

    /**
     * Obtiene todos los votos relacionados con una transacción
     * @param transactionId ID de la transacción
     * @return lista de votos de la transacción
     */
    public List<Votes> getVotesByTransactionId(UUID transactionId) {
        return votesRepository.findByTransactionId(transactionId);
    }

    /**
     * Obtener resultados de una votación
     * @param voteId ID de la votación
     * @return resultados de la votación
     */
    public Object getVoteResults(UUID voteId) {
        Votes vote = votesRepository.findById(voteId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la votación con ID: " + voteId));
        List<Votes> allVotes = votesRepository.findByVacaId(vote.getVacaId());
        long approves = allVotes.stream().filter(v -> "approve".equalsIgnoreCase(v.getVote())).count();
        long rejects = allVotes.stream().filter(v -> "reject".equalsIgnoreCase(v.getVote())).count();
        long total = allVotes.size();
        return java.util.Map.of(
                "vacaId", vote.getVacaId(),
                "voteId", voteId,
                "totalVotes", total,
                "approves", approves,
                "rejects", rejects
        );
    }

    /**
     * Emitir voto (cast)
     * @param voteId ID del voto
     * @param vote Datos del voto
     * @return voto emitido
     */
    @Transactional
    public Votes castVote(UUID voteId, Votes vote) {
        Votes existing = votesRepository.findById(voteId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la votación con ID: " + voteId));
        // No permitir cambiar vacaId, userId, transactionId
        vote.setId(voteId);
        vote.setVacaId(existing.getVacaId());
        vote.setUserId(existing.getUserId());
        vote.setTransactionId(existing.getTransactionId());
        if (vote.getVote() == null || (!vote.getVote().equals("approve") && !vote.getVote().equals("reject"))) {
            throw new IllegalArgumentException("El tipo de voto debe ser 'approve' o 'reject'");
        }
        if (vote.getCreatedAt() == null) {
            vote.setCreatedAt(existing.getCreatedAt());
        }
        return votesRepository.save(vote);
    }

    /**
     * Votaciones pendientes del usuario
     * @param userId ID del usuario
     * @return lista de votaciones pendientes
     */
    public List<Votes> getPendingVotesByUser(UUID userId) {
        // Consideramos como pendientes los votos donde el campo 'vote' es null o vacío
        return votesRepository.findByUserId(userId).stream()
                .filter(v -> v.getVote() == null || v.getVote().isEmpty())
                .toList();
    }

    /**
     * Actualiza un voto existente
     * @param id ID del voto a actualizar
     * @param vote Datos actualizados
     * @return voto actualizado
     */
    @Transactional
    public Votes updateVote(UUID id, Votes vote) {
        Votes existingVote = votesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el voto con ID: " + id));

        // No permitir cambiar el ID, vacaId, userId o transactionId
        vote.setId(id);
        vote.setVacaId(existingVote.getVacaId());
        vote.setUserId(existingVote.getUserId());
        vote.setTransactionId(existingVote.getTransactionId());

        // Validar el tipo de voto
        if (vote.getVote() == null || (!vote.getVote().equals("approve") && !vote.getVote().equals("reject"))) {
            throw new IllegalArgumentException("El tipo de voto debe ser 'approve' o 'reject'");
        }

        // Actualizar la fecha si no se proporciona
        if (vote.getCreatedAt() == null) {
            vote.setCreatedAt(existingVote.getCreatedAt());
        }

        return votesRepository.save(vote);
    }

    /**
     * Elimina un voto
     * @param id ID del voto a eliminar
     */
    @Transactional
    public void deleteVote(UUID id) {
        if (!votesRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el voto con ID: " + id);
        }
        votesRepository.deleteById(id);
    }
}
