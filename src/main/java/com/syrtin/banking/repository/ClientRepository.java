package com.syrtin.banking.repository;

import com.syrtin.banking.domain.Client;

import java.util.List;

public interface ClientRepository {
    Client save(Client client);
    List<Client> findAll();
    void deleteById(Long id);
}