package de.wg.ui;

import de.wg.service.*;
import de.wg.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;

public class MainApp extends Application {
    private Ledger ledger;
    private MemberManager memberManager;
    private UserManager userManager;

    @Override
    public void init() throws Exception {
        super.init();
        try {
            memberManager = MemberManager.loadFromFile("members.ser");
            ledger = Ledger.loadFromFile("ledger.ser");
            userManager = UserManager.loadFromFile("users.ser");
        } catch (IOException | ClassNotFoundException e) {
            memberManager = new MemberManager();
            ledger = new Ledger();
            userManager = new UserManager();
        }
        User admin = new User("Administrator", "admin", hash("admin"), true);
        userManager.addIfNotExists(admin);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            TabPane tabPane = (TabPane) root.lookup(".tab-pane");
            controller.setManagers(ledger, memberManager, userManager, tabPane);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("WG-Verwaltung (JavaFX)");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        memberManager.saveToFile("members.ser");
        ledger.saveToFile("ledger.ser");
        userManager.saveToFile("users.ser");
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
