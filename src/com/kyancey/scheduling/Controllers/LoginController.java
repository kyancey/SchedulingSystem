package com.kyancey.scheduling.Controllers;

import com.kyancey.scheduling.Main;
import com.kyancey.scheduling.Data.CurrentUser;
import com.kyancey.scheduling.Data.FileLogger;
import com.kyancey.scheduling.Data.LocalizationBundle;
import com.kyancey.scheduling.Entities.MySQLDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controls the login subform.
 */
public class LoginController {

    ZoneId myZone;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label zoneIDLabel;

    /**
     * Handles the logic of logging the user in.
     * Raises alerts if user fails to login.
     *
     * @param event Ignored
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onLoginClick(ActionEvent event) throws IOException, SQLException {
        var rb = LocalizationBundle.getInstance().getBundle();

        try {
            var user = getUserInfo(usernameField.getText());

            if (this.passwordField.getText().equals(user.get("password"))) {
                FileLogger.log(user.get("username") + " successfully logged in.");
                CurrentUser.setCurrentUser(user.get("username"));
                this.loadMainForm();
            } else {
                FileLogger.log(user.get("username") + " login failure.");
                Alert alert = new Alert(Alert.AlertType.WARNING, rb.getString("login_error"));
                alert.showAndWait();
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().equals("Non-existent user")) {
                FileLogger.log("Username does not exist.");
                Alert alert = new Alert(Alert.AlertType.WARNING, rb.getString("user_does_not_exist"));
                alert.showAndWait();
            }
        }

    }

    /**
     * Get's the username and password of a specified username.
     *
     * @param username Username for which information is required.
     * @return
     * @throws SQLException
     */
    private Map<String, String> getUserInfo(String username) throws SQLException {
        var result = new HashMap<String, String>();
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT User_Name, Password from users WHERE User_Name = ?");
        statement.setString(1, usernameField.getText());
        var rs = statement.executeQuery();

        if (!rs.next()) {
            throw new SQLException("Non-existent user");
        }

        result.put("username", rs.getString(1));
        result.put("password", rs.getString(2));

        return result;
    }

    /**
     * Encapsulates the logic of navigating to the Main Form.
     *
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
     * Initializes the login subform.
     */
    @FXML
    void initialize() {
        this.myZone = ZoneId.systemDefault();
        this.zoneIDLabel.setText(this.myZone.toString());
    }
}
