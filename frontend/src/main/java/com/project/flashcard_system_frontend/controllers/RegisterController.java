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

    @FXML
    public void initialize() {
        eyeIcon = new FontIcon("fas-eye-slash");
        eyeIcon.setIconSize(20);
        togglePasswordButton.setGraphic(eyeIcon);
        eyeIcon.setIconColor(Paint.valueOf("#f1f5f9"));

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
            feedbackLabel.setText("Preencha todos os campos!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            feedbackLabel.setText("As senhas não coincidem!");
            return;
        }

        boolean success = BackendClient.registerUser(username, password);
        if(success) {
            feedbackLabel.setTextFill(Paint.valueOf("#91ff7d"));
            feedbackLabel.setText("Cadastro realizado com sucesso!");
            goToLogin();
        } else {
            feedbackLabel.setTextFill(Paint.valueOf("#ffee8c"));
            feedbackLabel.setText("Erro ao cadastrar. Usuário já existe.");
        }

        //#ff7c7c vermelho
        //#ffee8c amarelo
        //#91ff7d verde

    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/login-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) createButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
