package org.example.banksystemteam5.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private Long id;
    private Account account;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String note;
}