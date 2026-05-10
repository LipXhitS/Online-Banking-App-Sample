package com.bank.cli;

import com.bank.config.DatabaseTransactionManager;
import com.bank.config.DatabaseInitializer;
import com.bank.config.TransactionManager;
import com.bank.dao.AccountDao;
import com.bank.dao.TransactionDao;
import com.bank.dao.UserDao;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import com.bank.service.TransactionService;
import com.bank.service.TransferService;

import java.io.Console;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandLineUI {
        private User loggedInUser;
        private final AccountService accountService;
        private final AuthService authService;
        private final Scanner scanner;
        private final TransferService transferService;
        private final TransactionService transactionService;

        public CommandLineUI(
                AuthService authService,
                AccountService accountService,
                TransferService transferService,
                TransactionService transactionService,
                Scanner scanner
        ) {
                this.authService = authService;
                this.accountService = accountService;
                this.transferService = transferService;
                this.transactionService = transactionService;
                this.scanner = scanner;
        }

        public static void main(String[] args) {
                DatabaseInitializer.initialize();

                UserRepository userRepository = new UserDao();
                AuthService authService = new AuthService(userRepository);

                AccountRepository accountRepository = new AccountDao();
                TransactionRepository transactionRepository = new TransactionDao();
                TransactionService transactionService = new TransactionService(transactionRepository);
                AccountService accountService = new AccountService(accountRepository, transactionService);
                TransactionManager transactionManager = new DatabaseTransactionManager();
                TransferService transferService = new TransferService(
                        accountRepository,
                        transactionService,
                        transactionManager
                );

                CommandLineUI commandLineUI = new CommandLineUI(
                        authService,
                        accountService,
                        transferService,
                        transactionService,
                        new Scanner(System.in)
                );
                commandLineUI.run();
        }

        public void run() {
                while (true) {
                        while (loggedInUser == null) {
                                ConsoleUI.printHeader("ONLINE BANKING LOGIN");
                                String input = readLineOrExit("Enter username/user number: ");
                                String pin = readPinOrExit("Enter PIN: ");
                                try {
                                        loggedInUser = authService.login(input, Integer.parseInt(pin));
                                        ConsoleUI.printSuccess("Login successful");
                                } catch (NumberFormatException e) {
                                        ConsoleUI.printError("Invalid credentials");
                                } catch (RuntimeException e) {
                                        ConsoleUI.printError("Invalid credentials");
                                }
                        }

                        while(loggedInUser != null) {
                                ConsoleUI.printMainMenu(loggedInUser.getUsername());
                                String choice = readLineOrExit("Enter Choice: ");
                                switch (choice) {
                                        case "1" -> checkBalance();
                                        case "2" -> cashIn();
                                        case "3" -> cashTransfer();
                                        case "4" -> transactionHistory();
                                        case "5" -> {
                                                authService.logout();
                                                loggedInUser = null;
                                                ConsoleUI.printSuccess("Logged out successfully");
                                        }
                                        case "0" -> {
                                                ConsoleUI.printLine("Exiting application...");
                                                return;
                                        }
                                        default -> ConsoleUI.printError("Invalid choice");

                                }
                        }
                }
        }

        private void checkBalance() {
                ConsoleUI.printHeader("BALANCE");

                try {
                        double balance = accountService.checkBalance(
                                loggedInUser.getUserId()
                        );

                        ConsoleUI.printLine("Current Balance: " + ConsoleUI.formatMoney(balance));
                } catch (Exception e) {
                        ConsoleUI.printError("Unable to retrieve balance");
                }

                ConsoleUI.printFooter();
                ConsoleUI.pause(scanner);
        }

        private void cashIn() {
                ConsoleUI.printHeader("CASH IN");

                String amount = readLineOrExit("Enter amount: ");

                try {
                        accountService.cashIn(
                                loggedInUser.getUserId(),
                                Double.parseDouble(amount)
                        );

                        double balance = accountService.checkBalance(loggedInUser.getUserId());
                        ConsoleUI.printSuccess("Cash in completed");
                        ConsoleUI.printLine("New Balance: " + ConsoleUI.formatMoney(balance));
                } catch (Exception e) {
                        ConsoleUI.printError("Invalid amount or transaction failed");
                }

                ConsoleUI.pause(scanner);
        }

        private void cashTransfer() {
                ConsoleUI.printHeader("CASH TRANSFER");
                String userId = readLineOrExit("Enter account to Cash Transfer: ");
                String amount = readLineOrExit("Enter amount to Cash Transfer: ");
                try {
                        transferService.cashTransfer(loggedInUser.getUserId(), userId, Double.parseDouble(amount));
                        double balance = accountService.checkBalance(loggedInUser.getUserId());
                        ConsoleUI.printSuccess("Cash transferred successfully");
                        ConsoleUI.printLine("New Balance: " + ConsoleUI.formatMoney(balance));
                } catch (NumberFormatException e) {
                        ConsoleUI.printError("Invalid amount");
                } catch (RuntimeException e) {
                        ConsoleUI.printError("Transfer failed: " + e.getMessage());
                }
                ConsoleUI.pause(scanner);
        }

        private void transactionHistory() {
                ConsoleUI.printHeader("TRANSACTION HISTORY");

                Account account = accountService.findByUserId(loggedInUser.getUserId());

                List<Transaction> transactions = transactionService.getTransactions(account.getAccountId());

                ConsoleUI.printTransactions(transactions);

                ConsoleUI.pause(scanner);
        }

        private String readLineOrExit(String prompt) {
                Console console = System.console();

                if (console != null) {
                        return console.readLine(prompt);
                }

                // fallback for IDE
                System.out.print(prompt);

                try {
                        return scanner.nextLine();
                } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("\nExiting application...");
                        System.exit(0);
                        return "";
                }
        }

        private String readPinOrExit(String prompt) {
                Console console = System.console();

                if (console != null) {
                        char[] pinArray = console.readPassword(prompt);
                        return new String(pinArray);
                }

                // fallback for IDE (PIN will be visible here)
                System.out.print(prompt);

                try {
                        return scanner.nextLine().trim();
                } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("\nExiting application...");
                        System.exit(0);
                        return "";
                }
        }
}
