package com.app.rationcart.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAddress extends Activity implements ApiResponse {

    private EditText txt_name, txt_lastname, txt_mobileno, txt_address1,
            txt_address2, txt_landmark, txt_zipcode, mEtEmail;
    private TextView city;
    private String name, lastname, address1, address2, landmark, zipcode, mobile;
    private Activity context;
    private Button btn_Save;
    private ArrayList<String> cityListNames = new ArrayList<>();
    private ArrayList<String> cityListId = new ArrayList<>();
    private ArrayList<String> zipListNames = new ArrayList<>();
    private ArrayList<String> zipListId = new ArrayList<>();
    private Spinner spinner_city, spinner_zipcode;
    private ArrayAdapter<String> adapterCity;
    private ArrayAdapter<String> adapterzipcode;
    String SelctZipCode = "", SelectCityId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_address);
        context = this;
        init();
        manageHeaderView();
        getCityList();
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
        spinner_city = findViewById(R.id.spinner_city);
        spinner_zipcode = findViewById(R.id.spinner_zipcode);
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
                    if (spinner_city.getSelectedItemPosition() != 0 && spinner_zipcode.getSelectedItemPosition() != 0) {
                        saveAdddress();
                    } else {
                        if (spinner_city.getSelectedItemPosition() == 0) {
                            Toast.makeText(context, "Please select city", Toast.LENGTH_SHORT).show();
                        } else if (spinner_zipcode.getSelectedItemPosition() == 0) {
                            Toast.makeText(context, "Please select zipcodes", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    getLocality(cityListId.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCityList() {

        //http://stackmindz.com/dev/rationcart/api/city

        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CITY;
                new CommonAsyncTaskHashmap(2, context, this).getqueryJsonbjectNoProgress(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocality(String cityId) {

        // http://stackmindz.com/dev/rationcart/api/getpincode?city_id=2
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_PINCODE + "city_id=" + cityId;
                new CommonAsyncTaskHashmap(3, context, this).getqueryJsonbjectNoProgress(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveAdddress() {
        int selectedserviceposition = spinner_zipcode.getSelectedItemPosition();
        SelctZipCode = zipListId.get(selectedserviceposition);
        int selectedservicecityposition = spinner_city.getSelectedItemPosition();
        SelectCityId = cityListId.get(selectedservicecityposition);


        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ADD_ADDRESS + "user_id=" + AppUtils.getUserId(context) + "&address="
                        + txt_address1.getText().toString() + txt_address2.getText().toString() + "&pincode=" + zipListId.get(selectedserviceposition) + "&city_id=" + cityListId.get(selectedserviceposition)
                        + "&email=" + mEtEmail.getText().toString() + "&mobile=" + txt_mobileno.getText().toString()
                        + "&fname=" + txt_name.getText().toString() + "&lname=" + txt_lastname.getText().toString()
                        + "&token=" + AppUtils.getImeiNo(context);
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
            }/* else if (zipcode.length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_valid_zipcode, Toast.LENGTH_SHORT).show();
            } */ else {
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
            }/* else if (zipcode.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, R.string.enter_zipcode, Toast.LENGTH_SHORT).show();
            }*/
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
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray cities = data.getJSONArray("cities");
                    cityListId.clear();
                    cityListNames.clear();
                    cityListId.add("-1");
                    cityListNames.add("Select City");
                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject jo = cities.getJSONObject(i);
                        cityListId.add(jo.getString("cityId"));
                        cityListNames.add(jo.getString("cityName"));
                    }
                    adapterCity = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.textview, cityListNames);
                    spinner_city.setAdapter(adapterCity);
                }
            } else if (method == 3) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray cities = data.getJSONArray("pincode");
                    zipListId.clear();
                    zipListNames.clear();
                    zipListId.add("-1");
                    zipListNames.add("Select Zipcode");
                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject jo = cities.getJSONObject(i);
                        zipListId.add(jo.getString("id"));
                        zipListNames.add(jo.getString("pincode"));
                    }
                    adapterzipcode = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.textview, zipListNames);
                    spinner_zipcode.setAdapter(adapterzipcode);
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
