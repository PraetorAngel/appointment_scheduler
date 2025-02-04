package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.DAO.CustomerDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Customer;
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
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * governs the ViewCustomers screen
 */
public class ViewCustomers implements Initializable {
    /**
     * the language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;
    // <editor-fold desc="data members">

    /**
     * Holds the list of customers displayed in the table.
     * Used to minimize database calls during filtering operations.
     * All CRUD operations directly interact with the database to ensure data consistency.
     */
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * TableView displaying customer data.
     */
    public TableView<Customer> customersTable;

    /**
     * TableColumn displaying customer IDs.
     */
    public TableColumn<Customer, Integer> customerIDColumn;

    /**
     * TableColumn displaying customer names.
     */
    public TableColumn<Customer, String> customerNameColumn;

    /**
     * TableColumn displaying customer addresses.
     */
    public TableColumn<Customer, String> addressColumn;

    /**
     * TableColumn displaying customer divisions.
     */
    public TableColumn<Customer, String> divisionColumn;

    /**
     * TableColumn displaying postal codes.
     */
    public TableColumn<Customer, String> postalCodeColumn;

    /**
     * TableColumn displaying phone numbers.
     */
    public TableColumn<Customer, String> phoneColumn;

    /**
     * TableColumn displaying country IDs.
     */
    public TableColumn<Customer, Integer> countryColumn; // a little bit janky because the displayed data will be Strings, but we bind to country ID

    /**
     * Label for the header of the screen.
     */
    public Label headerLabel;

    /**
     * Button to add a new customer.
     */
    public Button addButton;

    /**
     * Button to update an existing customer.
     */
    public Button updateButton;

    /**
     * Button to delete a customer.
     */
    public Button deleteButton;

    /**
     * Button to navigate back.
     */
    public Button backButton;

    /**
     * Field to input search terms
     */
    public TextField customerSearchField;
    // </editor-fold>

    //<editor-fold desc="methods">
    /**
     * opens the add customer form
     * @param actionEvent the add button is clicked
     */
    public void onAddButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/AddCustomerForm.fxml", actionEvent);
    }

    /**
     * opens the add customer form, but loads a customer to update
     * @param actionEvent the update button is clicked
     * @throws Exception any exception that is thrown
     */
    public void onUpdateButtonClick(ActionEvent actionEvent) throws Exception {
        if (!customersTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ogborn/c868final/AddCustomerForm.fxml"));
            Parent root = loader.load();
            AddCustomerForm controller = loader.getController();
            controller.loadCustomer(customersTable.getSelectionModel().getSelectedItem());
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } else{
            Alert nothingSelectedAlert = new Alert(Alert.AlertType.ERROR);
            nothingSelectedAlert.setTitle(loadedBundle.getString("nothingSelectedAlertTitle"));
            nothingSelectedAlert.setContentText(loadedBundle.getString("nothingSelectedAlertContent"));
            nothingSelectedAlert.setHeaderText(null);
            nothingSelectedAlert.showAndWait();
        }
    }

    /**
     * deletes the selected customer from the database
     * @param actionEvent the delete button is clicked
     * @throws Exception the exception if one is thrown
     */
    public void onDeleteButtonClick(ActionEvent actionEvent) throws Exception {
        if (!customersTable.getSelectionModel().isEmpty()) {
            Customer toBeDeleted = customersTable.getSelectionModel().getSelectedItem();

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle(loadedBundle.getString("customerDeleteConfirmationTitle"));
            confirmationDialog.setHeaderText(null); // No header text
            confirmationDialog.setContentText(MessageFormat.format(
                    loadedBundle.getString("customerDeleteConfirmationContent"),
                    toBeDeleted.getCustomerName()
            ));

            // Show confirmation dialog
            if (confirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Delete customer if confirmed
                AppointmentDAO.deleteAllAppointmentsForCustomer(toBeDeleted.getCustomerId());
                CustomerDAO.deleteCustomer(toBeDeleted.getCustomerId());
                customersTable.setItems(FXCollections.observableArrayList(CustomerDAO.getAllCustomers()));

                // Information dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null); // No header text
                alert.setTitle(loadedBundle.getString("customerDeleteDialogTitle"));
                alert.setContentText(MessageFormat.format(
                        loadedBundle.getString("customerDeleteDialogContent"),
                        toBeDeleted.getCustomerName()
                ));
                alert.showAndWait();
            }
        }
    }

    /**
     * returns to the main form
     * @param actionEvent the main form button is clicked
     */
    public void onBackButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/MainMenuForm.fxml", actionEvent);
    }

    /**
     * maps country names to id's, as the Customer object does not have a corresponding
     * field in the database called "country name"
     * @param countryId the country id
     * @return the string name of the country
     */
    private String getCountryNameById(int countryId){
        return switch (countryId){
            case 1 -> "USA";
            case 2 -> "UK";
            case 3 -> "Canada";
            default -> "Unknown";
        };
    }

    /**
     * Configures the country column to display country names based on country IDs.
     * The database customer object does not have a "country name" field
     * and I did not want to add one to my customer constructor just to end up dropping the
     * value anyway at time of database call. This is the most straightforward way I could come
     * up with, but it's still a bit janky.
     *
     * The column is bound to the "countryId" property of the Customer model using a PropertyValueFactory.
     * A lambda expression is used to implement the TableCellFactory functional interface, allowing
     * customization of how cells in the column are rendered. The lambda dynamically maps the country ID
     * to its corresponding name (e.g., 1 -> "USA") within the cell's update logic.
     */
    private void populateCountryColumn() {
        // column is already bound to countryId
        countryColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer countryId, boolean empty) {
                super.updateItem(countryId, empty);
                if (empty || countryId == null) {
                    setText(null);
                } else {
                    setText(getCountryNameById(countryId));
                }
            }
        });
    }
    //</editor-fold>

    /**
     * initializes the form
     * @param url the form to be loaded
     * @param resourceBundle the resource pack if one exists
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/ViewCustomers_" + Session.getCurrentLocale().getLanguage());

        // Set table column headers
        customerIDColumn.setText(loadedBundle.getString("customerIDColumn"));
        customerNameColumn.setText(loadedBundle.getString("customerNameColumn"));
        addressColumn.setText(loadedBundle.getString("addressColumn"));
        divisionColumn.setText(loadedBundle.getString("divisionColumn"));
        postalCodeColumn.setText(loadedBundle.getString("postalCodeColumn"));
        phoneColumn.setText(loadedBundle.getString("phoneColumn"));

        // Set labels and buttons
        headerLabel.setText(loadedBundle.getString("header"));
        addButton.setText(loadedBundle.getString("addButton"));
        updateButton.setText(loadedBundle.getString("updateButton"));
        deleteButton.setText(loadedBundle.getString("deleteButton"));
        backButton.setText(loadedBundle.getString("backButton"));
        customerSearchField.setPromptText(loadedBundle.getString("customerSearchFieldPromptText"));

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        populateCountryColumn(); //override from countryId to countryName, that way we don't mess up our customerDAO

        customers = CustomerDAO.getAllCustomers();
        customersTable.setItems(customers);
        customersTable.getSelectionModel().selectFirst();

        customerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
        String searchText = newValue.trim().toLowerCase();
        if (searchText.isEmpty()) {
            customersTable.setItems(customers);
        }
        else {
            ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
            for (Customer customer :customers) {
                String countryName = getCountryNameById(customer.getCountryId());
                if (String.valueOf(customer.getCustomerId()).contains(searchText) ||
                        customer.getCustomerName().toLowerCase().contains(searchText) ||
                        customer.getAddress().toLowerCase().contains(searchText) ||
                        customer.getDivisionName().toLowerCase().contains(searchText) ||
                        customer.getPostalCode().toLowerCase().contains(searchText) ||
                        customer.getPhone().toLowerCase().contains(searchText) ||
                        countryName.toLowerCase().contains(searchText)) {
                    filteredCustomers.add(customer);
                }
            }
            customersTable.setItems(filteredCustomers);
        }
        customersTable.getSelectionModel().selectFirst();
        });
    }
}
