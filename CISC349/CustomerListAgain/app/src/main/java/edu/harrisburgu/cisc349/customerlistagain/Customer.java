package edu.harrisburgu.cisc349.customerlistagain;

import java.util.List;

public class Customer {
    protected String name;
    protected String address;
    protected String phone;
    protected List<String> comments;

    public Customer(String name, String address, String phone)
    {
        this.name = name;
        this.address = address;
        this.phone = phone;
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
