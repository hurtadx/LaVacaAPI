package Repository;

import Model.Vaca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VacaRepository extends JpaRepository<Vaca, UUID> {

    // Búsquedas por nombre (exacto o conteniendo)
    List<Vaca> findByName(String name);
    List<Vaca> findByNameContainingIgnoreCase(String namePart);

    // Búsquedas por estado
    List<Vaca> findByStatus(String status);
    List<Vaca> findByIsActive(Boolean isActive);

    // Búsquedas por fechas
    List<Vaca> findByCreatedAtBefore(Timestamp date);
    List<Vaca> findByCreatedAtAfter(Timestamp date);
    List<Vaca> findByDeadlineBefore(Timestamp date);
    List<Vaca> findByDeadlineBetween(Timestamp startDate, Timestamp endDate);

    // Búsquedas por metas y montos actuales
    List<Vaca> findByGoalGreaterThan(BigDecimal amount);
    List<Vaca> findByGoalLessThan(BigDecimal amount);
    List<Vaca> findByCurrentGreaterThanEqual(BigDecimal amount);

    // Búsquedas combinadas
    List<Vaca> findByStatusAndIsActive(String status, Boolean isActive);
    List<Vaca> findByNameContainingIgnoreCaseAndIsActiveTrue(String namePart);

    // Búsqueda de vacas próximas a vencer (para las notificaciones)
    List<Vaca> findByDeadlineBeforeAndIsActiveTrue(Timestamp date);

    // Búsqueda avanzada para vacas que están cerca de alcanzar su meta
    @Query("SELECT v FROM Vaca v WHERE (v.current / v.goal) >= :percentage AND v.isActive = true")
    List<Vaca> findVacasNearingGoal(@Param("percentage") BigDecimal percentage);

    // Búsqueda para obtener las vacas más recientes
    List<Vaca> findTop5ByOrderByCreatedAtDesc();


    // Verificar si existe una vaca con cierto nombre (puede ser para validaciones)
    boolean existsByNameIgnoreCase(String name);

    // Buscar por ID y estado activo (para operaciones seguras)
    Optional<Vaca> findByIdAndIsActiveTrue(UUID id);

    //estos metodos pueden servir para:
    //Búsquedas por nombres para filtros en el dashboard
    //Verificar vacas activas/inactivas o por estado
    //Mostrar vacas próximas a vencer o recién creadas
    //Consultar vacas por avance de meta alcanzada
    //Obtener listados según diferentes criterios de búsqueda


}