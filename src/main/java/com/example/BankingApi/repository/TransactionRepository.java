package com.example.BankingApi.repository;

import com.example.BankingApi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);

}
