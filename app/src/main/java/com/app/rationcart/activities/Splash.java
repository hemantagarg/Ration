package com.app.rationcart.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.utils.AppUtils;
import com.app.rationcart.utils.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements ApiResponse {

    private Context context;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};
    String latitude = "0.0", longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        context = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        GPSTracker gps = new GPSTracker(context);
        if (gps.isGPSEnabled) {
            latitude = gps.getLatitude() + "";
            longitude = gps.getLongitude() + "";
            getHomeData();
        } else {
            gps.showSettingsAlert();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void getHomeData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/category.php?lat=&lon=
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CATEGORIES + "lat=" + latitude + "&lon=" + longitude;
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbjectNoProgress(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setHomeCategories(context, data.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Splash.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Splash.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 500);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
