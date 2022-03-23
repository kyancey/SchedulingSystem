package com.kyancey.scheduling.Data;

/**
 * Singleton that holds info about logged in user
 */
public class CurrentUser {
    private static CurrentUser _instance = new CurrentUser();
    private static String _username;

    /**
     * Private Constructor for singleton
     */
    private CurrentUser() {
    }

    /**
     * @return Stored instance for singleton
     */
    public static CurrentUser getInstance() {
        return _instance;
    }

    /**
     * @param username Current User
     */
    public static void setCurrentUser(String username) {
        _username = username;
    }

    /**
     * @return Current User
     */
    public static String getCurrentUser() {
        return _username;
    }
}
