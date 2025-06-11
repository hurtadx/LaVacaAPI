package com.lavacaapi.lavaca.withdrawals.repository;

import com.lavacaapi.lavaca.withdrawals.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, UUID> {
    List<Withdrawal> findByVacaId(UUID vacaId);
}

