package com.app.rationcart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.activities.LoginActivity;
import com.app.rationcart.adapter.AdapterCartList;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelProducts;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCartList extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentCartList fragmentHome;
    private Activity context;
    private ArrayList<ModelProducts> mProductsList = new ArrayList<>();
    private RecyclerView listProducts;
    private View view;
    private String TAG = FragmentCartList.class.getSimpleName();
    private String totalPrice = "", finalquantity = "";
    private int selectedPosition = 0;
    private RelativeLayout rl_bottom;
    private AdapterCartList adapterProductsList;
    private TextView mTvTotalAmount;

    public static FragmentCartList getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentCartList();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        //  DashboardActivity.getInstance().manageFooterVisibitlity(false);
        // DashboardActivity.getInstance().manageHeaderVisibitlity(false);
        getProducts();
        DashboardActivity.getInstance().changeMenuHeader("Review Order", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
        //   manageHeaderView();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Review Order");
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
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
            //categoryId = b.getString("id");
        }
    }


    private void setListener() {
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (mProductsList.size() > 0) {
                        FragmentSelectAddress fragmentSelectAddress = new FragmentSelectAddress();
                        Bundle bundle = new Bundle();
                        bundle.putString("price", totalPrice);
                        bundle.putString("finalquantity", finalquantity);
                        fragmentSelectAddress.setArguments(bundle);
                        DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragmentSelectAddress, true);
                    } else
                        Toast.makeText(context, "Please add atleast one item in cart", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getProducts() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/CART_PRODUCT.php?cat_id=1
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CART_PRODUCT + "token=" + AppUtils.getImeiNo(context)
                        + "&user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProducts(int count, String id, boolean isAdd, String unitId) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                int methodType = 2;
                if (isAdd) {
                    methodType = 3;
                }
                // http://stackmindz.com/dev/rationcart/api/product_cart_count?product_id=12&product_count=1&token=123456789
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ADD_PRODUCT + "product_id=" + id + "&product_count=" + count + "&token=" + AppUtils.getImeiNo(context) + "&unit_id=" + unitId
                        + "&user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(methodType, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCartItem(String id) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //   http://stackmindz.com/dev/rationcart/api/deletecartitem?cart_id=257
                // &token=355241080144570&user_id=
                String url = JsonApiHelper.BASEURL + JsonApiHelper.DELETE_CART_ITEM + "cart_id=" + id + "&token=" + AppUtils.getImeiNo(context)
                        + "&user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(6, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setData(JSONObject data) {
        try {
            mProductsList.clear();
            JSONArray product = data.getJSONArray("cart_product");
            for (int i = 0; i < product.length(); i++) {
                JSONObject jo = product.getJSONObject(i);

                ModelProducts modelProducts = new ModelProducts();
                modelProducts.setCartId(jo.getString("cartId"));
                modelProducts.setProductId(jo.getString("productId"));
                modelProducts.setProductName(jo.getString("productName"));
                modelProducts.setQuantity(jo.getString("quantity"));
                modelProducts.setProductPrice(jo.getString("productPrice"));
                modelProducts.setTotal_amount(jo.getString("total_amount"));
                modelProducts.setUnit_value(jo.getString("unit_value"));
                modelProducts.setRowType(1);
                modelProducts.setImage(jo.getString("image"));
                modelProducts.setProduct_cart_count(jo.getInt("quantity"));
                modelProducts.setUnit_id(jo.getString("unit_id"));

                mProductsList.add(modelProducts);
            }
            adapterProductsList = new AdapterCartList(context, this, mProductsList);
            listProducts.setAdapter(adapterProductsList);
            totalPrice = data.getString("finalAmount");
            finalquantity = data.getString("finalquantity");
            mTvTotalAmount.setText(data.getString("finalAmount") + " Rs");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        listProducts = view.findViewById(R.id.list_products);
        rl_bottom =  view.findViewById(R.id.mRlBottom);
        listProducts.setLayoutManager(new LinearLayoutManager(context));
        listProducts.setNestedScrollingEnabled(false);
        mTvTotalAmount = view.findViewById(R.id.mTvTotalAmount);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 5) {
            // delete item from cart
            selectedPosition = position;
            showConfirmtion(mProductsList.get(position).getCartId());
        } else if (flag == 2) {
            selectedPosition = position;
            int count = mProductsList.get(selectedPosition).getProduct_cart_count();
            count++;
            mProductsList.get(selectedPosition).setProduct_cart_count(count);
            adapterProductsList.notifyDataSetChanged();
            addProducts(count, mProductsList.get(selectedPosition).getProductId(), true, mProductsList.get(selectedPosition).getUnit_id());
        } else if (flag == 3) {
            selectedPosition = position;
            int count = mProductsList.get(selectedPosition).getProduct_cart_count();
            if (count > 1) {
                count--;
                mProductsList.get(selectedPosition).setProduct_cart_count(count);
                adapterProductsList.notifyDataSetChanged();
                addProducts(count, mProductsList.get(selectedPosition).getProductId(), false, mProductsList.get(selectedPosition).getUnit_id());
            }
        }
    }

    private void showConfirmtion(final String id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Delete Product");

        alertDialog.setMessage("Are you sure you want to delete this item?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        deleteCartItem(id);

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
                    getProducts();
                } else {
                    int count = mProductsList.get(selectedPosition).getProduct_cart_count();
                    count++;
                    mProductsList.get(selectedPosition).setProduct_cart_count(count);
                    adapterProductsList.notifyDataSetChanged();
                }
            } else if (method == 3) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    getProducts();
                } else {
                    int count = mProductsList.get(selectedPosition).getProduct_cart_count();
                    count--;
                    mProductsList.get(selectedPosition).setProduct_cart_count(count);
                    adapterProductsList.notifyDataSetChanged();
                }
            } else if (method == 6) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    getProducts();
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
