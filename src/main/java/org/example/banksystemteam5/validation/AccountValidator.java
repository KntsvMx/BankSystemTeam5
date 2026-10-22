package org.example.banksystemteam5.validation;

import org.example.banksystemteam5.model.Account;

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
                || account.getBalance().signum() < 0) {
            return false;
        }

        return true;
    }
}
