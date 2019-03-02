package com.example.hassan.smartsession.model;

import com.google.gson.annotations.SerializedName;

public class loginResponse {


    @SerializedName("response")
    private String response;

    @SerializedName("name")
    private String name;



    @SerializedName("roll_no")
    private String roll_no;

    @SerializedName("email")
    private String email;

    @SerializedName("department")
    private String department;

    @SerializedName("semester")
    private String semester;

    @SerializedName("password")
    private String password;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public String getPassword() {
        return password;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public String getResponse() {
        return response;
    }


}
