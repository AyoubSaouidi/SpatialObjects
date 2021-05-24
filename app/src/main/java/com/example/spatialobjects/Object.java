package com.example.spatialobjects;

import java.sql.Timestamp;

public class Object {

    private String id;
    private String designation;
    private String image;
    private String longitude;
    private String latitude;
    private String altitude;
    private String description;
    private String added_date;
    private String updated_date;

    public Object(String id,String designation, String image, String longitude, String latitude, String altitude, String description,String added_date,String updated_date) {
        this.id = id;
        this.designation = designation;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.description = description;
        this.added_date = added_date;
        this.updated_date = updated_date;
    }
    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getImage() {
        return image;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getDescription() {
        return description;
    }

    public String getAdded_date() {
        return added_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
