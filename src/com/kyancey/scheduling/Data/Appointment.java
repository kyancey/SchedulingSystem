package com.kyancey.scheduling.Data;

import java.time.LocalDateTime;

/**
 * Represents an Appointment record in the database.
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * @param builder Constructor that creates appointment from builder object
     */
    private Appointment(AppointmentBuilder builder) {
        this.appointmentId = builder.appointmentId;
        this.title = builder.title;
        this.description = builder.description;
        this.location = builder.location;
        this.type = builder.type;
        this.start = builder.start;
        this.end = builder.end;
        this.created = builder.created;
        this.createdBy = builder.createdBy;
        this.lastUpdated = builder.lastUpdated;
        this.lastUpdatedBy = builder.lastUpdatedBy;
        this.customerID = builder.customerID;
        this.userID = builder.userID;
        this.contactID = builder.contactID;
    }

    /**
     * @return Appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId Appointment ID
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location Location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Time for start of appointment
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start Time for start of appointment
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return Time for end of appointment
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end Time for end of appointment
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return Time when the appointment was created
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @param created Time when the appointment was created
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * @return Person who created the appointment
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy Person who created the appointment
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return Time when the appointment was last updated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated Time when the appointment was last updated
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return Person who last updated the appointment
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy Person who last updated the appointment
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return Customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID Customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return User ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID User ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

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
     * Implements Builder Pattern to create Appointment objects
     */
    public static class AppointmentBuilder {
        private int appointmentId;
        private String title;
        private String description;
        private String location;
        private String type;
        private LocalDateTime start;
        private LocalDateTime end;
        private LocalDateTime created;
        private String createdBy;
        private LocalDateTime lastUpdated;
        private String lastUpdatedBy;
        private int customerID;
        private int userID;
        private int contactID;

        /**
         * Default constructor
         */
        public AppointmentBuilder() {
        }

        /**
         * @param appointmentId Appointment ID
         * @return Builder
         */
        public AppointmentBuilder appointmentId(int appointmentId) {
            this.appointmentId = appointmentId;
            return this;
        }

        /**
         * @param title Title
         * @return Builder
         */
        public AppointmentBuilder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * @param description Description
         * @return Builder
         */
        public AppointmentBuilder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * @param location Location
         * @return Builder
         */
        public AppointmentBuilder location(String location) {
            this.location = location;
            return this;
        }

        /**
         * @param type Type
         * @return Builder
         */
        public AppointmentBuilder type(String type) {
            this.type = type;
            return this;

        }

        /**
         * @param start Start time
         * @return Builder
         */
        public AppointmentBuilder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        /**
         * @param end End time
         * @return Builder
         */
        public AppointmentBuilder end(LocalDateTime end) {
            this.end = end;
            return this;
        }

        /**
         * @param created Creation time
         * @return Builder
         */
        public AppointmentBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        /**
         * @param createdBy Person who created the appointment
         * @return Builder
         */
        public AppointmentBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        /**
         * @param lastUpdated Last time appointment was updated
         * @return Builder
         */
        public AppointmentBuilder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * @param lastUpdatedBy Person who last updated the appointment
         * @return Builder
         */
        public AppointmentBuilder lastUpdatedBy(String lastUpdatedBy) {
            this.lastUpdatedBy = lastUpdatedBy;
            return this;
        }

        /**
         * @param customerId Customer ID
         * @return Builder
         */
        public AppointmentBuilder customerID(int customerId) {
            this.customerID = customerId;
            return this;
        }

        /**
         * @param userId User ID
         * @return Builder
         */
        public AppointmentBuilder userID(int userId) {
            this.userID = userId;
            return this;
        }

        /**
         * @param contactId Contact ID
         * @return Builder
         */
        public AppointmentBuilder contactID(int contactId) {
            this.contactID = contactId;
            return this;
        }

        /**
         * Generates the appointment object based on stored static data.
         * @return Generated appointment object
         */
        public Appointment build() {
            Appointment appointment = new Appointment(this);
            return appointment;
        }
    }
}