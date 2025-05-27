package com.lavacaapi.lavaca.votes.controller;

import com.lavacaapi.lavaca.votes.Votes;
import com.lavacaapi.lavaca.votes.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/votes")
public class VotesController {

    @Autowired
    private VotesService votesService;

    @PostMapping
    public ResponseEntity<Votes> createVote(@RequestBody Votes vote) {
        return new ResponseEntity<>(votesService.createVote(vote), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Votes>> getAllVotes() {
        return ResponseEntity.ok(votesService.getAllVotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Votes> getVoteById(@PathVariable UUID id) {
        Optional<Votes> vote = votesService.getVoteById(id);
        return vote.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Votes>> getVotesByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(votesService.getVotesByVacaId(vacaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Votes>> getVotesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(votesService.getVotesByUserId(userId));
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<Votes>> getVotesByTransaction(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(votesService.getVotesByTransactionId(transactionId));
    }

    @GetMapping("/{voteId}/results")
    public ResponseEntity<?> getVoteResults(@PathVariable UUID voteId) {
        return ResponseEntity.ok(votesService.getVoteResults(voteId));
    }

    @PostMapping("/{voteId}/cast")
    public ResponseEntity<Votes> castVote(@PathVariable UUID voteId, @RequestBody Votes vote) {
        return ResponseEntity.ok(votesService.castVote(voteId, vote));
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<Votes>> getPendingVotesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(votesService.getPendingVotesByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Votes> updateVote(@PathVariable UUID id, @RequestBody Votes vote) {
        return ResponseEntity.ok(votesService.updateVote(id, vote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable UUID id) {
        votesService.deleteVote(id);
        return ResponseEntity.noContent().build();
    }
}
