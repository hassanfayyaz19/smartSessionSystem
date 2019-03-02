package com.example.hassan.smartsession.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.smartsession.MainActivity;
import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.loginResponse;
import com.example.hassan.smartsession.sharedPeference.SharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginAcivity extends AppCompatActivity {

    EditText t1, t2;
    Button textView;
    ProgressDialog progressDialog;
    private static api apiInterface;

    AnimationDrawable anim;
    AssetManager am;


    private static SharePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_login_acivity);
        am = this.getApplicationContext().getAssets();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(100);
        anim.setExitFadeDuration(1000);


        t1 = findViewById(R.id.rollTxt);
        t2 = findViewById(R.id.password);
        textView = findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(loginAcivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating......");

        apiInterface = ApiClient.getApiClient().create(api.class);


        pref = new SharePref(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (!(info != null && info.isConnected())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Internet Connectivity");
            builder.setTitle("Network Error!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    private void login() {
        String roll = t1.getText().toString().trim();
        String password = t2.getText().toString().trim();

        if (roll.isEmpty()) {
            t1.setError("Enter roll num");
            t1.requestFocus();
        }
        if (password.isEmpty()) {
            t2.setError("Enter password");
            t2.requestFocus();
        }
        progressDialog.show();
        disableBtn();
        Call<loginResponse> call = apiInterface.PerformLogin(roll, password);
        call.enqueue(new Callback<loginResponse>() {
            @Override
            public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                try {


                    if (response.body().getResponse().equals("ok")) {
                        pref.writeLoginStatus(true);
                        String roll = response.body().getRoll_no();
                        pref.WriteRollNo(roll);
                        String name = response.body().getName();
                        pref.WriteName(name);
                        String password = response.body().getPassword();
                        String email = response.body().getEmail();
                        String department = response.body().getDepartment();
                        String semester = response.body().getSemester();
                        pref.WriteDepartment(department);
                        pref.WriteSemester(semester);
                        progressDialog.dismiss();
                        Toast.makeText(loginAcivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();

                    } else if (response.body().getResponse().equals("failed")) {
                        progressDialog.dismiss();
                        Toast.makeText(loginAcivity.this, "Login Failed... Try Again", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(loginAcivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<loginResponse> call, Throwable t) {
                Toast.makeText(loginAcivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        t1.setText("");
        t2.setText("");
     enableBtn();


    }


    public void disableBtn() {

        t1.setEnabled(false);
        t2.setEnabled(false);
        textView.setEnabled(false);

    }

    public void enableBtn() {

        t1.setEnabled(true);
        t2.setEnabled(true);
        textView.setEnabled(true);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
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
