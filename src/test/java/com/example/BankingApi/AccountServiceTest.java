package com.example.BankingApi;

import com.example.BankingApi.dto.AccountRequest;
import com.example.BankingApi.entity.Account;
import com.example.BankingApi.entity.User;
import com.example.BankingApi.repository.AccountRepository;
import com.example.BankingApi.repository.UserRepository;
import com.example.BankingApi.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Before
    public void setUp() {
        User user1 = User.builder().id(1L).name("Arisha Barron").build();
        User user2 = User.builder().id(2L).name("Branden Gibson").build();
        User user3 = User.builder().id(3L).name("Rhonda Church").build();
        User user4 = User.builder().id(4L).name("Georgina Hazel").build();

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4));
    }

    @Test
    public void testCreateAccount() {
        AccountRequest accountRequest = AccountRequest.builder()
                .userId(1L)
                .balance(BigDecimal.valueOf(100))
                .build();

        Account createdAccount = accountService.createAccount(accountRequest);

        assertNotNull(createdAccount);
        assertEquals(1L, createdAccount.getUser().getId().longValue());
        assertEquals(BigDecimal.valueOf(100), createdAccount.getBalance());
    }

    @Test
    public void testTransferAmount() {
        AccountRequest accountRequest1 = AccountRequest.builder()
                .userId(1L)
                .balance(BigDecimal.valueOf(500))
                .build();

        AccountRequest accountRequest2 = AccountRequest.builder()
                .userId(2L)
                .balance(BigDecimal.valueOf(200))
                .build();

        Account fromAccount = accountService.createAccount(accountRequest1);
        Account toAccount = accountService.createAccount(accountRequest2);

        BigDecimal transferAmount = BigDecimal.valueOf(100);
        accountService.transferAmount(fromAccount.getId(), toAccount.getId(), transferAmount);

        BigDecimal expectedFromBalance = BigDecimal.valueOf(400);
        BigDecimal expectedToBalance = BigDecimal.valueOf(300);

        assertEquals(expectedFromBalance, accountService.getAccountBalance(fromAccount.getId()));
        assertEquals(expectedToBalance, accountService.getAccountBalance(toAccount.getId()));
    }


    @Test
    public void testGetAccountBalance() {
        AccountRequest accountRequest = AccountRequest.builder()
                .userId(3L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Account createdAccount = accountService.createAccount(accountRequest);

        // Check the initial balance
        BigDecimal expectedBalance = BigDecimal.valueOf(1000);
        assertEquals(expectedBalance, accountService.getAccountBalance(createdAccount.getId()));
    }

    @Test
    public void testGetTransferHistory() {
        // Implement this test based on your getTransferHistory logic
    }
}
