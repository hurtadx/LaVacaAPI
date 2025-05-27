package com.lavacaapi.lavaca.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.transactions.repository.TransactionsRepository;
import com.lavacaapi.lavaca.invitations.repository.InvitationsRepository;
import com.lavacaapi.lavaca.events.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private VacasRepository vacasRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private InvitationsRepository invitationsRepository;
    @Autowired
    private EventsRepository eventsRepository;

    @GetMapping("/summary/{userId}")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable UUID userId) {
        Map<String, Object> summary = new HashMap<>();
        long totalVacas = vacasRepository.findAll().stream().filter(v -> userId.equals(v.getUserId())).count();
        long totalContributions = transactionsRepository.findByUserId(userId).size();
        long pendingInvitations = invitationsRepository.findBySenderId(userId).stream().filter(i -> "PENDING".equalsIgnoreCase(i.getStatus())).count();
        summary.put("userId", userId);
        summary.put("totalVacas", totalVacas);
        summary.put("totalContributions", totalContributions);
        summary.put("pendingInvitations", pendingInvitations);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable UUID userId) {
        Map<String, Object> stats = new HashMap<>();
        long totalVacas = vacasRepository.findAll().stream().filter(v -> userId.equals(v.getUserId())).count();
        long totalContributions = transactionsRepository.findByUserId(userId).size();
        long pendingInvitations = invitationsRepository.findBySenderId(userId).stream().filter(i -> "PENDING".equalsIgnoreCase(i.getStatus())).count();
        stats.put("userId", userId);
        stats.put("totalVacas", totalVacas);
        stats.put("totalContributions", totalContributions);
        stats.put("pendingInvitations", pendingInvitations);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent-activity/{userId}")
    public ResponseEntity<Map<String, Object>> getRecentActivity(@PathVariable UUID userId, @RequestParam(required = false, defaultValue = "10") int limit) {
        Map<String, Object> activity = new HashMap<>();
        var recentEvents = eventsRepository.findByUserIdOrderByCreatedAtDesc(userId);
        activity.put("userId", userId);
        activity.put("limit", limit);
        activity.put("activities", recentEvents.stream().limit(limit).toList());
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<Map<String, Object>> getNotifications(@PathVariable UUID userId) {
        Map<String, Object> notifications = new HashMap<>();
        var pendingInvitations = invitationsRepository.findBySenderId(userId).stream().filter(i -> "PENDING".equalsIgnoreCase(i.getStatus())).toList();
        notifications.put("userId", userId);
        notifications.put("notifications", pendingInvitations);
        return ResponseEntity.ok(notifications);
    }
}
