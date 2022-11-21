package com.ShabrinaJSleepDN.jsleep_android.model;

import java.util.ArrayList;
import java.util.Date;

public class Room extends Serializable {
    public int accountId;
    public String name;
    public ArrayList<Date> booked;
    public String address;
    public Price price;
    public City city;
    public int size;
    public BedType bedType;
    public Facility facility;

    public Room(int id) {
        super(id);
    }
}