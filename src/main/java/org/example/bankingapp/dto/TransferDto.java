package org.example.bankingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransferDto {
    private Long toAccountId;
    private BigDecimal amount;
    private String pinCode;
}