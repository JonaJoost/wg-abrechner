

import de.wg.model.*;

import java.time.LocalDate;
import java.util.Arrays;

public class TestApp {

    public static void main(String[] args) {
        // Test 1: Member + Account
        Member anna = new Member("Anna");
        anna.getAccount().updateBalance(-20.0);
        System.out.println("Anna Kontostand: " + anna.getAccount().getBalance());

        // Test 2: User-Login
        User user = new User("Max", "max123", "abc123"); // kein echter Hash hier
        boolean korrekt = user.verifyPassword("abc123");
        System.out.println("Login korrekt? " + korrekt); // true

        // Test 3: Transaction
        Member tom = new Member("Tom");
        Member lisa = new Member("Lisa");

        Transaction einkauf = new Transaction(
                LocalDate.now(),
                30.0,
                tom,
                Arrays.asList(tom, lisa),
                "Wocheneinkauf"
        );

        System.out.println("Transaktion: " + einkauf.toString());
    }
}
