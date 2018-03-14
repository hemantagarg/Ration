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
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterCustomList;
import com.app.rationcart.adapter.AdapterProductsList;
import com.app.rationcart.adapter.AdapterSubcategoriesNames;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelHomeData;
import com.app.rationcart.models.ModelProducts;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentProductsAccToCategory extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentProductsAccToCategory fragmentHome;
    private Activity context;
    private ArrayList<ModelHomeData> mSubCategoriesList = new ArrayList<>();
    private ArrayList<ModelProducts> mProductsList = new ArrayList<>();
    private RecyclerView listSubcategories, listProducts;
    private View view;
    private String TAG = FragmentProductsAccToCategory.class.getSimpleName();
    private String categoryId = "";
    private int selectedPosition = 0;
    private AdapterCustomList adapterlist;
    private ListView list_weight;
    private RelativeLayout rl_bottom;
    AdapterProductsList adapterProductsList;

    public static FragmentProductsAccToCategory getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentProductsAccToCategory();
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
        view = inflater.inflate(R.layout.fragment_products_acc_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
        manageHeaderView();
        getProducts();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Products");
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

                // http://stackmindz.com/dev/rationcart/api/categoryproduct.php?cat_id=1
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CATEGORY_PRODUCT + "cat_id=" + categoryId + "&token=" + AppUtils.getImeiNo(context);
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
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ADD_PRODUCT + "product_id=" + id + "&product_count=" + count + "&token=" + AppUtils.getImeiNo(context) + "&unit_id=" + unitId;
                new CommonAsyncTaskHashmap(methodType, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViews(View view) {
        listSubcategories = view.findViewById(R.id.list_subcategories);
        listProducts = view.findViewById(R.id.list_products);
        list_weight = (ListView) view.findViewById(R.id.spinner_list);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        listSubcategories.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listProducts.setLayoutManager(new LinearLayoutManager(context));
        listProducts.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 511) {
            selectedPosition = position;
            int pos = mProductsList.get(selectedPosition).getCustomPosition();

            ArrayList<HashMap<String, String>> arrayList = mProductsList.get(position).getSetCustomOption();
            ArrayList<String> list = new ArrayList<>();
            ArrayList<String> price = new ArrayList<>();
            ArrayList<String> sprice = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {
                list.add(arrayList.get(i).get("unitprice"));
                sprice.add(arrayList.get(i).get("dis_price"));
                price.add(arrayList.get(i).get("price"));
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
        } else if (flag == 21) {
            selectedPosition = position;
            FragmentProductsAccToSubCategory fragment = new FragmentProductsAccToSubCategory();
            Bundle bundle = new Bundle();
            bundle.putString("id", mSubCategoriesList.get(position).getSubCategoryId());
            fragment.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);

        } else if (flag == 2) {
            selectedPosition = position;
            int count = mProductsList.get(selectedPosition).getProduct_cart_count();
            count++;
            mProductsList.get(selectedPosition).setProduct_cart_count(count);
            adapterProductsList.notifyDataSetChanged();
            HashMap<String, String> hm = mProductsList.get(position).getSetCustomOption().get(mProductsList.get(position).getCustomPosition());
            addProducts(count, mProductsList.get(selectedPosition).getProductId(), true, hm.get("id"));

        } else if (flag == 3) {
            selectedPosition = position;
            int count = mProductsList.get(selectedPosition).getProduct_cart_count();
            if (count >= 1) {
                count--;
                mProductsList.get(selectedPosition).setProduct_cart_count(count);
                HashMap<String, String> hm = mProductsList.get(position).getSetCustomOption().get(mProductsList.get(position).getCustomPosition());
                addProducts(count, mProductsList.get(selectedPosition).getProductId(), false, hm.get("id"));
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
                    int count = mProductsList.get(selectedPosition).getProduct_cart_count();
                    count++;
                    mProductsList.get(selectedPosition).setProduct_cart_count(count);
                    adapterProductsList.notifyDataSetChanged();
                }
            } else if (method == 3) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    int count = mProductsList.get(selectedPosition).getProduct_cart_count();
                    count--;
                    mProductsList.get(selectedPosition).setProduct_cart_count(count);
                    adapterProductsList.notifyDataSetChanged();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setData(JSONObject data) {
        try {

            JSONArray categories = data.getJSONArray("subcategories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject jo = categories.getJSONObject(i);
                ModelHomeData modelHomeData = new ModelHomeData();
                modelHomeData.setSubCategoryId(jo.getString("SubCategoryId"));
                modelHomeData.setSubCategoryImage(jo.getString("SubCategoryImage"));
                modelHomeData.setSubCategoryName(jo.getString("SubCategoryName"));
                mSubCategoriesList.add(modelHomeData);
            }
            AdapterSubcategoriesNames adapterHomeCategories = new AdapterSubcategoriesNames(context, this, mSubCategoriesList);
            listSubcategories.setAdapter(adapterHomeCategories);

            JSONArray product = data.getJSONArray("product");
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

                if (jo.has("unitprice")) {

                    JSONArray custom_option = jo.getJSONArray("unitprice");
                    if ((custom_option.length() > 0)) {
                        for (int j = 0; j < custom_option.length(); j++) {

                            JSONObject j1 = custom_option.getJSONObject(j);
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("unitprice", j1.getString("unitprice"));
                            hm.put("dis_price", j1.getString("dis_price"));
                            hm.put("unit", j1.getString("unit"));
                            hm.put("price", j1.getString("price"));
                            hm.put("id", j1.getString("id"));

                            listcustomoption.add(hm);
                        }

                        modelProducts.setCustomPosition(0);
                        modelProducts.setSetCustomOption(listcustomoption);
                        modelProducts.setIsCustomoption(true);
                    } else {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("unitprice", modelProducts.getProductPrice());
                        hm.put("dis_price", modelProducts.getProductDiscountPrice());
                        hm.put("unit", "");
                        hm.put("price", modelProducts.getProductPrice());
                        hm.put("id", "");

                        listcustomoption.add(hm);
                        modelProducts.setCustomPosition(0);
                        modelProducts.setIsCustomoption(false);
                        modelProducts.setSetCustomOption(listcustomoption);
                    }
                } else {
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("unitprice", modelProducts.getProductPrice());
                    hm.put("dis_price", modelProducts.getProductDiscountPrice());
                    hm.put("unit", "");
                    hm.put("price", modelProducts.getProductPrice());
                    hm.put("id", "");

                    listcustomoption.add(hm);
                    modelProducts.setCustomPosition(0);
                    modelProducts.setIsCustomoption(false);
                    modelProducts.setSetCustomOption(listcustomoption);
                }
                mProductsList.add(modelProducts);
            }
            adapterProductsList = new AdapterProductsList(context, this, mProductsList);
            listProducts.setAdapter(adapterProductsList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
