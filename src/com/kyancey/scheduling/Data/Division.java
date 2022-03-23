package com.kyancey.scheduling.Data;

/**
 * Represents a division
 */
public class Division {
    private int id;
    private int country_id;
    private String name;

    /**
     * @return Divison ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Division ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Country ID
     */
    public int getCountry_id() {
        return country_id;
    }

    /**
     * @param country_id Country ID
     */
    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    /**
     * @return Division Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Division Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String representation of division
     */
    public String toString() {
        return name;
    }
}
