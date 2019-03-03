package com.example.hassan.smartsession.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.signupResponse;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signupAcitvity extends AppCompatActivity {

    EditText t1, t2, t3, t4, t5, t6, t7;
    private static api apiInterface;
    static String macAdress;
    String mac;
    ProgressDialog progressDialog;

    AnimationDrawable anim;
    AssetManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_signup_acitvity);
        am = this.getApplicationContext().getAssets();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(100);
        anim.setExitFadeDuration(1000);



        t1 = findViewById(R.id.nameTxt1);
        t2 = findViewById(R.id.rollTxt1);
        t3 = findViewById(R.id.departTxt1);
        t4 = findViewById(R.id.semesterTxt1);
        t5 = findViewById(R.id.emailTxt);
        t6 = findViewById(R.id.passwordTxt);
        t7 = findViewById(R.id.passwordConfirmTxt);
        mac = getMacAddr();
        apiInterface = ApiClient.getApiClient().create(api.class);

        progressDialog = new ProgressDialog(signupAcitvity.this,
                R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Laoding......");

    }


    public void createNew(View view) {
        String name = t1.getText().toString().trim().toUpperCase();
        String roll = t2.getText().toString().trim();
        String depart = t3.getText().toString().trim().toUpperCase();
        String semester = t4.getText().toString().trim();
        String email = t5.getText().toString().trim();
        String password = t6.getText().toString().trim();
        String confrmpassword = t7.getText().toString().trim();

        if (name.isEmpty()) {
            t1.setError("Enter Name");
            t1.requestFocus();
        }

        if (roll.isEmpty()) {
            t2.setError("Enter roll num");
            t2.requestFocus();
        }

        if (depart.isEmpty()) {
            t3.setError("Enter Department");
            t3.requestFocus();
        }
        if (semester.isEmpty()) {
            t4.setError("Enter semester");
            t4.requestFocus();
        }
        if (email.isEmpty()) {
            t5.setError("Enter email");
            t5.requestFocus();
        }
        if (password.isEmpty()) {
            t6.setError("Enter password");
            t6.requestFocus();
        }
        if (confrmpassword.isEmpty()) {
            t7.setError("Enter password");
            t7.requestFocus();
        }
        if (password.equals(confrmpassword)) {

            if (confrmpassword.length() >= 6) {
                progressDialog.show();
                Call<signupResponse> call = apiInterface.signUp(name, roll, depart, semester, email, confrmpassword, mac);
                call.enqueue(new Callback<signupResponse>() {
                    @Override
                    public void onResponse(Call<signupResponse> call, Response<signupResponse> response) {
                        if(response.body().getResponse().equals("same"))
                        {
                            Toast.makeText(signupAcitvity.this, "User can't Create Account twice on same device", Toast.LENGTH_SHORT).show();
                       progressDialog.dismiss();
                        }
                       else if (response.body().getResponse().equals("ok")) {
                            Toast.makeText(signupAcitvity.this, "Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), loginAcivity.class));
                            finish();
                            t1.setText("");
                            t2.setText("");
                            t3.setText("");
                            t4.setText("");
                            t5.setText("");
                            t6.setText("");
                            t7.setText("");

                        } else if (response.body().getResponse().equals("failed")) {
                            Toast.makeText(signupAcitvity.this, "Error Try Again..!!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<signupResponse> call, Throwable t) {
                        Toast.makeText(signupAcitvity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            } else {
                Toast.makeText(this, "Minimum password have 6 characters", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
        }


    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    macAdress = res1.toString();
                }
                return macAdress;
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}
