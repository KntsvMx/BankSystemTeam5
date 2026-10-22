package validation;

import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.model.Transaction;
import org.example.banksystemteam5.model.TransactionType;
import org.example.banksystemteam5.validation.TransactionValidation;
import org.example.banksystemteam5.validation.TransactionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionValidatorTest {

    private final TransactionValidation validator = new TransactionValidator();

    private Transaction validTransaction() {
        return Transaction.builder()
                .account(new Account())
                .type(TransactionType.DEPOSIT)
                .amount(new BigDecimal("10.00"))
                .build();
    }

    @Test
    @DisplayName("Valid transaction passes validation")
    void shouldAcceptValidTransaction() {
        assertTrue(validator.validate(validTransaction()));
    }

    @Test
    @DisplayName("Null transaction fails validation")
    void shouldRejectNullTransaction() {
        assertFalse(validator.validate(null));
    }

    @Test
    @DisplayName("Transaction without an account fails validation")
    void shouldRejectMissingAccount() {
        Transaction transaction = validTransaction();
        transaction.setAccount(null);

        assertFalse(validator.validate(transaction));
    }

    @Test
    @DisplayName("Transaction without a type fails validation")
    void shouldRejectMissingType() {
        Transaction transaction = validTransaction();
        transaction.setType(null);

        assertFalse(validator.validate(transaction));
    }

    @Test
    @DisplayName("Transaction with a zero amount fails validation")
    void shouldRejectZeroAmount() {
        Transaction transaction = validTransaction();
        transaction.setAmount(BigDecimal.ZERO);

        assertFalse(validator.validate(transaction));
    }

    @Test
    @DisplayName("Transaction with a negative amount fails validation")
    void shouldRejectNegativeAmount() {
        Transaction transaction = validTransaction();
        transaction.setAmount(new BigDecimal("-10"));

        assertFalse(validator.validate(transaction));
    }
}