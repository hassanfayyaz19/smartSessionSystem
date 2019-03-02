package com.example.hassan.smartsession;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.smartsession.activities.ViewAttendence;
import com.example.hassan.smartsession.activities.loginAcivity;
import com.example.hassan.smartsession.activities.scanActivity;
import com.example.hassan.smartsession.sharedPeference.SharePref;

public class MainActivity extends AppCompatActivity {

    public static SharePref prefConfig;
    ImageButton textView;
    TextView textView1;

    private static final int REQUEST_CAMERARESULT = 201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.logoutButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        prefConfig = new SharePref(this);
        if (prefConfig.readLoginStatus()==false) {
            Intent intent=new Intent(this,loginAcivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
String name=prefConfig.readName();
        textView1=findViewById(R.id.textView3);
        textView1.setText(name);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPref();
            }
        });
    }


public void scan(View view)
{
startActivity(new Intent(this,scanActivity.class));
}


    private void clearPref() {
        prefConfig.writeLoginStatus(false);
        prefConfig.WriteRollNo("user");
        prefConfig.WriteSemester("sems");
        prefConfig.WriteName("name");
        prefConfig.WriteDepartment("depart");
        startActivity(new Intent(this, loginAcivity.class));
        finish();
    }



    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    public void viewAtt(View view)
    {
        startActivity(new Intent(this,ViewAttendence.class));
    }


}
