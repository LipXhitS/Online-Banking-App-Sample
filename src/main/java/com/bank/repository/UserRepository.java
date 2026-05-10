package com.bank.repository;

import com.bank.model.User;

public interface UserRepository {
    User findByLogin(String input);
}
