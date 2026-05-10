package com.bank.cli;

import com.bank.model.Transaction;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleUI {

    private static final String LINE = "========================================";
    private static final String SUB_LINE = "----------------------------------------";

    public static void printHeader(String title) {
        System.out.println(LINE);
        System.out.println(center(title.toUpperCase()));
        System.out.println(LINE);
    }

    public static void printLine(String message) {
        System.out.println(message);
    }

    public static void printMainMenu(String username) {
        printHeader("ONLINE BANKING");
        System.out.println("Logged in as: " + username);
        printSeparator();
        System.out.println("[1] Check Balance");
        System.out.println("[2] Cash In");
        System.out.println("[3] Cash Transfer");
        System.out.println("[4] Transaction History");
        System.out.println("[5] Logout");
        System.out.println("[0] Exit");
        printSeparator();
    }

    public static String formatMoney(double amount) {
        return String.format("PHP %,.2f", amount);
    }

    public static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printFooter() {
        System.out.println(LINE);
    }

    public static void printSeparator() {
        System.out.println(SUB_LINE);
    }

    public static void pause(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        try {
            scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\nExiting application...");
            System.exit(0);
        }
    }

    public static void printTransactions(List<Transaction> transactions) {

        printSeparator();
        System.out.printf("%-6s %-12s %-10s %-20s%n",
                "ID", "TYPE", "AMOUNT", "DATE");
        printSeparator();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            printSeparator();
            return;
        }

        for (Transaction t : transactions) {
            System.out.printf("%-6s %-12s %-15s %-20s%n",
                    t.getTransactionId(),
                    t.getType(),
                    formatMoney(t.getAmount()),
                    t.getDatetime()
            );
        }

        printSeparator();
    }

    private static String center(String text) {
        int width = LINE.length();
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
}
