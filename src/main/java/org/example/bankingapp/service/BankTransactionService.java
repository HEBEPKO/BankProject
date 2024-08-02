package org.example.bankingapp.service;

import org.example.bankingapp.model.BankTransaction;
import org.example.bankingapp.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankTransactionService {

    private final BankTransactionRepository transactionRepository;

    public BankTransactionService(BankTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveTransaction(Long accountId, BigDecimal amount, String type) {
        BankTransaction transaction = new BankTransaction(accountId, amount, type);
        transactionRepository.save(transaction);
    }

    public List<BankTransaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<BankTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}