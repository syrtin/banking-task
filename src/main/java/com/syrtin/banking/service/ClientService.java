package com.syrtin.banking.service;

import com.syrtin.banking.domain.Client;
import com.syrtin.banking.repository.ClientRepository;
import com.syrtin.banking.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private ClientRepository clientRepository;
    private Validator validator;

    public ClientService(ClientRepository clientRepository, Validator validator) {
        this.clientRepository = clientRepository;
        this.validator = validator;
    }

    public Client createClient(Client client) throws Exception {
        validator.validateClient(client);
        logger.info("create client {}", client);
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        logger.info("get all clients");
        return clientRepository.findAll();
    }

    public void deleteClient(Long id) {
        logger.info("delete client by id {}", id);
        clientRepository.deleteById(id);
    }
}