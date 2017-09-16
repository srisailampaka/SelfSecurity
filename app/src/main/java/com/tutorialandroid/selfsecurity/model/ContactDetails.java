package com.tutorialandroid.selfsecurity.model;

import java.io.Serializable;

/**
 * Created by VenkatPc on 5/25/2017.
 */
public class ContactDetails implements Serializable{
    private String name;
    private String number;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
