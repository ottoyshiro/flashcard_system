package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.AnimationFeedback;
import com.project.flashcard_system_frontend.AuthStore;
import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.project.flashcard_system_frontend.model.ReviewStartResponse;
import com.project.flashcard_system_frontend.service.ApiService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardListController {

    @FXML
    private Button newFlashcard;

    @FXML
    private Button logoutButton;

    @FXML
    private Button reviewSession;

    @FXML
    private Text newFlashcardText;

    @FXML
    private Label feedback;

    @FXML
    private Pagination pagination;

    @FXML
    private HBox createFlashcardText;

    private List<FlashcardDTO> flashcards;
    private static final int ITEMS_PER_PAGE = 3;
    private FontIcon exitIcon;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {

        exitIcon = new FontIcon("fas-sign-out-alt");
        exitIcon.setIconSize(20);
        logoutButton.setGraphic(exitIcon);
        exitIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        logoutButton.getStyleClass().add("button");
        exitIcon.getStyleClass().add("button-icon");

        flashcards = new ArrayList<>(ApiService.getAllFlashcards());


        reviewSession.setOnAction(e -> startReview());

        updateUI();

        if (flashcards.isEmpty()) {
            pagination.setVisible(false);
            createFlashcardText.setVisible(true);
        } else {
            pagination.setVisible(true);
            createFlashcardText.setVisible(false);
        }
        int pageCount = Math.max(1, (int) Math.ceil((double) flashcards.size() / ITEMS_PER_PAGE));
        pagination.setPageCount(pageCount);

        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(10));

        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, flashcards.size());

        for (FlashcardDTO f : flashcards.subList(start, end)) {
            Node card = createCardNode(f);
            vbox.getChildren().add(card);
        }

        return vbox;
    }

    private Node createCardNode(FlashcardDTO f) {

        // --- TEXTOS ---
        Text txtQuestion = new Text(f.getContent());
        txtQuestion.setFill(Color.WHITE);
        txtQuestion.setWrappingWidth(245.29);
        txtQuestion.setFont(Font.font("Segoe UI Historic", 24));

        Text txtSubject = new Text("Subject: " + f.getSubject());
        txtSubject.setFill(Color.WHITE);
        txtSubject.setWrappingWidth(245.29);
        txtSubject.setFont(Font.font("Segoe UI Historic", 18));

        VBox vboxText = new VBox(8, txtQuestion, txtSubject);
        vboxText.setPrefSize(250, 200);
        HBox.setMargin(vboxText, new Insets(0, 0, 0, 30));

        // --- BOTÃO EDIT ---
        Button btnEdit = new Button();
        FontIcon editIcon = new FontIcon("fas-pen");
        editIcon.setIconSize(20);
        editIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        btnEdit.setGraphic(editIcon);

        btnEdit.setPrefSize(55, 25);
        btnEdit.setOnAction(ev -> onEdit(f));
        btnEdit.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        btnEdit.getStyleClass().add("button");
        editIcon.getStyleClass().add("button-icon");

        // --- BOTÃO DELETE ---
        Button btnDelete = new Button();
        FontIcon trashIcon = new FontIcon("fas-trash");
        trashIcon.setIconSize(20);
        trashIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        btnDelete.setGraphic(trashIcon);


        btnDelete.setPrefSize(55, 25);
        btnDelete.setOnAction(ev -> onDelete(f));
        btnDelete.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        btnDelete.getStyleClass().add("button");
        trashIcon.getStyleClass().add("button-icon");

        VBox vboxButtons = new VBox(15, btnEdit, btnDelete);
        vboxButtons.setAlignment(Pos.CENTER_RIGHT);
        vboxButtons.setPrefSize(164, 120);

        // CARD FINAL
        HBox cardContainer = new HBox(10, vboxText, vboxButtons);
        cardContainer.setStyle("-fx-background-color: #120E20; -fx-background-radius: 8; -fx-padding: 10;");

        cardContainer.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(vboxText, Priority.ALWAYS);


        return cardContainer;
    }

    private void updateUI() {

        if (flashcards.isEmpty()) {
            pagination.setVisible(false);
            createFlashcardText.setVisible(true);
            return;
        }

        pagination.setVisible(true);
        createFlashcardText.setVisible(false);

        int pageCount = (int) Math.ceil((double) flashcards.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(1, pageCount));

        pagination.setCurrentPageIndex(
                Math.min(pagination.getCurrentPageIndex(), pageCount - 1)
        );
    }


    @FXML
    private void onEdit(FlashcardDTO f) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/project/flashcard_system_frontend/edit-flashcard-page.fxml"));
            Parent root = loader.load();

            EditFlashcardController controller = loader.getController();
            controller.setFlashcard(f);
            controller.setStage(stage);

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onDelete(FlashcardDTO f) {

        boolean deleted = ApiService.deleteFlashcard(f);

        if (deleted) {
            flashcards.remove(f);
            updateUI();
            pagination.setPageFactory(this::createPage);

            AnimationFeedback.showTimedFeedback(feedback, "Flashcard deleted successfully!", "green-feedback", 0.5);
        } else {
            System.out.println("Falha ao deletar o flashcard.");
        }
    }

    private void startReview() {

        if (!flashcards.isEmpty()) {

            ReviewStartResponse data = ApiService.startReview();

            if (data != null) {
                goToReviewSession(data);
            } else {
                System.out.println("Falha ao iniciar a revisão!.");
            }
        } else {
            AnimationFeedback.showTimedFeedback(feedback, "Add at least one flashcard to start!", "yellow-feedback", 3);
        }
    }


    @FXML
    private void goToNewFlashcard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/new-flashcard-page.fxml"));
            Parent root = loader.load();

            NewFlashcardController controller = loader.getController();
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
    private void goToNewFlashcardText() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/new-flashcard-page.fxml"));
            Parent root = loader.load();

            NewFlashcardController controller = loader.getController();
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
    private void goToLogin() {
        try {
            AuthStore.clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/login-page.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
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
    public void goToReviewSession(ReviewStartResponse data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/flashcard_system_frontend/review-session-page.fxml"));
            Parent root = loader.load();

            ReviewSessionController controller = loader.getController();
            controller.flashcardListController(this);
            controller.startReview(data);
            controller.setStage(stage);

            //Stage stage = (Stage) pagination.getScene().getWindow();

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

