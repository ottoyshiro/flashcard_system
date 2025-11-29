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

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private TextField confirmPasswordVisibleField;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private Button createButton;

    @FXML
    private Label feedbackLabel;

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
        confirmPasswordVisibleField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
    }

    @FXML
    protected void togglePassword() {
        showingPassword = !showingPassword;

        eyeIcon.setIconLiteral(showingPassword ? "fas-eye" : "fas-eye-slash");

        passwordField.setVisible(!showingPassword);
        passwordField.setManaged(!showingPassword);
        confirmPasswordField.setVisible(!showingPassword);
        confirmPasswordField.setManaged(!showingPassword);

        passwordVisibleField.setVisible(showingPassword);
        passwordVisibleField.setManaged(showingPassword);
        confirmPasswordVisibleField.setVisible(showingPassword);
        confirmPasswordVisibleField.setManaged(showingPassword);
    }

    @FXML
    protected void handleSubmit() throws Exception {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String username = usernameField.getText();

        feedbackLabel.setTextFill(Paint.valueOf("#ff7c7c"));
        if (password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "Fill all the fields!", "red-feedback", 3);
            return;
        }

        if (!password.equals(confirmPassword)) {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "Passwords aren't the same!", "red-feedback", 3);
            return;
        }

        boolean success = BackendClient.registerUser(username, password);
        if(success) {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "Register successful!", "green-feedback", 0.5);
            goToLogin();
        } else {
            AnimationFeedback.showTimedFeedback(feedbackLabel, "User already exists!", "yellow-feedback", 3);
        }

    }

    @FXML
    private void goToLogin() {
        PauseTransition wait = new PauseTransition(Duration.seconds(1));

        wait.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/login-page.fxml"));
                Parent root = loader.load();

                LoginController controller = loader.getController();
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
