package org.example.banksystemteam5.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;
    private String iban;
    private String ownerName;
    private BigDecimal balance;
}