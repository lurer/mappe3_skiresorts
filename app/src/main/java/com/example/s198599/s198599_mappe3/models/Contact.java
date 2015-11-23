package com.example.s198599.s198599_mappe3.models;

/**
 * Created by espen on 11/1/15.
 */
public class Contact {

    private String address;
    private String zip_code;
    private String city;
    private String phone_servicecenter;
    private String phone_skipatrol;
    private String call_number;
    private String email;

    public Contact(String address, String zip_code, String city, String phone_servicecenter, String phone_skipatrol, String call_number, String email) {
        this.address = address;
        this.zip_code = zip_code;
        this.city = city;
        this.phone_servicecenter = phone_servicecenter;
        this.phone_skipatrol = phone_skipatrol;
        this.call_number = call_number;
        this.email = email;
    }

    public Contact() {
    }

    public String getAddress() {
        return address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getCity() {
        return city;
    }

    public String getPhone_servicecenter() {
        return phone_servicecenter;
    }

    public String getPhone_skipatrol() {
        return phone_skipatrol;
    }

    public String getCall_number() {
        return call_number;
    }

    public String getEmail() {
        return email;
    }
}
