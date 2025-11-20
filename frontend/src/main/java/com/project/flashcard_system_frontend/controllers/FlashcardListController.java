package com.project.flashcard_system_frontend.controllers;

import com.project.flashcard_system_frontend.model.FlashcardDTO;
import com.project.flashcard_system_frontend.service.ApiService;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class FlashcardListController {

    @FXML
    private Pagination pagination;

    @FXML
    private HBox createFlashcardText;

    private List<FlashcardDTO> flashcards;
    private static final int ITEMS_PER_PAGE = 3;

    @FXML
    public void initialize() {
        flashcards = ApiService.getAllFlashcards();

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

        // --- BOTÃO DELETE ---
        Button btnDelete = new Button();
        FontIcon trashIcon = new FontIcon("fas-trash");
        trashIcon.setIconSize(20);
        trashIcon.setIconColor(Paint.valueOf("#f1f5f9"));
        btnDelete.setGraphic(trashIcon);

        btnDelete.setPrefSize(55, 25);
        btnDelete.setOnAction(ev -> onDelete(f));
        btnDelete.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

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

    private void onEdit(FlashcardDTO f) {
        System.out.println("EDIT: " + f.getId() + f.getId());
    }

    private void onDelete(FlashcardDTO f) {
        System.out.println("DELETE: " + f.getId());
    }

    //private void newFlashcard() {}

}

