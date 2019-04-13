package com.example.hassan.smartsession.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.signupResponse;
import com.example.hassan.smartsession.model.viewDepartmentResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.R.layout.simple_spinner_item;

public class signupAcitvity extends AppCompatActivity {

    EditText t1, t2, t3, t5, t6, t7;
    private static api apiInterface;
    static String macAdress;
    String mac;
    ProgressDialog progressDialog;

    AnimationDrawable anim, anim2;
    AssetManager am;

    Spinner spinnerSemester, spinnerDepartment;
    String semester, department;
    ArrayList<String> semesterArray;
    ArrayAdapter<String> arrayAdapter;
    ConstraintLayout layout;
    private ArrayList<viewDepartmentResponse> goodModelArrayList;
    private ArrayList<String> playerNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitvity);
        am = this.getApplicationContext().getAssets();


        t1 = findViewById(R.id.nameTxt1);
        t2 = findViewById(R.id.rollTxt1);
         layout=(ConstraintLayout)findViewById(R.id.container);

        /* t4 = findViewById(R.id.semesterTxt1);*/
        t5 = findViewById(R.id.emailTxt);
        t6 = findViewById(R.id.passwordTxt);
        t7 = findViewById(R.id.passwordConfirmTxt);
        mac = getMacAddr();
        apiInterface = ApiClient.getApiClient().create(api.class);


        progressDialog = new ProgressDialog(signupAcitvity.this,
                R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Laoding......");

        semesterSpinner();
        spinnerDepartment = findViewById(R.id.departTxt1);
        fetchJSON();
    }


    public void createNew(View view) {
        String name = t1.getText().toString().trim().toUpperCase();
        String roll = t2.getText().toString().trim();

        /*String semester = t4.getText().toString().trim();*/
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


        if (semester == "") {
            Toast.makeText(this, "select semester", Toast.LENGTH_SHORT).show();
            spinnerSemester.requestFocus();
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


        if (semester != "") {
            if (password.equals(confrmpassword)) {

                if (confrmpassword.length() >= 6) {
                    progressDialog.show();
                    layout.setVisibility(View.INVISIBLE);
                    Call<signupResponse> call = apiInterface.signUp(name, roll, department, semester, email, confrmpassword, mac);
                    call.enqueue(new Callback<signupResponse>() {
                        @Override
                        public void onResponse(Call<signupResponse> call, Response<signupResponse> response) {
                            if (response.body().getResponse().equals("same")) {
                                progressDialog.dismiss();
                                layout.setVisibility(View.VISIBLE);
                                Toast.makeText(signupAcitvity.this, "User can't Create Account twice on same device", Toast.LENGTH_SHORT).show();

                            } else if (response.body().getResponse().equals("ok")) {
                                progressDialog.dismiss();
                                layout.setVisibility(View.VISIBLE);
                                Toast.makeText(signupAcitvity.this, "Successful", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), loginAcivity.class));
                                finish();
                                t1.setText("");
                                t2.setText("");
                                t5.setText("");
                                t6.setText("");
                                t7.setText("");

                            } else if (response.body().getResponse().equals("failed")) {
                                progressDialog.dismiss();
                                layout.setVisibility(View.VISIBLE);
                                Toast.makeText(signupAcitvity.this, "Error Try Again..!!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<signupResponse> call, Throwable t) {
                            Toast.makeText(signupAcitvity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            layout.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    Toast.makeText(this, "Minimum password have 6 characters", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
            }
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


    }

    @Override
    protected void onPause() {
        super.onPause();


    }


    public void semesterSpinner() {
        spinnerSemester = findViewById(R.id.semesterTxt1);

        semesterArray = new ArrayList<>();
        semesterArray.add("Semester");
        semesterArray.add("1");
        semesterArray.add("2");
        semesterArray.add("3");
        semesterArray.add("4");
        semesterArray.add("5");
        semesterArray.add("6");
        semesterArray.add("7");
        semesterArray.add("8");

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, semesterArray);
        spinnerSemester.setAdapter(arrayAdapter);
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester = semesterArray.get(i);
                if (i == 0) {
                    semester = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


    private void fetchJSON() {
        progressDialog.show();
        progressDialog.setTitle("Please Wait..");

        layout.setVisibility(View.INVISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.Base_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        api api = retrofit.create(api.class);


        Call<String> call = api.getDepartment();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);
                        progressDialog.dismiss();
                        layout.setVisibility(View.VISIBLE);
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
                progressDialog.dismiss();
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void spinJSON(String response) {

        try {

            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equals("true")) {

                goodModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    viewDepartmentResponse spinnerModel = new viewDepartmentResponse();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    spinnerModel.setDepartment(dataobj.getString("department"));


                    goodModelArrayList.add(spinnerModel);

                }

                for (int i = 0; i < goodModelArrayList.size(); i++) {
                    playerNames.add(goodModelArrayList.get(i).getDepartment().toString());
                }


                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(signupAcitvity.this, R.layout.spinner_item, playerNames);
                /* spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/ // The drop down view
                spinnerDepartment.setAdapter(spinnerArrayAdapter);

                spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        department = playerNames.get(i);
                        }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
