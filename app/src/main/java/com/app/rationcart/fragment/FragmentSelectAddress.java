package com.app.rationcart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.AddAddress;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.activities.OrderSummary;
import com.app.rationcart.adapter.AdapterAddressList;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelAddressDetail;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSelectAddress extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentSelectAddress fragmentHome;
    private Activity context;
    private ArrayList<ModelAddressDetail> mAddressList = new ArrayList<>();
    private RecyclerView mRecyclerAddress;
    private View view;
    private String TAG = FragmentSelectAddress.class.getSimpleName();
    private int selectedPosition = 0;
    private TextView mTvTotalAmount;
    private RelativeLayout rl_bottom;
    private AdapterAddressList adapterAddressList;
    private int selectedAddressPosition = 0;
    private String finalquantity = "";
    private String totalPrice = "";

    public static FragmentSelectAddress getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentSelectAddress();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().manageFooterVisibitlity(false);
        DashboardActivity.getInstance().manageHeaderVisibitlity(false);
        getAddress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_address, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
        manageHeaderView();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Select Delivery Address");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(true, R.drawable.add_white);
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
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Intent intent = new Intent(context, AddAddress.class);
                startActivityForResult(intent, 21);
            }
        };
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
            totalPrice = b.getString("price");
            finalquantity = b.getString("finalquantity");
            mTvTotalAmount.setText(totalPrice);
        }
    }


    private void setListener() {
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSummary();
            }
        });
    }

    private void orderSummary() {
        boolean isAddressSelected = false;
        for (int i = 0; i < mAddressList.size(); i++) {
            if (mAddressList.get(i).getSelection_position() == 1) {
                isAddressSelected = true;
                selectedAddressPosition = i;
                break;
            }
        }
        if (isAddressSelected) {
            Intent in_time = new Intent(getActivity(), OrderSummary.class);
            in_time.putExtra("total_items", finalquantity);
            in_time.putExtra("total_quantity", finalquantity);
            in_time.putExtra("sub_total", totalPrice);
            in_time.putExtra("discountAmt", "");
            in_time.putExtra("isCouponApplied", false);
            in_time.putExtra("couponCode", "");
            in_time.putExtra("grand_total", totalPrice);
            in_time.putExtra("shipping_charges", "");
            in_time.putExtra("addressId", mAddressList.get(selectedAddressPosition).getAddress_id());
            in_time.putExtra("address", mAddressList.get(selectedAddressPosition).getAddress());
            in_time.putExtra("city", mAddressList.get(selectedAddressPosition).getName()
                    + "  " + mAddressList.get(selectedAddressPosition).getMobileno());
            in_time.putExtra("code", mAddressList.get(selectedAddressPosition).getCity()
                    + "  " + mAddressList.get(selectedAddressPosition).getZipcode());
            startActivity(in_time);
        } else {
            Toast.makeText(context, "Please select atleast one address", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddress() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/getAddress?user_id=2
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_ADDRESS + "user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAddress() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                // http://stackmindz.com/dev/rationcart/api/deleteaddress?address_id=7
                String url = JsonApiHelper.BASEURL + JsonApiHelper.DELETE_ADDRESS + "address_id=" + mAddressList.get(selectedPosition).getAddress_id();
                new CommonAsyncTaskHashmap(2, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setData(JSONObject data) {
        try {
            mAddressList.clear();
            JSONArray userAddress = data.getJSONArray("user_address");
            for (int i = 0; i < userAddress.length(); i++) {
                JSONObject jo = userAddress.getJSONObject(i);

                ModelAddressDetail modelAddressDetail = new ModelAddressDetail();

                modelAddressDetail.setName(jo.getString("fname") + " " + jo.getString("lname"));
                modelAddressDetail.setAddress(jo.getString("address"));
                modelAddressDetail.setCity(jo.getString("city"));
                modelAddressDetail.setLandmark(jo.getString("area"));
                modelAddressDetail.setMobileno(jo.getString("mobile"));
                modelAddressDetail.setAddress_id(jo.getString("id"));
                modelAddressDetail.setEmail(jo.getString("email"));
                modelAddressDetail.setZipcode(jo.getString("pincode"));
                mAddressList.add(modelAddressDetail);
            }
            adapterAddressList = new AdapterAddressList(context, this, mAddressList);
            mRecyclerAddress.setAdapter(adapterAddressList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        mRecyclerAddress = view.findViewById(R.id.mRecyclerAddress);
        rl_bottom = view.findViewById(R.id.rl_bottom);
        mRecyclerAddress.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerAddress.setNestedScrollingEnabled(false);
        mTvTotalAmount = view.findViewById(R.id.mTvTotalAmount);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            selectedPosition = position;
            showConfirmtion();

        }
        if (flag == 2) {
            for (int i = 0; i < mAddressList.size(); i++) {
                Log.e("size_alldetail", mAddressList.size() + "");
                mAddressList.get(i).setSelection_position(0);
            }
            Log.e("clickposition", mAddressList.get(position) + "");
            mAddressList.get(position).setSelection_position(1);
            adapterAddressList.notifyDataSetChanged();
        }
    }

    private void showConfirmtion() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Delete Address");

        alertDialog.setMessage("Are you sure you want to delete this address?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        deleteAddress();

                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    setData(data);

                }
            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    mAddressList.remove(selectedPosition);
                    adapterAddressList.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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
