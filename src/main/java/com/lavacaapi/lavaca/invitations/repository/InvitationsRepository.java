package com.lavacaapi.lavaca.invitations.repository;

import com.lavacaapi.lavaca.invitations.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationsRepository extends JpaRepository<Invitations, UUID> {

    // Búsquedas básicas
    List<Invitations> findByVacaId(UUID vacaId);
    List<Invitations> findBySenderId(UUID senderId);
    List<Invitations> findByStatus(String status);
    Optional<Invitations> findByVacaIdAndSenderId(UUID vacaId, UUID senderId);

    // Buscar invitaciones recibidas por usuario
    List<Invitations> findByReceiverId(UUID receiverId);

    // Buscar invitaciones enviadas por usuario
    List<Invitations> findBySenderId(UUID senderId);

    // Verificadores de existencia
    boolean existsByVacaIdAndSenderId(UUID vacaId, UUID senderId);
    boolean existsByVacaIdAndStatus(UUID vacaId, String status);

    // Buscar por estado y remitente o vaca
    List<Invitations> findByStatusAndSenderId(String status, UUID senderId);
    List<Invitations> findByStatusAndVacaId(String status, UUID vacaId);

    // Buscar invitaciones recibidas por usuario y estado
    List<Invitations> findByReceiverIdAndStatus(UUID receiverId, String status);

    // Buscar invitaciones enviadas por usuario y estado
    List<Invitations> findBySenderIdAndStatus(UUID senderId, String status);

    // Búsquedas por fecha
    List<Invitations> findByCreatedAtAfter(Timestamp date);
    List<Invitations> findByCreatedAtBefore(Timestamp date);
    List<Invitations> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas con fechas
    List<Invitations> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Invitations> findBySenderIdAndCreatedAtAfter(UUID senderId, Timestamp date);
    List<Invitations> findByStatusAndCreatedAtAfter(String status, Timestamp date);

    // Buscar invitaciones recibidas por usuario y vaca
    List<Invitations> findByReceiverIdAndVacaId(UUID receiverId, UUID vacaId);

    // Buscar invitaciones por receiverId y senderId
    List<Invitations> findByReceiverIdAndSenderId(UUID receiverId, UUID senderId);

    // Conteos
    @Query("SELECT COUNT(i) FROM Invitations i WHERE i.vacaId = :vacaId")
    Long countInvitationsByVaca(@Param("vacaId") UUID vacaId);

    @Query("SELECT COUNT(i) FROM Invitations i WHERE i.status = :status")
    Long countInvitationsByStatus(@Param("status") String status);

    // Invitaciones más recientes
    List<Invitations> findTop10ByOrderByCreatedAtDesc();
    List<Invitations> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);
    List<Invitations> findTop10BySenderIdOrderByCreatedAtDesc(UUID senderId);

    // Búsqueda por múltiples IDs
    List<Invitations> findByVacaIdIn(List<UUID> vacaIds);
    List<Invitations> findBySenderIdIn(List<UUID> senderIds);

    // Eliminar invitaciones
    void deleteByVacaId(UUID vacaId);
    void deleteByVacaIdAndSenderId(UUID vacaId, UUID senderId);
    void deleteByStatus(String status);
}

