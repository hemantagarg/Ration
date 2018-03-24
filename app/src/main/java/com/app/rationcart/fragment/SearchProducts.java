package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterSearch;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelHomeData;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchProducts extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static SearchProducts fragmentHome;
    private Activity context;
    private ArrayList<ModelHomeData> mProductsList = new ArrayList<>();
    private RecyclerView listProducts;
    private View view;
    private String TAG = SearchProducts.class.getSimpleName();
    private AdapterSearch adapterProductsList;
    private TextView mTvNoProduct;
    private EditText edt_search;
    private ImageView image_search;

    public static SearchProducts getInstance() {
        if (fragmentHome == null)
            fragmentHome = new SearchProducts();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  DashboardActivity.getInstance().manageHeaderVisibitlity(false);
        DashboardActivity.getInstance().manageFooterVisibitlity(false);*/
        DashboardActivity.getInstance().changeMenuHeader("Search Product", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
      //  manageHeaderView();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Ration Cart");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(true, R.drawable.cart_white);
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
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, new FragmentCartList(), true);
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
        }
    }


    private void setListener() {

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_search.getText().toString().equalsIgnoreCase("")) {
                    getProducts();
                } else {
                    Toast.makeText(context, "Please enter something to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!edt_search.getText().toString().equalsIgnoreCase("")) {
                        getProducts();
                    } else {
                        Toast.makeText(context, "Please enter something to search", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getProducts() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/search.php?keyword=fruit
                String url = JsonApiHelper.BASEURL + JsonApiHelper.SEARCH + "keyword=" + edt_search.getText().toString();
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViews(View view) {
        listProducts = view.findViewById(R.id.list_products);
        listProducts.setLayoutManager(new LinearLayoutManager(context));
        listProducts.setNestedScrollingEnabled(false);
        mTvNoProduct = view.findViewById(R.id.mTvNoProduct);
        image_search = view.findViewById(R.id.image_search);
        edt_search = view.findViewById(R.id.edt_search);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            if (mProductsList.get(position).getType().equalsIgnoreCase("Category")) {
                FragmentProductsAccToCategory fragment = new FragmentProductsAccToCategory();
                Bundle bundle = new Bundle();
                bundle.putString("id", mProductsList.get(position).getCategoryId());
                fragment.setArguments(bundle);
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
            } else if (mProductsList.get(position).getType().equalsIgnoreCase("SubCategory")) {
                FragmentProductsAccToSubCategory fragment = new FragmentProductsAccToSubCategory();
                Bundle bundle = new Bundle();
                bundle.putString("id", mProductsList.get(position).getCategoryId());
                fragment.setArguments(bundle);
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
            } else if (mProductsList.get(position).getType().equalsIgnoreCase("Sub To SubCategory")) {
                FragmentProductsAccToSubtoSubCategory fragment = new FragmentProductsAccToSubtoSubCategory();
                Bundle bundle = new Bundle();
                bundle.putString("id", mProductsList.get(position).getCategoryId());
                fragment.setArguments(bundle);
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
            } else {
                FragmentProductsDetail fragment = new FragmentProductsDetail();
                Bundle bundle = new Bundle();
                bundle.putString("id", mProductsList.get(position).getCategoryId());
                fragment.setArguments(bundle);
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
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
                ModelHomeData modelProducts = new ModelHomeData();

                modelProducts.setCategoryId(jo.getString("id"));
                modelProducts.setCategoryName(jo.getString("name"));
                modelProducts.setCategoryImage(jo.getString("image"));
                modelProducts.setType(jo.getString("type"));

                mProductsList.add(modelProducts);
            }
            adapterProductsList = new AdapterSearch(context, this, mProductsList);
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
