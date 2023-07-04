package com.syrtin.banking.service;
import com.syrtin.banking.TestData;
import com.syrtin.banking.domain.Client;
import com.syrtin.banking.repository.ClientRepository;
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

public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService(clientRepository, validator);
    }

    @Test
    void createClient() throws Exception {
        Client client = TestData.getFirstClientValid();

        when(clientRepository.save(client)).thenReturn(client);

        Client createdClient = clientService.createClient(client);

        verify(clientRepository, times(1)).save(client);

        assertEquals(client, createdClient);
    }

    @Test
    void getAllClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(TestData.getFirstClientValid());
        clients.add(TestData.getSecondClientValid());

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> allClients = clientService.getAllClients();

        verify(clientRepository, times(1)).findAll();

        assertEquals(clients, allClients);
    }

    @Test
    void deleteClient() {
        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }
}
