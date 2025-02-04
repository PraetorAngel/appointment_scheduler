package com.ogborn.c868final.Controller;

import com.ogborn.c868final.DAO.CountryDAO;
import com.ogborn.c868final.DAO.CustomerDAO;
import com.ogborn.c868final.DAO.DivisionDAO;
import com.ogborn.c868final.Helper.FormLoader;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.Country;
import com.ogborn.c868final.Model.Customer;
import com.ogborn.c868final.Model.Division;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * controls my add customer form (which is also the update form :) )
 */
public class AddCustomerForm implements Initializable {
    /**
     * language bundle to be loaded for localization
     */
    private static ResourceBundle loadedBundle;

    // <editor-fold desc="data members">
    /**
     * currently loaded customer, initialized to null to prevent accidental use in updates
     */
    private Customer loadedCustomer = null; // make sure we null this out so we don't use any update() things accidentally

    // labels
    /**
     * label for header of the screen
     */
    public Label headerLabel;

    /**
     * label for customer name
     */
    public Label customerNameLabel;

    /**
     * label for customer address
     */
    public Label addressLabel;

    /**
     * label for division ID
     */
    public Label divisionIdLabel;

    /**
     * label for customer phone number
     */
    public Label phoneNumberLabel;

    /**
     * label for postal code
     */
    public Label postalCodeLabel;

    /**
     * label for customer ID
     */
    public Label customerIdLabel;

    /**
     * label for country ID
     */
    public Label countryIDLabel;

    // fields
    /**
     * text field for entering customer name
     */
    public TextField customerNameField;

    /**
     * text field for entering customer ID
     */
    public TextField customerIdField;

    /**
     * text field for entering customer address
     */
    public TextField addressField;

    /**
     * text field for entering postal code
     */
    public TextField postalCodeField;

    /**
     * text field for entering phone number
     */
    public TextField phoneNumberField;

    // buttons
    /**
     * button to update customer information
     */
    public Button updateButton;

    /**
     * button to cancel updates
     */
    public Button cancelButton;

    // combo boxes
    /**
     * combo box for selecting country ID
     */
    public ComboBox<Country> countryIdComboBox;

    /**
     * combo box for selecting division ID
     */
    public ComboBox<Division> divisionIdComboBox;
    // </editor-fold>

    /**
     * adds or updates a customer to the database, depending on if a customer is loaded or not
     * @param actionEvent the add/update button is clicked
     * @throws Exception any exception if one is thrown
     */
    public void onUpdateButtonClick(ActionEvent actionEvent) throws Exception {
        String customerName = customerNameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        Division division = divisionIdComboBox.getSelectionModel().getSelectedItem(); // not sure about that one

        if (!customerName.isEmpty() && !address.isEmpty() && !postalCode.isEmpty() && !phoneNumber.isEmpty() && !(division == null))
        {
            if (loadedCustomer == null) {
                CustomerDAO.addCustomer(customerName, address, postalCode, phoneNumber, division.getDivisionId());
            } else{
                CustomerDAO.updateCustomer(customerName, address, postalCode, phoneNumber, division.getDivisionId(), loadedCustomer.getCustomerId());
            }

            FormLoader.openForm("/com/ogborn/c868final/ViewCustomers.fxml", actionEvent);
        }
        // case for missing field data
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loadedBundle.getString("addErrorTitle"));
            alert.setHeaderText(null);
            alert.setContentText(loadedBundle.getString("addErrorContent"));
            alert.showAndWait();
        }
    }

    /**
     * returns the user to the ViewCustomers form
     * @param actionEvent the cancel button is clicked
     */
    public void onCancelButtonClick(ActionEvent actionEvent) {
        FormLoader.openForm("/com/ogborn/c868final/ViewCustomers.fxml", actionEvent);
    }

    /**
     * initializes the addCustomerForm
     * I used a lambda expression here because the addListener method expects a ChangeListener, which is
     * a functional interface with a single method. Rather than creating a full anonymous class to
     * implement that interface, the lambda provides a more concise and readable way to define the
     * behavior directly inline. The lambda expression (observable, oldValue, newValue) -> {
     * setDivisions(null); } allows for the direct execution of the code when the selection changes.
     *      *
     * @param url the url of the form being loaded
     * @param resourceBundle the resource bundle if one exists
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadedBundle = ResourceBundle.getBundle("com/ogborn/c868final/AddCustomerForm_" + Session.getCurrentLocale().getLanguage());

            countryIdComboBox.setItems(CountryDAO.getAllCountries());

            // I’m using a lambda here because addListener expects a ChangeListener, which is a functional interface. In lieu
            // of writing out an entire ChangeListener implementation or messing around with an anonymous class, the lambda
            // does exactly what needs to happen when the selection changes—call setDivisions(null). It’s straightforward and
            // clean.
            countryIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    setDivisions(); //its really cringe to not be able to lean on the other try catch block, I'm not sure why I can't, but it doesn't work lol
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Set default country and divisions if adding a customer; I don't like having it default to blank
            // looks cleaner to have US preselected and if they want something else they'll just pick that
            // and when updating a customer these will be overwritten by the loaded values
            countryIdComboBox.setValue(countryIdComboBox.getItems().get(0));
            divisionIdComboBox.setItems(DivisionDAO.getUSDivisions());
            divisionIdComboBox.setValue(divisionIdComboBox.getItems().get(0));

            //labels and fields
            headerLabel.setText(loadedBundle.getString("headerAdd"));
            customerIdLabel.setText(loadedBundle.getString("customerId"));
            customerNameLabel.setText(loadedBundle.getString("customerName"));
            addressLabel.setText(loadedBundle.getString("address"));
            postalCodeLabel.setText(loadedBundle.getString("postalCode"));
            divisionIdLabel.setText(loadedBundle.getString("divisionId"));
            phoneNumberLabel.setText(loadedBundle.getString("phoneNumber"));
            countryIDLabel.setText(loadedBundle.getString("countryIDLabel"));
            customerIdField.setPromptText(loadedBundle.getString("customerIdPlaceholder"));
            updateButton.setText(loadedBundle.getString("updateButtonAdd"));
            cancelButton.setText(loadedBundle.getString("cancelButton"));
            divisionIdComboBox.setPromptText(loadedBundle.getString("divisionIdComboBoxPlaceholder"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * populates the first-level divisions based on the chosen country
     * @throws Exception any other error that may occur
     */
    public void setDivisions() throws Exception {
        Country selectedCountry = countryIdComboBox.getSelectionModel().getSelectedItem();

        if (selectedCountry != null) {
            switch (selectedCountry.getCountryID()) {
                case 1:
                    divisionIdLabel.setText(loadedBundle.getString("divisionIdLabelUS"));
                    divisionIdComboBox.setItems(DivisionDAO.getUSDivisions());
                    break;
                case 2:
                    divisionIdLabel.setText(loadedBundle.getString("divisionIdLabelUK"));
                    divisionIdComboBox.setItems(DivisionDAO.getUKDivisions());
                    break;
                case 3:
                    divisionIdLabel.setText(loadedBundle.getString("divisionIdLabelCanada"));
                    divisionIdComboBox.setItems(DivisionDAO.getCanadaDivisions());
                    break;
                default:
                    divisionIdLabel.setText(loadedBundle.getString("divisionIdLabelDefault"));
                    divisionIdComboBox.setDisable(true);
                    return;
            }
            divisionIdComboBox.getSelectionModel().selectFirst();
            divisionIdComboBox.setDisable(false);
        } else {
            divisionIdLabel.setText(loadedBundle.getString("divisionIdLabelDefault"));
            divisionIdComboBox.setDisable(true);
        }
    }


    /**
     * loads customer data into the table, also renames the header and one button to 'update'
     * @param customer the customer to load
     * @throws Exception any exception thrown
     */
    public void loadCustomer(Customer customer) throws Exception {
        this.loadedCustomer = customer;
        headerLabel.setText(loadedBundle.getString("headerUpdate"));
        updateButton.setText(loadedBundle.getString("updateButtonUpdate"));
        customerIdField.setText(String.valueOf(customer.getCustomerId()));
        customerNameField.setText(customer.getCustomerName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneNumberField.setText(customer.getPhone());

        // Set country and populate divisions
        for (Country country : countryIdComboBox.getItems()) {
            if (country.getCountryID() == customer.getCountryId()) {
                countryIdComboBox.setValue(country);

                setDivisions();
                break;
            }
        }

        // Set division after divisions are populated
        for (Division division : divisionIdComboBox.getItems()) {
            if (division.getDivisionId() == customer.getDivisionId()) {
                divisionIdComboBox.setValue(division);
                break;
            }
        }
    }
}
