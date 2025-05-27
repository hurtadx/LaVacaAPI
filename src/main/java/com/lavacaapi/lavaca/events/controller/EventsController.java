package com.lavacaapi.lavaca.events.controller;

import com.lavacaapi.lavaca.events.Events;
import com.lavacaapi.lavaca.events.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @PostMapping
    public ResponseEntity<Events> createEvent(@RequestBody Events event) {
        return new ResponseEntity<>(eventsService.createEvent(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents() {
        return ResponseEntity.ok(eventsService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable UUID id) {
        Optional<Events> event = eventsService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Events>> getEventsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(eventsService.getEventsByVacaId(vacaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Events>> getEventsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(eventsService.getEventsByUserId(userId));
    }

    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<Events>> getRecentEvents(@PathVariable int limit) {
        return ResponseEntity.ok(eventsService.getRecentEvents(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Events>> searchEvents(@RequestParam String query) {
        return ResponseEntity.ok(eventsService.searchEvents(query));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable UUID id, @RequestBody Events event) {
        return ResponseEntity.ok(eventsService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventsService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
