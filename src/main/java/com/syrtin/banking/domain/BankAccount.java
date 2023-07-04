package com.syrtin.banking.domain;

import java.util.Objects;

public class BankAccount {
    private Long id;
    private String number;
    private String currency;
    private Long clientId;

    public BankAccount() {
    }

    public BankAccount(Long id, String number, String currency, Long clientId) {
        this.id = id;
        this.number = number;
        this.currency = currency;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!number.equals(that.number)) return false;
        if (!currency.equals(that.currency)) return false;
        return clientId.equals(that.clientId);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + number.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + clientId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", currency='" + currency + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}