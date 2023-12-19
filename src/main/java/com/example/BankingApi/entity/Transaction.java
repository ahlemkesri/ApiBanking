package com.example.BankingApi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {
   @Id
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime timestamp;
}
