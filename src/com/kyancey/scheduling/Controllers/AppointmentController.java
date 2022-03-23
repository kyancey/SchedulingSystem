package com.kyancey.scheduling.Controllers;

import com.kyancey.scheduling.Data.Appointment;
import com.kyancey.scheduling.Data.Contact;
import com.kyancey.scheduling.Data.DateTimeConversion;
import com.kyancey.scheduling.Data.FormMode;
import com.kyancey.scheduling.Entities.MySQLDB;
import com.kyancey.scheduling.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class controls the Appointment Form.
 */
public class AppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField textFieldAppointmentID;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldLocation;

    @FXML
    private TextField textFieldType;

    @FXML
    private ChoiceBox<Contact> choiceBoxContact;

    @FXML
    private Button buttonSubmit;

    @FXML
    private Button buttonCancel;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private ChoiceBox<Integer> choiceBoxHourStart;

    @FXML
    private ChoiceBox<Integer> choiceBoxMinuteStart;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private ChoiceBox<Integer> choiceBoxHourEnd;

    @FXML
    private ChoiceBox<Integer> choiceBoxMinuteEnd;

    @FXML
    private TextField textFieldCustomerID;

    @FXML
    private TextField textFieldUserID;

    public static Stage primaryStage;

    private Appointment selectedAppointment;

    /**
     * Sets the Stage reference
     */
    public void setStage() {
        this.primaryStage = MainController.primaryStage;
    }

    /**
     * @throws SQLException
     * Initializes form
     */
    @FXML
    public void initialize() throws SQLException {
        choiceBoxHourStart.getItems().setAll(IntStream.range(0, 24).boxed().collect(Collectors.toList()));
        choiceBoxHourEnd.getItems().setAll(IntStream.range(0, 24).boxed().collect(Collectors.toList()));
        choiceBoxMinuteStart.getItems().setAll(IntStream.range(0, 60).boxed().collect(Collectors.toList()));
        choiceBoxMinuteEnd.getItems().setAll(IntStream.range(0, 60).boxed().collect(Collectors.toList()));

        choiceBoxContact.getItems().setAll(MySQLDB.ContactList());
        datePickerStart.valueProperty().addListener((ov, oldValue, newValue) -> {
            datePickerEnd.setValue(datePickerStart.getValue());
        });
    }

    /**
     * Sets the mode of the form.
     * Sets different lambda to submit button depending
     * on the form mode rather than hardcoding one event
     * handler. This makes the code more adaptable and
     * separates logic.
     * Add Lambda: This lambda handles the logic of adding
     * an appointment to the database.
     * Modify Lambda: This lambda handles the logic of
     * modifying an existing appointment.
     * @param formMode Mode the form should start in
     * @throws SQLException
     */
    public void setMode(FormMode formMode) throws SQLException {
        if (formMode == FormMode.ADD) {
            buttonSubmit.setText("Create");

            buttonSubmit.setOnAction(e -> {
                int appointmentID = 0;
                int customerID = 0;
                int userID = 0;
                // Check for valid numbers
                try {
                    customerID = Integer.parseInt(textFieldCustomerID.getText().trim());
                    userID = Integer.parseInt(textFieldUserID.getText().trim());
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All numeric fields must be valid integers.");
                    alert.showAndWait();
                    return;
                }

                // Check for blank text fields
                if (textFieldTitle.getText().trim().length() == 0 ||
                        textFieldDescription.getText().trim().length() == 0 ||
                        textFieldLocation.getText().trim().length() == 0 ||
                        textFieldType.getText().trim().length() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All text fields must be non-empty.");
                    alert.showAndWait();
                    return;
                }

                // Check for valid date and time.
                LocalDateTime localStartTime = LocalDateTime.of(datePickerStart.getValue(),
                        LocalTime.of(choiceBoxHourStart.getValue(),
                                choiceBoxMinuteStart.getValue()));
                LocalDateTime localEndTime = LocalDateTime.of(datePickerEnd.getValue(),
                        LocalTime.of(choiceBoxHourEnd.getValue(),
                                choiceBoxMinuteEnd.getValue()));

                // Get the hour and minute for each datetime.
                ZonedDateTime ESTStartTime = DateTimeConversion.toESTFromLocal(localStartTime);
                ZonedDateTime ESTEndTime = DateTimeConversion.toESTFromLocal(localEndTime);
                var test = ESTStartTime.toLocalDateTime();
                int ESTStartHour = ESTStartTime.toLocalDateTime().getHour();
                int ESTEndHour = ESTEndTime.toLocalDateTime().getHour();
                DayOfWeek ESTStartDayOfWeek = ESTStartTime.getDayOfWeek();
                DayOfWeek ESTEndDayOfWeek = ESTEndTime.getDayOfWeek();

                if (ESTStartHour < 8 || ESTStartHour > 22) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment Start Time is not between 8 AM EST and 10 PM EST.");
                    alert.showAndWait();
                    return;
                }
                else {
                    if (ESTStartDayOfWeek == DayOfWeek.SUNDAY || ESTStartDayOfWeek == DayOfWeek.SATURDAY)
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment cannot be scheduled for a weekend.");
                        alert.showAndWait();
                        return;
                    }
                }

                if (ESTEndHour < 8 || ESTEndHour > 22) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment End Time is not between 8 AM EST and 10 PM EST.");
                    alert.showAndWait();
                    return;
                }
                else {
                    if (ESTEndDayOfWeek == DayOfWeek.SUNDAY || ESTEndDayOfWeek == DayOfWeek.SATURDAY)
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment cannot be scheduled for a weekend.");
                        alert.showAndWait();
                        return;
                    }
                }

                try {
                    if (MySQLDB.customerAppointmentOverlaps(customerID, localStartTime, localEndTime)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Customer already has an appointment at this time.");
                        alert.showAndWait();
                        return;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                var appointment = new Appointment.AppointmentBuilder()
                        .appointmentId(0)
                        .customerID(customerID)
                        .title(textFieldTitle.getText().trim())
                        .description(textFieldDescription.getText().trim())
                        .location(textFieldLocation.getText().trim())
                        .type(textFieldType.getText().trim())
                        .userID(userID)
                        .contactID(choiceBoxContact.getValue().getContactID())
                        .start(localStartTime)
                        .end(localEndTime)
                        .build();
                try {
                    MySQLDB.insertAppointment(appointment);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    loadMainForm();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        } else if (formMode == FormMode.UPDATE) {
            buttonSubmit.setText("Update");

            textFieldAppointmentID.setText(String.valueOf(selectedAppointment.getAppointmentId()));
            textFieldTitle.setText(selectedAppointment.getTitle());
            textFieldDescription.setText(selectedAppointment.getDescription());
            textFieldLocation.setText(selectedAppointment.getLocation());
            textFieldType.setText(selectedAppointment.getType());
            textFieldCustomerID.setText(String.valueOf(selectedAppointment.getCustomerID()));
            textFieldUserID.setText(String.valueOf(selectedAppointment.getUserID()));

            datePickerStart.setValue(LocalDate.from(DateTimeConversion.toLocalFromUTC(selectedAppointment.getStart())));
            choiceBoxHourStart.setValue(DateTimeConversion.toLocalFromUTC(selectedAppointment.getStart()).getHour());
            choiceBoxMinuteStart.setValue(DateTimeConversion.toLocalFromUTC(selectedAppointment.getStart()).getMinute());

            datePickerEnd.setValue(LocalDate.from(DateTimeConversion.toLocalFromUTC(selectedAppointment.getEnd())));
            choiceBoxHourEnd.setValue(DateTimeConversion.toLocalFromUTC(selectedAppointment.getEnd()).getHour());
            choiceBoxMinuteEnd.setValue(DateTimeConversion.toLocalFromUTC(selectedAppointment.getEnd()).getMinute());

            choiceBoxContact.getSelectionModel().select(MySQLDB.getContact(selectedAppointment.getContactID()));

            // Button Submit Event
            buttonSubmit.setOnAction(e -> {

                int appointmentID = 0;
                int customerID = 0;
                int userID = 0;
                // Check for valid numbers
                try {
                    appointmentID = Integer.parseInt(textFieldAppointmentID.getText().trim());
                    customerID = Integer.parseInt(textFieldCustomerID.getText().trim());
                    userID = Integer.parseInt(textFieldUserID.getText().trim());
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All numeric fields must be valid integers.");
                    alert.showAndWait();
                    return;
                }

                // Check for blank text fields
                if (textFieldTitle.getText().trim().length() == 0 ||
                        textFieldDescription.getText().trim().length() == 0 ||
                        textFieldLocation.getText().trim().length() == 0 ||
                        textFieldType.getText().trim().length() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All text fields must be non-empty.");
                    alert.showAndWait();
                    return;
                }

                // Check for valid date and time.
                LocalDateTime localStartTime = LocalDateTime.of(datePickerStart.getValue(),
                        LocalTime.of(choiceBoxHourStart.getValue(),
                                choiceBoxMinuteStart.getValue()));
                LocalDateTime localEndTime = LocalDateTime.of(datePickerEnd.getValue(),
                        LocalTime.of(choiceBoxHourEnd.getValue(),
                                choiceBoxMinuteEnd.getValue()));

                // Get the hour and minute for each datetime.
                ZonedDateTime ESTStartTime = DateTimeConversion.toESTFromLocal(localStartTime);
                ZonedDateTime ESTEndTime = DateTimeConversion.toESTFromLocal(localEndTime);
                var test = ESTStartTime.toLocalDateTime();
                int ESTStartHour = ESTStartTime.toLocalDateTime().getHour();
                int ESTEndHour = ESTEndTime.toLocalDateTime().getHour();
                DayOfWeek ESTStartDayOfWeek = ESTStartTime.getDayOfWeek();
                DayOfWeek ESTEndDayOfWeek = ESTEndTime.getDayOfWeek();

                if (ESTStartHour < 8 || ESTStartHour > 22) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment Start Time is not between 8 AM EST and 10 PM EST.");
                    alert.showAndWait();
                    return;
                }
                else {
                    if (ESTStartDayOfWeek == DayOfWeek.SUNDAY || ESTStartDayOfWeek == DayOfWeek.SATURDAY)
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment cannot be scheduled for a weekend.");
                        alert.showAndWait();
                        return;
                    }
                }

                if (ESTEndHour < 8 || ESTEndHour > 22) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment End Time is not between 8 AM EST and 10 PM EST.");
                    alert.showAndWait();
                    return;
                }
                else {
                    if (ESTEndDayOfWeek == DayOfWeek.SUNDAY || ESTEndDayOfWeek == DayOfWeek.SATURDAY)
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment cannot be scheduled for a weekend.");
                        alert.showAndWait();
                        return;
                    }
                }

                try {
                    if (MySQLDB.customerAppointmentOverlaps(customerID, localStartTime, localEndTime)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Customer already has an appointment at this time.");
                        alert.showAndWait();
                        return;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                selectedAppointment.setCustomerID(customerID);
                selectedAppointment.setTitle(textFieldTitle.getText().trim());
                selectedAppointment.setDescription(textFieldDescription.getText().trim());
                selectedAppointment.setLocation(textFieldLocation.getText().trim());
                selectedAppointment.setType(textFieldType.getText().trim());
                selectedAppointment.setUserID(userID);
                selectedAppointment.setContactID(choiceBoxContact.getValue().getContactID());
                selectedAppointment.setStart(localStartTime);
                selectedAppointment.setEnd(localEndTime);

                try {
                    MySQLDB.updateAppointment(selectedAppointment);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    loadMainForm();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }

    /**
     * Sets the appointment reference.
     * @param selectedItem Reference to appointment selected in Main Form.
     */
    public void setAppointment(Appointment selectedItem) {
        this.selectedAppointment = selectedItem;
    }

    /**
     * Returns to the main form without saving.
     * @param actionEvent Ignored
     * @throws IOException
     */
    public void onButtonCancel(ActionEvent actionEvent) throws IOException {
        loadMainForm();
    }

    /**
     * Encapsulates the logic for returning to the main form.
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
}
