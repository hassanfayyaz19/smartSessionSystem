package com.example.hassan.smartsession.activities;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hassan.smartsession.R;
import com.example.hassan.smartsession.api.ApiClient;
import com.example.hassan.smartsession.api.api;
import com.example.hassan.smartsession.model.attendenceResponse;
import com.example.hassan.smartsession.sharedPeference.SharePref;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class scanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    ZXingScannerView scannerView;
    String split[];
    String department, semester,subject, status, name, roll, mac,userDep,userSemester;
    private static api apiInterface;
    SharePref pref;
   static String macAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        apiInterface = ApiClient.getApiClient().create(api.class);
        pref = new SharePref(this);
        getMacAddr();

    }

    @Override
    public void handleResult(Result result) {
        try {
            String r = result.getText();
// Get instance of Vibrator from current Context
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
            v.vibrate(400);
            Log.i("data", r);
            split = r.split("\\s+");
            name = pref.readName();
            roll = pref.readRollNo();
            userDep=pref.readDepartment();
            userSemester=pref.readSemester();
            mac = "default";
            department = split[0];
            semester = split[1];
            subject=split[2];
            status = split[3];
            String  id = split[4];
            if(id.equals("12345"))
            {
                //    Toast.makeText(this, department+semester+subject+status, Toast.LENGTH_SHORT).show();
                if(userDep.equals(department) && userSemester.equals(semester))
                {
                    Call<attendenceResponse> call = apiInterface.createuser(name, roll,subject, department, semester, status, macAdress);
                    call.enqueue(new Callback<attendenceResponse>() {
                        @Override
                        public void onResponse(Call<attendenceResponse> call, Response<attendenceResponse> response) {
                            if (response.body().getResponse().equals("ok")) {
                                try {
                                    Toast.makeText(scanActivity.this, "Attendence uploaded", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(scanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else if(response.body().getResponse().equals("failed")){
                                Toast.makeText(scanActivity.this, "Attendence Already inserted", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<attendenceResponse> call, Throwable t) {
                            Toast.makeText(scanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    Toast.makeText(this, "Department did'nt match", Toast.LENGTH_LONG).show();
                }
            }else
                {
                    Toast.makeText(this, "QR is Invalid", Toast.LENGTH_SHORT).show();
                }



        } catch (Exception e) {
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
        }
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
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
                    macAdress=res1.toString();
                }
                return macAdress;
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
}
