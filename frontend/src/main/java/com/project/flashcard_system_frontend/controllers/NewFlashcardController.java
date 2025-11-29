package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.AnimationFeedback;
import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.project.flashcard_system_frontend.service.ApiService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class NewFlashcardController {

    @FXML
    private Label feedback;

    @FXML
    private TextField contentField;

    @FXML
    private TextField subjectField;

    @FXML
    private VBox answersBox;

    @FXML
    private ToggleGroup answersGroup;

    @FXML
    private Button arrowBackButton;

    private FontIcon arrowIcon;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // --- BOTÃO BACK ---
        arrowIcon = new FontIcon("fas-arrow-left");
        arrowIcon.setIconSize(20);
        arrowBackButton.setGraphic(arrowIcon);
        arrowBackButton.getStyleClass().add("button");
        arrowIcon.getStyleClass().add("button-icon");

        answersGroup = new ToggleGroup();
        addAnswerField();

    }

    @FXML
    private void addAnswerField() {
        HBox box = new HBox(10);

        // Radio button para marcar resposta correta
        RadioButton rb = new RadioButton();
        rb.setToggleGroup(answersGroup);
        rb.getStyleClass().add("material-radio");

        TextField answerField = new TextField();
        answerField.setPromptText("Type an answer...");

        Button removeBtn = new Button();
        removeBtn.getStyleClass().add("reset-button");
        FontIcon xIcon = new FontIcon("fas-times");
        xIcon.setIconSize(20);
        xIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        removeBtn.getStyleClass().add("button");
        xIcon.getStyleClass().add("button-icon");
        removeBtn.setGraphic(xIcon);
        removeBtn.setOnAction(e -> answersBox.getChildren().remove(box));


        box.getChildren().addAll(rb, answerField, removeBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        answersBox.getChildren().add(box);
    }

    @FXML
    private void handleSave() {
        String content = contentField.getText();
        String subject = subjectField.getText();

        Integer correctAnswer = null;

        List<String> answers = answersBox.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .map(hbox -> (TextField) hbox.getChildren().get(1))
                .map(TextField::getText)
                .filter(s -> !s.isBlank())
                .toList();

        List<HBox> answerLines = answersBox.getChildren().stream()
                .filter(n -> n instanceof HBox)
                .map(n -> (HBox) n)
                .toList();

        for (int i = 0; i < answerLines.size(); i++) {
            HBox line = answerLines.get(i);
            RadioButton rb = (RadioButton) line.getChildren().get(0);

            if (rb.isSelected()) {
                TextField answer = (TextField) line.getChildren().get(1);
                if (answer.getText().isEmpty()) {
                    AnimationFeedback.showTimedFeedback(feedback, "Fill your selected answer!", "red-feedback", 3);
                    return;
                }
                correctAnswer = i;
                break;
            }
        }

        if (correctAnswer == null) {
            AnimationFeedback.showTimedFeedback(feedback, "Select your correct answer!", "yellow-feedback", 3);
            return;
        }

        FlashcardDTO dto = new FlashcardDTO();
        dto.setContent(content);
        dto.setSubject(subject);
        dto.setAnswers(answers);
        dto.setCorrectAnswer(correctAnswer);

        boolean success = ApiService.newFlashcard(dto);

        if (success) {
            AnimationFeedback.showTimedFeedback(feedback, "Flashcard created successfully!", "green-feedback", 0.5);
            goToFlashcardList();
        } else {
            System.out.println("Falha ao criar flashcard.");
        }
    }

    @FXML
    private void goToFlashcardList() {
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

