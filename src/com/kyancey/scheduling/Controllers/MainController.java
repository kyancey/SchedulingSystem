package com.kyancey.scheduling.Controllers;

import javafx.stage.Stage;

/**
 * Controls the Main Form.
 */
public class MainController {
    public static Stage primaryStage;

    /**
     * Sets the reference for the primary stage.
     * @param primaryStage Stage reference
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
