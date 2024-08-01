package org.example.bankingapp.service;

import org.example.bankingapp.model.Account;
import org.example.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class DataInitializationService {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        Account account1 = new Account("Ivan Ivanov", "1234");
        account1.setBalance(new BigDecimal("1000.00"));
        accountRepository.save(account1);

        Account account2 = new Account("Den Krylov", "5678");
        account2.setBalance(new BigDecimal("2000.00"));
        accountRepository.save(account2);
    }
}