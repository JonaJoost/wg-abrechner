package de.wg.ui;

import de.wg.model.*;
import de.wg.service.*;
import de.wg.exception.*;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;

    @FXML private TextField memberNameField;
    @FXML private PasswordField userPasswordField;
    @FXML private CheckBox adminCheckBox;
    @FXML private Button addMemberButton;
    
    @FXML
    private void handleRefreshMembersButton(ActionEvent event) {
        allMembers.setAll(memberManager.getAllMembers());
        transactionPayerComboBox.setItems(allMembers);
        beneficiariesListView.setItems(allMembers);
        memberListView.setItems(allMembers);
    }

    
    @FXML private Label memberMessageLabel;
    @FXML private ListView<Member> memberListView;
    @FXML private Button refreshMembersButton;

    @FXML private TextField transactionDescriptionField;
    @FXML private TextField transactionAmountField;
    @FXML private DatePicker transactionDateField;
    @FXML private ComboBox<Member> transactionPayerComboBox;
    @FXML private ListView<Member> beneficiariesListView;
    @FXML private Button recordTransactionButton;
    @FXML private Label transactionMessageLabel;

    @FXML private TableView<BalanceEntry> balanceTableView;
    @FXML private TableColumn<BalanceEntry, String> memberColumn;
    @FXML private TableColumn<BalanceEntry, Double> balanceColumn;
    @FXML private Button refreshBalancesButton;
    @FXML private Button saveButton;

    @FXML private TableView<Transaction> transactionHistoryTableView;
    @FXML private TableColumn<Transaction, LocalDate> transactionHistoryDateColumn;
    @FXML private TableColumn<Transaction, String> transactionHistoryDescriptionColumn;
    @FXML private TableColumn<Transaction, Member> transactionHistoryPayerColumn;
    @FXML private TableColumn<Transaction, Double> transactionHistoryAmountColumn;
    @FXML private TableColumn<Transaction, String> transactionHistoryBeneficiariesColumn;
    @FXML private Button refreshTransactionHistoryButton;

    private Ledger ledger;
    private MemberManager memberManager;
    private LoginManager loginManager;
    private UserManager userManager;
    private ObservableList<Member> allMembers;
    private ObservableList<BalanceEntry> balanceData;
    private ObservableList<Transaction> transactionHistoryData;
    private User currentUser;
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        transactionDateField.setValue(LocalDate.now());
        beneficiariesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        memberColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        balanceData = FXCollections.observableArrayList();
        balanceTableView.setItems(balanceData);

        transactionHistoryDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionHistoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionHistoryPayerColumn.setCellValueFactory(new PropertyValueFactory<>("payer"));
        transactionHistoryAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        transactionHistoryBeneficiariesColumn.setCellValueFactory(cellData -> {
            String str = cellData.getValue().getBeneficiaries().stream().map(Member::getName).collect(Collectors.joining(", "));
            return new SimpleStringProperty(str);
        });
        transactionHistoryData = FXCollections.observableArrayList();
        transactionHistoryTableView.setItems(transactionHistoryData);
    }

    public void setManagers(Ledger ledger, MemberManager memberManager, UserManager userManager, TabPane mainTabPane) {
        this.ledger = ledger;
        this.memberManager = memberManager;
        this.userManager = userManager;
        this.loginManager = new LoginManager(new RuleSet());
        this.mainTabPane = mainTabPane;

        for (Tab tab : mainTabPane.getTabs()) {
            if (!tab.getText().equals("Login")) {
                tab.setDisable(true);
            }
        }

        allMembers = FXCollections.observableArrayList(memberManager.getAllMembers());
        memberListView.setItems(allMembers);
        transactionPayerComboBox.setItems(allMembers);
        beneficiariesListView.setItems(allMembers);

        refreshBalances(null);
        refreshTransactionHistory(null);
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userManager.getByUsername(username);

        if (user != null && user.verifyPassword(hash(password))) {
            loginMessageLabel.setText("Login erfolgreich.\n" + schuldenHinweis(user));
            loginMessageLabel.setTextFill(Color.GREEN);
            this.currentUser = user;
            for (Tab tab : mainTabPane.getTabs()) {
                tab.setDisable(false);
            }
        } else {
            loginMessageLabel.setText("Login fehlgeschlagen: Benutzername oder Passwort falsch.");
            loginMessageLabel.setTextFill(Color.RED);
        }
    }

    private String schuldenHinweis(User user) {
    	if (!(user instanceof AccountHolder)) return "";
    	AccountHolder ah = (AccountHolder) user;
        double saldo = ah.getAccount().getBalance();
        StringBuilder hinweis = new StringBuilder();
        if (saldo < -200) {
            hinweis.append("Warnung: Hohes Minus von " + saldo + " EUR.\n");
        } else {
            hinweis.append("Aktueller Saldo: " + saldo + " EUR\n");
        }
        return hinweis.toString();
    }

    @FXML
    private void handleAddMemberButton(ActionEvent event) {
        String memberName = memberNameField.getText().trim();
        String pass = userPasswordField.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        if (memberManager.getMemberByName(memberName) != null) return;

        Member member = new Member(memberName);
        memberManager.addMember(member);
        User user = new User(memberName, memberName, hash(pass), isAdmin);
        userManager.addUser(user);

        allMembers.add(member);
        transactionPayerComboBox.setItems(allMembers);
        beneficiariesListView.setItems(allMembers);
        refreshBalances(null);
    }

    @FXML
    private void handleRecordTransactionButton(ActionEvent event) {
        if (!currentUser.isAdmin() && transactionDateField.getValue().isBefore(LocalDate.now())) {
            transactionMessageLabel.setText("Nur Admins dürfen rückdatierte Buchungen machen.");
            transactionMessageLabel.setTextFill(Color.RED);
            return;
        }
        String description = transactionDescriptionField.getText().trim();
        String amountText = transactionAmountField.getText().trim();
        LocalDate date = transactionDateField.getValue();
        Member payer = transactionPayerComboBox.getValue();
        List<Member> beneficiaries = beneficiariesListView.getSelectionModel().getSelectedItems();

        if (description.isEmpty() || amountText.isEmpty() || date == null || payer == null || beneficiaries.isEmpty()) {
            transactionMessageLabel.setText("Bitte alle Felder ausfüllen!");
            transactionMessageLabel.setTextFill(Color.RED);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            Transaction transaction = new Transaction(date, amount, payer, new ArrayList<>(beneficiaries), description);
            ledger.addTransaction(transaction);
            transactionMessageLabel.setText("Transaktion erfasst.");
            transactionMessageLabel.setTextFill(Color.GREEN);
            refreshBalances(null);
            refreshTransactionHistory(null);
        } catch (Exception e) {
            transactionMessageLabel.setText("Fehler: " + e.getMessage());
            transactionMessageLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void refreshBalances(ActionEvent event) {
        balanceData.clear();
        for (Member member : memberManager.getAllMembers()) {
            double balance = ledger.getBalance(member);
            balanceData.add(new BalanceEntry(member.getName(), balance));
        }
        balanceData.sort(Comparator.comparingDouble(BalanceEntry::getBalance));
    }

    @FXML
    private void refreshTransactionHistory(ActionEvent event) {
        transactionHistoryData.clear();
        transactionHistoryData.addAll(ledger.getTransactionsSortedByDate());
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        try {
            memberManager.saveToFile("members.ser");
            ledger.saveToFile("ledger.ser");
            userManager.saveToFile("users.ser");
            loginMessageLabel.setText("Daten gespeichert.");
            loginMessageLabel.setTextFill(Color.GREEN);
        } catch (Exception e) {
            loginMessageLabel.setText("Fehler beim Speichern: " + e.getMessage());
            loginMessageLabel.setTextFill(Color.RED);
        }
    }

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

        public double getBalance() {
            return balance.get();
        }
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}
