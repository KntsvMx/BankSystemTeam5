package org.example.banksystemteam5.validation;

import org.example.banksystemteam5.model.Transaction;

public interface TransactionValidation {
    boolean validate(Transaction transaction);
}
