package com.syrtin.banking.service;

import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.repository.BankAccountRepository;
import com.syrtin.banking.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private BankAccountRepository bankAccountRepository;
    private Validator validator;

    public BankAccountService(BankAccountRepository bankAccountRepository, Validator validator) {
        this.bankAccountRepository = bankAccountRepository;
        this.validator = validator;
    }

    public BankAccount createBankAccount(BankAccount bankAccount) throws Exception {
        validator.validateBankAccount(bankAccount);
        logger.info("create bank account {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getClientBankAccounts(Long clientId) {
        logger.info("get all bank accounts");
        return bankAccountRepository.findAllByClientId(clientId);
    }

    public void deleteBankAccount(Long id) {
        logger.info("delete bank account by id {}", id);
        bankAccountRepository.deleteById(id);
    }
}