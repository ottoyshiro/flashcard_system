package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.AnimationFeedback;
import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.project.flashcard_system_frontend.model.ReviewStartResponse;
import com.project.flashcard_system_frontend.service.ApiService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReviewSessionController {
    @FXML
    private Button reportButton;

    @FXML
    private Text number;

    @FXML
    private Text content;

    @FXML
    private Text subject;

    @FXML
    private VBox answersBox;

    @FXML
    private ToggleGroup answersGroup;

    @FXML
    private Label feedback;

    private FlashcardDTO currentFlashcard;

    private List<FlashcardDTO> flashcards;

    private int flashcardIndex = 0;

    private String sessionId;

    private FlashcardListController flashcardListController;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {

        answersGroup = new ToggleGroup();

    }

    public void flashcardListController(FlashcardListController controller) {
        this.flashcardListController = controller;
    }

    public void startReview(ReviewStartResponse data) {
        this.flashcards = data.getFlashcards();
        this.flashcardIndex = 0;
        setFlashcard(flashcards.get(0));
        sessionId = data.getSessionId();
    }

    public void setFlashcard(FlashcardDTO dto) {
        this.currentFlashcard = dto;

        // Preencher campos básicos
        number.setText("Question " + (flashcardIndex+1));
        content.setText(dto.getContent());
        subject.setText(dto.getSubject());

        // Limpar qualquer resposta criada automaticamente no initialize
        answersBox.getChildren().clear();
        answersGroup = new ToggleGroup();

        // Preencher respostas
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            String answer = dto.getAnswers().get(i);

            HBox box = new HBox(10);

            RadioButton rb = new RadioButton();
            rb.setToggleGroup(answersGroup);
            rb.getStyleClass().add("material-radio");

            Text text = new Text(answer);
            text.setFont(new Font(18));

            box.getChildren().addAll(rb, text);
            answersBox.getChildren().add(box);
        }
    }

    @FXML
    public void nextFlashcard() {
        Integer selectedAnswer = null;

        // Descobrir qual resposta foi selecionada
        List<HBox> answerLines = answersBox.getChildren().stream()
                .filter(n -> n instanceof HBox)
                .map(n -> (HBox) n)
                .toList();

        for (int i = 0; i < answerLines.size(); i++) {
            HBox line = answerLines.get(i);
            RadioButton rb = (RadioButton) line.getChildren().get(0);

            if (rb.isSelected()) {
                selectedAnswer = i;
                break;
            }
        }

        if (selectedAnswer == null) {
            AnimationFeedback.showTimedFeedback(feedback, "Select your correct answer!", "yellow-feedback", 3);
            return;
        }

        boolean result = ApiService.answerFlashcard(
                sessionId,
                flashcards.get(flashcardIndex).getId(),
                selectedAnswer
        );

        if (!result) {
            System.out.println("Falha ao enviar resposta para o backend!");
            return;
        }

        // Ir para o próximo flashcard
        if (flashcardIndex + 1 < flashcards.size()) {
            flashcardIndex++;
            setFlashcard(flashcards.get(flashcardIndex));
        } else {
            goToReport(ApiService.finishReview(sessionId));
        }
    }

    @FXML
    private void goToReport(ReviewStartResponse report) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/review-report-page.fxml"));
            Parent root = loader.load();

            ReviewReportController controller = loader.getController();

            controller.flashcardListController(flashcardListController);
            controller.showReport(report);
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
