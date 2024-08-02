package org.example.bankingapp.repository;

import org.example.bankingapp.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
    List<BankTransaction> findByAccountId(Long accountId);
}
