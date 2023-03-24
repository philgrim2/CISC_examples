package edu.harrisburg.cis349.customerlist;

import org.json.JSONException;
import org.json.JSONObject;

public class Customer {
    protected String name;
    protected String address;
    protected String phone;


    public Customer(JSONObject jsonData) throws JSONException {
        this.setName(jsonData.getString("name"));
        this.setAddress(jsonData.getString("address"));
        this.setPhone(jsonData.getString("phone"));
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
