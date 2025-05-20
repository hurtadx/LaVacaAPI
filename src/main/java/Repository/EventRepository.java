package Repository;

import Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {

    // Búsquedas básicas
    Optional<Event> findById(String id);
    List<Event> findByVaca_id(String vacaId);
    List<Event> findByUser_id(String userId);

    // Búsqueda por título y descripción
    List<Event> findByTitleContainingIgnoreCase(String titlePattern);
    List<Event> findByDescriptionContainingIgnoreCase(String descPattern);
    List<Event> findByVaca_idAndTitleContainingIgnoreCase(String vacaId, String titlePattern);

    // Búsquedas por fecha
    List<Event> findByCreated_atAfter(Timestamp date);
    List<Event> findByCreated_atBefore(Timestamp date);
    List<Event> findByCreated_atBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas
    List<Event> findByVaca_idAndCreated_atAfter(String vacaId, Timestamp date);
    List<Event> findByUser_idAndCreated_atAfter(String userId, Timestamp date);
    List<Event> findByVaca_idAndUser_id(String vacaId, String userId);

    // Ordenamiento
    List<Event> findByVaca_idOrderByCreated_atDesc(String vacaId);
    List<Event> findByUser_idOrderByCreated_atDesc(String userId);

    // Eventos más recientes
    List<Event> findTop10ByOrderByCreated_atDesc();
    List<Event> findTop10ByVaca_idOrderByCreated_atDesc(String vacaId);

    // Verificadores de existencia
    boolean existsByVaca_idAndUser_id(String vacaId, String userId);
    boolean existsByVaca_idAndTitle(String vacaId, String title);

    // Contadores
    @Query("SELECT COUNT(e) FROM Event e WHERE e.vaca_id = :vacaId")
    Long countEventsByVaca(@Param("vacaId") String vacaId);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.created_at BETWEEN :startDate AND :endDate")
    Long countEventsBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // Búsqueda por múltiples IDs
    List<Event> findByVaca_idIn(List<String> vacaIds);
    List<Event> findByUser_idIn(List<String> userIds);

    // Eliminar eventos
    void deleteByVaca_id(String vacaId);
    void deleteByUser_id(String userId);
    void deleteByCreated_atBefore(Timestamp date);
}