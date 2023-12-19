package com.example.BankingApi.service;

import com.example.BankingApi.dto.AccountRequest;
import com.example.BankingApi.entity.Account;
import com.example.BankingApi.entity.Transaction;
import com.example.BankingApi.entity.User;
import com.example.BankingApi.repository.AccountRepository;
import com.example.BankingApi.repository.TransactionRepository;
import com.example.BankingApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Méthode pour créer un compte
    @Override
    public Account createAccount(AccountRequest accountRequest) {
        // Récupérer l'utilisateur associé au compte
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Créer un nouveau compte avec le solde initial
        Account newAccount = Account.builder()
                .user(user)
                .balance(accountRequest.getBalance())
                .build();
        // Enregistrer le nouveau compte dans la base de données
        return accountRepository.save(newAccount);
    }

    // Méthode pour transférer un montant entre deux comptes
    @Override
    public void transferAmount(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // Récupérer les comptes source et destinataire
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From Account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To Account not found"));

        // Vérifier si le solde du compte source est suffisant pour le transfert
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for transfer");
        }

        // Mettre à jour les soldes des comptes source et destinataire
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Enregistrer la transaction
        Transaction transaction = Transaction.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .amount(amount)
                .build();
        transactionRepository.save(transaction);
    }

    // Méthode pour obtenir le solde d'un compte
    @Override
    public BigDecimal getAccountBalance(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    // Méthode pour obtenir l'historique des transactions d'un compte
    @Override
    public List<String> getTransferHistory(Long accountId) {
        // Récupérer les transactions liées au compte source ou destinataire
        List<Transaction> transactions = transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);

        // Mapper les transactions en messages lisibles
        return transactions.stream()
                .map(transaction -> {
                    if (transaction.getFromAccountId().equals(accountId)) {
                        return "Sent " + transaction.getAmount() + " to Account ID " + transaction.getToAccountId();
                    } else {
                        return "Received " + transaction.getAmount() + " from Account ID " + transaction.getFromAccountId();
                    }
                })
                .collect(Collectors.toList());
    }
}


