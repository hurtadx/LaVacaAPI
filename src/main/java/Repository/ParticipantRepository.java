package Repository;

import Model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, String> {

    // Búsquedas por identificadores
    List<Participant> findByVaca_id(String vacaId);
    List<Participant> findByUser_id(String userId);
    Optional<Participant> findByVaca_idAndUser_id(String vacaId, String userId);
    Optional<Participant> findByEmailAndVaca_id(String email, String vacaId);

    // Verificar existencia
    boolean existsByVaca_idAndUser_id(String vacaId, String userId);
    boolean existsByVaca_idAndEmail(String vacaId, String email);

    // Búsquedas por nombre o email
    List<Participant> findByNameContainingIgnoreCase(String namePattern);
    List<Participant> findByEmailContainingIgnoreCase(String emailPattern);
    List<Participant> findByVaca_idAndNameContainingIgnoreCase(String vacaId, String namePattern);

    // Búsquedas por fecha
    List<Participant> findByCreated_atAfter(Timestamp date);
    List<Participant> findByVaca_idAndCreated_atAfter(String vacaId, Timestamp date);
    List<Participant> findByCreated_atBetween(Timestamp startDate, Timestamp endDate);

    // Conteos
    @Query("SELECT COUNT(p) FROM Participant p WHERE p.vaca_id = :vacaId")
    Long countParticipantsByVaca(@Param("vacaId") String vacaId);
    @Query("SELECT COUNT(DISTINCT p.vaca_id) FROM Participant p WHERE p.user_id = :userId")
    Long countVacasByUser(@Param("userId") String userId);

    // Participantes sin usuario asociado (invitados externos)
    List<Participant> findByVaca_idAndUser_idIsNull(String vacaId);

    // Participantes con usuario asociado (miembros registrados)
    List<Participant> findByVaca_idAndUser_idIsNotNull(String vacaId);

    // Búsqueda por múltiples vacas
    List<Participant> findByVaca_idIn(List<String> vacaIds);
    @Query("SELECT p FROM Participant p WHERE p.vaca_id IN :vacaIds")
    Page<Participant> findParticipantsInMultipleVacas(@Param("vacaIds") List<String> vacaIds, Pageable pageable);

    // Búsqueda por múltiples usuarios
    List<Participant> findByUser_idIn(List<String> userIds);

    // Obtener participantes más recientes de una vaca
    List<Participant> findTop10ByVaca_idOrderByCreated_atDesc(String vacaId);

    // Eliminar participantes
    void deleteByVaca_idAndUser_id(String vacaId, String userId);
    void deleteByVaca_id(String vacaId);

    // Ordenamiento
    List<Participant> findByVaca_idOrderByNameAsc(String vacaId);

    // Obtener participantes con email pero sin usuario (potenciales invitaciones)
    List<Participant> findByEmailIsNotNullAndUser_idIsNull();

    // Soporte de paginación por vacaId
    Page<Participant> findByVaca_id(String vacaId, Pageable pageable);

    // Obtener todos los participantes con un email específico en todas las vacas
    List<Participant> findByEmail(String email);

    // Participantes que se unieron después de una fecha específica
    @Query("SELECT p FROM Participant p WHERE p.created_at > :date ORDER BY p.created_at DESC")
    List<Participant> findRecentParticipants(@Param("date") Timestamp date);
}

