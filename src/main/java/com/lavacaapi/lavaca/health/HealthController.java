package com.lavacaapi.lavaca.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "La Vaca API");
        response.put("timestamp", System.currentTimeMillis());

        // Información del sistema
        response.put("java", System.getProperty("java.version"));
        response.put("memory",
            Map.of(
                "free", Runtime.getRuntime().freeMemory(),
                "total", Runtime.getRuntime().totalMemory(),
                "max", Runtime.getRuntime().maxMemory()
            )
        );

        return ResponseEntity.ok(response);
    }
}
