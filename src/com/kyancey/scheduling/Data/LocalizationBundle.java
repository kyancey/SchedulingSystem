package com.kyancey.scheduling.Data;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton for referencing the localization bundle
 */
public class LocalizationBundle {
    private static LocalizationBundle _instance = new LocalizationBundle();
    private final ResourceBundle rb;

    /**
     * Private constructor for singleton
     */
    private LocalizationBundle() {
        this.rb = ResourceBundle.getBundle("com.kyancey.scheduling.localization", Locale.getDefault());
    }

    /**
     * @return Stored instance for singleton
     */
    public static LocalizationBundle getInstance() {
        return _instance;
    }

    /**
     * @return Localization bundle
     */
    public ResourceBundle getBundle() {
        return this.rb;
    }
}
