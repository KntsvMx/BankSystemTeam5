package validation;

import org.example.banksystemteam5.model.Account;
import org.example.banksystemteam5.validation.AccountValidation;
import org.example.banksystemteam5.validation.AccountValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountValidatorTest {

    private final AccountValidation validator = new AccountValidator();

    private Account validAccount() {
        return Account.builder()
                .ownerName("Arthur")
                .iban("TEST")
                .balance(new BigDecimal("100.00"))
                .build();
    }

    @Test
    @DisplayName("Valid account passes validation")
    void shouldAcceptValidAccount() {
        assertTrue(validator.validate(validAccount()));
    }

    @Test
    @DisplayName("Null account fails validation")
    void shouldRejectNullAccount() {
        assertFalse(validator.validate(null));
    }

    @Test
    @DisplayName("Blank owner name fails validation")
    void shouldRejectBlankOwnerName() {
        Account account = validAccount();
        account.setOwnerName(" ");

        assertFalse(validator.validate(account));
    }

    @Test
    @DisplayName("Account without an IBAN fails validation")
    void shouldRejectMissingIban() {
        Account account = validAccount();
        account.setIban(null);

        assertFalse(validator.validate(account));
    }

    @Test
    @DisplayName("Account with a negative balance fails validation")
    void shouldRejectNegativeBalance() {
        Account account = validAccount();
        account.setBalance(new BigDecimal("-1"));

        assertFalse(validator.validate(account));
    }

    @Test
    @DisplayName("Account with a zero balance passes validation")
    void shouldAcceptZeroBalance() {
        Account account = validAccount();
        account.setBalance(BigDecimal.ZERO);

        assertTrue(validator.validate(account));
    }
}