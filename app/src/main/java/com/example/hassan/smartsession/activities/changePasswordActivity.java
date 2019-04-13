package com.example.hassan.smartsession.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.changePasswordResponse;
import com.example.hassan.smartsession.navi;
import com.example.hassan.smartsession.sharedPeference.SharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class changePasswordActivity extends AppCompatActivity {

    private static SharePref pref;
    private static api apiInterface;
    String old_pass, new_pass, conf_pass;
    EditText t1, t2, t3;
    AnimationDrawable anim;
    AssetManager am;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        am = this.getApplicationContext().getAssets();
        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.lay);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(100);
        anim.setExitFadeDuration(1000);


        pref = new SharePref(this);
        apiInterface = ApiClient.getApiClient().create(api.class);

        t1 = findViewById(R.id.editText_oldpass);
        t2 = findViewById(R.id.editText_newpass);
        t3 = findViewById(R.id.editText_confpass);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void update(View view) {
        try {
            String roll = pref.readRollNo();
            String pas = pref.readPassword();
            old_pass = t1.getText().toString().trim();
            new_pass = t2.getText().toString().trim();
            conf_pass = t3.getText().toString().trim();

            if (old_pass.equals(pas)) {
                if (new_pass.equals(conf_pass)) {
                    progressBar.setVisibility(View.VISIBLE);
                    Call<changePasswordResponse> call = apiInterface.changePass(new_pass, roll);
                    call.enqueue(new Callback<changePasswordResponse>() {
                        @Override
                        public void onResponse(Call<changePasswordResponse> call, Response<changePasswordResponse> response) {
                            if (response.body().getMessage().equals("Success")) {
                                pref.writePassword(new_pass);
                                Toast.makeText(changePasswordActivity.this, "Password Updated", Toast.LENGTH_LONG).show();
                            } else if (response.body().getMessage().equals("Failure")) {
                                Toast.makeText(changePasswordActivity.this, "Error..!! try Again ", Toast.LENGTH_LONG).show();
                            } else if (response.body().getMessage().equals("Error")) {
                                Toast.makeText(changePasswordActivity.this, "Server not Responding", Toast.LENGTH_LONG).show();
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<changePasswordResponse> call, Throwable t) {
                            Toast.makeText(changePasswordActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    t1.setText("");
                    t2.setText("");
                    t3.setText("");
                } else {
                    Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }


            } else {
                t1.setError("Enter Correct Password");
                t1.requestFocus();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void back(View view) {
        startActivity(new Intent(this, navi.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
