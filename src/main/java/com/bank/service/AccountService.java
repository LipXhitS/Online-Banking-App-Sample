package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account findByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    public double checkBalance(String userId) {
        Account account = this.accountRepository.findByUserId(userId);

        if (account == null) {
            throw new AccountNotFoundException();
        }

        return account.getBalance();
    }

    public Account cashIn(String userId, double amount) {
        Account account = this.accountRepository.findByUserId(userId);

        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (amount <= 0) {
            throw new InvalidAmountException();
        }

        account.deposit(amount);
        this.accountRepository.update(account);

        transactionService.logTransaction(
                account.getAccountId(),
                "CASH IN",
                amount
        );

        return account;
    }
}
