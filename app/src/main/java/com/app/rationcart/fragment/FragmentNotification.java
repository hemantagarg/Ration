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
import com.app.rationcart.adapter.AdapterNotificatonList;
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

public class FragmentNotification extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentNotification fragmentHome;
    private Activity context;
    private ArrayList<ModelOrderDetail> mNotificationList = new ArrayList<>();
    private RecyclerView mRecyclerAddress;
    private View view;
    private TextView mTvNoProduct;
    private String TAG = FragmentNotification.class.getSimpleName();
    private AdapterNotificatonList adapterNotificatonList;

    public static FragmentNotification getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentNotification();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        //  DashboardActivity.getInstance().manageFooterVisibitlity(false);
        //DashboardActivity.getInstance().manageHeaderVisibitlity(false);
        DashboardActivity.getInstance().changeMenuHeader("Notification", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notificaton, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        // manageHeaderView();
        setListener();
        getNotificatons();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Notification");
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

    private void getNotificatons() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/notification-list.php?user_id=10&token=1234567890
                String url = JsonApiHelper.BASEURL + JsonApiHelper.NOTIFICATION_LIST + "user_id=" + AppUtils.getUserId(context)
                        + "&token=" + AppUtils.getImeiNo(context);
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
            mNotificationList.clear();
            JSONArray userAddress = data.getJSONArray("notifications");
            for (int i = 0; i < userAddress.length(); i++) {
                JSONObject jo = userAddress.getJSONObject(i);

                ModelOrderDetail modelAddressDetail = new ModelOrderDetail();
                modelAddressDetail.setDataJson(jo.toString());
                modelAddressDetail.setNotificationId(jo.getString("NotificationId"));
                modelAddressDetail.setMessage(jo.getString("Message"));
                modelAddressDetail.setCreateDate(jo.getString("CreateDate"));
                mNotificationList.add(modelAddressDetail);
            }
            adapterNotificatonList = new AdapterNotificatonList(context, this, mNotificationList);
            mRecyclerAddress.setAdapter(adapterNotificatonList);
            if (mNotificationList.size() > 0) {
                mTvNoProduct.setVisibility(View.GONE);
            } else {
                mTvNoProduct.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        mRecyclerAddress = view.findViewById(R.id.list_products);
        mRecyclerAddress.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerAddress.setNestedScrollingEnabled(false);
        mTvNoProduct = view.findViewById(R.id.mTvNoProduct);
    }

    @Override
    public void onItemClickListener(int position, int flag) {

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
