package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
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

public class UserProfile extends BaseFragment implements ApiResponse {

    private EditText txt_name, txt_lastname, txt_mobileno, txt_address1,
            txt_address2, txt_landmark, txt_zipcode, mEtEmail;
    private TextView city;
    private String name, lastname, address1, address2, landmark, zipcode, mobile;
    private Activity context;
    private Button btn_Save;
    private ArrayList<String> cityListNames = new ArrayList<>();
    private ArrayList<String> cityListId = new ArrayList<>();
    private Spinner spinner_city;
    private ArrayAdapter<String> adapterCity;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        init(view);
        manageHeaderView();
        getCityList();

        setListener();
        txt_mobileno.setText(AppUtils.getUserMobile(context));
        mEtEmail.setText(AppUtils.getUseremail(context));
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().changeMenuHeader("Profile", false);
    }

    private void init(View view) {
        txt_address1 = view.findViewById(R.id.add_addressline1);
        txt_address2 = view.findViewById(R.id.add_addressline2);
        txt_landmark = view.findViewById(R.id.add_landmark);
        txt_mobileno = view.findViewById(R.id.add_phone_number);
        txt_name = view.findViewById(R.id.add_name);
        txt_lastname = view.findViewById(R.id.add_lastname);
        spinner_city = view.findViewById(R.id.spinner_city);
        txt_zipcode = view.findViewById(R.id.add_zipcode);
        mEtEmail = view.findViewById(R.id.mEtEmail);
        btn_Save = view.findViewById(R.id.btn_Save);

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Profile");
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
                context.onBackPressed();
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
                    if (spinner_city.getSelectedItemPosition() != 0) {
                        saveAdddress();
                    } else {
                        Toast.makeText(context, "Please select city", Toast.LENGTH_SHORT).show();
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

    private void getProfile() {

        //http://stackmindz.com/dev/rationcart/api/getProfile?user_id=2
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_PROFILE + "user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(4, context, this).getqueryJsonbjectNoProgress(url, null, Request.Method.GET);
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

        //http://stackmindz.com/dev/rationcart/api/update-profile?name=sumit
        // &email=sumit@gmail.com&mobile=7894562351&address=Noida&city=2&pincode=110044&user_id=12
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.UPDATE_PROFILE + "user_id=" + AppUtils.getUserId(context) + "&address="
                        + txt_address1.getText().toString() + txt_address2.getText().toString() + "&pincode=" + txt_zipcode.getText().toString()
                        + "&email=" + mEtEmail.getText().toString() + "&mobile=" + txt_mobileno.getText().toString()
                        + "&name=" + txt_name.getText().toString() + txt_lastname.getText().toString()
                        + "&token=" + AppUtils.getImeiNo(context) + "&city=" + cityListId.get(spinner_city.getSelectedItemPosition());
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
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
            } else if (zipcode.length() < 5) {
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
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    context.onBackPressed();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray cities = data.getJSONArray("cities");

                    cityListId.add("-1");
                    cityListNames.add("Select City");
                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject jo = cities.getJSONObject(i);
                        cityListId.add(jo.getString("cityId"));
                        cityListNames.add(jo.getString("cityName"));
                    }
                    adapterCity = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.textview, cityListNames);
                    spinner_city.setAdapter(adapterCity);
                    getProfile();
                }
            } else if (method == 3) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");

                }
            } else if (method == 4) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");

                    txt_name.setText(data.getString("fname"));
                    txt_address1.setText(data.getString("address"));
                    txt_zipcode.setText(data.getString("pincode"));
                    String cityId = data.getString("city");
                    if (cityListId.contains(cityId)) {
                        spinner_city.setSelection(cityListId.indexOf(cityId));
                    }

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
