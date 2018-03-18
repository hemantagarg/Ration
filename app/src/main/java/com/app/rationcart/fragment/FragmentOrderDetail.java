package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterOderProductList;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelOrderDetail;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentOrderDetail extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentOrderDetail fragmentHome;
    private Activity context;
    private ArrayList<ModelOrderDetail> mAddressList = new ArrayList<>();
    private RecyclerView mRecyclerAddress;
    private View view;
    private String TAG = FragmentOrderDetail.class.getSimpleName();
    private AdapterOderProductList adapterOderList;
    private TextView text_order_id, text_items_ordered, text_ordered_date, text_product_name, text_subtotal, text_shipping, text_grand_total;
    private TextView text_address1, text_address2, text_name1, text_zipcode1, text_paymentmode, text_order_info;


    public static FragmentOrderDetail getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentOrderDetail();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().manageFooterVisibitlity(false);
        DashboardActivity.getInstance().manageHeaderVisibitlity(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_detail, container, false);
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
        getOrders();
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
            String mainData = b.getString("data");
            try {
                JSONObject jsonObject = new JSONObject(mainData);
                setData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Order Detail");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.add_white);
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

            }
        };
    }

    private void setListener() {
    }

    private void getOrders() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/getAddress?user_id=2
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_ORDERS + "user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
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
            JSONArray userAddress = data.getJSONArray("order_details");
            for (int i = 0; i < userAddress.length(); i++) {
                JSONObject jo = userAddress.getJSONObject(i);

                ModelOrderDetail modelAddressDetail = new ModelOrderDetail();

                modelAddressDetail.setProductId(jo.getString("productId"));
                modelAddressDetail.setProductPrice(jo.getString("productPrice"));
                modelAddressDetail.setProductQuanitity(jo.getString("productQuanitity"));
                modelAddressDetail.setUnit_value(jo.getString("unit_value"));
                modelAddressDetail.setProductName(jo.getString("productName"));
                modelAddressDetail.setImage(jo.getString("image"));
                mAddressList.add(modelAddressDetail);
            }
            adapterOderList = new AdapterOderProductList(context, this, mAddressList);
            mRecyclerAddress.setAdapter(adapterOderList);

            text_order_id.setText("Order #" + data.getString("orderId") + " - " + data.getString("status"));
            text_order_info.setText("Order Information: " + data.getString("email"));
            text_items_ordered.setText("Items ordered: " + data.getString("quantity"));
            text_ordered_date.setText("Order Date: " + data.getString("mobile"));
            text_subtotal.setText("SubTotal:  " + "Rs " + data.getString("total_amount"));
            text_shipping.setText("Shipping & Handling:  " + "Rs " + "0");
            text_grand_total.setText("Grand total:  " + "Rs " + data.getString("total_amount"));

            text_paymentmode.setText("Cash On Delivery");

            text_address1.setText(data.getString("fname") + " " + data.getString("lname"));
            text_address2.setText(data.getString("address"));
            text_zipcode1.setText("T: " + data.getString("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        mRecyclerAddress = view.findViewById(R.id.mRecyclerAddress);
        mRecyclerAddress.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerAddress.setNestedScrollingEnabled(false);
        text_address1 = view.findViewById(R.id.address1);
        text_address2 = view.findViewById(R.id.address2);
        text_order_id = view.findViewById(R.id.text_order_id);
        text_items_ordered = view.findViewById(R.id.text_items_ordered);
        text_ordered_date = view.findViewById(R.id.text_ordered_date);
        text_product_name = view.findViewById(R.id.text_product_name);
        text_subtotal = view.findViewById(R.id.text_subtotal);
        text_shipping = view.findViewById(R.id.text_shipping);
        text_grand_total = view.findViewById(R.id.text_grand_total);
        text_name1 = view.findViewById(R.id.name1);
        text_zipcode1 = view.findViewById(R.id.zipcode1);
        text_paymentmode = view.findViewById(R.id.text_paymentmode);
        text_order_info = view.findViewById(R.id.text_order_info);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {

        }
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
