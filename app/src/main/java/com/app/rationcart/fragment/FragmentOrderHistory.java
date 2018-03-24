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
import com.app.rationcart.adapter.AdapterOderList;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelOrderDetail;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentOrderHistory extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentOrderHistory fragmentHome;
    private Activity context;
    private ArrayList<ModelOrderDetail> mAddressList = new ArrayList<>();
    private RecyclerView mRecyclerAddress;
    private View view;
    private String TAG = FragmentOrderHistory.class.getSimpleName();
    private AdapterOderList adapterOderList;
    private TextView mTvNoProduct;

    public static FragmentOrderHistory getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentOrderHistory();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   DashboardActivity.getInstance().manageFooterVisibitlity(false);
        DashboardActivity.getInstance().manageHeaderVisibitlity(false);*/
        DashboardActivity.getInstance().changeMenuHeader("Order History", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        //  manageHeaderView();
        setListener();
        getOrders();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Order History");
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
            JSONArray userAddress = data.getJSONArray("order");
            for (int i = 0; i < userAddress.length(); i++) {
                JSONObject jo = userAddress.getJSONObject(i);

                ModelOrderDetail modelAddressDetail = new ModelOrderDetail();
                modelAddressDetail.setDataJson(jo.toString());
                modelAddressDetail.setOrderId(jo.getString("orderId"));
                modelAddressDetail.setTotal_amount(jo.getString("total_amount"));
                modelAddressDetail.setQuantity(jo.getString("quantity"));
                modelAddressDetail.setStatus(jo.getString("status"));
                modelAddressDetail.setAddress(jo.getString("address"));
                modelAddressDetail.setEmail(jo.getString("email"));
                modelAddressDetail.setFname(jo.getString("fname") + " " + jo.getString("lname"));
                modelAddressDetail.setMobile(jo.getString("mobile"));
                mAddressList.add(modelAddressDetail);
            }
            adapterOderList = new AdapterOderList(context, this, mAddressList);
            mRecyclerAddress.setAdapter(adapterOderList);

            if (mAddressList.size() > 0) {
                mTvNoProduct.setVisibility(View.GONE);
            } else {
                mTvNoProduct.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        mRecyclerAddress = view.findViewById(R.id.mRecyclerAddress);
        mRecyclerAddress.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerAddress.setNestedScrollingEnabled(false);
        mTvNoProduct = view.findViewById(R.id.mTvNoProduct);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            Bundle bundle = new Bundle();
            bundle.putString("data", mAddressList.get(position).getDataJson());
            FragmentOrderDetail orderDetail = new FragmentOrderDetail();
            orderDetail.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, orderDetail, true);
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
