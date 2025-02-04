package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.DAO.UserDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controls the behavior of my login form
 */
public class LoginForm implements Initializable {
    /**
     * language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;

    // <editor-fold desc="data members">
    /**
     * label for header of the screen
     */
    public Label headerLabel;

    /**
     * label for displaying author information
     */
    public Label authorLabel;

    /**
     * label for username field
     */
    public Label usernameLabel;

    /**
     * label for password field
     */
    public Label passwordLabel;

    /**
     * label displaying time zone information
     */
    public Label zoneLabel;

    /**
     * text field for entering username
     */
    public TextField usernameField;

    /**
     * password field for entering password
     */
    public PasswordField passwordField;

    /**
     * button to log in to the application
     */
    public Button loginButton;
    // </editor-fold>

    /**
     * initializes the form
     *
     * @param url            the form to be loaded
     * @param resourceBundle the resource bundle if one exists
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // uncomment to set locale manually
        // Session.setCurrentLocale(Locale.ENGLISH);
        // Session.setCurrentLocale(Locale.FRENCH);
        // Session.setCurrentLocale(new Locale("ru","RU")); //Russian apparently isn't a named Locale
        // Session.setCurrentLocale(new Locale("es","ES")); //and neither is Spanish

        // comment out if setting locale manually above
        //Session.setCurrentLocale(Locale.getDefault());
        // Check current locale and load that resource bundle

        // ^ ^ ^ ^ ^
        // moved to main since we apparently cant localize the title otherwise
        // without introducing jank
        // we could use this.loadedBundle = resourceBundle and use the one
        //passed by main, but all of the other forms load it like this
        loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/LoginForm_" + Session.getCurrentLocale().getLanguage());

        // I initially had this as a data member, but we only use it here
        zoneLabel.setText(String.valueOf(ZoneId.systemDefault()));

        headerLabel.setText(loadedBundle.getString("header"));
        authorLabel.setText(loadedBundle.getString("author"));
        usernameLabel.setText(loadedBundle.getString("usernameLabel"));
        usernameField.setPromptText(loadedBundle.getString("usernameFieldPrompt"));
        passwordLabel.setText(loadedBundle.getString("passwordLabel"));
        passwordField.setPromptText(loadedBundle.getString("passwordFieldPrompt"));
        loginButton.setText(loadedBundle.getString("loginButton"));
    }

    /**
     * performs the login if the user clicks the login button
     *
     * @param actionEvent the login button is clicked
     * @throws Exception any exception that is thrown
     */
    public void onLoginButtonClick(ActionEvent actionEvent) throws Exception {
        performLogin(actionEvent);
    }

    /**
     * performs the login if the user presses enter after typing a username and password
     * in lieu of clicking the login button with the mouse
     *
     * @param event the enter key is pressed
     * @throws Exception any exception thrown if one exists
     */
    public void onPressEnterKeyLogin(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            performLogin(new ActionEvent(event.getSource(), event.getTarget()));
        }
    }

    /**
     * the actual login method. logs in an authenticated user
     * also logs all successful and unsuccessful login attempts to file
     *
     * @throws Exception any exception that is thrown
     */
    private void performLogin(ActionEvent actionEvent) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        ZonedDateTime currentUTCDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        String formattedTimestamp = dateTimeFormatter.format(currentUTCDateTime);

        int authenticatedId = UserDAO.authenticate(username, password);

        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter activityLogWriter = new PrintWriter(fileWriter);

        // logon attempt is valid
        if (authenticatedId != -1) {
            activityLogWriter.println(formattedTimestamp + " UTC: " + username + " successfully logged in");
            activityLogWriter.close();

            checkForUpcomingAppointments(authenticatedId);

            FormLoader.openForm("/com/ogborn/c868final/MainMenuForm.fxml", actionEvent);
        } else {
            // log the attempt
            activityLogWriter.println(formattedTimestamp + " UTC: " + username + " unsuccessfully logged in");
            activityLogWriter.close();

            // pop an alert notifying the user of the invalid credentials
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(loadedBundle.getString("authenticateError"));
            alert.setTitle(loadedBundle.getString("authenticateErrorTitle"));

            Optional<ButtonType> result = alert.showAndWait();
            // clear the form after the popup is gone
            if (result.isPresent() && result.get() == ButtonType.OK) {
                usernameField.clear();
                passwordField.clear();
            }
        }
    }

    /**
     * helper method to check for upcoming appointments.
     * not really necessary to split out this method but the performLogin method was huge and clunky
     * pops an error message describing the appointment if one exists, otherwise pops no error
     * WE COULD CHEESE THIS WITHOUT HAVING ANY ITERATION BY CREATING A FAKE APPOINTMENT NOW(), AND THEN USE
     * CHECKFORCONFLICTINGAPPOINTMENTS TO SEE IF A
     * CONFLICT EXISTS, BUT THAT REMOVES THE LAMBDA OPPORTUNITY
     * "I used a lambda expression in the filtered method to filter appointments based on the userId. The
     * filtered method expects a Predicate, which is a functional interface. Instead of writing a full method
     * or an anonymous class to filter appointments, I used the lambda expression appointment -> appointment
     * .getUserId() == userId. . The lambda is passed directly to filtered, where it evaluates each
     * appointment to see if its userId matches the userId provided to the method."
     *
     * @param userId the user to check appointments for
     */
    private void checkForUpcomingAppointments(int userId) {
        LocalDateTime now = LocalDateTime.now();

        // need to explain the use of the lambda here
        ObservableList<Appointment> userAppointments = AppointmentDAO.getAllAppointments().filtered(appointment -> appointment.getUserId() == userId);

        //iterate through and check each appointment. should be no way to have multiple appointments in the 15-minute window, but we're going to check them all just in case
        for (Appointment appointment : userAppointments) {
            if (appointment.getStart().toLocalDateTime().isAfter(now) &&
                    appointment.getStart().toLocalDateTime().isBefore(now.plusMinutes(15))) {

                Alert upcomingAlert = new Alert(Alert.AlertType.ERROR);
                upcomingAlert.setTitle(loadedBundle.getString("upcomingAppointmentTitle"));
                upcomingAlert.setHeaderText(loadedBundle.getString("upcomingAppointmentHeader"));
                upcomingAlert.setContentText(MessageFormat.format(
                        loadedBundle.getString("upcomingAppointmentContent"),
                        appointment.getTitle(),
                        appointment.getDescription(),
                        appointment.getStart().toLocalDateTime()
                ));
                upcomingAlert.showAndWait();
                return;
            }
        }

        // If no conflict, show a "No Upcoming Appointments" alert
        Alert noUpcomingAlert = new Alert(Alert.AlertType.INFORMATION);
        noUpcomingAlert.setTitle(loadedBundle.getString("noUpcomingAppointmentTitle"));
        noUpcomingAlert.setHeaderText(loadedBundle.getString("noUpcomingAppointmentHeader"));
        noUpcomingAlert.setContentText(loadedBundle.getString("noUpcomingAppointmentContent"));
        noUpcomingAlert.showAndWait();

    }
}