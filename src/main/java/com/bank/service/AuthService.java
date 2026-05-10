package com.bank.service;

import com.bank.exception.InvalidCredentialsException;
import com.bank.model.User;
import com.bank.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String input, int pin) {
        User user = userRepository.findByLogin(input);

        if (user == null || !user.validatePin(pin)) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public void logout() {
    }
}
