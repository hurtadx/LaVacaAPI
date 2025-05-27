package com.lavacaapi.lavaca.vacas.repository;

import com.lavacaapi.lavaca.vacas.Vacas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VacasRepository extends JpaRepository<Vacas, UUID> {

    // Búsquedas por nombre (exacto o conteniendo)
    List<Vacas> findByName(String name);
    List<Vacas> findByNameContainingIgnoreCase(String namePart);

    // Búsquedas por estado
    List<Vacas> findByStatus(String status);
    List<Vacas> findByIsActive(Boolean isActive);

    // Búsquedas por fechas
    List<Vacas> findByCreatedAtBefore(Timestamp date);
    List<Vacas> findByCreatedAtAfter(Timestamp date);
    List<Vacas> findByDeadlineBefore(Timestamp date);
    List<Vacas> findByDeadlineBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas por metas y montos actuales
    List<Vacas> findByGoalGreaterThan(BigDecimal amount);
    List<Vacas> findByGoalLessThan(BigDecimal amount);
    List<Vacas> findByCurrentGreaterThanEqual(BigDecimal amount);

    // Búsquedas combinadas
    List<Vacas> findByStatusAndIsActive(String status, Boolean isActive);
    List<Vacas> findByNameContainingIgnoreCaseAndIsActiveTrue(String namePart);

    // Búsqueda de vacas próximas a vencer (para las notificaciones)
    List<Vacas> findByDeadlineBeforeAndIsActiveTrue(Timestamp date);

    // Búsqueda avanzada para vacas que están cerca de alcanzar su meta
    @Query("SELECT v FROM Vacas v WHERE (v.current / v.goal) >= :percentage AND v.isActive = true")
    List<Vacas> findVacasNearingGoal(@Param("percentage") BigDecimal percentage);

    // Búsqueda para obtener las vacas más recientes
    List<Vacas> findTop5ByOrderByCreatedAtDesc();

    // Verificar si existe una vaca con cierto nombre (puede ser para validaciones)
    boolean existsByNameIgnoreCase(String name);

    // Buscar por ID y estado activo (para operaciones seguras)
    Optional<Vacas> findByIdAndIsActiveTrue(UUID id);
}
