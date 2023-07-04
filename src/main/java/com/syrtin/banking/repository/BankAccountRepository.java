package com.syrtin.banking.repository;

import com.syrtin.banking.domain.BankAccount;

import java.util.List;

public interface BankAccountRepository {
    BankAccount save(BankAccount bankAccount);
    List<BankAccount> findAllByClientId(Long clientId);
    void deleteById(Long id);
}