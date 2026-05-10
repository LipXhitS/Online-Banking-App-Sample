package com.bank.cli;

import com.bank.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleUITest {

    private final PrintStream originalOut = System.out;

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void printTransactionsDisplaysTransactionDetails() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ConsoleUI.printTransactions(List.of(
                new Transaction("T0001", "A0001", "Cash In", 5000.00, "2026-05-10T14:30:00")
        ));

        String text = output.toString();

        assertTrue(text.contains("ID"));
        assertTrue(text.contains("TYPE"));
        assertTrue(text.contains("AMOUNT"));
        assertTrue(text.contains("T0001"));
        assertTrue(text.contains("Cash In"));
        assertTrue(text.contains("PHP 5,000.00"));
    }
}
