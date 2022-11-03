package com.example.project3;

import javafx.collections.ObservableArray;

/**
 * The Location enum defines the locations of the gym. A location
 * consists of the city, zip code, and county.
 * @author Jackson Lee, Serena Zeng
 */
public enum Location {
    BRIDGEWATER("Bridgewater", "08807", "Somerset"),
    EDISON("Edison", "08837", "Middlesex"),
    FRANKLIN("Franklin", "08873", "Somerset"),
    PISCATAWAY("Piscataway","08854", "Middlesex"),
    SOMERVILLE("Somerville", "08876", "Somerset"),
    ONLINE(null, null, null);

    private final String city;
    private final String county;
    private final String zipCode;

    /**
     * Generate a Location given the city, zipcode, and county
     * @param city      city of gym location
     * @param zipCode   zip code of gym location
     * @param county    county of gym location
     */
    Location(String city, String zipCode, String county){
        this.city = city;
        this.zipCode = zipCode;
        this.county = county;
    }

    /**
     * Returns county of gym location
     * @return  county name
     */
    public String getCounty() {
        return county;
    }

    /**
     * Returns zip code of gym location
     * @return  zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Get a Location object with the same city as the
     * given city parameter
     * @param city  city of Location to be obtained
     * @return Location object of the city, null if no Location with given city
     */
    public static Location toLocation(String city){
        for (Location location : values()) {
            if (city.equalsIgnoreCase(location.name())) {
                return location;
            }
        }
        return null;
    }

    /**
     * Formats Location as a String that can be printed
     * @return  String consisting of Location's city, zip code, and county
     */
    @Override
    public String toString(){
        return city.toUpperCase() + ", " + zipCode +
                ", " + county.toUpperCase();
    }

    /**
     * Check if two locations are the same
     * @param loc Location to be compared to
     * @return true if same city name, false otherwise
     */
    public boolean equals(Location loc){
        if(loc == null){
            return false;
        }
        if(this.name().equals(loc.name())){
            return true;
        }
        return false;
    }
}
