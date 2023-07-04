package com.syrtin.banking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.domain.Client;
import com.syrtin.banking.mapper.JacksonObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:data.sql")
@TestPropertySource(locations = "classpath:application.properties")
@DisplayName("Integration Test")
class BankingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = JacksonObjectMapper.getMapper();

    @AfterEach
    public void cleanUpDatabase() throws SQLException {
        executeDataSql();
    }

    @Test
    @DisplayName("Check: Creating client and getting all clients including him from DB")
    void createClientAndThenRetrieveIt() throws Exception {
        // Creating and posting second client in DB
        Client client2 = TestData.getSecondClientValid();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client2)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    Client responseObject = objectMapper.readValue(responseBody, Client.class);
                    client2.setId(responseObject.getId());
                    assertEquals(client2, responseObject);
                });

        // Checking that Client has been added and table was increased
        mockMvc.perform(MockMvcRequestBuilders.get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<Client> clientsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(2, clientsList.size());
                        assertTrue(clientsList.contains(client2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @DisplayName("Check: After client is deleted, its bank account deleted as well")
    void deleteClientAndCheckIfBankAccountIsAlsoDeleted() throws Exception {

        // Retrieving client's id to make delete of Client
        String clientResponseBody = mockMvc.perform(MockMvcRequestBuilders.get("/api/client"))
                .andReturn().getResponse().getContentAsString();
        List<Client> clientsList = objectMapper.readValue(clientResponseBody, new TypeReference<>() {
        });
        long clientId = clientsList.get(0).getId();

        // Check the existence of Bank account which belongs to client
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/client/%s/bank-accounts", clientId)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<BankAccount> bankAccountsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(1, bankAccountsList.size());
                        Long actualBankAccountClientId = bankAccountsList.get(0).getClientId();
                        assertEquals(clientId, actualBankAccountClientId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        // Delete of the client
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/client/%s", clientId)))
                .andExpect(status().isNoContent());

        // Check that there is a no client
        mockMvc.perform(MockMvcRequestBuilders.get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<Client> updatedClientsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(0, updatedClientsList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        // And now lets check that there is no account of this user
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/client/%s/bank-accounts", clientId)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<BankAccount> bankAccountsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(0, bankAccountsList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @DisplayName("Check: No influence of operations under one bank account on client and other accounts")
    void createBankAccountsGetThemAllAndDeleteOneOfThem() throws Exception {
        // Retrieving client's id to make a post of Bank account
        // We assume, that data.sql works fine, but nevertheless retrieving for first account its id and clint's id
        String clientResponseBody = mockMvc.perform(MockMvcRequestBuilders.get("/api/client"))
                .andReturn().getResponse().getContentAsString();
        List<Client> clientsList = objectMapper.readValue(clientResponseBody, new TypeReference<>() {
        });
        long clientId = clientsList.get(0).getId();
        String accountResponseBody = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/client/%s/bank-accounts", clientId)))
                .andReturn().getResponse().getContentAsString();
        List<BankAccount> accountsList = objectMapper.readValue(accountResponseBody, new TypeReference<>() {
        });
        long account1Id = accountsList.get(0).getId();

        // Creating and posting second bank account of client1 in DB
        BankAccount account2 = new BankAccount(null, "987654321", "EUR", clientId);
        mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/client/%s/bank-accounts", clientId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account2)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    BankAccount responseObject = objectMapper.readValue(responseBody, BankAccount.class);
                    account2.setId(responseObject.getId());
                    assertEquals(account2, responseObject);
                });

        // Checking that BankAccount has been added and table was increased
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/client/%s/bank-accounts", clientId)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<BankAccount> updatedAccountsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(2, updatedAccountsList.size());
                        assertTrue(updatedAccountsList.contains(account2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        // Deleting BankAccount
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/bank-accounts/%s", account2.getId())))
                .andExpect(status().isNoContent());

        // Checking that BankAccount has been deleted and table was decreased
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/client/%s/bank-accounts", clientId)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    try {
                        List<BankAccount> updatedAccountsList = objectMapper.readValue(
                                responseBody,
                                new TypeReference<>() {
                                }
                        );
                        assertEquals(1, updatedAccountsList.size());
                        assertFalse(updatedAccountsList.contains(account2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @DisplayName("Check: Validates POST client request, by receiving Errors in result")
    void checkInvalidClientRequest() throws Exception {
        Client client2 = TestData.getSecondClientInvalid();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].Error").value("Validation fail for lastname : Lastname is required"))
                .andExpect(jsonPath("$.[1].Error").value("Validation fail for firstname : Firstname is required"))
                .andExpect(jsonPath("$.[2].Error").value("Validation fail for birthDate : Birth Date is required"))
                .andExpect(jsonPath("$.[3].Error").value("Validation fail for documentType : Document Type is required"))
                .andExpect(jsonPath("$.[4].Error").value("Validation fail for documentSN : Document Serial Number is required"));
    }

    @Test
    @DisplayName("Check: Validates POST client request, by matching documentType in application.properties")
    void checkInvalidClientRequestByDefinedDocumentType() throws Exception {
        Client client2 = TestData.getSecondClientInvalidByDocumentType();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].Error").value("Validation fail for documentType : Document Type is not valid"));

    }

    @Test
    @DisplayName("Check: Validates POST BankAccount request, by receiving Errors in result")
    void checkInvalidBankAccountRequest() throws Exception {
        BankAccount account2 = TestData.getFirstBankAccountInvalid();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/client/1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].Error").value("Validation fail for number : Account number is required"))
                .andExpect(jsonPath("$.[1].Error").value("Validation fail for currency : Currency is required"))
                .andExpect(jsonPath("$.[2].Error").value("Validation fail for clientId : Client ID is required"));
    }

    @Test
    @DisplayName("Check: Validates POST BankAccount request, by matching Currency Code in application.properties")
    void checkInvalidBankAccountRequestByCurrencyCode() throws Exception {
        BankAccount account2 = TestData.getFirstBankAccountInvalidByCurrencyCode();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/client/1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].Error").value("Validation fail for documentType : Document Type is not valid"));
    }

    private void executeDataSql() {
        String dataSqlPath = "data.sql";
        ClassPathResource resource = new ClassPathResource(dataSqlPath);
        try {
            String dataSql = new String(resource.getInputStream().readAllBytes());
            jdbcTemplate.execute(dataSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



