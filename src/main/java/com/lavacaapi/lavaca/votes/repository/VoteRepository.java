package com.lavacaapi.lavaca.votes.repository;

import com.lavacaapi.lavaca.votes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    List<Vote> findByWithdrawalId(UUID withdrawalId);
}

