package com.syrtin.banking.validation;

import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.domain.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    @Value("#{'${banking.document-types}'.split(',\\s*')}")
    public Set<String> validDocumentTypes;

    @Value("#{'${banking.currency-codes}'.split(',\\s*')}")
    public Set<String> validCurrencyCodes;


    public void validateClient(Client client) throws ValidationException {
        List<ValidationError> errors = new ArrayList<>();

        String lastname = client.getLastname();
        if (lastname == null || lastname.isEmpty()) {
            errors.add(new ValidationError("lastname", "Lastname is required"));
        } else if (!lastname.matches("[a-zA-Zа-яА-Я]+")) {
            errors.add(new ValidationError("lastname", "Lastname should contain only letters"));
        }

        String firstname = client.getFirstname();
        if (firstname == null || firstname.isEmpty()) {
            errors.add(new ValidationError("firstname", "Firstname is required"));
        } else if (!firstname.matches("[a-zA-Zа-яА-Я]+")) {
            errors.add(new ValidationError("firstname", "Firstname should contain only letters"));
        }

        String middlename = client.getMiddlename();
        if (!(middlename == null || middlename.isEmpty())) {
            if (!middlename.matches("[a-zA-Zа-яА-Я]+")) {
                errors.add(new ValidationError("middlename", "Middlename should contain only letters"));
            }
        }

        if (client.getBirthDate() == null) {
            errors.add(new ValidationError("birthDate", "Birth Date is required"));
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalDate minValidDate = currentDate.minusYears(14);
            if (client.getBirthDate().isAfter(minValidDate)) {
                errors.add(new ValidationError("birthDate", "You are too young to have a bank account"));
            }
        }

        String documentType = client.getDocumentType();
        if (documentType == null || documentType.isEmpty()) {
            errors.add(new ValidationError("documentType", "Document Type is required"));
        } else if (!validDocumentTypes.contains(documentType)) {
            errors.add(new ValidationError("documentType", "Document Type is not valid"));
        }

        String documentSN = client.getDocumentSN();
        if (documentSN == null || documentSN.isEmpty()) {
            errors.add(new ValidationError("documentSN", "Document Serial Number is required"));
        } else if (!documentSN.matches("[\\d]+")) {
            errors.add(new ValidationError("firstname", "Firstname should contain only letters"));
        }

        if (!errors.isEmpty()) {
            logger.error("validation error for client parsing");
            throw new ValidationException(errors);
        }
    }

    public void validateBankAccount(BankAccount bankAccount) throws Exception {
        List<ValidationError> errors = new ArrayList<>();

        String accountNumber = bankAccount.getNumber();
        if (accountNumber == null || accountNumber.isEmpty()) {
            errors.add(new ValidationError("number", "Account number is required"));
        }

        String currency = bankAccount.getCurrency();
        if (currency == null || currency.isEmpty()) {
            errors.add(new ValidationError("currency", "Currency is required"));
        } else if (!validCurrencyCodes.contains(currency)) {
            errors.add(new ValidationError("documentType", "Document Type is not valid"));
        }

        if (bankAccount.getClientId() == null) {
            errors.add(new ValidationError("clientId", "Client ID is required"));
        }

        if (!errors.isEmpty()) {
            logger.error("validation error for bank account parsing");
            throw new ValidationException(errors);
        }
    }
}