package Repository;

import Model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {

    // Buscar por ID de vaca
    List<Rule> findByVaca_id(String vacaId);

    // Buscar por tipo de regla
    List<Rule> findByRule_type(String ruleType);

    // Buscar reglas combinando vaca_id y tipo
    List<Rule> findByVaca_idAndRule_type(String vacaId, String ruleType);

    // Buscar reglas por valor
    List<Rule> findByValueContainingIgnoreCase(String valuePattern);

    // Buscar reglas por descripción
    List<Rule> findByDescriptionContainingIgnoreCase(String descPattern);

    // Buscar reglas creadas después de una fecha
    List<Rule> findByCreated_atAfter(Timestamp date);

    // Buscar reglas creadas antes de una fecha
    List<Rule> findByCreated_atBefore(Timestamp date);

    // Buscar reglas ordenadas por fecha de creación (más recientes primero)
    List<Rule> findByVaca_idOrderByCreated_atDesc(String vacaId);

    // Verificar si una vaca tiene una regla específica
    boolean existsByVaca_idAndRule_type(String vacaId, String ruleType);

    // Contar número de reglas por vaca
    @Query("SELECT COUNT(r) FROM Rule r WHERE r.vaca_id = :vacaId")
    Long countRulesByVaca(@Param("vacaId") String vacaId);

    // Obtener reglas por múltiples tipos
    List<Rule> findByRule_typeIn(List<String> ruleTypes);

    // Obtener reglas por vaca y múltiples tipos
    List<Rule> findByVaca_idAndRule_typeIn(String vacaId, List<String> ruleTypes);

    // Encontrar las reglas más recientes (útil para auditorías)
    List<Rule> findTop10ByOrderByCreated_atDesc();

    // Buscar reglas con un valor específico
    List<Rule> findByValue(String value);

    // Eliminar reglas de una vaca específica
    void deleteByVaca_id(String vacaId);

    //estos metodos sirven pa:

}