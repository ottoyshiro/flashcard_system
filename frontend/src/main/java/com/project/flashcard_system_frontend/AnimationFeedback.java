package com.project.flashcard_system_frontend;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AnimationFeedback {

    public static void showTimedFeedback(Label feedback, String message, String cssClass, double seconds) {

        feedback.getStyleClass().removeAll("yellow-feedback", "red-feedback", "green-feedback");

        feedback.getStyleClass().add(cssClass);

        feedback.setText(message);
        feedback.setOpacity(1.0);
        feedback.setVisible(true);

        PauseTransition wait = new PauseTransition(Duration.seconds(seconds));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), feedback);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        wait.setOnFinished(e -> fadeOut.play());

        fadeOut.setOnFinished(e -> feedback.setVisible(false));

        wait.play();
    }
}
