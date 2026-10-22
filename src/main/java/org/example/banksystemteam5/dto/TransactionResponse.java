package org.example.banksystemteam5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.banksystemteam5.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private Long accountId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String note;
}