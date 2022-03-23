package com.kyancey.scheduling.Data;

/**
 * Represents contact information
 */
public class Contact {
    private int contactID;
    private String name;
    private String email;

    /**
     * @return Contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID Contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return Contact Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Contact Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Contact Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email Contact Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String representation of Contact
     */
    public String toString() {
        return this.name;
    }
}
