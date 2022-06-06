package com.myapp.app.App;

public class Alerts {

    public static void errorAlert(String errMsg, String errTitle, String errContent) {
        javafx.scene.control.Alert error = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR); // Создаём

        error.setTitle(errTitle);
        error.setHeaderText(errMsg);
        error.setContentText(errContent);

        error.showAndWait();
    }

    public static void infoAlert(String infoMsg, String infoTitle, String infoContent) {
        javafx.scene.control.Alert info = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION); // Создаём

        info.setTitle(infoTitle);
        info.setHeaderText(infoMsg);
        info.setContentText(infoContent);

        info.show();
    }
}
