package com.bank.service;

import com.bank.model.User;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    public User login(String usernameNumber, int userPin) {
        List<User> users = new ArrayList<>();
        users.add(new User("00001", "User1", "12345", 1234));
        users.add(new User("00002", "User2", "12345", 1234));
        users.add(new User("00003", "User3", "12345", 1234));
        for (User user : users) {
            if ((user.usernameGetter().equals(usernameNumber) || 
                user.userNumberGetter().equals(usernameNumber))
                && user.userPINGetter() == userPin) {
               return user;
            }
        }
        return null;
    }
}