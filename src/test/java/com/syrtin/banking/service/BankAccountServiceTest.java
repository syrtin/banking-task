package com.syrtin.banking.service;

import com.syrtin.banking.TestData;
import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.repository.BankAccountRepository;
import com.syrtin.banking.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BankAccountServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankAccountService = new BankAccountService(bankAccountRepository, validator);
    }

    @Test
    void testCreateBankAccount() throws Exception {
        BankAccount bankAccount = TestData.getFirstBankAccountValid();

        doNothing().when(validator).validateBankAccount(bankAccount);

        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount);

        verify(validator, times(1)).validateBankAccount(bankAccount);

        verify(bankAccountRepository, times(1)).save(bankAccount);

        assertEquals(bankAccount, createdBankAccount);
    }

    @Test
    void testGetClientBankAccounts() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(new BankAccount());
        bankAccounts.add(new BankAccount());

        when(bankAccountRepository.findAllByClientId(1L)).thenReturn(bankAccounts);

        List<BankAccount> clientBankAccounts = bankAccountService.getClientBankAccounts(1L);

        verify(bankAccountRepository, times(1)).findAllByClientId(1L);

        assertEquals(bankAccounts, clientBankAccounts);
    }

    @Test
    void testDeleteBankAccount() {
        bankAccountService.deleteBankAccount(1L);

        verify(bankAccountRepository, times(1)).deleteById(1L);
    }
}
