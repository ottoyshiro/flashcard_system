module com.project.flashcard_system_frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires okhttp3;
    requires javafx.graphics;
    requires java.desktop;
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.fontawesome5;
    requires javafx.base;

    opens com.project.flashcard_system_frontend to javafx.fxml;
    opens com.project.flashcard_system_frontend.controllers to javafx.fxml;

    opens com.project.flashcard_system_frontend.model to com.fasterxml.jackson.databind;
    exports com.project.flashcard_system_frontend.model to com.fasterxml.jackson.databind;

    exports com.project.flashcard_system_frontend;
    exports com.project.flashcard_system_frontend.controllers;

}