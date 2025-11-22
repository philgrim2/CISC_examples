package edu.harrisburg.cis349.walmartlist;

import org.json.JSONException;
import org.json.JSONObject;

public class Store {
    protected String name;
    protected String address;
    protected String phoneNumber;
    protected String city;

    public Store(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        this.address = object.getString("street_address");
        this.phoneNumber = object.getString("phone_number_1");
        this.city = object.getString("city");
    }

    public Store()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
