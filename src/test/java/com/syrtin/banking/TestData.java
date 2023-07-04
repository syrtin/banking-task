package com.syrtin.banking;

import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.domain.Client;

import java.time.LocalDate;

public class TestData {
    public static Client getFirstClientValid() {
        return new Client(null,
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "PASSPORT",
                "1234567890",
                LocalDate.of(1991, 1, 1));
    }

    public static Client getFirstClientInvalidByDate() {
        return new Client(null,
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "PASSPORT",
                "1234567890",
                LocalDate.of(2021, 1, 1));
    }

    public static Client getSecondClientValid() {
        return new Client(null,
                "Petrov",
                "Petr",
                "Petrovich",
                "DRIVER_LICENSE",
                "0987654321",
                LocalDate.of(2001, 1, 1));
    }

    public static Client getSecondClientInvalid() {
        return new Client(null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public static Client getSecondClientInvalidByDocumentType() {
        return new Client(null,
                "Petrov",
                "Petr",
                "Petrovich",
                "WRONG",
                "0987654321",
                LocalDate.of(2001, 1, 1));
    }

    public static BankAccount getFirstBankAccountValid() {
        return new BankAccount(null,
                "123456789",
                "USD",
                1L);
    }


    public static BankAccount getFirstBankAccountInvalid() {
        return new BankAccount(null,
                null,
                null,
                null);
    }

    public static BankAccount getFirstBankAccountInvalidByCurrencyCode() {
        return new BankAccount(null,
                "12345",
                "YEN",
                1L);
    }
}
