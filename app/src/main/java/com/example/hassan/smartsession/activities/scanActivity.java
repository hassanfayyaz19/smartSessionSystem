package com.example.hassan.smartsession.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
    String department, semester, subject, status, name, roll, mac, userDep, userSemester;
    private static api apiInterface;
    SharePref pref;
    static String macAdress;


    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String lattitude, longitude,facultyCode;
    double distance;
    String qrlat, qrLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        apiInterface = ApiClient.getApiClient().create(api.class);
        pref = new SharePref(this);
        getMacAddr();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        getLatLong();

    }

    @Override
    public void handleResult(Result result) {
        try {
            String r = result.getText();
        //    Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();

        // Get instance of Vibrator from current Context
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            // Vibrate for 400 milliseconds
            v.vibrate(400);
            Log.i("data", r);
            split = r.split("\\s+");
            name = pref.readName();
            roll = pref.readRollNo();
            userDep = pref.readDepartment();
            userSemester = pref.readSemester();
            mac = "default";
            department = split[0];
            semester = split[1];
            subject = split[2];
            status = split[3];
            String id = split[4];
            qrlat = split[5];
            qrLong = split[6];
            facultyCode=split[7];

            /* Toast.makeText(this, lat+" and " + longi, Toast.LENGTH_SHORT).show();
             */
            if (id.equals("12345")) {
                //    Toast.makeText(this, department+semester+subject+status, Toast.LENGTH_SHORT).show();
                if (userDep.equals(department) && userSemester.equals(semester)) {
                    calculateLatLong();
                    if (distance >= 100) {
                        Toast.makeText(this, "Out of Range", Toast.LENGTH_SHORT).show();
                    } else {
                        Call<attendenceResponse> call = apiInterface.createuser(name, roll, subject,facultyCode, department, semester, status, macAdress);
                        call.enqueue(new Callback<attendenceResponse>() {
                            @Override
                            public void onResponse(Call<attendenceResponse> call, Response<attendenceResponse> response) {
                                if (response.body().getResponse().equals("ok")) {
                                    try {
                                        Toast.makeText(scanActivity.this, "Attendence uploaded", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(scanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else if (response.body().getResponse().equals("failed")) {
                                    Toast.makeText(scanActivity.this, "Attendence Already inserted", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<attendenceResponse> call, Throwable t) {
                                Toast.makeText(scanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(this, "Department did'nt match", Toast.LENGTH_LONG).show();
                }
            } else {
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
                    macAdress = res1.toString();
                }
                return macAdress;
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(scanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (scanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(scanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else {

                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void getLatLong() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void calculateLatLong() {


        if (lattitude == "" && longitude == "") {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        } else {
            Location startPoint = new Location("locationA");
            double lat = Double.parseDouble(lattitude);
            double log = Double.parseDouble(longitude);
            startPoint.setLatitude(lat);
            startPoint.setLongitude(log);

            Location endPoint = new Location("location2");
            double lat2 = Double.parseDouble(qrlat);
            double log2 = Double.parseDouble(qrLong);
            endPoint.setLatitude(lat2);
            endPoint.setLongitude(log2);

            distance = startPoint.distanceTo(endPoint);
            double km = distance / 1000;

              /*  Toast.makeText(this, "Distance in meters : " + distance + " AND " + "In KM : " + km, Toast.LENGTH_SHORT).show();
Log.i("error",lattitude+" ;"+longitude +" and "+qrlat+qrLong);*/
           /* Toast.makeText(this, lattitude + " & " + longitude + " and " + qrlat + " & " + qrLong, Toast.LENGTH_LONG).show();
   */     }
    }
}
