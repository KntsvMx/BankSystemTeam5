package org.example.banksystemteam5.validation;

import org.example.banksystemteam5.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator implements TransactionValidation {

    @Override
    public boolean validate(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (transaction.getAccount() == null
                || transaction.getAccount().getId() == null
                || transaction.getAccount().getId() <= 0) {
            return false;
        }

        if (transaction.getType() == null) {
            return false;
        }

        if (transaction.getAmount() == null
                || transaction.getAmount().signum() <= 0
                || transaction.getAmount().scale() > 2) {
            return false;
        }

        return transaction.getCreatedAt() != null
                && (transaction.getNote() == null || transaction.getNote().length() <= 255);
    }
}
