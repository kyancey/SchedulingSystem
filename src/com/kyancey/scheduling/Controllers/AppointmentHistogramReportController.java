package com.kyancey.scheduling.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
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
 * Controls the Appointment Histogram Report Form
 */
public class AppointmentHistogramReportController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea textAreaReport;

    @FXML
    private Button buttonBack;

    /**
     * Returns user to main form.
     * @param event
     * @throws IOException
     */
    @FXML
    void onButtonBack(ActionEvent event) throws IOException {
        loadMainForm();
    }

    /**
     * Initializes form and loads report.
     * @throws SQLException
     */
    @FXML
    void initialize() throws SQLException {
        textAreaReport.setText(reportText());
    }

    /**
     * This function generates the text of the report.
     * It technically already loads the text into the
     * textarea. So returning the text is superfluous.
     * However the structure was kept the same
     * to match the other reports.
     * @return Text of report
     * @throws SQLException
     */
    private String reportText() throws SQLException {
        var appointments = MySQLDB.AppointmentList();
        HashMap<DayOfWeek, Integer> dayHash = new HashMap<>();

        for (var day: java.time.DayOfWeek.values()) {
            dayHash.put(day, 0);
        }

        for (var appointment: appointments) {
            dayHash.put(appointment.getStart().getDayOfWeek(), dayHash.get(appointment.getStart().getDayOfWeek()) + 1);
        }

        textAreaReport.setText("Appointment by Day of Week Histogram\n\n");

        for (var day: java.time.DayOfWeek.values()) {
            textAreaReport.appendText(String.format("%14.14s", day.name() + ": "));
            for (int i = 0; i < dayHash.get(day); i++) {
                textAreaReport.appendText("-");
            }
            textAreaReport.appendText("\n");
        }

        return textAreaReport.getText();
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
