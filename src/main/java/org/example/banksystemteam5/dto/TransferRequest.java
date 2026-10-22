package org.example.banksystemteam5.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "Sender account id is required")
    @Positive(message = "Sender account id must be positive")
    private Long fromAccountId;

    @NotNull(message = "Receiver account id is required")
    @Positive(message = "Receiver account id must be positive")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 15 integer digits and 2 decimal places")
    private BigDecimal amount;

    @Size(max = 255, message = "Note must be max 255 characters")
    private String note;
}
