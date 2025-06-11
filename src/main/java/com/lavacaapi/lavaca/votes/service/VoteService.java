package com.lavacaapi.lavaca.votes.service;

import com.lavacaapi.lavaca.votes.Vote;
import com.lavacaapi.lavaca.votes.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> getVotesByWithdrawalId(UUID withdrawalId) {
        return voteRepository.findByWithdrawalId(withdrawalId);
    }

    public Optional<Vote> getVoteById(UUID id) {
        return voteRepository.findById(id);
    }
}

