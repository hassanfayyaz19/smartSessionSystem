package com.example.hassan.smartsession.sharedPeference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hassan.smartsession.R;

public class SharePref {
    public static final String smartApp = "smartSession1";
    public static final String loginStatus = "loginStatus";
    public static final String rollNO = "rollNO";
    public static final String UserName = "default";
    public static final String password = "password";
    public static final String email = "email";
    public static final String department = "department";
    public static final String semester = "semester";
    public static final String mac="mac";

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharePref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(smartApp, Context.MODE_PRIVATE);
    }


    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(loginStatus, status);
        editor.commit();
    }

    public boolean readLoginStatus() {
        return sharedPreferences.getBoolean(loginStatus, false);
    }

    public void WriteRollNo(String rollNp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(rollNO, rollNp);
        editor.commit();
    }

    public String readRollNo() {
        return sharedPreferences.getString(rollNO, "user");
    }

    public void WriteName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UserName, name);
        editor.commit();
    }

    public String readName() {
        return sharedPreferences.getString(UserName, "name");
    }

    public void WriteDepartment(String dep)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(department, dep);
        editor.commit();
    }
    public String readDepartment() {
        return sharedPreferences.getString(department, "depart");
    }

    public void WriteSemester(String s)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(semester, s);
        editor.commit();
    }

    public String readSemester() {
        return sharedPreferences.getString(semester, "semes");
    }

    public void WriteMacAddress(String mac)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mac, mac);
        editor.commit();
    }

    public String readMac() {
        return sharedPreferences.getString(mac, "mac");
    }


}
