package com.example.hassan.smartsession.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.sharedPeference.SharePref;

public class profileActivity extends AppCompatActivity {

    TextView t,t1,t2,t3,t4,t5;

    SharePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        t=findViewById(R.id.profileTitle);
        t1=findViewById(R.id.profileName);
        t2=findViewById(R.id.profileroll);
        t3=findViewById(R.id.profiledepartment);
        t4=findViewById(R.id.profileemail);
        t5=findViewById(R.id.profilesemester);

        pref=new SharePref(this);

        t.setText(pref.readName());
        t1.setText(pref.readName());
        t2.setText(pref.readRollNo());
        t3.setText(pref.readDepartment());
        t4.setText(pref.readEmail());
        t5.setText(pref.readSemester());

    }
}
