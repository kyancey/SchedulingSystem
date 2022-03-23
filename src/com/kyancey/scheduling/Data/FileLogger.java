package com.kyancey.scheduling.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Utility class for logging to a file
 */
public class FileLogger {
    private static String filename = "login_activity.txt";

    /**
     * Takes a message and saves it to a file.
     * The filename is set in the static filename
     * class variable.
     * @param message Message to be logged.
     * @throws IOException
     */
    public static void log(String message) throws IOException {
        var logger = new FileWriter(FileLogger.filename, true);
        logger.write(LocalDateTime.now().toString() + " ");
        logger.write(message + "\n");
        logger.close();
    }
}
