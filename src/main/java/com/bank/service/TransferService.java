package com.bank.service;

import com.bank.config.TransactionManager;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.SameAccountTransferException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;

public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionManager transactionManager;
    private final TransactionService transactionService;

    public TransferService(
            AccountRepository accountRepository,
            TransactionService transactionService,
            TransactionManager transactionManager
    ) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.transactionManager = transactionManager;
    }

    public void cashTransfer(String userIdFrom, String userIdTo, double amount) {
        if (userIdFrom.equals(userIdTo)) {
            throw new SameAccountTransferException();
        }

        transactionManager.executeInTransaction(connection -> {
            Account from = this.accountRepository.findByUserId(userIdFrom, connection);
            String targetUserIdTo = this.accountRepository.findUserIdByUsernameUserNumber(userIdTo, connection);
            Account to = this.accountRepository.findByUserId(targetUserIdTo, connection);

            if(from == null || to == null) {
                throw new AccountNotFoundException();
            }

            if(amount <= 0) {
                throw new InvalidAmountException();
            }

            if(from.getBalance() < amount) {
                throw new InsufficientFundsException();
            }

            from.withdraw(amount);
            to.deposit(amount);

            accountRepository.update(from, connection);
            accountRepository.update(to, connection);

            transactionService.logTransaction(
                    from.getAccountId(),
                    "TRANSFER OUT",
                    amount,
                    connection
            );

            transactionService.logTransaction(
                    to.getAccountId(),
                    "TRANSFER IN",
                    amount,
                    connection
            );
        });
    }
}
