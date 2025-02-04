package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * controls the View appointments form
 */
public class ViewAppointments implements Initializable {
    /**
     * language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;

    /**
     * Holds the list of appointments displayed in the table.
     * Used to reduce database calls during filtering operations.
     * All CRUD operations directly interact with the database to maintain data integrity.
     */
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    //<editor-fold desc="data members">


    /**
     * table displaying appointment data
     */
    public TableView<Appointment> appointmentsTable;

    /**
     * column displaying appointment IDs
     */
    public TableColumn<Appointment, Integer> appointmentIdColumn;

    /**
     * column displaying appointment titles
     */
    public TableColumn<Appointment, String> titleColumn;

    /**
     * column displaying appointment descriptions
     */
    public TableColumn<Appointment, String> descriptionColumn;

    /**
     * column displaying appointment locations
     */
    public TableColumn<Appointment, String> locationColumn;

    /**
     * column displaying appointment contacts
     */
    public TableColumn<Appointment, String> contactColumn;

    /**
     * column displaying appointment types
     */
    public TableColumn<Appointment, String> typeColumn;

    /**
     * column displaying appointment start times
     */
    public TableColumn<Appointment, LocalDateTime> startColumn;

    /**
     * column displaying appointment end times
     */
    public TableColumn<Appointment, LocalDateTime> endColumn;

    /**
     * column displaying customer IDs associated with appointments
     */
    public TableColumn<Appointment, Integer> customerIdColumn;

    /**
     * column displaying user IDs associated with appointments
     */
    public TableColumn<Appointment, Integer> userIdColumn;

    /**
     * radio button to show all appointments
     */
    public RadioButton showAllRadioButton;

    /**
     * radio button to filter appointments for current week
     */
    public RadioButton currentWeekRadioButton;

    /**
     * radio button to filter appointments for current month
     */
    public RadioButton currentMonthRadioButton;

    /**
     * radio button to filter appointments for next 7 days
     */
    public RadioButton next7DaysRadioButton;

    /**
     * radio button to filter appointments for next 30 days
     */
    public RadioButton next30DaysRadioButton;

    /**
     * label for header of the screen
     */
    public Label headerLabel;

    /**
     * button to add a new appointment
     */
    public Button addButton;

    /**
     * button to update an existing appointment
     */
    public Button updateButton;

    /**
     * button to delete an appointment
     */
    public Button deleteButton;

    /**
     * button to navigate back
     */
    public Button backButton;

    /**
     * label for filter section
     */
    public Label filterLabel;

    /**
     * toggle group for filter options
     */
    public ToggleGroup filterGroup;

    /**
     * A text field for searching appointments.
     * Used to input search criteria for filtering appointment data.
     */
    public TextField appointmentSearchField;
    //</editor-fold>

    //<editor-fold desc="methods">
    /**
     * opens the add appointment form
     * @param actionEvent the add button is clicked
     */
    public void onAddButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/AddAppointmentForm.fxml", actionEvent);
    }

    /**
     * Opens the add appointment form, but loads an appointment to update.
     * @param actionEvent the update button is clicked
     * @throws Exception any exception that is thrown
     */
    public void onUpdateButtonClick(ActionEvent actionEvent) throws Exception {
        if (!appointmentsTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ogborn/c868final/AddAppointmentForm.fxml"));
            Parent root = loader.load();
            AddAppointmentForm controller = loader.getController();
            controller.loadAppointment(appointmentsTable.getSelectionModel().getSelectedItem());
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } else {
            Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle(loadedBundle.getString("nothingSelectedAlertTitle"));
            nothingSelectedAlert.setContentText(loadedBundle.getString("nothingSelectedAlertContent"));
            nothingSelectedAlert.setHeaderText(null);
            nothingSelectedAlert.showAndWait();
        }
    }

    /**
     * Deletes the selected appointment after confirmation and displays a success dialog.
     *
     * This method first shows a confirmation dialog with the appointment ID and type.
     * If the user confirms, the appointment is deleted, and a success dialog is displayed.
     * The TableView is refreshed to reflect the updated list of appointments.
     *
     * @param actionEvent The event triggered by the delete button click.
     * @throws Exception If a database error occurs during the deletion process.
     */
    public void onDeleteButtonClick(ActionEvent actionEvent) throws Exception {
        if (!appointmentsTable.getSelectionModel().isEmpty()){
            Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
            AppointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId());

            Alert deleteAppointmentConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAppointmentConfirmationDialog.setTitle(loadedBundle.getString("deleteDialogTitle"));
            deleteAppointmentConfirmationDialog.setHeaderText(loadedBundle.getString("deleteDialogHeader"));
            deleteAppointmentConfirmationDialog.setContentText(
                    MessageFormat.format(
                            loadedBundle.getString("deleteDialogContent"),
                            selectedAppointment.getAppointmentId(),
                            selectedAppointment.getType()
                    )
            );

            // Show dialog and capture user response
            if (deleteAppointmentConfirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Delete appointment if confirmed
                AppointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId());

                // Success dialog
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle(loadedBundle.getString("deleteSuccessDialogTitle"));
                successDialog.setHeaderText(null); // No header text
                successDialog.setContentText(MessageFormat.format(loadedBundle.getString("deleteSuccessDialogContent"), selectedAppointment.getAppointmentId(), selectedAppointment.getType()));
                successDialog.showAndWait();

                // Refresh the table
                appointmentsTable.setItems(AppointmentDAO.getAllAppointments());
            }
        }
    }

    /**
     * returns to the main menu
     * @param actionEvent the main menu button is clicked
     */
    public void onBackButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/MainMenuForm.fxml", actionEvent);
    }

    /**
     * sets the table view to show all appointments. .
     * @param actionEvent the show all radio button is selected
     *                    note that this will be preselected by default
     */
    public void onSelectShowAll(ActionEvent actionEvent) {
        allAppointments.setAll(AppointmentDAO.getAllAppointments());
        filteredAppointments.setAll(allAppointments);
        applySearch();
    }

    /**
     * sets the table view to show all appointments for the current CALENDAR week
     * @param actionEvent the radio button is toggled
     * @throws Exception handling for any exception if one is thrown
     */
    public void onSelectShowWeekly(ActionEvent actionEvent) throws Exception {
        allAppointments = AppointmentDAO.getWeeklyAppointments();
        filteredAppointments.setAll(allAppointments);
        applySearch();
    }

    /** included due to inconsistent understanding of the word "weekly"
     * sets the tableview to show all appointments in the next 7 days
     * @param actionEvent the radio button is selected
     * @throws SQLException any sql error that is thrown
     */
    public void onSelectShowNext7Days(ActionEvent actionEvent) throws Exception {
        allAppointments = AppointmentDAO.getNext7DaysAppointments();
        filteredAppointments.setAll(allAppointments);
        applySearch();
    }

    /**
     * sets the table view to show all appointments for the current CALENDAR month e.g. December
     * @param actionEvent the radio button is toggled
     * @throws Exception handling for any exception if thrown
     */
    public void onSelectShowMonthly(ActionEvent actionEvent) throws Exception {
        allAppointments = AppointmentDAO.getMonthlyAppointments();
        filteredAppointments.setAll(allAppointments);
        applySearch();
    }

    /**
     * included due to inconsistent understanding of the word "monthly"
     * sets the tableview to show all appointments in the next 30 days
     * @param actionEvent the radio button is selected
     * @throws SQLException any sql error that is thrown
     */
    public void onSelectShowNext30Days(ActionEvent actionEvent) throws Exception {
        allAppointments = AppointmentDAO.getNext30DaysAppointments();
        filteredAppointments.setAll(allAppointments);
        applySearch();
    }
    //</editor-fold>

    /**
     * Applies the search filter to the current list of appointments.
     * If the search field is empty, resets the table to the filtered list based on the selected radio button.
     */
    private void applySearch() {
        String searchText = appointmentSearchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            filteredAppointments.setAll(allAppointments);// Set unfiltered dataset
        }
        else {

            filteredAppointments.setAll(allAppointments.filtered(appointment ->
                    String.valueOf(appointment.getAppointmentId()).contains(searchText) ||
                        appointment.getTitle().toLowerCase().contains(searchText) ||
                        appointment.getDescription().toLowerCase().contains(searchText) ||
                        appointment.getLocation().toLowerCase().contains(searchText) ||
                        appointment.getContactName().toLowerCase().contains(searchText) ||
                        appointment.getType().toLowerCase().contains(searchText) ||
                        appointment.getStart().toString().toLowerCase().contains(searchText) ||
                        appointment.getEnd().toString().toLowerCase().contains(searchText) ||
                        String.valueOf(appointment.getCustomerId()).contains(searchText) ||
                        String.valueOf(appointment.getUserId()).contains(searchText)));
        }
        appointmentsTable.refresh();
        appointmentsTable.getSelectionModel().selectFirst();
    }

    /**
     * initializes the View Appointments form
     * @param url the path to the fxml file
     * @param resourceBundle the resource bundle if one exists
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load the resource bundle for localization
            loadedBundle = ResourceBundle.getBundle("com.ogborn.c868final.ViewAppointments_" + Session.getCurrentLocale().getLanguage());

            // Set cell value factories for TableView columns
            appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            // Set Labels and Buttons
            headerLabel.setText(loadedBundle.getString("headerLabel"));
            addButton.setText(loadedBundle.getString("addButton"));
            updateButton.setText(loadedBundle.getString("updateButton"));
            deleteButton.setText(loadedBundle.getString("deleteButton"));
            backButton.setText(loadedBundle.getString("backButton"));
            filterLabel.setText(loadedBundle.getString("filterLabel"));
            appointmentSearchField.setPromptText(loadedBundle.getString("appointmentSearchFieldPromptText"));


            // Localize TableView column headers
            appointmentIdColumn.setText(loadedBundle.getString("appointmentIdColumn"));
            titleColumn.setText(loadedBundle.getString("titleColumn"));
            descriptionColumn.setText(loadedBundle.getString("descriptionColumn"));
            locationColumn.setText(loadedBundle.getString("locationColumn"));
            contactColumn.setText(loadedBundle.getString("contactColumn"));
            typeColumn.setText(loadedBundle.getString("typeColumn"));
            startColumn.setText(loadedBundle.getString("startColumn"));
            endColumn.setText(loadedBundle.getString("endColumn"));
            customerIdColumn.setText(loadedBundle.getString("customerIdColumn"));
            userIdColumn.setText(loadedBundle.getString("userIdColumn"));

            // Localize RadioButton labels
            showAllRadioButton.setText(loadedBundle.getString("showAllRadioButton"));
            currentWeekRadioButton.setText(loadedBundle.getString("currentWeekRadioButton"));
            currentMonthRadioButton.setText(loadedBundle.getString("currentMonthRadioButton"));
            next7DaysRadioButton.setText(loadedBundle.getString("next7DaysRadioButton"));
            next30DaysRadioButton.setText(loadedBundle.getString("next30DaysRadioButton"));

            // Populate the TableView with all appointments initially
            allAppointments.setAll(AppointmentDAO.getAllAppointments());
            filteredAppointments.setAll(allAppointments);
            appointmentsTable.setItems(filteredAppointments);
            appointmentsTable.getSelectionModel().selectFirst();

            appointmentSearchField.textProperty().addListener((observable, oldValue, newValue) -> applySearch());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
