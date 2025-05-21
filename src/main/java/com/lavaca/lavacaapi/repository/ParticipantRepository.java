package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

    // Búsquedas por identificadores
    List<Participant> findByVacaId(UUID vacaId);
    List<Participant> findByUserId(UUID userId);
    Optional<Participant> findByVacaIdAndUserId(UUID vacaId, UUID userId);
    Optional<Participant> findByEmailAndVacaId(String email, UUID vacaId);

    // Verificar existencia
    boolean existsByVacaIdAndUserId(UUID vacaId, UUID userId);
    boolean existsByVacaIdAndEmail(UUID vacaId, String email);

    // Búsquedas por nombre o email
    List<Participant> findByNameContainingIgnoreCase(String namePattern);
    List<Participant> findByEmailContainingIgnoreCase(String emailPattern);
    List<Participant> findByVacaIdAndNameContainingIgnoreCase(UUID vacaId, String namePattern);

    // Búsquedas por fecha
    List<Participant> findByCreatedAtAfter(Timestamp date);
    List<Participant> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Participant> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Conteos
    @Query("SELECT COUNT(p) FROM Participant p WHERE p.vacaId = :vacaId")
    Long countParticipantsByVaca(@Param("vacaId") UUID vacaId);
    @Query("SELECT COUNT(DISTINCT p.vacaId) FROM Participant p WHERE p.userId = :userId")
    Long countVacasByUser(@Param("userId") UUID userId);

    // Participantes sin usuario asociado (invitados externos)
    List<Participant> findByVacaIdAndUserIdIsNull(UUID vacaId);

    // Participantes con usuario asociado (miembros registrados)
    List<Participant> findByVacaIdAndUserIdIsNotNull(UUID vacaId);

    // Búsqueda por múltiples vacas
    List<Participant> findByVacaIdIn(List<UUID> vacaIds);
    @Query("SELECT p FROM Participant p WHERE p.vacaId IN :vacaIds")
    Page<Participant> findParticipantsInMultipleVacas(@Param("vacaIds") List<UUID> vacaIds, Pageable pageable);

    // Búsqueda por múltiples usuarios
    List<Participant> findByUserIdIn(List<UUID> userIds);

    // Obtener participantes más recientes de una vaca
    List<Participant> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Eliminar participantes
    void deleteByVacaIdAndUserId(UUID vacaId, UUID userId);
    void deleteByVacaId(UUID vacaId);

    // Ordenamiento
    List<Participant> findByVacaIdOrderByNameAsc(UUID vacaId);

    // Obtener participantes con email pero sin usuario (potenciales invitaciones)
    List<Participant> findByEmailIsNotNullAndUserIdIsNull();

    // Soporte de paginación por vacaId
    Page<Participant> findByVacaId(UUID vacaId, Pageable pageable);

    // Obtener todos los participantes con un email específico en todas las vacas
    List<Participant> findByEmail(String email);

    // Participantes que se unieron después de una fecha específica
    @Query("SELECT p FROM Participant p WHERE p.createdAt > :date ORDER BY p.createdAt DESC")
    List<Participant> findRecentParticipants(@Param("date") Timestamp date);

    // Filtrar participantes por vaca y status
    List<Participant> findByVacaIdAndStatus(UUID vacaId, String status);
}
