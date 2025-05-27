package com.lavacaapi.lavaca.rules.service;

import com.lavacaapi.lavaca.rules.Rules;
import com.lavacaapi.lavaca.rules.repository.RulesRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RulesService {

    @Autowired
    private RulesRepository rulesRepository;

    @Autowired
    private VacasRepository vacasRepository;

    /**
     * Crea una nueva regla
     * @param rule datos de la regla
     * @return regla creada
     */
    @Transactional
    public Rules createRule(Rules rule) {
        // Validar datos obligatorios
        if (rule.getVacaId() == null) {
            throw new IllegalArgumentException("El ID de la vaca es obligatorio");
        }

        if (rule.getRuleType() == null || rule.getRuleType().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de regla es obligatorio");
        }

        // Verificar que la vaca exista
        if (!vacasRepository.existsById(rule.getVacaId())) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + rule.getVacaId());
        }

        // Generar ID si no se proporciona
        if (rule.getId() == null) {
            rule.setId(UUID.randomUUID());
        }

        // Establecer fecha de creación si no se proporciona
        if (rule.getCreatedAt() == null) {
            rule.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        return rulesRepository.save(rule);
    }

    /**
     * Obtiene todas las reglas
     * @return lista de reglas
     */
    public List<Rules> getAllRules() {
        return rulesRepository.findAll();
    }

    /**
     * Obtiene una regla por su ID
     * @param id ID de la regla
     * @return regla encontrada o empty si no existe
     */
    public Optional<Rules> getRuleById(UUID id) {
        return rulesRepository.findById(id);
    }

    /**
     * Obtiene todas las reglas relacionadas con una vaca
     * @param vacaId ID de la vaca
     * @return lista de reglas de la vaca
     */
    public List<Rules> getRulesByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return rulesRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todas las reglas de un tipo específico
     * @param ruleType tipo de regla
     * @return lista de reglas del tipo especificado
     */
    public List<Rules> getRulesByType(String ruleType) {
        return rulesRepository.findByRuleType(ruleType);
    }

    /**
     * Obtiene todas las reglas de una vaca y un tipo específico
     * @param vacaId ID de la vaca
     * @param ruleType tipo de regla
     * @return lista de reglas que cumplen ambas condiciones
     */
    public List<Rules> getRulesByVacaIdAndType(UUID vacaId, String ruleType) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return rulesRepository.findByVacaIdAndRuleType(vacaId, ruleType);
    }

    /**
     * Actualiza una regla existente
     * @param id ID de la regla a actualizar
     * @param rule Datos actualizados
     * @return regla actualizada
     */
    @Transactional
    public Rules updateRule(UUID id, Rules rule) {
        Rules existingRule = rulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la regla con ID: " + id));

        // No permitir cambiar el ID o vacaId
        rule.setId(id);
        rule.setVacaId(existingRule.getVacaId());

        // Validar el tipo de regla
        if (rule.getRuleType() == null || rule.getRuleType().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de regla es obligatorio");
        }

        // Preservar fecha de creación
        if (rule.getCreatedAt() == null) {
            rule.setCreatedAt(existingRule.getCreatedAt());
        }

        return rulesRepository.save(rule);
    }

    /**
     * Elimina una regla
     * @param id ID de la regla a eliminar
     */
    @Transactional
    public void deleteRule(UUID id) {
        if (!rulesRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la regla con ID: " + id);
        }
        rulesRepository.deleteById(id);
    }

    /**
     * Elimina todas las reglas de una vaca
     * @param vacaId ID de la vaca
     */
    @Transactional
    public void deleteRulesByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        rulesRepository.deleteByVacaId(vacaId);
    }
}
