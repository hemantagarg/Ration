package com.app.rationcart.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAddress extends Activity implements ApiResponse {

    private EditText txt_name, txt_lastname, txt_mobileno, txt_address1,
            txt_address2, txt_landmark, txt_zipcode, mEtEmail;
    private TextView city;
    private String name, lastname, address1, address2, landmark, zipcode, mobile;
    private Activity context;
    private Button btn_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_address);
        context = this;
        init();
        manageHeaderView();
        setListener();
        txt_mobileno.setText(AppUtils.getUserMobile(context));
        mEtEmail.setText(AppUtils.getUseremail(context));
    }

    private void init() {
        txt_address1 = findViewById(R.id.add_addressline1);
        txt_address2 = findViewById(R.id.add_addressline2);
        txt_landmark = findViewById(R.id.add_landmark);
        txt_mobileno = findViewById(R.id.add_phone_number);
        txt_name = findViewById(R.id.add_name);
        txt_lastname = findViewById(R.id.add_lastname);

        txt_zipcode = findViewById(R.id.add_zipcode);
        mEtEmail = findViewById(R.id.mEtEmail);
        btn_Save = findViewById(R.id.btn_Save);

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(this, null, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Add New Address");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);
        HeaderViewManager.getInstance().setLogoView(false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                finish();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setListener() {
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidLoginDetails()) {
                    saveAdddress();
                }
            }
        });

    }

    private void saveAdddress() {

        //http://stackmindz.com/dev/rationcart/api/addAddress?user_id=2&address=dadas&
        // city_id=3&area=2&pincode=5545545
        // &email=sfsfs@gmail.com&mobile=8778778787&fname=dd&lname=dd&token=
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ADD_ADDRESS + "user_id=" + AppUtils.getUserId(context) + "&address="
                        + txt_address1.getText().toString() + "&pincode=" + txt_zipcode.getText().toString()
                        + "&email=" + mEtEmail.getText().toString() + "&mobile=" + txt_mobileno.getText().toString()
                        + "&fname=" + txt_name.getText().toString() + "&lname=" + txt_lastname.getText().toString() + "&token=" + AppUtils.getImeiNo(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbjectNoProgress(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        name = txt_name.getText().toString();
        lastname = txt_lastname.getText().toString();
        mobile = txt_mobileno.getText().toString();
        address1 = txt_address1.getText().toString();
        address2 = txt_address2.getText().toString();
        landmark = txt_landmark.getText().toString();
        zipcode = txt_zipcode.getText().toString();

        if (!name.equalsIgnoreCase("") && !lastname.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("") && !address1.equalsIgnoreCase("")
                && !landmark.equalsIgnoreCase("") && !zipcode.equalsIgnoreCase("")) {

            if (mobile.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else if (zipcode.length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_valid_zipcode, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_name, Toast.LENGTH_SHORT).show();
            } else if (lastname.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_lastname, Toast.LENGTH_SHORT).show();
            } else if (mobile.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_mobileno, Toast.LENGTH_SHORT).show();
            } else if (address1.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_address, Toast.LENGTH_SHORT).show();
            } else if (landmark.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_landmark, Toast.LENGTH_SHORT).show();
            } else if (zipcode.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_zipcode, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    setResult(512);

                    txt_name.setText("");
                    txt_lastname.setText("");
                    txt_address2.setText("");
                    txt_zipcode.setText("");
                    txt_address1.setText("");
                    txt_mobileno.setText("");
                    txt_landmark.setText("");

                    finish();

                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();
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
