package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    // Búsquedas básicas
    List<Invitation> findByVacaId(UUID vacaId);
    List<Invitation> findBySenderId(UUID senderId);
    List<Invitation> findByStatus(String status);
    Optional<Invitation> findByVacaIdAndSenderId(UUID vacaId, UUID senderId);

    // Verificadores de existencia
    boolean existsByVacaIdAndSenderId(UUID vacaId, UUID senderId);
    boolean existsByVacaIdAndStatus(UUID vacaId, String status);

    // Buscar por estado y remitente o vaca
    List<Invitation> findByStatusAndSenderId(String status, UUID senderId);
    List<Invitation> findByStatusAndVacaId(String status, UUID vacaId);

    // Búsquedas por fecha
    List<Invitation> findByCreatedAtAfter(Timestamp date);
    List<Invitation> findByCreatedAtBefore(Timestamp date);
    List<Invitation> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas con fechas
    List<Invitation> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Invitation> findBySenderIdAndCreatedAtAfter(UUID senderId, Timestamp date);
    List<Invitation> findByStatusAndCreatedAtAfter(String status, Timestamp date);

    // Conteos
    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.vacaId = :vacaId")
    Long countInvitationsByVaca(@Param("vacaId") UUID vacaId);

    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.status = :status")
    Long countInvitationsByStatus(@Param("status") String status);

    // Invitaciones más recientes
    List<Invitation> findTop10ByOrderByCreatedAtDesc();
    List<Invitation> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);
    List<Invitation> findTop10BySenderIdOrderByCreatedAtDesc(UUID senderId);

    // Búsqueda por múltiples IDs
    List<Invitation> findByVacaIdIn(List<UUID> vacaIds);
    List<Invitation> findBySenderIdIn(List<UUID> senderIds);

    // Eliminar invitaciones
    void deleteByVacaId(UUID vacaId);
    void deleteByVacaIdAndSenderId(UUID vacaId, UUID senderId);
    void deleteByStatus(String status);
}

