package com.syrtin.banking.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Objects;

public class Client {
    private Long id;
    private String lastname;
    private String firstname;
    private String middlename;
    private String documentType;
    private String documentSN;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public Client() {
    }

    public Client(Long id, String lastname, String firstname, String middlename, String documentType, String documentSN, LocalDate birthDate) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.documentType = documentType;
        this.documentSN = documentSN;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentSN() {
        return documentSN;
    }

    public void setDocumentSN(String documentSN) {
        this.documentSN = documentSN;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!Objects.equals(id, client.id)) return false;
        if (!lastname.equals(client.lastname)) return false;
        if (!firstname.equals(client.firstname)) return false;
        if (!Objects.equals(middlename, client.middlename)) return false;
        if (!documentType.equals(client.documentType)) return false;
        if (!documentSN.equals(client.documentSN)) return false;
        return birthDate.equals(client.birthDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + lastname.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + documentType.hashCode();
        result = 31 * result + documentSN.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentSN='" + documentSN + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}