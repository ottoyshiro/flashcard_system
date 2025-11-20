package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.service.BackendClient;
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

    @FXML
    public void initialize() {
        eyeIcon = new FontIcon("fas-eye-slash");
        eyeIcon.setIconSize(20);
        togglePasswordButton.setGraphic(eyeIcon);
        eyeIcon.setIconColor(Paint.valueOf("#f1f5f9"));

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

        feedbackLabel.setTextFill(Paint.valueOf("#ff7c7c"));
        if (password.isEmpty() || username.isEmpty()) {
            feedbackLabel.setText("Preencha todos os campos!");
            return;
        }

        boolean success = BackendClient.loginUser(username, password);
        if(success) {
            feedbackLabel.setTextFill(Paint.valueOf("#91ff7d"));
            feedbackLabel.setText("Login realizado com sucesso!");
            goToHomePage();
        } else {
            feedbackLabel.setTextFill(Paint.valueOf("#ffee8c"));
            feedbackLabel.setText("Erro ao logar, usuário e/ou senha inválido(s)!");
        }

        //#ff7c7c vermelho
        //#ffee8c amarelo
        //#91ff7d verde

    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/register-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/flashcard-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.sizeToScene();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

