package org.example.bankingapp.service;

import org.example.bankingapp.model.Account;
import org.example.bankingapp.model.BankTransaction;
import org.example.bankingapp.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankTransactionService transactionService;

    public AccountService(AccountRepository accountRepository, BankTransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account createAccount(String recipientName, String pinCode) {
        if (pinCode.length() != 4 || !pinCode.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN code must be a four-digit number");
        }
        Account account = new Account(recipientName, pinCode);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByRecipientName(String recipientName) {
        return accountRepository.findByRecipientName(recipientName);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        transactionService.saveTransaction(accountId, amount, "DEPOSIT");
    }

    public void withdraw(Long accountId, BigDecimal amount, String pinCode) {
        Account account = accountRepository.findById(accountId).orElseThrow(NoSuchElementException::new);
        if (!account.getPinCode().equals(pinCode)) {
            throw new IllegalArgumentException("Invalid PIN code");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        transactionService.saveTransaction(accountId, amount, "WITHDRAWAL");
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String pinCode) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(NoSuchElementException::new);
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(NoSuchElementException::new);
        if (!fromAccount.getPinCode().equals(pinCode)) {
            throw new IllegalArgumentException("Invalid PIN code");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionService.saveTransaction(fromAccountId, amount, "TRANSFER_OUT");
        transactionService.saveTransaction(toAccountId, amount, "TRANSFER_IN");
    }

    public List<BankTransaction> getTransactions(Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    public List<BankTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}