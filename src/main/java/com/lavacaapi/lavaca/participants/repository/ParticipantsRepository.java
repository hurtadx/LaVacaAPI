package com.lavacaapi.lavaca.participants.repository;

import com.lavacaapi.lavaca.participants.Participants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantsRepository extends JpaRepository<Participants, UUID> {

    // Búsquedas por identificadores
    List<Participants> findByVacaId(UUID vacaId);
    List<Participants> findByUserId(UUID userId);
    Optional<Participants> findByVacaIdAndUserId(UUID vacaId, UUID userId);
    Optional<Participants> findByEmailAndVacaId(String email, UUID vacaId);

    // Verificar existencia
    boolean existsByVacaIdAndUserId(UUID vacaId, UUID userId);
    boolean existsByVacaIdAndEmail(UUID vacaId, String email);

    // Búsquedas por nombre o email
    List<Participants> findByNameContainingIgnoreCase(String namePattern);
    List<Participants> findByEmailContainingIgnoreCase(String emailPattern);
    List<Participants> findByVacaIdAndNameContainingIgnoreCase(UUID vacaId, String namePattern);

    // Búsquedas por fecha
    List<Participants> findByCreatedAtAfter(Timestamp date);
    List<Participants> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Participants> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Conteos
    @Query("SELECT COUNT(p) FROM Participants p WHERE p.vacaId = :vacaId")
    Long countParticipantsByVaca(@Param("vacaId") UUID vacaId);
    @Query("SELECT COUNT(DISTINCT p.vacaId) FROM Participants p WHERE p.userId = :userId")
    Long countVacasByUser(@Param("userId") UUID userId);

    // Participantes sin usuario asociado (invitados externos)
    List<Participants> findByVacaIdAndUserIdIsNull(UUID vacaId);

    // Participantes con usuario asociado (miembros registrados)
    List<Participants> findByVacaIdAndUserIdIsNotNull(UUID vacaId);

    // Búsqueda por múltiples vacas
    List<Participants> findByVacaIdIn(List<UUID> vacaIds);
    @Query("SELECT p FROM Participants p WHERE p.vacaId IN :vacaIds")
    Page<Participants> findParticipantsInMultipleVacas(@Param("vacaIds") List<UUID> vacaIds, Pageable pageable);

    // Búsqueda por múltiples usuarios
    List<Participants> findByUserIdIn(List<UUID> userIds);

    // Obtener participantes más recientes de una vaca
    List<Participants> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Eliminar participantes
    void deleteByVacaIdAndUserId(UUID vacaId, UUID userId);
    void deleteByVacaId(UUID vacaId);

    // Ordenamiento
    List<Participants> findByVacaIdOrderByNameAsc(UUID vacaId);

    // Obtener participantes con email pero sin usuario (potenciales invitaciones)
    List<Participants> findByEmailIsNotNullAndUserIdIsNull();

    // Soporte de paginación por vacaId
    Page<Participants> findByVacaId(UUID vacaId, Pageable pageable);

    // Obtener todos los participantes con un email específico en todas las vacas
    List<Participants> findByEmail(String email);

    // Participantes que se unieron después de una fecha específica
    @Query("SELECT p FROM Participants p WHERE p.createdAt > :date ORDER BY p.createdAt DESC")
    List<Participants> findRecentParticipants(@Param("date") Timestamp date);

    // Filtrar participantes por vaca y status
    List<Participants> findByVacaIdAndStatus(UUID vacaId, String status);
}
