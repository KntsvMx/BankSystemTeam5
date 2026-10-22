package org.example.banksystemteam5.validation;

import org.example.banksystemteam5.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator implements AccountValidation {

    @Override
    public boolean validate(Account account) {
        if (account == null) {
            return false;
        }

        if (account.getOwnerName() == null
                || account.getOwnerName().isBlank()) {
            return false;
        }

        if (account.getIban() == null
                || account.getIban().isBlank()) {
            return false;
        }

        if (account.getBalance() == null
                || account.getBalance().signum() < 0
                || account.getBalance().scale() > 2) {
            return false;
        }

        return true;
    }
}
