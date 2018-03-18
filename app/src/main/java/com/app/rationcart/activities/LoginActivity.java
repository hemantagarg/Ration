package com.app.rationcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ApiResponse {

    private Activity mActivity;
    private EditText edtEmail, edtPassword;
    private Button btn_login;
    private TextView createAccount, forgotPassword, signup;
    String latitude = "0.0", longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mActivity = LoginActivity.this;
        initViews();
        setListener();

    }

    private void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEmail.getText().toString().equalsIgnoreCase("") && !edtPassword.getText().toString().equalsIgnoreCase("")) {
                    if (AppUtils.isEmailValid(edtEmail.getText().toString())) {
                        loginUser();
                    } else {
                        edtEmail.setError(getString(R.string.enter_valid_emailid));
                        edtEmail.requestFocus();
                    }
                } else {
                    if (edtEmail.getText().toString().equalsIgnoreCase("")) {
                        edtEmail.setError(getString(R.string.enter_email));
                        edtEmail.requestFocus();
                    } else if (edtPassword.getText().toString().equalsIgnoreCase("")) {
                        edtPassword.setError(getString(R.string.enter_password));
                        edtPassword.requestFocus();
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, SignupActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ForgotActivity.class));
            }
        });
    }


    private void loginUser() {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            try {
              /*  http://stackmindz.com/dev/rationcart/api/login?mobile=amitg1370@gmail.com
                // &password=admin&gcm=123456&device_type=1*/
                String url = JsonApiHelper.BASEURL + JsonApiHelper.LOGIN + "mobile=" + edtEmail.getText().toString()
                        + "&password=" + edtPassword.getText().toString() + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity)
                        + "&device_type=" + AppConstant.DEVICE_TYPE;
                new CommonAsyncTaskHashmap(1, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        createAccount = (TextView) findViewById(R.id.createAccount);
        signup = (TextView) findViewById(R.id.signup);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.setUserId(mActivity, data.getString("UserId"));
                    AppUtils.setUserName(mActivity, data.getString("Name"));
                    AppUtils.setUserMobile(mActivity, data.getString("Mobile"));
                    AppUtils.setUseremail(mActivity, data.getString("Email"));
                    AppUtils.setUserImage(mActivity, data.getString("ProfilePic"));
                    AppUtils.setAuthToken(mActivity, data.getString("DeviceToken"));
                    Intent intent = new Intent(mActivity, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
