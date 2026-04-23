
import java.io.Console;


public class CommandLineUI {    public static void main(String[] args) {
        Console console = System.console();
        System.out.println("Welcome to Bank");

        System.out.print("Username/number: ");
        String userNameNumber = console.readLine();

        System.out.print("PIN: ");
        String pin = new String(console.readPassword());

        System.out.println("Choose Transaction:");
        System.out.println("[1] Check Balance");
        System.out.println("[2] Cash In");
        System.out.println("[3] Cash Transfer");
        System.out.println("[4] Transactions");
    }
}