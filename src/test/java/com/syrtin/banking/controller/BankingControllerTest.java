package com.syrtin.banking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syrtin.banking.TestData;
import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.domain.Client;
import com.syrtin.banking.mapper.JacksonObjectMapper;
import com.syrtin.banking.service.BankAccountService;
import com.syrtin.banking.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankingController.class)
public class BankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private ClientService clientService;

    private final ObjectMapper objectMapper = JacksonObjectMapper.getMapper();

    @Test
    void createClient() throws Exception {
        Client client = TestData.getFirstClientValid();

        doReturn(client).when(clientService).createClient(client);

        when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllClients() throws Exception {
        Client client1 = TestData.getFirstClientValid();

        Client client2 = TestData.getSecondClientValid();

        List<Client> clientList = new ArrayList<>();
        clientList.add(client1);
        clientList.add(client2);

        Mockito.when(clientService.getAllClients()).thenReturn(clientList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void deleteClient() throws Exception {
        Long id = 1L;
        doNothing().when(clientService).deleteClient(id);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/client/{id}", id))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(id);
    }

    @Test
    public void getClientBankAccounts() throws Exception {
        Long clientId = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setClientId(clientId);
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(bankAccount);

        when(bankAccountService.getClientBankAccounts(clientId)).thenReturn(bankAccounts);

        mockMvc.perform(get("/api/client/{clientId}/bank-accounts", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].clientId").value(clientId));

        verify(bankAccountService, times(1)).getClientBankAccounts(clientId);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    public void createBankAccount() throws Exception {
        Long clientId = 1L;

        BankAccount bankAccount = TestData.getFirstBankAccountValid();

        given(bankAccountService.createBankAccount(any(BankAccount.class))).willReturn(bankAccount);

        mockMvc.perform(post("/api/client/{clientId}/bank-accounts", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("123456789"))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(bankAccountService, times(1)).createBankAccount(any(BankAccount.class));
    }

    @Test
    public void deleteBankAccount() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/bank-accounts/{id}", id))
                .andExpect(status().isNoContent());

        verify(bankAccountService, times(1)).deleteBankAccount(id);
        verifyNoMoreInteractions(bankAccountService);
    }
}