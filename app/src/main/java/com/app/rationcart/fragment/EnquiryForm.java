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

public class EnquiryForm extends BaseFragment implements ApiResponse {

    private EditText add_street, add_aptnumber,  addstreetmesssage,
            txt_address2, txt_landmark, txt_zipcode, mEtEmail;
    private TextView city;
    private String street, aptnumber, messagae;
    private Activity context;
    private Button btn_Save;
    private ArrayList<String> cityListNames = new ArrayList<>();
    private ArrayList<String> cityListId = new ArrayList<>();
    private Spinner spinner_city,spinner_zipcode;
    private ArrayAdapter<String> adapterCity;
    private View view;
    private ArrayAdapter<String> adapterzipcode;
    private ArrayList<String> zipListNames = new ArrayList<>();
    private ArrayList<String> zipListId = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_customerform, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        init(view);
        getCityList();

        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().changeMenuHeader("Profile", false);
    }

    private void init(View view) {
        add_street = view.findViewById(R.id.add_street);
        add_aptnumber = view.findViewById(R.id.add_aptnumber);
        addstreetmesssage = view.findViewById(R.id.addstreetmesssage);

        spinner_city = view.findViewById(R.id.spinner_city);
        spinner_zipcode = view.findViewById(R.id.spinner_zipcode);
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

                    if (spinner_city.getSelectedItemPosition() != 0) {
                        custmerenquiry();
                    } else {
                        Toast.makeText(context, "Please select city", Toast.LENGTH_SHORT).show();
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


    private void custmerenquiry() {

      //  http://stackmindz.com/dev/rationcart/api/addUserdata?user_id=2&street_address=dadas&city_id=10&area=43&pincode=43&=hbhf
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CUSTOMERENQ + "user_id=" + AppUtils.getUserId(context) + "&street_address="
                        + add_street.getText().toString() + "&apartment_no=" + add_aptnumber.getText().toString() + "&message=" + addstreetmesssage.getText().toString()

                        + "&city=" + cityListId.get(spinner_city.getSelectedItemPosition())+ "&area=" + zipListId.get(spinner_zipcode.getSelectedItemPosition());
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
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

                }
            } else if (method == 4) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");

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
