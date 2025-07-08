package de.wg.test;

import de.wg.exception.UngueltigerBetragException;
import de.wg.model.*;
import de.wg.service.Ledger;
import de.wg.service.LoginManager;
import de.wg.service.MemberManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator; // Import für Comparator falls benötigt

public class TestApp {

    public static void main(String[] args) {
        System.out.println("--- Start der Testausführung ---");

        //region Testblock 1: Person, Member, User - Grundfunktionalität und Comparable (nach Name)
        System.out.println("\n--- Testblock 1: Person, Member, User - Grundfunktionalität und Comparable ---");

        // Test 1.1: Person-Konstruktor und getName/setName
        Person person1 = new Person("Alice");
        System.out.println("Test 1.1: Person erstellt: " + person1.getName());
        person1.setName("Alicia");
        System.out.println("Test 1.1: Person Name geändert zu: " + person1.getName());

        // Test 1.2: Member-Konstruktor und getAccount/getName
        Member member1 = new Member("Bob");
        Member member2 = new Member("Charlie");
        System.out.println("Test 1.2: Mitglieder erstellt: " + member1.getName() + ", " + member2.getName());
        System.out.println("Test 1.2: Bob's Kontostand (initial): " + member1.getAccount().getBalance());

        // Test 1.3: User-Konstruktor und verifyPassword (Korrektur: Account muss übergeben werden)
        // Erstellen Sie einen Dummy-Account für den User, da der User-Konstruktor nun einen Account erwartet
        Account daveAccount = new Account(new Member("Dave_Dummy_Member")); // Dummy-Member für den Account-Besitzer
        User user1 = new User("Dave", "dave_user", "hashed_password_dave", daveAccount);
        System.out.println("Test 1.3: User erstellt: " + user1.getName() + " (" + user1.getUsername() + ")");
        System.out.println("Test 1.3: Dave Login korrekt (richtiges PW)? " + user1.verifyPassword("hashed_password_dave")); // Erwartet: true
        System.out.println("Test 1.3: Dave Login korrekt (falsches PW)? " + user1.verifyPassword("wrong_password"));      // Erwartet: false

        // Test 1.4: Comparable für Person (und geerbte Member/User)
        List<Person> personen = new ArrayList<>();
        personen.add(new Person("Zoe"));
        personen.add(new Member("Anna-Test1")); // Member erbt compareTo von Person
        // Korrektur: Auch für User einen Account erstellen
        personen.add(new User("Ben-Test1", "ben_user_test1", "pwd_test1", new Account(new Member("Ben_Dummy_Member")))); // User erbt compareTo von Person
        personen.add(new Person("Frank"));
        System.out.println("Test 1.4: Personenliste vor Sortierung: " + personen);
        Collections.sort(personen); // Vorausgesetzt, Person implementiert Comparable<Person>
        System.out.println("Test 1.4: Personenliste nach Sortierung: " + personen); // Erwartet: Anna-Test1, Ben-Test1, Frank, Zoe
        //endregion

        //region Testblock 2: Account - Grundfunktionalität und Comparable (Schulden zuerst)
        System.out.println("\n--- Testblock 2: Account - Grundfunktionalität und Comparable ---");

        Member mEmma = new Member("Emma");
        Member mPaul = new Member("Paul");
        Member mLena = new Member("Lena");
        Member mTom = new Member("Tom");

        // Initialisierung von Kontoständen für Sortierung
        mEmma.getAccount().updateBalance(50.0);    // Guthaben
        mPaul.getAccount().updateBalance(-100.0);  // Hohe Schulden
        mLena.getAccount().updateBalance(-20.0);   // Geringere Schulden
        mTom.getAccount().updateBalance(0.0);      // Ausgeglichen

        List<Account> accounts = new ArrayList<>();
        accounts.add(mEmma.getAccount());
        accounts.add(mPaul.getAccount());
        accounts.add(mLena.getAccount());
        accounts.add(mTom.getAccount());

        System.out.println("Test 2.1: Kontenliste vor Sortierung (Saldo):");
        for (Account acc : accounts) {
            System.out.println("  " + acc.getOwner().getName() + ": " + acc.getBalance() + " EUR");
        }

        Collections.sort(accounts); // Vorausgesetzt, Account implementiert Comparable<Account>

        System.out.println("Test 2.1: Kontenliste nach Sortierung (Schulden zuerst):");
        for (Account acc : accounts) {
            System.out.println("  " + acc.getOwner().getName() + ": " + acc.getBalance() + " EUR");
        }
        // Erwartet: Paul (-100), Lena (-20), Tom (0), Emma (50)
        //endregion

        //region Testblock 3: Transaction - Grundfunktionalität und Comparable (nach Datum)
        System.out.println("\n--- Testblock 3: Transaction - Grundfunktionalität und Comparable ---");

        Member tAlice = new Member("Alice-Tx");
        Member tBob = new Member("Bob-Tx");
        Member tCharlie = new Member("Charlie-Tx");

        Transaction tx1 = new Transaction(LocalDate.of(2024, 7, 10), 50.0, tAlice, Arrays.asList(tAlice, tBob), "Miete");
        Transaction tx2 = new Transaction(LocalDate.of(2024, 7, 8), 10.0, tBob, Arrays.asList(tBob, tCharlie), "Kaffee");
        Transaction tx3 = new Transaction(LocalDate.of(2024, 7, 9), 25.0, tCharlie, Arrays.asList(tAlice, tBob, tCharlie), "Strom");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(tx1);
        transactions.add(tx2);
        transactions.add(tx3);

        System.out.println("Test 3.1: Transaktionsliste vor Sortierung:");
        for (Transaction tx : transactions) {
            System.out.println("  " + tx.getDate() + ": " + tx.getDescription());
        }

        Collections.sort(transactions); // Vorausgesetzt, Transaction implementiert Comparable<Transaction>

        System.out.println("Test 3.1: Transaktionsliste nach Sortierung (nach Datum):");
        for (Transaction tx : transactions) {
            System.out.println("  " + tx.getDate() + ": " + tx.getDescription());
        }
        // Erwartet: Kaffee (08.07.), Strom (09.07.), Miete (10.07.)
        //endregion

        //region Testblock 4: Ausnahmebehandlung (IllegalArgumentException)
        System.out.println("\n--- Testblock 4: Ausnahmebehandlung (IllegalArgumentException) ---");

        // Test 4.1: Person mit null-Namen
        try {
            System.out.println("Test 4.1: Versuch, Person mit null-Namen zu erstellen...");
            new Person(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }

        // Test 4.2: Person mit leerem Namen
        try {
            System.out.println("Test 4.2: Versuch, Person mit leerem Namen zu erstellen...");
            new Person("");
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }

        // Test 4.3: setName mit null-Namen
        try {
            System.out.println("Test 4.3: Versuch, Person.setName() mit null aufzurufen...");
            Person tempPerson = new Person("Valid Name");
            tempPerson.setName(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }

        // Test 4.4: Transaction mit null-Datum
        try {
            System.out.println("Test 4.4: Versuch, Transaktion mit null-Datum zu erstellen...");
            new Transaction(null, 10.0, member1, Arrays.asList(member1), "Test");
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }

        // Test 4.5: Transaction mit null-Zahler
        try {
            System.out.println("Test 4.5: Versuch, Transaktion mit null-Zahler zu erstellen...");
            new Transaction(LocalDate.now(), 10.0, null, Arrays.asList(member1), "Test");
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }

        // Test 4.6: Transaction mit leerer Begünstigtenliste
        try {
            System.out.println("Test 4.6: Versuch, Transaktion mit leerer Begünstigtenliste zu erstellen...");
            new Transaction(LocalDate.now(), 10.0, member1, new ArrayList<>(), "Test");
        } catch (IllegalArgumentException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme gefangen: " + e.getMessage());
        }
        //endregion

        //region Testblock 5: Ledger - addTransaction und UngueltigerBetragException
        System.out.println("\n--- Testblock 5: Ledger - addTransaction und UngueltigerBetragException ---");
        Ledger ledger = new Ledger();
        Member lAlice = new Member("LedgerAlice");
        Member lBob = new Member("LedgerBob");

        // Test 5.1: addTransaction mit gültigem Betrag
        try {
            Transaction validTx = new Transaction(LocalDate.now(), 25.0, lAlice, Arrays.asList(lAlice, lBob), "Gültige Transaktion");
            ledger.addTransaction(validTx);
            System.out.println("Test 5.1: Gültige Transaktion hinzugefügt. Anzahl Transaktionen im Ledger: " + ledger.getAllTransactions().size());
        } catch (UngueltigerBetragException | IllegalArgumentException e) {
            System.out.println("Test 5.1: Fehler - Unerwartete Ausnahme: " + e.getMessage());
        }

        // Test 5.2: addTransaction mit Betrag 0
        try {
            System.out.println("Test 5.2: Versuch, Transaktion mit Betrag 0 hinzuzufügen...");
            Transaction zeroTx = new Transaction(LocalDate.now(), 0.0, lAlice, Arrays.asList(lAlice, lBob), "Null Betrag");
            ledger.addTransaction(zeroTx);
        } catch (UngueltigerBetragException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme 'UngueltigerBetragException' gefangen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Fehler! Unerwartete IllegalArgumentException: " + e.getMessage());
        }

        // Test 5.3: addTransaction mit negativem Betrag
        try {
            System.out.println("Test 5.3: Versuch, Transaktion mit negativem Betrag hinzuzufügen...");
            Transaction negativeTx = new Transaction(LocalDate.now(), -10.0, lAlice, Arrays.asList(lAlice, lBob), "Negativer Betrag");
            ledger.addTransaction(negativeTx);
        } catch (UngueltigerBetragException e) {
            System.out.println("  Erfolg! Erwartete Ausnahme 'UngueltigerBetragException' gefangen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Fehler! Unerwartete IllegalArgumentException: " + e.getMessage());
        }
        //endregion

        //region Testblock 6: RuleSet und LoginManager
        System.out.println("\n--- Testblock 6: RuleSet und LoginManager ---");

        RuleSet ruleSet = new RuleSet();
        LoginManager loginManager = new LoginManager(ruleSet);

        // Test 6.1: RuleSet Standardwert
        System.out.println("Test 6.1: Max Schulden in RuleSet (Standard): " + ruleSet.getMaxSchulden());

        // Test 6.2: Login erfolgreich ohne Schuldenhinweis
        // Korrektur: Account für normalUser übergeben
        User normalUser = new User("Normalo", "normalo_user", "normal_pwd", new Account(new Member("Normalo_Dummy_Member")));
        System.out.println("Test 6.2: Login 'Normalo': " + loginManager.login(normalUser, "normal_pwd"));

        // Test 6.3: Login fehlgeschlagen (falsches Passwort)
        System.out.println("Test 6.3: Login 'Normalo' (falsches PW): " + loginManager.login(normalUser, "wrong_pwd"));

        // Korrektur: Die folgenden Zeilen sind Duplikate oder fehl am Platz und werden entfernt oder refaktoriert.
        // `anna` ist bereits in Test 1.1/1.4 definiert. `userMax`, `tom`, `lisa` sind ebenfalls bereits verwendet/definiert.
        // Der folgende Block sollte `schuldenMember` und `schuldenUser` richtig verwenden, die bereits in Test 6.4 korrekt waren.
        
        // Test 6.4: Login mit Schuldenhinweis - Anpassung an AccountHolder-User
        // Diese Instanzen können direkt verwendet werden, da sie hier erstellt und genutzt werden.
        Member schuldenMember = new Member("Schuldner");
        schuldenMember.getAccount().updateBalance(-150.0); // Mehr als 100 Schulden
        User schuldenUser = new User(schuldenMember.getName(), schuldenMember.getName().toLowerCase() + "_user", "schuldner_pwd", schuldenMember.getAccount());

        System.out.println("Test 6.4: Login 'Schuldner' (als User mit Schulden): " + loginManager.login(schuldenUser, "schuldner_pwd"));
        // Erwartet: Login erfolgreich. Hinweis: Schuldner hat -150.0 EUR Schulden und überschreitet das Limit von 100.0 EUR.
        //endregion

        //region Testblock 7: MemberManager - Grundfunktionalität
        System.out.println("\n--- Test 7: MemberManager ---");

        MemberManager memberManager = new MemberManager();

        // Korrektur: Verwenden Sie neue, eindeutige Member-Objekte für den MemberManager-Test,
        // um Konflikte mit zuvor erstellten Membern mit gleichen Namen zu vermeiden.
        // Oder fangen Sie die IllegalArgumentException ab, wenn Sie absichtlich Duplikate hinzufügen wollen.
        // Ich erstelle hier neue Members mit eindeutigen Namen für diesen Testblock.
        Member m1 = new Member("MaxMustermann");
        Member m2 = new Member("LenaMeier");
        Member m3 = new Member("TimSchulze");
        Member m4 = new Member("AnnaMueller"); // Neue Anna für diesen Testblock

        try {
            memberManager.addMember(m1);
            memberManager.addMember(m2);
            memberManager.addMember(m3);
            memberManager.addMember(m4);
            System.out.println("Mitglieder erfolgreich dem MemberManager hinzugefügt.");
        } catch (IllegalArgumentException e) {
            System.err.println("Fehler beim Hinzufügen von Mitgliedern zum MemberManager: " + e.getMessage());
        }

        System.out.println("\nAlle Mitglieder im MemberManager:");
        List<Member> allMembers = memberManager.getAllMembers();
        for (Member m : allMembers) {
            System.out.println("- " + m.getName() + " (Saldo: " + m.getAccount().getBalance() + " EUR)");
        }
        System.out.println("Anzahl der Mitglieder: " + allMembers.size());
        //endregion

        System.out.println("\n--- Ende der Testausführung ---");
    }
}