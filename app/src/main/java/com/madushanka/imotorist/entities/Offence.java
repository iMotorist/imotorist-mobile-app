package com.madushanka.imotorist.entities;

/**
 * Created by madushanka on 10/19/17.
 */

public class Offence {

    String id, description, fine, dip;

    public Offence(String id, String description, String fine, String dip) {
        this.id = id;
        this.description = description;
        this.fine = fine;
        this.dip = dip;
    }

    public Offence() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }
}


