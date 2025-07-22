package de.wg.ui;

import de.wg.model.Member;
import de.wg.model.User;
import de.wg.model.Transaction;
import de.wg.service.Ledger;
import de.wg.service.LoginManager;
import de.wg.model.RuleSet;
import de.wg.service.MemberManager;
import de.wg.exception.UngueltigerBetragException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Der {@code MainController} verwaltet die Benutzeroberfläche der
 * JavaFX-Anwendung. Er verbindet FXML-Elemente mit Anwendungslogik zur
 * Mitgliederverwaltung, Transaktionserfassung, Login-Verarbeitung,
 * Saldenanzeige und Datenspeicherung.
 */
public class MainController {
	/** Textfeld für Benutzername im Login-Tab. */
	@FXML
	private TextField usernameField;
	/** Passwortfeld im Login-Tab. */
	@FXML
	private PasswordField passwordField;
	/** Button zum Login. */
	@FXML
	private Button loginButton;
	/** Label zur Anzeige von Login-Ergebnissen. */
	@FXML
	private Label loginMessageLabel;
	@FXML
	private TextField memberNameField;
	@FXML
	private Button addMemberButton;
	@FXML
	private Label memberMessageLabel;
	@FXML
	private ListView<Member> memberListView;
	@FXML
	private Button refreshMembersButton;
	@FXML
	private TextField transactionDescriptionField;
	@FXML
	private TextField transactionAmountField;
	@FXML
	private DatePicker transactionDateField;
	@FXML
	private ComboBox<Member> transactionPayerComboBox;
	@FXML
	private ListView<Member> beneficiariesListView;
	@FXML
	private Button recordTransactionButton;
	@FXML
	private Label transactionMessageLabel;
	@FXML
	private TableView<BalanceEntry> balanceTableView;
	@FXML
	private TableColumn<BalanceEntry, String> memberColumn;
	@FXML
	private TableColumn<BalanceEntry, Double> balanceColumn;
	@FXML
	private Button refreshBalancesButton;
	@FXML
	private Button saveButton;
	@FXML
	private TableView<Transaction> transactionHistoryTableView;
	@FXML
	private TableColumn<Transaction, LocalDate> transactionHistoryDateColumn;
	@FXML
	private TableColumn<Transaction, String> transactionHistoryDescriptionColumn;
	@FXML
	private TableColumn<Transaction, Member> transactionHistoryPayerColumn;
	@FXML
	private TableColumn<Transaction, Double> transactionHistoryAmountColumn;
	@FXML
	private TableColumn<Transaction, String> transactionHistoryBeneficiariesColumn;
	@FXML
	private Button refreshTransactionHistoryButton;
	private Ledger ledger;
	private MemberManager memberManager;
	private LoginManager loginManager;
	private ObservableList<Member> allMembers;
	private ObservableList<BalanceEntry> balanceData;
	private ObservableList<Transaction> transactionHistoryData;
	private User currentUser;

	/**
	 * Wird automatisch beim Laden des Controllers aufgerufen. Initialisiert die
	 * Oberflächenelemente und bereitet Tabellen und Felder vor.
	 */
	@FXML
	public void initialize() {
		memberMessageLabel.setText("");
		transactionDateField.setValue(LocalDate.now());
		beneficiariesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		transactionMessageLabel.setText("");

		// Saldenübersicht Tab
		memberColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
		balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
		balanceData = FXCollections.observableArrayList();
		balanceTableView.setItems(balanceData);

		// Login Tab
		loginMessageLabel.setText("Bitte melden Sie sich an.");
		loginMessageLabel.setTextFill(Color.BLACK);

		// Initialisierung für den Transaktionshistorie Tab
		transactionHistoryDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		transactionHistoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		transactionHistoryPayerColumn.setCellValueFactory(new PropertyValueFactory<>("payer"));
		transactionHistoryAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		transactionHistoryBeneficiariesColumn.setCellValueFactory(cellData -> {
			String beneficiariesString = cellData.getValue().getBeneficiaries().stream().map(Member::getName)
					.collect(Collectors.joining(", "));
			return new SimpleStringProperty(beneficiariesString);
		});
		transactionHistoryData = FXCollections.observableArrayList();
		transactionHistoryTableView.setItems(transactionHistoryData);
	}

	/**
	 * Setzt die Datenmanager für Ledger und Mitglieder. Wird von der MainApp beim
	 * Start aufgerufen.
	 *
	 * @param ledger        das Transaktionsbuch
	 * @param memberManager der Mitgliederverwalter
	 */
	public void setManagers(Ledger ledger, MemberManager memberManager) {
		this.ledger = ledger;
		this.memberManager = memberManager;
		this.loginManager = new LoginManager(new RuleSet());

		if (allMembers == null || allMembers.isEmpty()) {
			allMembers = FXCollections.observableArrayList(memberManager.getAllMembers());
		} else {
			allMembers.setAll(memberManager.getAllMembers());
		}
		memberListView.setItems(allMembers);
		transactionPayerComboBox.setItems(allMembers);
		beneficiariesListView.setItems(allMembers);
		refreshBalances(null);
		refreshTransactionHistory(null);
		System.out.println("MainController: Manager erfolgreich gesetzt und UI aktualisiert.");
	}

	/**
	 * Führt den Login-Prozess durch und zeigt Erfolg oder Fehler an.
	 */
	@FXML
	private void handleLoginButton(ActionEvent event) {
		String username = usernameField.getText();
		String password = passwordField.getText();

		Member userAsMember = memberManager.getMemberByName(username);
		User user = null;
		if (userAsMember == null) {
			user = new User(username, username, "pass");
		} else {
			user = new User(userAsMember.getName(), userAsMember.getName(), "pass");
		}

		if (user != null) {
			String loginResult = loginManager.login(user, password);
			loginMessageLabel.setText(loginResult);
			if (loginResult.contains("erfolgreich")) {
				loginMessageLabel.setTextFill(Color.GREEN);
				this.currentUser = user;
				System.out.println("Benutzer '" + currentUser.getName() + "' erfolgreich angemeldet.");
			} else {
				loginMessageLabel.setTextFill(Color.RED);
			}
		} else {
			loginMessageLabel.setText("Login fehlgeschlagen: Benutzer nicht gefunden.");
			loginMessageLabel.setTextFill(Color.RED);
		}
	}

	/**
	 * Fügt ein neues Mitglied hinzu, wenn Name gültig ist und noch nicht existiert.
	 */
	@FXML
	private void handleAddMemberButton(ActionEvent event) {
		String memberName = memberNameField.getText().trim();
		if (memberName.isEmpty()) {
			memberMessageLabel.setText("Name des Mitglieds darf nicht leer sein.");
			memberMessageLabel.setTextFill(Color.RED);
			return;
		}

		if (memberManager.getMemberByName(memberName) != null) {
			memberMessageLabel.setText("Mitglied mit diesem Namen existiert bereits!");
			memberMessageLabel.setTextFill(Color.RED);
			return;
		}

		Member newMember = new Member(memberName);
		memberManager.addMember(newMember);
		allMembers.add(newMember);
		memberMessageLabel.setText("Mitglied '" + memberName + "' erfolgreich hinzugef�gt!");
		memberMessageLabel.setTextFill(Color.GREEN);
		memberNameField.clear();

		transactionPayerComboBox.setItems(allMembers);
		beneficiariesListView.setItems(allMembers);
		refreshBalances(null);
	}

	@FXML
	private void handleRefreshMembersButton(ActionEvent event) {
		memberManager.sortMembersByName();
		allMembers.setAll(memberManager.getAllMembers());
		memberMessageLabel.setText("Mitgliederliste aktualisiert und sortiert.");
		memberMessageLabel.setTextFill(Color.BLACK);
	}

	/**
	 * Sortiert und aktualisiert die Mitgliederliste.
	 */
	@FXML
	private void handleRecordTransactionButton(ActionEvent event) {
		String description = transactionDescriptionField.getText().trim();
		String amountText = transactionAmountField.getText().trim();
		LocalDate date = transactionDateField.getValue();
		Member payer = transactionPayerComboBox.getValue();
		List<Member> beneficiaries = beneficiariesListView.getSelectionModel().getSelectedItems();

		if (description.isEmpty() || amountText.isEmpty() || date == null || payer == null || beneficiaries.isEmpty()) {
			transactionMessageLabel.setText("Bitte alle Felder ausf�llen und mindestens einen Beg�nstigten ausw�hlen!");
			transactionMessageLabel.setTextFill(Color.RED);
			return;
		}

		try {
			double amount = Double.parseDouble(amountText);
			Transaction newTransaction = new Transaction(date, amount, payer, new ArrayList<>(beneficiaries),
					description);
			ledger.addTransaction(newTransaction);
			transactionMessageLabel.setText("Transaktion erfolgreich erfasst!");
			transactionMessageLabel.setTextFill(Color.GREEN);

			transactionDescriptionField.clear();
			transactionAmountField.clear();
			transactionDateField.setValue(LocalDate.now());
			transactionPayerComboBox.getSelectionModel().clearSelection();
			beneficiariesListView.getSelectionModel().clearSelection();

			refreshBalances(null);
			refreshTransactionHistory(null);
		} catch (NumberFormatException e) {
			transactionMessageLabel.setText("Betrag muss eine g�ltige Zahl sein!");
			transactionMessageLabel.setTextFill(Color.RED);
		} catch (UngueltigerBetragException e) {
			transactionMessageLabel.setText(e.getMessage());
			transactionMessageLabel.setTextFill(Color.RED);
		} catch (IllegalArgumentException e) {
			transactionMessageLabel.setText(e.getMessage());
			transactionMessageLabel.setTextFill(Color.RED);
		}
	}

	/**
	 * Aktualisiert die Saldenanzeige basierend auf aktuellen Transaktionen.
	 */
	@FXML
	private void refreshBalances(ActionEvent event) {
		balanceData.clear();

		if (memberManager == null || ledger == null || memberManager.getAllMembers().isEmpty()) {
			System.out.println("Manager oder Mitglieder nicht verf�gbar, kann Salden nicht berechnen.");
			return;
		}

		for (Member member : memberManager.getAllMembers()) {
			double balance = ledger.getBalance(member);
			balanceData.add(new BalanceEntry(member.getName(), balance));
		}

		balanceData.sort((e1, e2) -> Double.compare(e1.getBalance(), e2.getBalance()));

		Optional<BalanceEntry> robertEntry = balanceData.stream()
				.filter(entry -> "Robert".equals(entry.getMemberName())).findFirst();
		if (robertEntry.isPresent()) {
			double robertBalance = robertEntry.get().getBalance();
			RuleSet ruleSet = new RuleSet();
			String hinweis = ruleSet.getHinweisWennSchuldenZuHoch("Robert", robertBalance);
			if (hinweis != null) {
				System.out.println(hinweis);
			}
			if (robertBalance < 0) {
				System.out.println(
						"Hinweis: Robert hat einen Saldo von " + String.format("%.2f", robertBalance) + " EUR.");
				System.out.println("Das bedeutet, er schuldet insgesamt "
						+ String.format("%.2f", Math.abs(robertBalance)) + " EUR.");
			} else {
				System.out.println(
						"Hinweis: Die direkten Schulden zwischen Maria und Robert sind aus den Gesamtsalden nicht direkt ersichtlich oder sie schulden sich nichts.");
			}
		}
	}

 	/**
	 * Lädt die Transaktionshistorie aus dem Ledger und zeigt sie tabellarisch an.
	 */
	@FXML
	private void refreshTransactionHistory(ActionEvent event) {
		transactionHistoryData.clear();
		if (ledger != null) {
			List<Transaction> transactions = new ArrayList<>(ledger.getAllTransactions());
			Collections.sort(transactions);
			transactionHistoryData.addAll(transactions);
		} else {
			System.out.println("Ledger ist nicht initialisiert, kann Transaktionshistorie nicht laden.");
		}
	}

	/**
	 * Speichert Mitglieder- und Ledger-Daten manuell.
	 */
	@FXML
	private void handleSaveButton(ActionEvent event) {
		try {
			if (memberManager != null) {
				memberManager.saveToFile("members.ser");
			}
			if (ledger != null) {
				ledger.saveToFile("ledger.ser");
			}
			System.out.println("Daten manuell gespeichert.");
			loginMessageLabel.setText("Daten erfolgreich gespeichert!");
			loginMessageLabel.setTextFill(Color.GREEN);
		} catch (IOException e) {
			System.err.println("Fehler beim manuellen Speichern: " + e.getMessage());
			e.printStackTrace();
			loginMessageLabel.setText("Fehler beim Speichern der Daten!");
			loginMessageLabel.setTextFill(Color.RED);
		}
	}

	/**
	 * Datenmodell für die Saldenanzeige in einer TableView.
	 */
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