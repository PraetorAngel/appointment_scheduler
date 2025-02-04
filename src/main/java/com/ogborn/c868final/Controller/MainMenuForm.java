package com.ogborn.c868final.Controller;

import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * controls the main menu form
 */
public class MainMenuForm implements Initializable {
    /**
     * language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;

    //<editor-fold desc="data members">
    /**
     * label for header of the screen
     */
    public Label headerLabel;

    /**
     * label for displaying author information
     */
    public Label authorLabel;

    /**
     * button to navigate to customers screen
     */
    public Button customersButton;

    /**
     * button to navigate to appointments screen
     */
    public Button appointmentsButton;

    /**
     * button to navigate to reports screen
     */
    public Button reportsButton;

    /**
     * button to log out of the application
     */
    public Button logoutButton;
    //</editor-fold>

    /**
     * opens the view customers screen
     * @param actionEvent the customers button is clicked
     */
    public void onCustomersButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/ViewCustomers.fxml", actionEvent);
    }

    /**
     * opens the view appointments screen
     * @param actionEvent the appointments button is clicked
     */
    public void onAppointmentsButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/ViewAppointments.fxml", actionEvent);
    }

    /**
     * opens the reports screen
     * @param actionEvent the reports button is clicked
     */
    public void onReportsButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/ViewReports.fxml", actionEvent);
    }

    /**
     * logs out the user and returns to the login screen
     * @param actionEvent the logout button is clicked
     */
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        Session.setCurrentUser(null); //logout the currently logged-in user
        FormLoader.openForm("/com/ogborn/c868final/LoginForm.fxml", actionEvent);
    }

    /**
     * initializes the main menu form. used only for localization
     * @param url the url of the form being loaded
     * @param resourceBundle the resource bundle if one exists
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/MainMenuForm_" + Session.getCurrentLocale().getLanguage());

        headerLabel.setText(loadedBundle.getString("header"));
        authorLabel.setText(loadedBundle.getString("author"));
        customersButton.setText(loadedBundle.getString("customersButton"));
        appointmentsButton.setText(loadedBundle.getString("appointmentsButton"));
        reportsButton.setText(loadedBundle.getString("reportsButton"));
        logoutButton.setText(loadedBundle.getString("logoutButton"));
    }
}
