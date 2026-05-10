package com.bank.service;

import com.bank.config.DatabaseTransactionManager;
import com.bank.config.DatabaseInitializer;
import com.bank.config.TransactionManager;
import com.bank.dao.AccountDao;
import com.bank.dao.TransactionDao;
import com.bank.dao.UserDao;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankingServiceTest {

    @TempDir
    Path tempDir;

    private AuthService authService;
    private AccountService accountService;
    private TransferService transferService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        System.setProperty("bank.db.url", "jdbc:sqlite:" + tempDir.resolve("test-bank.db"));
        DatabaseInitializer.initialize();

        AccountRepository accountRepository = new AccountDao();
        TransactionRepository transactionRepository = new TransactionDao();
        UserRepository userRepository = new UserDao();

        transactionService = new TransactionService(transactionRepository);
        accountService = new AccountService(accountRepository, transactionService);
        TransactionManager transactionManager = new DatabaseTransactionManager();
        transferService = new TransferService(accountRepository, transactionService, transactionManager);
        authService = new AuthService(userRepository);
    }

    @Test
    void userAuthenticationAllowsValidLoginFromDatabase() {
        User user = authService.login("jgrinham0", 75551);

        assertEquals("1", user.getUserId());
        assertEquals("jgrinham0", user.getUsername());
    }

    @Test
    void userAuthenticationRejectsWrongPasswordOrLogin() {
        assertThrows(RuntimeException.class, () -> authService.login("jgrinham0", 11111));
        assertThrows(RuntimeException.class, () -> authService.login("wrong@example.com", 75551));
    }

    @Test
    void checkBalanceReturnsDatabaseBalanceForUserId() {
        double balance = accountService.checkBalance("1");

        assertEquals(5000.00, balance, 0.001);
    }

    @Test
    void cashInUpdatesBalance() {
        accountService.cashIn("1", 250.00);

        assertEquals(5250.00, accountService.checkBalance("1"), 0.001);
    }

    @Test
    void cashTransferUpdatesBalancesForBothUsers() {
        transferService.cashTransfer("1", "2", 700.00);

        assertEquals(4300.00, accountService.checkBalance("1"), 0.001);
        assertEquals(4200.00, accountService.checkBalance("2"), 0.001);
    }

    @Test
    void transactionsIncludeDatabaseRowsForAccount() {
        List<Transaction> transactions = transactionService.getTransactions("A0001");

        assertEquals(1, transactions.size());
        assertEquals("T0001", transactions.getFirst().getTransactionId());
        assertEquals("Cash In", transactions.getFirst().getType());
        assertEquals(5000.00, transactions.getFirst().getAmount(), 0.001);
    }
}
