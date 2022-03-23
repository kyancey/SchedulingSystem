package com.kyancey.scheduling.Controllers;

import com.kyancey.scheduling.Data.*;
import com.kyancey.scheduling.Main;
import com.kyancey.scheduling.Data.*;
import com.kyancey.scheduling.Entities.MySQLDB;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Controls the MainForm Form
 */
public class MainFormController {

    @FXML
    private RadioButton radioButtonAll;

    @FXML
    private RadioButton radioButtonWeek;

    @FXML
    private RadioButton radioButtonMonth;

    @FXML
    private ToggleGroup timeToggle;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonForward;

    @FXML
    private Label labelCurrentMonthWeek;

    @FXML
    private Label labelAlert;

    @FXML
    private Button buttonAddAppointment;

    @FXML
    private Button buttonUpdateAppointment;

    @FXML
    private Button buttonDeleteAppointment;

    @FXML
    private Button buttonAddCustomer;

    @FXML
    private Button buttonUpdateCustomer;

    @FXML
    private Button buttonDeleteCustomer;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerID;

    @FXML
    private TableColumn<Customer, String> customerName;

    @FXML
    private TableColumn<Customer, String> customerAddress;

    @FXML
    private TableColumn<Customer, String> customerPostalCode;

    @FXML
    private TableColumn<Customer, String> customerPhone;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentID;

    @FXML
    private TableColumn<Appointment, String> appointmentTitle;

    @FXML
    private TableColumn<Appointment, String> appointmentDescription;

    @FXML
    private TableColumn<Appointment, String> appointmentLocation;

    @FXML
    private TableColumn<Appointment, Integer> appointmentContact;

    @FXML
    private TableColumn<Appointment, String> appointmentType;

    @FXML
    private TableColumn<Appointment, String> appointmentStart;

    @FXML
    private TableColumn<Appointment, String> appointmentEnd;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerID;

    @FXML
    private TableColumn<Appointment, Integer> appointmentUserID;

    private ZonedDateTime currentWeek = ZonedDateTime.now();

    private List<Customer> customers;

    private List<Appointment> appointments;

    private FilteredList<Customer> filteredCustomers;

    private FilteredList<Appointment> filteredAppointments;

    private ObservableList<Customer> observableCustomers;

    private ObservableList<Appointment> observableAppointments;

    private Timeline timeline;

    /**
     * Initializes the MainForm Form
     * @throws SQLException
     */
    @FXML
    void initialize() throws SQLException {

        // Set Month/Week label
        labelCurrentMonthWeek.setText(currentWeek.getMonth().name() + " " + currentWeek.getYear() +
                " Week: " + Integer.toString(currentWeek.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));

        // Set columns up for customer table
        customerID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerId()));
        customerName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerName()));
        customerAddress.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerAddress()));
        customerPostalCode.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPostalCode()));
        customerPhone.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPhone()));

        // Set columns up for appointment table
        appointmentID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAppointmentId()));
        appointmentTitle.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle()));
        appointmentDescription.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription()));
        appointmentLocation.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLocation()));
        appointmentContact.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContactID()));
        appointmentType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        appointmentStart.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(DateTimeConversion.formatAsLocalDateTime(cellData.getValue().getStart())));
        appointmentEnd.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(DateTimeConversion.formatAsLocalDateTime(cellData.getValue().getEnd())));
        appointmentCustomerID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerID()));
        appointmentUserID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUserID()));


        // Set up filtered lists
        try {
            customers = MySQLDB.CustomerList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            appointments = MySQLDB.AppointmentList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        observableCustomers = FXCollections.observableList(customers);
        observableAppointments = FXCollections.observableList(appointments);
        filteredCustomers = new FilteredList<>(observableCustomers);
        filteredAppointments = new FilteredList<>(observableAppointments);

        // Set items and event listener callback to change predicate
        customerTableView.setItems(filteredCustomers);
        appointmentTableView.setItems(filteredAppointments);
        radioButtonAll.setOnAction(e -> filteredAppointments.setPredicate(createAllAppointmentPredicate()));
        radioButtonWeek.setOnAction(e -> filteredAppointments.setPredicate(createWeekAppointmentPredicate()));
        radioButtonMonth.setOnAction(e -> filteredAppointments.setPredicate(createMonthAppointmentPredicate()));

        buttonBack.setOnAction(e -> filteredAppointments.setPredicate(decrementTimeFrame()));
        buttonForward.setOnAction(e -> filteredAppointments.setPredicate(incrementTimeFrame()));

        // Set up a timer so we check for upcoming meetings over and over.
        reloadUpcomingMeetingAlert();
        timeline = new Timeline(new KeyFrame(
                javafx.util.Duration.minutes(5.0),
                ae -> {
                    try {
                        reloadUpcomingMeetingAlert();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Checks for upcoming meetings in the next 15
     * minutes and sets an alert on the form.
     * @throws SQLException
     */
    private void reloadUpcomingMeetingAlert() throws SQLException {
        // Alert if appointment within 15 minutes
        Appointment possibleAppointment = new Appointment.AppointmentBuilder().build();
        var userName = CurrentUser.getCurrentUser();
        if (MySQLDB.userAppointmentOverlaps15Minute(userName, LocalDateTime.now(), possibleAppointment)) {
            labelAlert.setText("Appointment \"" + possibleAppointment.getTitle() + "\" within 15 minutes");
        }
        else {
            labelAlert.setText("No meetings within the next 15 minutes.");
        }
    }

    /**
     * Creates a predicate that decrements the time frame
     * depending on whether months or weeks have been selected.
     * @return Predicate that decrements the Time Frame
     */
    private Predicate<Appointment> decrementTimeFrame() {
        if (radioButtonAll.isSelected()) return appointment -> true;
        if (radioButtonMonth.isSelected()) {
            currentWeek = currentWeek.minusMonths(1);
            labelCurrentMonthWeek.setText(currentWeek.getMonth().name() + " " + currentWeek.getYear() +
                    " Week: " + Integer.toString(currentWeek.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
            return createMonthAppointmentPredicate();
        }
        if (radioButtonWeek.isSelected()) {
            currentWeek = currentWeek.minusWeeks(1);
            labelCurrentMonthWeek.setText(currentWeek.getMonth().name() + " " + currentWeek.getYear() +
                    " Week: " + Integer.toString(currentWeek.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
            return createWeekAppointmentPredicate();
        }
        return appointment -> true;
    }

    /**
     * Creates a predicate that increments the time frame
     * depending on whether months or weeks have been selected.
     * @return Predicate that increments the Time Frame.
     */
    private Predicate<Appointment> incrementTimeFrame() {
        if (radioButtonAll.isSelected()) return appointment -> true;
        if (radioButtonMonth.isSelected()) {
            currentWeek = currentWeek.plusMonths(1);
            labelCurrentMonthWeek.setText(currentWeek.getMonth().name() + " " + currentWeek.getYear() +
                    " Week: " + Integer.toString(currentWeek.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
            return createMonthAppointmentPredicate();
        }
        if (radioButtonWeek.isSelected()) {
            currentWeek = currentWeek.plusWeeks(1);
            labelCurrentMonthWeek.setText(currentWeek.getMonth().name() + " " + currentWeek.getYear() +
                    " Week: " + Integer.toString(currentWeek.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
            return createWeekAppointmentPredicate();
        }
        return appointment -> true;
    }

    /**
     * Creates a predicate that filters by months.
     * Starts filtering from currently selected month.
     * @return Predicate that filters by months.
     */
    private Predicate<Appointment> createMonthAppointmentPredicate() {
        return appointment -> {
            if (appointment.getStart().getMonth() == currentWeek.getMonth() &&
                    appointment.getStart().getYear() == currentWeek.getYear()) return true;
            else if (appointment.getEnd().getMonth() == currentWeek.getMonth() &&
                    appointment.getEnd().getYear() == currentWeek.getYear()) return true;
            else return false;
        };
    }

    /**
     * Creates a predicate that filters by weeks.
     * Starts filtering from the currently selected week.
     * @return Predicate that filters by weeks.
     */
    private Predicate<Appointment> createWeekAppointmentPredicate() {
        return appointment -> {
            ZonedDateTime sundayBefore = currentWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            int sundayBeforeDay = sundayBefore.getDayOfMonth();
            ZonedDateTime saturdayAfter = currentWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            int saturdayAfterDay = saturdayAfter.getDayOfMonth();
            int startDay = appointment.getStart().getDayOfMonth();
            int endDay = appointment.getEnd().getDayOfMonth();

            boolean isSameYear = appointment.getStart().getYear() == currentWeek.getYear();
            boolean isSameMonth = appointment.getStart().getMonth() == currentWeek.getMonth();
            boolean isStartInWeek = (startDay >= sundayBeforeDay && startDay <= saturdayAfterDay);
            boolean isEndInWeek = (endDay >= sundayBeforeDay && endDay <= saturdayAfterDay);
            return (isStartInWeek || isEndInWeek) && isSameYear && isSameMonth;
        };
    }

    /**
     * Creates a predicate that does not filter
     * appointments by time.
     * @return Predicate that does not filter.
     */
    private Predicate<Appointment> createAllAppointmentPredicate() {
        return appointment -> true;
    }

    /**
     * Encapsulates the logic for navigating to the
     * appointment form in ADD mode.
     * @param actionEvent Ignored
     * @throws IOException
     * @throws SQLException
     */
    public void buttonAddAppointmentClicked(ActionEvent actionEvent) throws IOException, SQLException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/appointment.fxml"));
        Parent root = loader.load();

        // Set the scene and pass inventory to the controller
        Scene scene = new Scene(root);
        AppointmentController controller = loader.getController();
        controller.setStage();
        controller.setMode(FormMode.ADD);

        // Set the window up
        MainController.primaryStage.setTitle("Add Appointment");
        MainController.primaryStage.setScene(scene);
        MainController.primaryStage.show();
    }

    /**
     * Encapsulates the logic for navigating to the
     * appointment form in UPDATE mode. Passes the
     * appointment to be updated through to the
     * AppointmentController.
     * @param actionEvent Ignored
     * @throws IOException
     * @throws SQLException
     */
    public void buttonUpdateAppointmentClicked(ActionEvent actionEvent) throws IOException, SQLException {
        if (appointmentTableView.getSelectionModel().getSelectedItem() == null) return;

        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/appointment.fxml"));
        Parent root = loader.load();

        // Set the scene and pass inventory to the controller
        Scene scene = new Scene(root);
        AppointmentController controller = loader.getController();
        controller.setStage();
        controller.setAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
        controller.setMode(FormMode.UPDATE);

        // Set the window up
        MainController.primaryStage.setTitle("Update Appointment");
        MainController.primaryStage.setScene(scene);
        MainController.primaryStage.show();
    }

    /**
     * Encapsulates the logic for navigating to the
     * customer form in ADD mode.
     * @param actionEvent Ignored
     * @throws IOException
     * @throws SQLException
     */
    public void buttonAddCustomerClicked(ActionEvent actionEvent) throws IOException, SQLException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/customer.fxml"));
        Parent root = loader.load();

        // Set the scene and pass inventory to the controller
        Scene scene = new Scene(root);
        CustomerController controller = loader.getController();
        controller.setStage();
        controller.setMode(FormMode.ADD);

        // Set the window up
        MainController.primaryStage.setTitle("Add Customer");
        MainController.primaryStage.setScene(scene);
        MainController.primaryStage.show();
    }

    /**
     * Encapsulates the logic for navigating to the
     * customer form in UPDATE mode. Passes the
     * customer to be updated though to the
     * CustomerController
     * @param actionEvent Ignored
     * @throws IOException
     * @throws SQLException
     */
    public void buttonUpdateCustomerClicked(ActionEvent actionEvent) throws IOException, SQLException {
        if (customerTableView.getSelectionModel().getSelectedItem() == null) return;

        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/customer.fxml"));
        Parent root = loader.load();

        // Set the scene and pass inventory to the controller
        Scene scene = new Scene(root);
        CustomerController controller = loader.getController();
        controller.setStage();
        controller.setCustomer(customerTableView.getSelectionModel().getSelectedItem());
        controller.setMode(FormMode.UPDATE);

        // Set the window up
        MainController.primaryStage.setTitle("Update Customer");
        MainController.primaryStage.setScene(scene);
        MainController.primaryStage.show();
    }

    /**
     * Deletes the appointment selected in the table.
     * @param actionEvent Ignored
     * @throws SQLException
     */
    public void buttonDeleteAppointmentClicked(ActionEvent actionEvent) throws SQLException {
        Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the appointment?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            MySQLDB.deleteAppointment(appointment);
            observableAppointments.remove(appointment);
        }
    }

    /**
     * Deletes the customer selected in the table.
     * Warns the user and also deletes all associated
     * appointments.
     * @param actionEvent Ignored
     * @throws SQLException
     */
    public void buttonDeleteCustomerClicked(ActionEvent actionEvent) throws SQLException {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the customer? This will also " +
                "delete all of their appointments.");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            for (var appointment : observableAppointments) {
                if (appointment.getCustomerID() == customer.getCustomerId()) {
                    MySQLDB.deleteAppointment(appointment);
                    observableAppointments.remove(appointment);
                }
            }
            MySQLDB.deleteCustomer(customer);
            observableCustomers.remove(customer);
        }
    }

    /**
     * Encapsulates the logic for navigating to
     * the Total Appointments Report Form.
     * @param actionEvent Ignored
     * @throws IOException
     */
    public void buttonTotalAppointmentsClicked(ActionEvent actionEvent) throws IOException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/totalappointmentsreport.fxml"));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        TotalAppointmentsReportController controller = loader.getController();

        // Set the window up
        Main.primaryStage.setTitle("Total Appointments Report");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }

    /**
     * Encapsulates the logic for navigating to
     * the Schedule Form
     * @param actionEvent Ignored
     * @throws IOException
     */
    public void buttonScheduleClicked(ActionEvent actionEvent) throws IOException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/schedulereport.fxml"));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        ScheduleReportController controller = loader.getController();

        // Set the window up
        Main.primaryStage.setTitle("Schedule Report");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }

    /**
     * Encapsulates the logic for navigating to
     * the Appointment Histogram Form
     * @param actionEvent Ignored
     * @throws IOException
     */
    public void buttonAppointmentHistogramClicked(ActionEvent actionEvent) throws IOException {
        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/appointmenthistogramreport.fxml"));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root);
        AppointmentHistogramReportController controller = loader.getController();

        // Set the window up
        Main.primaryStage.setTitle("Schedule Report");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }
}
