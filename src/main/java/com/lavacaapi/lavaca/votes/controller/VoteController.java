package com.lavacaapi.lavaca.votes.controller;

import com.lavacaapi.lavaca.votes.Vote;
import com.lavacaapi.lavaca.votes.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
        Vote created = voteService.createVote(vote);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/withdrawal/{withdrawalId}")
    public ResponseEntity<List<Vote>> getVotesByWithdrawal(@PathVariable UUID withdrawalId) {
        return ResponseEntity.ok(voteService.getVotesByWithdrawalId(withdrawalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable UUID id) {
        Optional<Vote> vote = voteService.getVoteById(id);
        return vote.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

