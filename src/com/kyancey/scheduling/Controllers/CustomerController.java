package com.kyancey.scheduling.Controllers;

import com.kyancey.scheduling.Data.Country;
import com.kyancey.scheduling.Data.Customer;
import com.kyancey.scheduling.Data.Division;
import com.kyancey.scheduling.Data.FormMode;

import java.io.IOException;
import java.net.URL;

import com.kyancey.scheduling.Entities.MySQLDB;
import com.kyancey.scheduling.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controls the Customer form.
 */
public class CustomerController {
    public static Stage primaryStage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField textFieldCustomerID;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private TextField textFieldPostalCode;

    @FXML
    private TextField textFieldPhone;

    @FXML
    private ChoiceBox<Country> choiceBoxCountry;

    @FXML
    private ChoiceBox<Division> choiceBoxDivision;

    @FXML
    private Button buttonSubmit;

    @FXML
    private Button buttonCancel;

    private Customer selectedCustomer;

    /**
     * Sets the Stage reference
     */
    public void setStage() {
        this.primaryStage = MainController.primaryStage;
    }

    /**
     * Initializes the Customer form.
     * @throws SQLException
     */
    @FXML
    void initialize() throws SQLException {
        choiceBoxCountry.getItems().setAll(MySQLDB.CountryList());
        choiceBoxCountry.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Country>() {
            @Override
            public void changed(ObservableValue<? extends Country> observableValue, Country oldCountry, Country newCountry) {
                try {
                    choiceBoxDivision.getItems().setAll(MySQLDB.DivisionList(newCountry));
                    choiceBoxDivision.setDisable(false);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets the mode of the form.
     * Sets different lambda to submit button depending
     * on the form mode rather than hardcoding one event
     * handler. This makes the code more adaptable and
     * separates logic.
     * Add Lambda: This lambda handles the logic of adding
     * a customer to the database.
     * Modify Lambda: This lambda handles the logic of
     * modifying an existing customer.
     * @param formMode Mode the form should start in.
     * @throws SQLException
     */
    public void setMode(FormMode formMode) throws SQLException {
        if (formMode == FormMode.ADD) {
            buttonSubmit.setText("Create");
            buttonSubmit.setOnAction(e -> {
                // Check to see if all fields are valid
                if (textFieldName.getText().trim().length() == 0 ||
                    textFieldAddress.getText().trim().length() == 0 ||
                    textFieldPostalCode.getText().trim().length() == 0 ||
                    textFieldPhone.getText().trim().length() == 0 ||
                    choiceBoxDivision.isDisable()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be valid.");
                    alert.showAndWait();
                    return;
                }
                else {
                    // Build and insert customer
                    var customer = new Customer.CustomerBuilder()
                            .customerId(0)
                            .customerName(textFieldName.getText().trim())
                            .customerAddress(textFieldAddress.getText().trim())
                            .postalCode(textFieldPostalCode.getText().trim())
                            .phone(textFieldPhone.getText().trim())
                            .divisionID(choiceBoxDivision.getSelectionModel().getSelectedItem().getId())
                            .build();
                    try {
                        MySQLDB.insertCustomer(customer);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        loadMainForm();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            return;
        }
        else if (formMode == FormMode.UPDATE) {
            buttonSubmit.setText("Update");
            textFieldCustomerID.setText(String.valueOf(selectedCustomer.getCustomerId()));
            textFieldName.setText(selectedCustomer.getCustomerName());
            textFieldAddress.setText(selectedCustomer.getCustomerAddress());
            textFieldPostalCode.setText(selectedCustomer.getPostalCode());
            textFieldPhone.setText(selectedCustomer.getPhone());

            var division = MySQLDB.getDivision(selectedCustomer.getDivisionID());
            var country = MySQLDB.getCountry(division);
            choiceBoxCountry.getSelectionModel().select(country);
            choiceBoxDivision.getSelectionModel().select(division);

            buttonSubmit.setOnAction(e -> {
                // Check to see if all fields are valid
                if (textFieldName.getText().trim().length() == 0 ||
                        textFieldAddress.getText().trim().length() == 0 ||
                        textFieldPostalCode.getText().trim().length() == 0 ||
                        textFieldPhone.getText().trim().length() == 0 ||
                        choiceBoxDivision.isDisable()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be valid.");
                    alert.showAndWait();
                    return;
                }
                else {
                    // Update customer
                    selectedCustomer.setCustomerName(textFieldName.getText().trim());
                    selectedCustomer.setCustomerAddress(textFieldAddress.getText().trim());
                    selectedCustomer.setPostalCode(textFieldPostalCode.getText().trim());
                    selectedCustomer.setPhone(textFieldPhone.getText().trim());
                    selectedCustomer.setDivisionID(choiceBoxDivision.getSelectionModel().getSelectedItem().getId());

                    try {
                        MySQLDB.updateCustomer(selectedCustomer);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        loadMainForm();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            return;
        }
    }

    /**
     * Sets the customer reference
     * @param selectedItem Reference to customer selected in Main Form.
     */
    public void setCustomer(Customer selectedItem) {
        this.selectedCustomer = selectedItem;
    }

    /**
     * Encapsulates the logic of returning to the main form.
     * @throws IOException
     */
    private void loadMainForm() throws IOException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/mainform.fxml"));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        MainFormController controller = loader.getController();

        // Set the window up
        Main.primaryStage.setTitle("Scheduling System");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }

    /**
     * Returns the user to the main form.
     * @param actionEvent Ignored
     * @throws IOException
     */
    public void onButtonCancel(ActionEvent actionEvent) throws IOException {
        loadMainForm();
    }
}
