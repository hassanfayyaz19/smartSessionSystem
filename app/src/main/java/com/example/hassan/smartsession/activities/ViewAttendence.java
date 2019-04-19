package com.example.hassan.smartsession.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.adapter.userAdapter;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.Students;
import com.example.hassan.smartsession.model.ViewDataResponse;
import com.example.hassan.smartsession.sharedPeference.SharePref;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.hassan.smartsession.MainActivity.prefConfig;

public class ViewAttendence extends AppCompatActivity {

    SharePref config;
    String roll;
    private RecyclerView recyclerView;
    private ArrayList<Students> data;
    private userAdapter adapter;
    private static api apiInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence);
        progressDialog = new ProgressDialog(ViewAttendence.this,
                R.style.ThemeOverlay_AppCompat_Dialog_Alert);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading......");
        progressDialog.show();
        config = new SharePref(this);
        apiInterface = ApiClient.getApiClient().create(api.class);
        roll = config.readRollNo();
        recyclerView = findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJson();

    }

    private void loadJson() {

        Call<ViewDataResponse> call = apiInterface.getView(roll);
        call.enqueue(new Callback<ViewDataResponse>() {
            @Override
            public void onResponse(Call<ViewDataResponse> call, Response<ViewDataResponse> response) {
                try {
                    ViewDataResponse viewDataResponse = response.body();
                    data = new ArrayList<>(Arrays.asList(viewDataResponse.getUsers()));
                    adapter = new userAdapter(data);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(ViewAttendence.this, "error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ViewDataResponse> call, Throwable t) {
                Toast.makeText(ViewAttendence.this,"Fail: "+ t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                Log.i("Error",t.getMessage());
            }
        });
    }
}
