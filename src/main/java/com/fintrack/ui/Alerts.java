package com.fintrack.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

final class Alerts {

    private Alerts() {
        // utility class: not meant to be instantiated
    }

    static void error(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(title);
        return alert.showAndWait()
                .filter(button -> button == ButtonType.YES)
                .isPresent();
    }
}
