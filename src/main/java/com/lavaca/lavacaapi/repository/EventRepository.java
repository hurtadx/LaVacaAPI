package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    // Búsquedas básicas
    Optional<Event> findById(UUID id);
    List<Event> findByVacaId(UUID vacaId);
    List<Event> findByUserId(UUID userId);

    // Búsqueda por título y descripción
    List<Event> findByTitleContainingIgnoreCase(String titlePattern);
    List<Event> findByDescriptionContainingIgnoreCase(String descPattern);
    List<Event> findByVacaIdAndTitleContainingIgnoreCase(UUID vacaId, String titlePattern);

    // Búsquedas por fecha
    List<Event> findByCreatedAtAfter(Timestamp date);
    List<Event> findByCreatedAtBefore(Timestamp date);
    List<Event> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas
    List<Event> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Event> findByUserIdAndCreatedAtAfter(UUID userId, Timestamp date);
    List<Event> findByVacaIdAndUserId(UUID vacaId, UUID userId);

    // Ordenamiento
    List<Event> findByVacaIdOrderByCreatedAtDesc(UUID vacaId);
    List<Event> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // Eventos más recientes
    List<Event> findTop10ByOrderByCreatedAtDesc();
    List<Event> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Verificadores de existencia
    boolean existsByVacaIdAndUserId(UUID vacaId, UUID userId);
    boolean existsByVacaIdAndTitle(UUID vacaId, String title);

    // Contadores
    @Query("SELECT COUNT(e) FROM Event e WHERE e.vacaId = :vacaId")
    Long countEventsByVaca(@Param("vacaId") UUID vacaId);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.createdAt BETWEEN :startDate AND :endDate")
    Long countEventsBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // Búsqueda por múltiples IDs
    List<Event> findByVacaIdIn(List<UUID> vacaIds);
    List<Event> findByUserIdIn(List<UUID> userIds);

    // Eliminar eventos
    void deleteByVacaId(UUID vacaId);
    void deleteByUserId(UUID userId);
    void deleteByCreatedAtBefore(Timestamp date);
}

