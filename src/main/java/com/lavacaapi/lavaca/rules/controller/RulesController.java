package com.lavacaapi.lavaca.rules.controller;

import com.lavacaapi.lavaca.rules.Rules;
import com.lavacaapi.lavaca.rules.service.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    @Autowired
    private RulesService rulesService;

    @PostMapping
    public ResponseEntity<Rules> createRule(@RequestBody Rules rule) {
        return new ResponseEntity<>(rulesService.createRule(rule), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rules>> getAllRules() {
        return ResponseEntity.ok(rulesService.getAllRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rules> getRuleById(@PathVariable UUID id) {
        Optional<Rules> rule = rulesService.getRuleById(id);
        return rule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Rules>> getRulesByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(rulesService.getRulesByVacaId(vacaId));
    }

    @GetMapping("/type/{ruleType}")
    public ResponseEntity<List<Rules>> getRulesByType(@PathVariable String ruleType) {
        return ResponseEntity.ok(rulesService.getRulesByType(ruleType));
    }

    @GetMapping("/vaca/{vacaId}/type/{ruleType}")
    public ResponseEntity<List<Rules>> getRulesByVacaAndType(
            @PathVariable UUID vacaId,
            @PathVariable String ruleType) {
        return ResponseEntity.ok(rulesService.getRulesByVacaIdAndType(vacaId, ruleType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rules> updateRule(@PathVariable UUID id, @RequestBody Rules rule) {
        return ResponseEntity.ok(rulesService.updateRule(id, rule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        rulesService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaca/{vacaId}")
    public ResponseEntity<Void> deleteRulesByVaca(@PathVariable UUID vacaId) {
        rulesService.deleteRulesByVacaId(vacaId);
        return ResponseEntity.noContent().build();
    }
}
