package com.example.BankingApi.controller;

import com.example.BankingApi.dto.AccountRequest;
import com.example.BankingApi.entity.Account;
import com.example.BankingApi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Endpoint pour créer un nouveau compte
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest accountRequest) {
        // Appeler le service pour créer un compte
        Account createdAccount = accountService.createAccount(accountRequest);
        // Retourner la réponse avec le compte créé et le statut HTTP 201 (CREATED)
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Endpoint pour obtenir le solde d'un compte
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Long accountId) {
        // Appeler le service pour obtenir le solde du compte
        BigDecimal balance = accountService.getAccountBalance(accountId);
        // Retourner la réponse avec le solde et le statut HTTP 200 (OK)
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    // Endpoint pour effectuer un transfert entre comptes
    @PostMapping("/transfer")
    public ResponseEntity<String> transferAmount(@RequestParam Long fromAccountId,
                                                 @RequestParam Long toAccountId,
                                                 @RequestParam BigDecimal amount) {
        // Appeler le service pour effectuer le transfert
        accountService.transferAmount(fromAccountId, toAccountId, amount);
        // Retourner la réponse avec le message de succès et le statut HTTP 200 (OK)
        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
    }

    // Endpoint pour obtenir l'historique des transferts d'un compte
    @GetMapping("/{accountId}/transfers")
    public ResponseEntity<List<String>> getTransferHistory(@PathVariable Long accountId) {
        // Appeler le service pour obtenir l'historique des transferts
        List<String> transferHistory = accountService.getTransferHistory(accountId);
        // Retourner la réponse avec l'historique et le statut HTTP 200 (OK)
        return new ResponseEntity<>(transferHistory, HttpStatus.OK);
    }
}
