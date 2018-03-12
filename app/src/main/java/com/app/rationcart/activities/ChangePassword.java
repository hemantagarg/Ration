package com.app.rationcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.HeaderViewClickListener;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hemanta on 02-08-2017.
 */

public class ChangePassword extends AppCompatActivity  {

    public static ChangePassword changePassword;
    private Button btnSubmit;
    private EditText edtold_password, edt_newpassword, edtconfirmpassword;
    private Activity mActivity;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mActivity = ChangePassword.this;
        init();
        //  setListener();
        manageHeaderView();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                       // submitRequest();
                    } else {
                        Toast.makeText(mActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
            }
        });
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(mActivity, null, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, getResources().getString(R.string.change_password));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);
        HeaderViewManager.getInstance().setLogoView(false);
        HeaderViewManager.getInstance().setProgressLoader(false, false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

 /*   public void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            HashMap<String, String> hm = new HashMap<>();
            // user_id,leave_type_id,leave_date_from,leave_date_to, latitude,longitude,location,remark
            hm.put("user_id", AppUtils.getUserId(mActivity));
            hm.put("current_pwd", edtold_password.getText().toString());
            hm.put("new_pwd", edt_newpassword.getText().toString());
            hm.put("confirm_pwd", edtconfirmpassword.getText().toString());

            //  http://dev.stackmindz.com/sky/api/apply-leave
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CHNAGE_PASSWORD;
            new CommonAsyncTask(1, mActivity, this).getqueryJson(url, hm, Request.Method.POST);


        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }*/


    private void init() {

        edtold_password = (EditText) findViewById(R.id.edtold_password);
        edt_newpassword = (EditText) findViewById(R.id.edt_newpassword);
        edtconfirmpassword = (EditText) findViewById(R.id.edtconfirmpassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                      //  submitRequest();
                    } else {
                        Toast.makeText(mActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
            }
        });

    }

   /* @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {
                if (response.getString("status").equalsIgnoreCase("1")) {

                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_SHORT).show();
                    AppUtils.setUserId(mActivity, "");
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }*/

}


