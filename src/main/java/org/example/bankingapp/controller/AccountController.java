package org.example.bankingapp.controller;

import org.example.bankingapp.model.Account;
import org.example.bankingapp.model.BankTransaction;
import org.example.bankingapp.dto.AccountDto;
import org.example.bankingapp.dto.TransferDto;
import org.example.bankingapp.dto.WithdrawDto;
import org.example.bankingapp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<BankTransaction>> getAllTransactions() {
        List<BankTransaction> transactions = accountService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto request) {
        try {
            Account account = accountService.createAccount(request.getRecipientName(), request.getPinCode());
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/recipient")
    public ResponseEntity<List<Account>> getAccountsByRecipientName(@RequestParam String recipientName) {
        return ResponseEntity.ok(accountService.getAccountsByRecipientName(recipientName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        try {
            return accountService.getAccountById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long id, @RequestBody BigDecimal amount) {
        try {
            accountService.deposit(id, amount);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id, @RequestBody WithdrawDto request) {
        try {
            accountService.withdraw(id, request.getAmount(), request.getPinCode());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> transfer(@PathVariable Long id, @RequestBody TransferDto request) {
        try {
            accountService.transfer(id, request.getToAccountId(), request.getAmount(), request.getPinCode());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<BankTransaction>> getTransactions(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(accountService.getTransactions(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}