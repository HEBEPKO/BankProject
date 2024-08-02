package org.example.bankingapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDto {
    private String recipientName;
    private String pinCode;
}