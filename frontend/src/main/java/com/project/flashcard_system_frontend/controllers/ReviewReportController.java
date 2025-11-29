package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.model.ReviewStartResponse;
import com.project.flashcard_system_frontend.service.ApiService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class ReviewReportController {

    @FXML
    private Text total;

    @FXML
    private Text totalCorrect;

    @FXML
    private Text report;

    @FXML
    private Button arrowBackButton;

    private FontIcon arrowIcon;

    private FlashcardListController flashcardListController;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // --- BOTÃO BACK ---
        arrowIcon = new FontIcon("fas-arrow-left");
        arrowIcon.setIconSize(20);
        arrowBackButton.setGraphic(arrowIcon);
        arrowBackButton.getStyleClass().add("button");
        arrowIcon.getStyleClass().add("button-icon");

    }

    public void flashcardListController(FlashcardListController controller) {
        this.flashcardListController = controller;
    }

    public void showReport(ReviewStartResponse report) {
        total.setText("" + report.getTotalAnswers());
        totalCorrect.setText("" + report.getCorrectAnswers());
        float percentage = report.getCorrectAnswers()/(float)report.getTotalAnswers()*100;
        this.report.setText(String.format("%.2f%%", percentage));
    }

    @FXML
    public void tryAgain() {
        ReviewStartResponse data = ApiService.startReview();

        if (data != null) {
            flashcardListController.goToReviewSession(data);
        } else {
            System.out.println("Erro ao iniciar revisão.");
        }
    }

    @FXML
    private void goToFlashcardList() {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
