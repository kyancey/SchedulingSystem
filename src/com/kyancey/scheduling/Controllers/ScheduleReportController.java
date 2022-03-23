package com.kyancey.scheduling.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.kyancey.scheduling.Data.Appointment;
import com.kyancey.scheduling.Data.Contact;
import com.kyancey.scheduling.Entities.MySQLDB;
import com.kyancey.scheduling.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Controls the Schedule Report Form
 */
public class ScheduleReportController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textAreaReport;

    @FXML
    private Button buttonBack;

    /**
     * Navigates the user back to MainForm Form
     * @param event Ignored
     * @throws IOException
     */
    @FXML
    void onButtonBack(ActionEvent event) throws IOException {
        loadMainForm();
    }

    /**
     * Initializes the Schedule Report Form.
     * @throws SQLException
     */
    @FXML
    void initialize() throws SQLException {
        textAreaReport.setText(reportText());
    }

    /**
     * Generates the report text
     * @return Report text
     * @throws SQLException
     */
    private String reportText() throws SQLException {
        var appointments = MySQLDB.AppointmentList();
        HashMap<Contact, ArrayList<Appointment>> contactAppointmentHash = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (var appointment: MySQLDB.AppointmentList()) {
            Contact contact = MySQLDB.getContact(appointment.getContactID());
            if (!contactAppointmentHash.containsKey(contact)) {
                contactAppointmentHash.put(contact, new ArrayList<>());
            }
            contactAppointmentHash.get(contact).add(appointment);
        }

        for (var contact: contactAppointmentHash.keySet()) {
            sb.append("Contact ID: ");
            sb.append(contact.getContactID());
            sb.append("\n\n");

            for (var appointment: contactAppointmentHash.get(contact)) {
                sb.append("\tAppointment ID: ");
                sb.append(appointment.getAppointmentId());
                sb.append("\n");
                sb.append("\tTitle: ");
                sb.append(appointment.getTitle());
                sb.append("\n");
                sb.append("\tType: ");
                sb.append(appointment.getType());
                sb.append("\n");
                sb.append("\tDescription: ");
                sb.append(appointment.getDescription());
                sb.append("\n");
                sb.append("\tStart: ");
                sb.append(appointment.getStart());
                sb.append("\n");
                sb.append("\tEnd: ");
                sb.append(appointment.getEnd());
                sb.append("\n");
                sb.append("\tCustomer ID: ");
                sb.append(appointment.getCustomerID());
                sb.append("\n\n\n");
            }
        }

        return sb.toString();
    }

    /**
     * Encapsulates the logic to navigate to
     * the MainForm Form.
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
