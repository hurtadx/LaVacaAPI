package com.lavacaapi.lavaca.events.repository;

import com.lavacaapi.lavaca.events.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventsRepository extends JpaRepository<Events, UUID> {

    // Búsquedas básicas
    Optional<Events> findById(UUID id);
    List<Events> findByVacaId(UUID vacaId);
    List<Events> findByUserId(UUID userId);

    // Búsqueda por título y descripción
    List<Events> findByTitleContainingIgnoreCase(String titlePattern);
    List<Events> findByDescriptionContainingIgnoreCase(String descPattern);
    List<Events> findByVacaIdAndTitleContainingIgnoreCase(UUID vacaId, String titlePattern);

    // Búsquedas por fecha
    List<Events> findByCreatedAtAfter(Timestamp date);
    List<Events> findByCreatedAtBefore(Timestamp date);
    List<Events> findByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas combinadas
    List<Events> findByVacaIdAndCreatedAtAfter(UUID vacaId, Timestamp date);
    List<Events> findByUserIdAndCreatedAtAfter(UUID userId, Timestamp date);
    List<Events> findByVacaIdAndUserId(UUID vacaId, UUID userId);

    // Ordenamiento
    List<Events> findByVacaIdOrderByCreatedAtDesc(UUID vacaId);
    List<Events> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // Eventos más recientes
    List<Events> findTop10ByOrderByCreatedAtDesc();
    List<Events> findTop10ByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Verificadores de existencia
    boolean existsByVacaIdAndUserId(UUID vacaId, UUID userId);
    boolean existsByVacaIdAndTitle(UUID vacaId, String title);

    // Contadores
    @Query("SELECT COUNT(e) FROM Events e WHERE e.vacaId = :vacaId")
    Long countEventsByVaca(@Param("vacaId") UUID vacaId);

    @Query("SELECT COUNT(e) FROM Events e WHERE e.createdAt BETWEEN :startDate AND :endDate")
    Long countEventsBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // Búsqueda por múltiples IDs
    List<Events> findByVacaIdIn(List<UUID> vacaIds);
    List<Events> findByUserIdIn(List<UUID> userIds);

    // Eliminar eventos
    void deleteByVacaId(UUID vacaId);
    void deleteByUserId(UUID userId);
    void deleteByCreatedAtBefore(Timestamp date);
}

