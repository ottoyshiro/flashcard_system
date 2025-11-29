package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.AnimationFeedback;
import com.project.flashcard_system_frontend.service.BackendClient;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label registerButton;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private TextField passwordVisibleField;

    private boolean showingPassword = false;
    private FontIcon eyeIcon;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        eyeIcon = new FontIcon("fas-eye-slash");
        eyeIcon.setIconSize(20);
        togglePasswordButton.setGraphic(eyeIcon);
        eyeIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        togglePasswordButton.getStyleClass().add("button");
        eyeIcon.getStyleClass().add("button-icon");

        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    protected void togglePassword() {
        showingPassword = !showingPassword;

        eyeIcon.setIconLiteral(showingPassword ? "fas-eye" : "fas-eye-slash");

        passwordField.setVisible(!showingPassword);
        passwordField.setManaged(!showingPassword);

        passwordVisibleField.setVisible(showingPassword);
        passwordVisibleField.setManaged(showingPassword);
    }

    @FXML
    protected void handleLoginSubmit() throws Exception {

        String password = passwordField.getText();
        String username = usernameField.getText();

        if (password.isEmpty() || username.isEmpty()) {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "Fill all the fields!", "red-feedback", 3);
            return;
        }

        boolean success = BackendClient.loginUser(username, password);
        if(success) {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "Login successful!", "green-feedback", 0.5);
            goToHomePage();
        } else {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "User and/or password invalid(s)!", "yellow-feedback", 3);
        }

    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/register-page.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();

            stage.sizeToScene();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHomePage() {

        PauseTransition wait = new PauseTransition(Duration.seconds(1));

        wait.setOnFinished(e -> {
             try {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/flashcard-page.fxml"));
                 Parent root = loader.load();

                 FlashcardListController controller = loader.getController();
                 controller.setStage(stage);
                 Scene scene = new Scene(root);

                 stage.setScene(scene);
                 stage.centerOnScreen();
                 stage.sizeToScene();
                 stage.show();
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
        });

        wait.play();
    }
}

