package org.example.bankingapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    public BankTransaction() {
    }

    public BankTransaction(Long accountId, BigDecimal amount, String type) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}