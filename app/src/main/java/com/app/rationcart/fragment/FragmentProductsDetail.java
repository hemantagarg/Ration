package com.app.rationcart.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterCustomList;
import com.app.rationcart.adapter.AdapterProductCustomList;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentProductsDetail extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    public static FragmentProductsDetail fragmentHome;
    private Activity context;
    private ArrayList<ModelProducts> mProductsList = new ArrayList<>();
    private RecyclerView listProducts;
    private View view;
    private String TAG = FragmentProductsDetail.class.getSimpleName();
    private String productId = "";
    private int selectedPosition = 0;
    private AdapterCustomList adapterlist;
    private ListView list_weight;
    private RelativeLayout rl_bottom;
    private AdapterProductsList adapterProductsList;
    private ImageView image_top, image_subtract, image_add;
    private TextView productName, productType, productPrice, special_price, spinner_text, text_itemcount, sub_description, Similar_items;
    private ArrayList<ModelProducts> single_product;
    private ModelProducts modelProducts;
    private AdapterProductCustomList adapterCustomList;
    private Button btn_checkout;

    public static FragmentProductsDetail getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentProductsDetail();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   DashboardActivity.getInstance().manageFooterVisibitlity(false);
        DashboardActivity.getInstance().manageHeaderVisibitlity(false);*/
        DashboardActivity.getInstance().changeMenuHeader("", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        getBundle();
        //   manageHeaderView();
        getProductDetail();
        setListener();
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Product Detail");
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
            }
        };
    }

    private void getBundle() {
        Bundle b = getArguments();
        if (b != null) {
            productId = b.getString("id");
        }
    }


    private void setListener() {
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, new FragmentCartList(), true);
            }
        });
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_bottom.setVisibility(View.GONE);
            }
        });
        spinner_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelProducts.isCustomoption()) {

                    ArrayList<ModelUnitPrice> arrayList = modelProducts.getListPrice();
                    ArrayList<String> list = new ArrayList<>();
                    ArrayList<String> price = new ArrayList<>();
                    ArrayList<String> sprice = new ArrayList<>();

                    for (int i = 0; i < arrayList.size(); i++) {
                        ModelUnitPrice model = arrayList.get(i);
                        list.add(model.getUnitprice());
                        sprice.add(model.getDis_price());
                        price.add(model.getPrice());
                    }
                    adapterCustomList = new AdapterProductCustomList(context, FragmentProductsDetail.this, list, price, sprice, modelProducts.getCustomPosition());
                    list_weight.setAdapter(adapterCustomList);
                    rl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelUnitPrice modelUnitPrice = modelProducts.getListPrice().get(modelProducts.getCustomPosition());
                int count = modelUnitPrice.getCart_count();
                count++;
                modelProducts.getListPrice().get(modelProducts.getCustomPosition()).setCart_count(count);
                text_itemcount.setText(count + "");
                addSingleProducts(count, productId, true, modelUnitPrice.getId());
            }
        });


        image_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelUnitPrice modelUnitPrice = modelProducts.getListPrice().get(modelProducts.getCustomPosition());
                int count = modelUnitPrice.getCart_count();
                if (count > 0) {
                    count--;
                    modelProducts.getListPrice().get(modelProducts.getCustomPosition()).setCart_count(count);
                    text_itemcount.setText(count + "");
                    addSingleProducts(count, productId, true, modelUnitPrice.getId());
                }
            }
        });
    }

    private void getProductDetail() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                // http://stackmindz.com/dev/rationcart/api/categoryproduct.php?cat_id=1
                String url = JsonApiHelper.BASEURL + JsonApiHelper.PRODUCT_DETAIL + "product_id=" + productId + "&token=" + AppUtils.getImeiNo(context)
                        + "&user_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSingleProducts(int count, String id, boolean isAdd, String unitId) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                int methodType = 4;
                if (isAdd) {
                    methodType = 5;
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
        list_weight = (ListView) view.findViewById(R.id.spinner_list);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        listProducts.setLayoutManager(new LinearLayoutManager(context));
        listProducts.setNestedScrollingEnabled(false);
        productName = view.findViewById(R.id.productName);
        productType = view.findViewById(R.id.productType);
        productPrice = view.findViewById(R.id.productPrice);
        special_price = view.findViewById(R.id.special_price);
        spinner_text = view.findViewById(R.id.spinner_text);
        text_itemcount = view.findViewById(R.id.text_itemcount);
        sub_description = view.findViewById(R.id.sub_description);
        Similar_items = view.findViewById(R.id.Similar_items);
        image_top = view.findViewById(R.id.image_top);
        image_subtract = view.findViewById(R.id.image_subtract);
        image_add = view.findViewById(R.id.image_add);
        btn_checkout = view.findViewById(R.id.btn_checkout);
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
        } else if (flag == 12) {
            modelProducts.setCustomPosition(position);
            setProductPrice();
            Log.e(" po", "****" + position);
            Log.e(" pro", "****" + modelProducts.getCustomPosition());
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
            } else if (method == 4) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    ModelUnitPrice modelUnitPrice = modelProducts.getListPrice().get(modelProducts.getCustomPosition());
                    int count = modelUnitPrice.getCart_count();
                    count++;
                    modelProducts.getListPrice().get(modelProducts.getCustomPosition()).setCart_count(count);
                    text_itemcount.setText(count + "");
                }
            } else if (method == 5) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                } else {
                    ModelUnitPrice modelUnitPrice = modelProducts.getListPrice().get(modelProducts.getCustomPosition());
                    int count = modelUnitPrice.getCart_count();
                    count--;
                    modelProducts.getListPrice().get(modelProducts.getCustomPosition()).setCart_count(count);
                    text_itemcount.setText(count + "");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setData(JSONObject data) {
        try {
            modelProducts = new ModelProducts();
            single_product = new ArrayList<>();
            productName.setText(data.getString("productName"));
            productName.setText(data.getString("productName"));

            if (!data.getString("image").equalsIgnoreCase("")) {
                Picasso.with(context).load(data.getString("image"))
                        .into(image_top);
            }
            ArrayList<ModelUnitPrice> priceList = new ArrayList<>();
            if (data.has("unitprice")) {

                JSONArray custom_option = data.getJSONArray("unitprice");
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
            single_product.add(modelProducts);

            productPrice.setText(data.getString("productPrice"));
            sub_description.setText(Html.fromHtml(data.getString("description")));

            if (modelProducts.isCustomoption()) {
                spinner_text.setBackgroundResource(R.drawable.list_bg);
            } else {
                spinner_text.setBackgroundColor(Color.argb(28, 204, 204, 204));
            }
            setProductPrice();
            JSONArray product = data.getJSONArray("related_product");
            for (int i = 0; i < product.length(); i++) {
                JSONObject jo = product.getJSONObject(i);
                ModelProducts products = new ModelProducts();

                products.setProductId(jo.getString("productId"));
                products.setProductName(jo.getString("productName"));
                products.setQuantity(jo.getString("quantity"));
                products.setDiscount_value(jo.getString("discount_value"));
                products.setProductDiscountPrice(jo.getString("productDiscountPrice"));
                products.setProductPrice(jo.getString("productPrice"));
                products.setUnitType(jo.getString("unitType"));
                products.setRowType(1);
                products.setImage(jo.getString("image"));
                products.setProduct_cart_count(jo.getInt("product_cart_count"));

                ArrayList<ModelUnitPrice> priceList1 = new ArrayList<>();
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
                            priceList1.add(modelUnitPrice);

                        }
                        products.setListPrice(priceList1);
                        products.setCustomPosition(0);
                        products.setIsCustomoption(true);
                    } else {
                        ModelUnitPrice modelUnitPrice = new ModelUnitPrice();
                        modelUnitPrice.setCart_count(products.getProduct_cart_count());
                        modelUnitPrice.setUnitprice(products.getProductPrice());
                        modelUnitPrice.setUnit("");
                        modelUnitPrice.setDis_price(products.getProductDiscountPrice());
                        modelUnitPrice.setPrice(products.getProductPrice());
                        modelUnitPrice.setId("");
                        priceList1.add(modelUnitPrice);

                        products.setListPrice(priceList1);

                        products.setCustomPosition(0);
                        products.setIsCustomoption(false);
                    }
                } else {

                    ModelUnitPrice modelUnitPrice = new ModelUnitPrice();
                    modelUnitPrice.setCart_count(products.getProduct_cart_count());
                    modelUnitPrice.setUnitprice(products.getProductPrice());
                    modelUnitPrice.setUnit("");
                    modelUnitPrice.setDis_price(products.getProductDiscountPrice());
                    modelUnitPrice.setPrice(products.getProductPrice());
                    modelUnitPrice.setId("");
                    priceList1.add(modelUnitPrice);

                    products.setListPrice(priceList1);

                    products.setCustomPosition(0);
                    products.setIsCustomoption(false);
                }
                mProductsList.add(products);
            }
            adapterProductsList = new AdapterProductsList(context, this, mProductsList);
            listProducts.setAdapter(adapterProductsList);

            if (mProductsList.size() > 0) {
                Similar_items.setVisibility(View.VISIBLE);
            } else {
                Similar_items.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProductPrice() {
        ModelUnitPrice modelUnitPrice = modelProducts.getListPrice().get(modelProducts.getCustomPosition());
        if (!modelUnitPrice.getDis_price().equalsIgnoreCase("") && !modelUnitPrice.getDis_price().equalsIgnoreCase("0") && !modelUnitPrice.getDis_price().equalsIgnoreCase("0.00")) {

            String dataspan = modelUnitPrice.getPrice();
            Spannable wordtoSpan = new SpannableString(dataspan);

            wordtoSpan.setSpan(new StrikethroughSpan(), 0, wordtoSpan.length(), 0);
            special_price.setVisibility(View.VISIBLE);
            productPrice.setText(wordtoSpan);
            special_price.setText(modelUnitPrice.getDis_price());
        } else {
            productPrice.setText(modelUnitPrice.getPrice());
            special_price.setVisibility(View.GONE);
        }
        spinner_text.setText(modelUnitPrice.getUnitprice());
        text_itemcount.setText(modelUnitPrice.getCart_count() + "");

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
