package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterCustomList;
import com.app.rationcart.adapter.AdapterProductsList;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelProducts;
import com.app.rationcart.models.ModelUnitPrice;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Offers extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static Offers fragmentHome;
    private Activity context;
    private ArrayList<ModelProducts> mProductsList = new ArrayList<>();
    private RecyclerView listProducts;
    private View view;
    private String TAG = Offers.class.getSimpleName();
    private String categoryId = "";
    private int selectedPosition = 0;
    private AdapterCustomList adapterlist;
    private ListView list_weight;
    private RelativeLayout rl_bottom;
    private AdapterProductsList adapterProductsList;
    private TextView mTvNoProduct;

    public static Offers getInstance() {
        if (fragmentHome == null)
            fragmentHome = new Offers();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().changeMenuHeader("Offers", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.offers_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
       // manageHeaderView();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Offers");
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
            categoryId = b.getString("id");
        }
    }


    private void setListener() {
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_bottom.setVisibility(View.GONE);
            }
        });
    }

    private void getProducts() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/search.php?keyword=fruit
                String url = JsonApiHelper.BASEURL + JsonApiHelper.SEARCH + "keyword=" + "";
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


    private void initViews(View view) {
        listProducts = view.findViewById(R.id.list_products);
        list_weight = view.findViewById(R.id.spinner_list);
        rl_bottom = view.findViewById(R.id.rl_bottom);
        listProducts.setLayoutManager(new LinearLayoutManager(context));
        listProducts.setNestedScrollingEnabled(false);
        mTvNoProduct = view.findViewById(R.id.mTvNoProduct);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 511) {
            selectedPosition = position;
            int pos = mProductsList.get(selectedPosition).getCustomPosition();

            ArrayList<ModelUnitPrice> arrayList = mProductsList.get(position).getListPrice();
            ArrayList<String> list = new ArrayList<>();
            ArrayList<String> price = new ArrayList<>();
            ArrayList<String> sprice = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {
                ModelUnitPrice model = arrayList.get(i);
                list.add(model.getUnitprice());
                sprice.add(model.getDis_price());
                price.add(model.getPrice());
            }
            adapterlist = new AdapterCustomList(context, this, list, price, sprice, pos);
            list_weight.setAdapter(adapterlist);
            rl_bottom.setVisibility(View.VISIBLE);
        } else if (flag == 11) {
            mProductsList.get(selectedPosition).setCustomPosition(position);
            Log.e(" po", "****" + position);
            Log.e(" pro", "****" + mProductsList.get(selectedPosition).getCustomPosition());
            adapterProductsList.notifyItemChanged(selectedPosition);
            rl_bottom.setVisibility(View.GONE);
        } else if (flag == 22) {
            selectedPosition = position;
            FragmentProductsDetail fragment = new FragmentProductsDetail();
            Bundle bundle = new Bundle();
            bundle.putString("id", mProductsList.get(position).getProductId());
            fragment.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);

        } else if (flag == 2) {
            selectedPosition = position;
            ModelUnitPrice modelUnitPrice = mProductsList.get(position).getListPrice().get(mProductsList.get(position).getCustomPosition());
            int count = modelUnitPrice.getCart_count();
            count++;
            mProductsList.get(position).getListPrice().get(mProductsList.get(position).getCustomPosition()).setCart_count(count);
            adapterProductsList.notifyDataSetChanged();
            addProducts(count, mProductsList.get(selectedPosition).getProductId(), true, modelUnitPrice.getId());

        } else if (flag == 3) {
            selectedPosition = position;
            ModelUnitPrice modelUnitPrice = mProductsList.get(position).getListPrice().get(mProductsList.get(position).getCustomPosition());
            int count = modelUnitPrice.getCart_count();
            if (count >= 1) {
                count--;
                mProductsList.get(position).getListPrice().get(mProductsList.get(position).getCustomPosition()).setCart_count(count);
                addProducts(count, mProductsList.get(selectedPosition).getProductId(), false, modelUnitPrice.getId());
                adapterProductsList.notifyDataSetChanged();
            }
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
            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    ModelUnitPrice modelUnitPrice = mProductsList.get(selectedPosition).getListPrice().get(mProductsList.get(selectedPosition).getCustomPosition());
                    int count = modelUnitPrice.getCart_count();
                    count++;
                    mProductsList.get(selectedPosition).getListPrice().get(mProductsList.get(selectedPosition).getCustomPosition()).setCart_count(count);
                    adapterProductsList.notifyDataSetChanged();
                }
            } else if (method == 3) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    ModelUnitPrice modelUnitPrice = mProductsList.get(selectedPosition).getListPrice().get(mProductsList.get(selectedPosition).getCustomPosition());
                    int count = modelUnitPrice.getCart_count();
                    count--;
                    mProductsList.get(selectedPosition).getListPrice().get(mProductsList.get(selectedPosition).getCustomPosition()).setCart_count(count);
                    adapterProductsList.notifyDataSetChanged();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setData(JSONObject data) {
        try {

            JSONArray product = data.getJSONArray("search");
            for (int i = 0; i < product.length(); i++) {
                JSONObject jo = product.getJSONObject(i);
                ModelProducts modelProducts = new ModelProducts();

                modelProducts.setProductId(jo.getString("productId"));
                modelProducts.setProductName(jo.getString("productName"));
                modelProducts.setQuantity(jo.getString("quantity"));
                modelProducts.setDiscount_value(jo.getString("discount_value"));
                modelProducts.setProductDiscountPrice(jo.getString("productDiscountPrice"));
                modelProducts.setProductPrice(jo.getString("productPrice"));
                modelProducts.setUnitType(jo.getString("unitType"));
                modelProducts.setRowType(1);
                modelProducts.setImage(jo.getString("image"));
                modelProducts.setProduct_cart_count(jo.getInt("product_cart_count"));

                ArrayList<HashMap<String, String>> listcustomoption = new ArrayList<HashMap<String, String>>();
                ArrayList<ModelUnitPrice> priceList = new ArrayList<>();
                if (jo.has("unitprice")) {

                    JSONArray custom_option = jo.getJSONArray("unitprice");
                    if ((custom_option.length() > 0)) {
                        for (int j = 0; j < custom_option.length(); j++) {

                            JSONObject j1 = custom_option.getJSONObject(j);

                            ModelUnitPrice modelUnitPrice = new ModelUnitPrice();
                            modelUnitPrice.setCart_count(j1.getInt("cart_count"));
                            modelUnitPrice.setUnitprice(j1.getString("unitprice"));
                            modelUnitPrice.setUnit(j1.getString("unit"));
                            modelUnitPrice.setDis_price(j1.getString("dis_price"));
                            modelUnitPrice.setPrice(j1.getString("price"));
                            modelUnitPrice.setId(j1.getString("id"));
                            priceList.add(modelUnitPrice);

                        }
                        modelProducts.setListPrice(priceList);
                        modelProducts.setCustomPosition(0);
                        modelProducts.setIsCustomoption(true);
                    } else {
                        ModelUnitPrice modelUnitPrice = new ModelUnitPrice();
                        modelUnitPrice.setCart_count(modelProducts.getProduct_cart_count());
                        modelUnitPrice.setUnitprice(modelProducts.getProductPrice());
                        modelUnitPrice.setUnit("");
                        modelUnitPrice.setDis_price(modelProducts.getProductDiscountPrice());
                        modelUnitPrice.setPrice(modelProducts.getProductPrice());
                        modelUnitPrice.setId("");
                        priceList.add(modelUnitPrice);

                        modelProducts.setListPrice(priceList);

                        modelProducts.setCustomPosition(0);
                        modelProducts.setIsCustomoption(false);
                    }
                } else {

                    ModelUnitPrice modelUnitPrice = new ModelUnitPrice();
                    modelUnitPrice.setCart_count(modelProducts.getProduct_cart_count());
                    modelUnitPrice.setUnitprice(modelProducts.getProductPrice());
                    modelUnitPrice.setUnit("");
                    modelUnitPrice.setDis_price(modelProducts.getProductDiscountPrice());
                    modelUnitPrice.setPrice(modelProducts.getProductPrice());
                    modelUnitPrice.setId("");
                    priceList.add(modelUnitPrice);

                    modelProducts.setListPrice(priceList);

                    modelProducts.setCustomPosition(0);
                    modelProducts.setIsCustomoption(false);
                }
                mProductsList.add(modelProducts);
            }
            adapterProductsList = new AdapterProductsList(context, this, mProductsList);
            listProducts.setAdapter(adapterProductsList);
            if (mProductsList.size() > 0) {
                mTvNoProduct.setVisibility(View.GONE);
            } else {
                mTvNoProduct.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
