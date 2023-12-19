package com.example.BankingApi.service;

import com.example.BankingApi.dto.AccountRequest;
import com.example.BankingApi.entity.Account;
import com.example.BankingApi.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest);

    void transferAmount(Long fromAccountId, Long toAccountId, BigDecimal amount);

    BigDecimal getAccountBalance(Long accountId);

    List<String> getTransferHistory(Long accountId);
}

