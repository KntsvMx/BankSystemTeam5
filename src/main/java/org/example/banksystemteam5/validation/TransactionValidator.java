package org.example.banksystemteam5.validation;

import org.example.banksystemteam5.model.Transaction;

public class TransactionValidator implements TransactionValidation {

    @Override
    public boolean validate(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (transaction.getAccount() == null) {
            return false;
        }

        if (transaction.getType() == null) {
            return false;
        }

        if (transaction.getAmount() == null
                || transaction.getAmount().signum() <= 0) {
            return false;
        }

        return true;
    }
}
