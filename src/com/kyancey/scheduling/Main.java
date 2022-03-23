package com.kyancey.scheduling;

import com.kyancey.scheduling.Controllers.MainController;
import com.kyancey.scheduling.Data.LocalizationBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage;
    /**
     * Initializes app.
     * @param primaryStage Primary Stage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // Store primary stage for other controllers to use
        Main.primaryStage = primaryStage;

        // Set locale for testing purposes
        //Locale.setDefault(new Locale("fr"));

        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(LocalizationBundle.getInstance().getBundle());
        loader.setLocation(getClass().getResource("fxml/main.fxml"));
        Parent root = loader.load();


        // Set the scene and controller
        Scene scene = new Scene(root);
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        // Set the window up
        primaryStage.setTitle("Scheduling System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
