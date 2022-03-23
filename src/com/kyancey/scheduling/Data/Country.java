package com.kyancey.scheduling.Data;

/**
 * Represents Country information
 */
public class Country {
    private int id;
    private String name;

    /**
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Country Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Country Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String representation of Country
     */
    public String toString() {
        return name;
    }
}