package de.wg.ui;

import de.wg.model.Member;
import de.wg.model.User;
import de.wg.model.Transaction;
import de.wg.service.Ledger;
import de.wg.service.LoginManager;
import de.wg.model.RuleSet; // Für LoginManager
import de.wg.exception.UngueltigerBetragException; // Für Transaction
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Für Dialoge

public class MainController {

    // ========================================================================
    // FXML Elemente für den LOGIN Tab
    // ========================================================================
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;

    // ========================================================================
    // FXML Elemente für den MITGLIEDERVERWALTUNG Tab
    // ========================================================================
    @FXML private TextField memberNameField;
    @FXML private Button addMemberButton;
    @FXML private Label memberMessageLabel;
    @FXML private ListView<Member> memberListView; // ListView für die Anzeige aller Mitglieder
    @FXML private Button refreshMembersButton; // Button, der in FXML hinzugefügt wurde


    // ========================================================================
    // FXML Elemente für den TRANSAKTION ERFASSEN Tab
    // ========================================================================
    @FXML private TextField transactionDescriptionField;
    @FXML private TextField transactionAmountField;
    @FXML private DatePicker transactionDateField;
    @FXML private ComboBox<Member> transactionPayerComboBox; // ComboBox für den Zahler
    @FXML private ListView<Member> beneficiariesListView; // ListView für die Begünstigten
    @FXML private Button recordTransactionButton;
    @FXML private Label transactionMessageLabel;


    // ========================================================================
    // FXML Elemente für den SALDENÜBERSICHT Tab
    // ========================================================================
    @FXML private TableView<BalanceEntry> balanceTableView;
    @FXML private TableColumn<BalanceEntry, String> memberColumn;
    @FXML private TableColumn<BalanceEntry, Double> balanceColumn;
    @FXML private Button refreshBalancesButton;

    // ========================================================================
    // Backend / Service Klassen Instanzen
    // ========================================================================
    private Ledger ledger;
    private LoginManager loginManager;
    private ObservableList<Member> allMembers; // Liste aller Mitglieder
    private ObservableList<BalanceEntry> balanceData; // Daten für die Salden-Tabelle
    private User currentUser; // Der aktuell angemeldete Benutzer

    // ========================================================================
    // Initialisierung des Controllers (wird automatisch von FXMLLoader aufgerufen)
    // ========================================================================
    @FXML
    public void initialize() {
        // Initialisierung der Service-Klassen
        // Stellen Sie sicher, dass diese Instanzen einmalig erstellt und verwaltet werden.
        // In einer größeren Anwendung würde dies oft durch ein Dependency Injection Framework geschehen.
        if (ledger == null) {
            ledger = new Ledger();
        }
        if (loginManager == null) {
            loginManager = new LoginManager(new RuleSet()); // RuleSet wird hier direkt instanziiert
        }
        if (allMembers == null) {
            allMembers = FXCollections.observableArrayList();
            // Beispiel-Mitglieder für den Start (später aus Persistenz laden)
            allMembers.add(new Member("Robert"));
            allMembers.add(new Member("Maria"));
            allMembers.add(new Member("Anna"));
            allMembers.add(new Member("Tom"));
        }

        // ====================================================================
        // Konfiguration des Mitgliederverwaltung Tabs
        // ====================================================================
        memberListView.setItems(allMembers);
        memberMessageLabel.setText(""); // Initial leeren

        // ====================================================================
        // Konfiguration des Transaktion Erfassen Tabs
        // ====================================================================
        transactionPayerComboBox.setItems(allMembers); // ComboBox mit allen Mitgliedern füllen
        transactionDateField.setValue(LocalDate.now()); // Standarddatum auf heute setzen
        // beneficiariesListView soll Mehrfachauswahl erlauben und mit allen Mitgliedern befüllt werden
        beneficiariesListView.setItems(allMembers);
        beneficiariesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        transactionMessageLabel.setText("");

        // ====================================================================
        // Konfiguration des Saldenübersicht Tabs
        // ====================================================================
        // Spalten mit den Eigenschaften der BalanceEntry-Klasse verbinden
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceData = FXCollections.observableArrayList();
        balanceTableView.setItems(balanceData);
        
        // Beim Start direkt Salden und Mitglieder aktualisieren
        refreshMembers(null); // Da keine ActionEvent benötigt wird, kann null übergeben werden
        refreshBalances(null);
    }

    // ========================================================================
    // Event Handler Methoden
    // ========================================================================

    // Login Handler
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText(); // In echten Anwendungen niemals Klartext-Passwort verwenden!

        // Hier würde normalerweise das Passwort gehasht und mit einem gespeicherten Hash verglichen
        // Für diesen Prototyp simulieren wir einfache Benutzer
        User testUserRobert = new User("Robert", "robert", "pass123");
        User testUserMaria = new User("Maria", "maria", "pass123");

        User userToLogin = null;
        if (username.equals(testUserRobert.getUsername())) {
            userToLogin = testUserRobert;
        } else if (username.equals(testUserMaria.getUsername())) {
            userToLogin = testUserMaria;
        }

        if (userToLogin != null) {
            // Wir verwenden hier das Klartext-Passwort als "Hash" für den Test
            String result = loginManager.login(userToLogin, password);
            loginMessageLabel.setText(result);
            if (result.startsWith("Login erfolgreich")) {
                currentUser = userToLogin;
                System.out.println("Benutzer " + currentUser.getName() + " erfolgreich angemeldet.");
                // Hier könnten Sie die UI anpassen, z.B. Login-Tab deaktivieren
            }
        } else {
            loginMessageLabel.setText("Login fehlgeschlagen: Benutzer nicht gefunden.");
        }
    }

    // Mitglieder hinzufügen Handler
    @FXML
    private void addMember(ActionEvent event) {
        String memberName = memberNameField.getText();

        if (memberName != null && !memberName.trim().isEmpty()) {
            if (allMembers.stream().anyMatch(m -> m.getName().equalsIgnoreCase(memberName.trim()))) {
                memberMessageLabel.setText("Fehler: Mitglied '" + memberName + "' existiert bereits.");
                memberMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
            } else {
                Member newMember = new Member(memberName.trim());
                allMembers.add(newMember); // Fügt das Mitglied zur ObservableList hinzu
                memberMessageLabel.setText("Mitglied '" + memberName + "' hinzugefügt.");
                memberMessageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                memberNameField.clear(); // Textfeld leeren
                // memberListView wird automatisch aktualisiert, da sie an allMembers gebunden ist
                // Auch die ComboBox für Zahler und die ListView für Begünstigte werden aktualisiert.
                transactionPayerComboBox.setItems(allMembers);
                beneficiariesListView.setItems(allMembers);
            }
        } else {
            memberMessageLabel.setText("Bitte geben Sie einen Namen für das Mitglied ein.");
            memberMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    // Mitgliederliste aktualisieren Handler
    @FXML
    private void refreshMembers(ActionEvent event) {
        // Da allMembers bereits eine ObservableList ist, wird die ListView automatisch aktualisiert,
        // sobald Mitglieder hinzugefügt oder entfernt werden.
        // Dieser Button könnte nützlich sein, wenn Mitglieder aus einer externen Quelle geladen werden.
        memberListView.refresh();
        System.out.println("Mitgliederliste aktualisiert.");
    }


    // Transaktion verbuchen Handler
    @FXML
    private void recordTransaction(ActionEvent event) {
        String description = transactionDescriptionField.getText();
        String amountText = transactionAmountField.getText();
        LocalDate date = transactionDateField.getValue();
        Member payer = transactionPayerComboBox.getSelectionModel().getSelectedItem();
        List<Member> beneficiaries = beneficiariesListView.getSelectionModel().getSelectedItems();

        // Einfache Validierung
        if (description.trim().isEmpty() || amountText.trim().isEmpty() || date == null || payer == null || beneficiaries.isEmpty()) {
            transactionMessageLabel.setText("Bitte alle Felder ausfüllen und mindestens einen Begünstigten auswählen.");
            transactionMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            transactionMessageLabel.setText("Ungültiger Betrag. Bitte eine positive Zahl eingeben.");
            transactionMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        try {
            // Erstellen der Transaktion
            Transaction transaction = new Transaction(date, amount, payer, new ArrayList<>(beneficiaries), description);
            ledger.addTransaction(transaction); // Transaktion zum Ledger hinzufügen

            // Kontostände der betroffenen Mitglieder anpassen
            payer.getAccount().updateBalance(amount); // Zahler bekommt Betrag gutgeschrieben

            double share = amount / beneficiaries.size();
            for (Member beneficiary : beneficiaries) {
                beneficiary.getAccount().updateBalance(-share); // Begünstigte bekommen Anteil abgezogen
            }

            transactionMessageLabel.setText("Transaktion erfolgreich verbucht: " + description + " (" + amount + " EUR)");
            transactionMessageLabel.setTextFill(javafx.scene.paint.Color.GREEN);

            // Felder leeren und Salden aktualisieren
            transactionDescriptionField.clear();
            transactionAmountField.clear();
            transactionDateField.setValue(LocalDate.now()); // Datum zurücksetzen
            transactionPayerComboBox.getSelectionModel().clearSelection();
            beneficiariesListView.getSelectionModel().clearSelection();
            refreshBalances(null); // Saldenübersicht aktualisieren
            System.out.println("Transaktion verbucht: " + transaction);
            
        } catch (UngueltigerBetragException e) {
            transactionMessageLabel.setText("Fehler beim Verbuchen: " + e.getMessage());
            transactionMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        } catch (IllegalArgumentException e) {
             transactionMessageLabel.setText("Fehler: " + e.getMessage());
            transactionMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }


    // Salden aktualisieren Handler
    @FXML
    private void refreshBalances(ActionEvent event) {
        balanceData.clear(); // Alte Daten löschen

        // Für jedes Mitglied den aktuellen Saldo abrufen und zur Tabelle hinzufügen
        for (Member member : allMembers) {
            double balance = ledger.getBalance(member);
            balanceData.add(new BalanceEntry(member.getName(), balance));
        }
        balanceTableView.refresh(); // Tabelle aktualisieren
        System.out.println("Salden aktualisiert.");
        
        // Spezifische Abfrage "Wie viel schuldet Maria Robert?"
        // Annahme: Robert hat ein positives Guthaben, Maria ein negatives Schuldenkonto
        // oder umgekehrt. Hier vereinfachen wir es auf direkte Saldo-Anzeige.
        // Für die spezifische Frage müsste man die Konten von Maria und Robert abrufen.
        // Beispiel:
        Optional<Member> robertOpt = allMembers.stream().filter(m -> m.getName().equals("Robert")).findFirst();
        Optional<Member> mariaOpt = allMembers.stream().filter(m -> m.getName().equals("Maria")).findFirst();

        if (robertOpt.isPresent() && mariaOpt.isPresent()) {
            Member robert = robertOpt.get();
            Member maria = mariaOpt.get();
            double robertBalance = ledger.getBalance(robert);
            double mariaBalance = ledger.getBalance(maria);

            // Um zu prüfen, wie viel Maria Robert schuldet, schauen wir auf Marias Saldo.
            // Wenn Marias Saldo negativ ist, schuldet sie Geld.
            // Die genaue Beziehung zwischen zwei Personen ist komplexer und würde
            // eine tiefere Analyse der Transaktionen erfordern, aber für "Maria schuldet Robert"
            // ist ein negativer Saldo bei Maria und ein positiver bei Robert ein Indikator.
            if (mariaBalance < 0 && robertBalance > 0) {
                System.out.println("Hinweis: Maria hat einen Saldo von " + String.format("%.2f", mariaBalance) + " EUR.");
                System.out.println("Das bedeutet, sie schuldet insgesamt " + String.format("%.2f", Math.abs(mariaBalance)) + " EUR.");
            } else if (mariaBalance > 0 && robertBalance < 0) {
                 System.out.println("Hinweis: Robert hat einen Saldo von " + String.format("%.2f", robertBalance) + " EUR.");
                System.out.println("Das bedeutet, er schuldet insgesamt " + String.format("%.2f", Math.abs(robertBalance)) + " EUR.");
            } else {
                 System.out.println("Hinweis: Die direkten Schulden zwischen Maria und Robert sind aus den Gesamtsalden nicht direkt ersichtlich oder sie schulden sich nichts.");
            }
        }
    }


    // ========================================================================
    // Hilfsklasse für die TableView (um Mitgliedsname und Saldo anzuzeigen)
    // ========================================================================
    public static class BalanceEntry {
        private final SimpleStringProperty memberName;
        private final SimpleDoubleProperty balance;

        public BalanceEntry(String memberName, double balance) {
            this.memberName = new SimpleStringProperty(memberName);
            this.balance = new SimpleDoubleProperty(balance);
        }

        public String getMemberName() {
            return memberName.get();
        }

        public SimpleStringProperty memberNameProperty() {
            return memberName;
        }

        public double getBalance() {
            return balance.get();
        }

        public SimpleDoubleProperty balanceProperty() {
            return balance;
        }
    }
}