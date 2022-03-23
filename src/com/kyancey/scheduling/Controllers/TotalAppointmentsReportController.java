package com.kyancey.scheduling.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.ResourceBundle;

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
 * Controls the Total Appointments Report Form
 */
public class TotalAppointmentsReportController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textAreaReport;

    @FXML
    private Button buttonBack;

    /**
     * Navigates the user to the MainForm Form
     * @param event Ignored
     * @throws IOException
     */
    @FXML
    void onButtonBack(ActionEvent event) throws IOException {
        loadMainForm();
    }

    /**
     * Initializes the Total Appointments Report Form
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
        HashMap<Month, Integer> monthHash = new HashMap<>();
        var appointmentTypeHash = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder();

        // Initialize Month Hash
        for (var month: Month.values()) {
            monthHash.put(month, 0);
        }

        for (var appointment: appointments) {
            // Increment month count
            monthHash.put(appointment.getStart().getMonth(), monthHash.get(appointment.getStart().getMonth()) + 1);

            // Increment appointment type count
            if (appointmentTypeHash.containsKey(appointment.getType())) {
                appointmentTypeHash.put(appointment.getType(), appointmentTypeHash.get(appointment.getType()) + 1);
            }
            else {
                appointmentTypeHash.put(appointment.getType(), 1);
            }
        }

        sb.append("Appointment Count by Month\n\n");
        for (var month: Month.values()) {
            sb.append(month);
            sb.append(": ");
            sb.append(monthHash.get(month));
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Appointment Count by Type\n\n");
        for (var appointmentType: appointmentTypeHash.keySet()) {
            sb.append(appointmentType);
            sb.append(": ");
            sb.append(appointmentTypeHash.get(appointmentType));
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Encapsulates the logic to navigate to
     * the MainForm Form
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
