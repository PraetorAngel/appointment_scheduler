package com.ogborn.c868final;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Helper.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main entry point of the Appointment Scheduling System application.
 * This class initializes the JavaFX application, sets up the main stage,
 * and manages the application's lifecycle.
 *
 * This application allows users to schedule, manage, and view appointments.
 *
 * * @author PraetorAngel
 * *
 * The generated Javadocs HTML file is located in the "javadoc" folder in the root of the application.
 */
public class Main extends Application {
    /**
     * Starts the JavaFX application.
     * Loads the initial FXML file for the login form, sets the stage title, and displays the primary scene.
     * The locale is set here to ensure proper localization of stage titles and other resources.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If there is an error loading the FXML resource.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // uncomment to set locale manually. shouldn't be as necessary now that we have the unit tests
        // Session.setCurrentLocale(Locale.ENGLISH);
        // Session.setCurrentLocale(Locale.FRENCH);
        // Session.setCurrentLocale(new Locale("ru","RU")); //Russian apparently isn't a named Locale
        // Session.setCurrentLocale(new Locale("es","ES")); //and neither is Spanish

        // comment out if setting locale manually above
        Session.setCurrentLocale(Locale.getDefault());
        // Check current locale and load that resource bundle
        // Locale must be set here to ensure proper localization of the stage title
        ResourceBundle loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/LoginForm_" + Session.getCurrentLocale().getLanguage());

        // Set application icons for title bar, taskbar, and task manager
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/com/ogborn/c868final/images/as_16x16.png")),
                new Image(getClass().getResourceAsStream("/com/ogborn/c868final/images/as_32x32.png")),
                new Image(getClass().getResourceAsStream("/com/ogborn/c868final/images/as_48x48.png")),
                new Image(getClass().getResourceAsStream("/com/ogborn/c868final/images/as_128x128.png")),
                new Image(getClass().getResourceAsStream("/com/ogborn/c868final/images/as_256x256.png"))
        );

        Parent root = FXMLLoader.load(getClass().getResource("/com/ogborn/c868final/LoginForm.fxml"), loadedBundle);
        stage.setTitle(loadedBundle.getString("stageTitle"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * The main method of the application.
     * <p>
     * This method opens the database connection, launches the JavaFX application, and then closes the database connection upon exit.
     * </p>
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}