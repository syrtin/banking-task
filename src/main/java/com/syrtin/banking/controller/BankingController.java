package com.syrtin.banking.controller;

import com.syrtin.banking.domain.BankAccount;
import com.syrtin.banking.domain.Client;
import com.syrtin.banking.service.BankAccountService;
import com.syrtin.banking.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BankingController {
    private ClientService clientService;
    private BankAccountService bankAccountService;

    public BankingController(ClientService clientService, BankAccountService bankAccountService) {
        this.clientService = clientService;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/client")
    public ResponseEntity<Client> createClient(@RequestBody Client client) throws Exception {
        Client created = clientService.createClient(client);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/client")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @DeleteMapping("/client/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}/bank-accounts")
    public ResponseEntity<List<BankAccount>> getClientBankAccounts(@PathVariable Long clientId) {
        List<BankAccount> bankAccounts = bankAccountService.getClientBankAccounts(clientId);
        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/client/{clientId}/bank-accounts")
    public ResponseEntity<BankAccount> createBankAccount(@PathVariable Long clientId, @RequestBody BankAccount bankAccount) throws Exception {
        BankAccount created = bankAccountService.createBankAccount(bankAccount);
        bankAccount.setClientId(clientId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/bank-accounts/{accountId}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/bank-accounts/{bankAccountId}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long bankAccountId) {
        bankAccountService.deleteBankAccount(bankAccountId);
        return ResponseEntity.noContent().build();
    }
}