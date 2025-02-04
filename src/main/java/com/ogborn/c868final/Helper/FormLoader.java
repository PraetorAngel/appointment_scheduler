package com.ogborn.c868final.Helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * helper class to open forms, allowing for cleaner code in the rest of the project
 */
public class FormLoader {

    /**
     * opens a form and reuses the existing window
     * cleaner and less annoying than orphaning windows
     * @param fxmlPath the path to the fxml file
     * @param resourceBundle the path to the resource file
     * @param actionEvent the source of the event
     */
    public static void openForm(String fxmlPath, ResourceBundle resourceBundle, ActionEvent actionEvent) {
        try {
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(FormLoader.class.getResource(fxmlPath), resourceBundle);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading form: " + fxmlPath);
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error launching form").showAndWait();
        }
    }

    /**
     * used to open a form that does not have a resource bundle
     * for extra cheese, we just call the other overload and null out the one argument
     * @param fxmlPath the path of the fxml file
     * @param actionEvent the source of the event
     */
    public static void openForm(String fxmlPath, ActionEvent actionEvent) {
        openForm(fxmlPath, null, actionEvent);
    }
}
