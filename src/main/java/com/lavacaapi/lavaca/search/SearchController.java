package com.lavacaapi.lavaca.search;

import com.lavacaapi.lavaca.profiles.service.ProfilesService;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.events.repository.EventsRepository;
import com.lavacaapi.lavaca.profiles.Profiles;
import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.events.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ProfilesService profilesService;
    @Autowired
    private VacasRepository vacasRepository;
    @Autowired
    private EventsRepository eventsRepository;

    @GetMapping("/users")
    public ResponseEntity<List<Profiles>> searchUsers(@RequestParam String q, @RequestParam(required = false, defaultValue = "10") int limit) {
        List<Profiles> results = profilesService.searchProfiles(q);
        if (limit > 0 && results.size() > limit) {
            results = results.subList(0, limit);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/vacas")
    public ResponseEntity<List<Vacas>> searchVacas(@RequestParam String q, @RequestParam UUID user_id) {
        List<Vacas> vacas = vacasRepository.findByNameContainingIgnoreCase(q);
        List<Vacas> filtered = vacas.stream().filter(v -> user_id.equals(v.getUserId())).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> searchGlobal(@RequestParam String q, @RequestParam(required = false) String type) {
        Map<String, Object> result = new HashMap<>();
        if (type == null || type.equalsIgnoreCase("users")) {
            result.put("users", profilesService.searchProfiles(q));
        }
        if (type == null || type.equalsIgnoreCase("vacas")) {
            result.put("vacas", vacasRepository.findByNameContainingIgnoreCase(q));
        }
        if (type == null || type.equalsIgnoreCase("events")) {
            result.put("events", eventsRepository.findByTitleContainingIgnoreCase(q));
        }
        return ResponseEntity.ok(result);
    }
}

