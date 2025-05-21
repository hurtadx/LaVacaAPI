package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {

    // Buscar por ID de vaca
    List<Rule> findByVacaId(UUID vacaId);

    // Buscar por tipo de regla
    List<Rule> findByRuleType(String ruleType);

    // Buscar reglas combinando vacaId y tipo
    List<Rule> findByVacaIdAndRuleType(UUID vacaId, String ruleType);

    // Buscar reglas por valor
    List<Rule> findByValueContainingIgnoreCase(String valuePattern);

    // Buscar reglas por descripción
    List<Rule> findByDescriptionContainingIgnoreCase(String descPattern);

    // Buscar reglas creadas después de una fecha
    List<Rule> findByCreatedAtAfter(Timestamp date);

    // Buscar reglas creadas antes de una fecha
    List<Rule> findByCreatedAtBefore(Timestamp date);

    // Buscar reglas ordenadas por fecha de creación (más recientes primero)
    List<Rule> findByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Verificar si una vaca tiene una regla específica
    boolean existsByVacaIdAndRuleType(UUID vacaId, String ruleType);

    // Contar número de reglas por vaca
    @Query("SELECT COUNT(r) FROM Rule r WHERE r.vacaId = :vacaId")
    Long countRulesByVaca(@Param("vacaId") UUID vacaId);

    // Obtener reglas por múltiples tipos
    List<Rule> findByRuleTypeIn(List<String> ruleTypes);

    // Obtener reglas por vaca y múltiples tipos
    List<Rule> findByVacaIdAndRuleTypeIn(UUID vacaId, List<String> ruleTypes);

    // Encontrar las reglas más recientes (útil para auditorías)
    List<Rule> findTop10ByOrderByCreatedAtDesc();

    // Buscar reglas con un valor específico
    List<Rule> findByValue(String value);

    // Eliminar reglas de una vaca específica
    void deleteByVacaId(UUID vacaId);

    //estos metodos sirven pa:

}

