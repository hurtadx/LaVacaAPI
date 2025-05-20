package Repository;

import Model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    // Búsquedas básicas
    List<Invitation> findByVaca_id(String vacaId);
    List<Invitation> findBySender_id(String senderId);
    List<Invitation> findByStatus(String status);
    Optional<Invitation> findByVaca_idAndSender_id(String vacaId, String senderId);

    // Verificadores de existencia
    boolean existsByVaca_idAndSender_id(String vacaId, String senderId);
    boolean existsByVaca_idAndStatus(String vacaId, String status);

    // Buscar por estado y remitente o vaca
    List<Invitation> findByStatusAndSender_id(String status, String senderId);
    List<Invitation> findByStatusAndVaca_id(String status, String vacaId);

    // Búsquedas por fecha
    List<Invitation> findByCreated_atAfter(Timestamp date);
    List<Invitation> findByCreated_atBefore(Timestamp date);
    List<Invitation> findByCreated_atBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas con fechas
    List<Invitation> findByVaca_idAndCreated_atAfter(String vacaId, Timestamp date);
    List<Invitation> findBySender_idAndCreated_atAfter(String senderId, Timestamp date);
    List<Invitation> findByStatusAndCreated_atAfter(String status, Timestamp date);

    // Conteos
    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.vaca_id = :vacaId")
    Long countInvitationsByVaca(@Param("vacaId") String vacaId);

    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.status = :status")
    Long countInvitationsByStatus(@Param("status") String status);

    // Invitaciones más recientes
    List<Invitation> findTop10ByOrderByCreated_atDesc();
    List<Invitation> findTop10ByVaca_idOrderByCreated_atDesc(String vacaId);
    List<Invitation> findTop10BySender_idOrderByCreated_atDesc(String senderId);

    // Búsqueda por múltiples IDs
    List<Invitation> findByVaca_idIn(List<String> vacaIds);
    List<Invitation> findBySender_idIn(List<String> senderIds);

    // Eliminar invitaciones
    void deleteByVaca_id(String vacaId);
    void deleteByVaca_idAndSender_id(String vacaId, String senderId);
    void deleteByStatus(String status);
}