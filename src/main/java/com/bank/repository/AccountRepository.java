package com.bank.repository;

import com.bank.model.Account;

import java.sql.Connection;

public interface AccountRepository {
    Account findByUserId(String userId);

    Account findByUserId(String userId, Connection connection);

    String findUserIdByUsernameUserNumber(String usernameUserNumber, Connection connection);

    void update(Account account);

    void update(Account account, Connection connection);
}
