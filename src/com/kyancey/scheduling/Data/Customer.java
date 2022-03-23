package com.kyancey.scheduling.Data;

import java.time.LocalDateTime;

/**
 * Represents a Customer record in the database.
 * Customer ID should be set to zero if this is a new record.
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String postalCode;
    private String phone;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String updatedBy;
    private int divisionID;
    private int countryID;

    /**
     * Constructor that creates Customer from builder object
     * @param builder Builder object
     */
    private Customer(CustomerBuilder builder) {
        this.customerId = builder.customerId;
        this.customerName = builder.customerName;
        this.customerAddress = builder.customerAddress;
        this.postalCode = builder.postalCode;
        this.phone = builder.phone;
        this.created = builder.created;
        this.createdBy = builder.createdBy;
        this.lastUpdated = builder.lastUpdated;
        this.updatedBy = builder.updatedBy;
        this.divisionID = builder.divisionID;
        this.countryID = builder.countryID;
    }

    /**
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId Customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return Customer Name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName Customer Name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return Customer Address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress Customer Address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return Postal Code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode Postal Code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return Phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone Phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Created on date
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @param created Created on date
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * @return Person who created customer record
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy Person who created customer record
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return Last updated date
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated Last updated date
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return Person who last updated record
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy Perosn who last updated record
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return Division ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID Division ID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return Country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID Country ID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Implements Builder Pattern to create Customer objects
     */
    public static class CustomerBuilder {
        private int customerId;
        private String customerName;
        private String customerAddress;
        private String postalCode;
        private String phone;
        private LocalDateTime created;
        private String createdBy;
        private LocalDateTime lastUpdated;
        private String updatedBy;
        private int divisionID;
        private int countryID;

        /**
         * Default constructor
         */
        public CustomerBuilder() {
        }

        /**
         * @param customerId Customer ID
         * @return Builder
         */
        public CustomerBuilder customerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * @param customerName Customer name
         * @return Builder
         */
        public CustomerBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        /**
         * @param customerAddress Customer Address
         * @return Builder
         */
        public CustomerBuilder customerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
            return this;
        }

        /**
         * @param postalCode Postal Code
         * @return Builder
         */
        public CustomerBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * @param phone Phone Number
         * @return Builder
         */
        public CustomerBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * @param created Created on date
         * @return Builder
         */
        public CustomerBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        /**
         * @param createdBy Person who created the record
         * @return Builder
         */
        public CustomerBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        /**
         * @param lastUpdated Last updated date
         * @return Builder
         */
        public CustomerBuilder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * @param updatedBy Person who updated the record
         * @return Builder
         */
        public CustomerBuilder lastUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        /**
         * @param divisionID Division ID
         * @return Builder
         */
        public CustomerBuilder divisionID(int divisionID) {
            this.divisionID = divisionID;
            return this;
        }

        /**
         * @param countryID Country ID
         * @return
         */
        public CustomerBuilder countryID(int countryID) {
            this.countryID = countryID;
            return this;
        }

        /**
         * Creates a customer object from builder variables
         * @return Customer Object created by builder
         */
        public Customer build() {
            Customer customer = new Customer(this);
            return customer;
        }
    }
}
