package com.lavacaapi.lavaca.rules.repository;

import com.lavacaapi.lavaca.rules.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface RulesRepository extends JpaRepository<Rules, UUID> {

    // Buscar por ID de vaca
    List<Rules> findByVacaId(UUID vacaId);

    // Buscar por tipo de regla
    List<Rules> findByRuleType(String ruleType);

    // Buscar reglas combinando vacaId y tipo
    List<Rules> findByVacaIdAndRuleType(UUID vacaId, String ruleType);

    // Buscar reglas por valor
    List<Rules> findByValueContainingIgnoreCase(String valuePattern);

    // Buscar reglas por descripción
    List<Rules> findByDescriptionContainingIgnoreCase(String descPattern);

    // Buscar reglas creadas después de una fecha
    List<Rules> findByCreatedAtAfter(Timestamp date);

    // Buscar reglas creadas antes de una fecha
    List<Rules> findByCreatedAtBefore(Timestamp date);

    // Buscar reglas ordenadas por fecha de creación (más recientes primero)
    List<Rules> findByVacaIdOrderByCreatedAtDesc(UUID vacaId);

    // Verificar si una vaca tiene una regla específica
    boolean existsByVacaIdAndRuleType(UUID vacaId, String ruleType);

    // Contar número de reglas por vaca
    @Query("SELECT COUNT(r) FROM Rules r WHERE r.vacaId = :vacaId")
    Long countRulesByVaca(@Param("vacaId") UUID vacaId);

    // Obtener reglas por múltiples tipos
    List<Rules> findByRuleTypeIn(List<String> ruleTypes);

    // Obtener reglas por vaca y múltiples tipos
    List<Rules> findByVacaIdAndRuleTypeIn(UUID vacaId, List<String> ruleTypes);

    // Encontrar las reglas más recientes (útil para auditorías)
    List<Rules> findTop10ByOrderByCreatedAtDesc();

    // Buscar reglas con un valor específico
    List<Rules> findByValue(String value);

    // Eliminar reglas de una vaca específica
    void deleteByVacaId(UUID vacaId);

    //estos metodos sirven pa:

}

