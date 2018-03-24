package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.AdapterHomeCategories;
import com.app.rationcart.adapter.AdapterHomeTopBrands;
import com.app.rationcart.adapter.AdapterHomeTopDeals;
import com.app.rationcart.adapter.CustomPagerAdapter;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelHomeData;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHome extends BaseFragment implements OnCustomItemClicListener {

    public static FragmentHome fragmentHome;
    private ViewPager mViewPager;
    private Activity context;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    private Handler handler;
    private Runnable runabble;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ArrayList<ModelHomeData> bannerImagesList = new ArrayList<>();
    private ArrayList<ModelHomeData> mCategoriesList = new ArrayList<>();
    private ArrayList<ModelHomeData> mDealsList = new ArrayList<>();
    private ArrayList<ModelHomeData> mBrandsList = new ArrayList<>();
    private RecyclerView list_categories, list_topdeals, list_topbrands;
    private TextView edt_search;
    private ImageView image_search;

    public static FragmentHome getInstance() {
        if (fragmentHome == null)
            fragmentHome = new FragmentHome();
        return fragmentHome;
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().manageFooterVisibitlity(true);
        DashboardActivity.getInstance().manageHeaderVisibitlity(true);
        DashboardActivity.getInstance().changeMenuHeader("Your Location", true);
        //  pagerMove();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initViews(view);
        setData();
        setListener();
    }

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    if (position >= dotsCount) {
                        position = (position % dotsCount);
                    }
                    Log.e("position", "*" + position);

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dotwhite));
                    }
                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, new SearchProducts(), true);
            }
        });
    }

    private void setData() {
        try {
            String mainData = AppUtils.getHomeCategories(context);
            JSONObject data = new JSONObject(mainData);

            JSONArray banners = data.getJSONArray("banners");
            for (int i = 0; i < banners.length(); i++) {
                JSONObject jo = banners.getJSONObject(i);
                ModelHomeData modelHomeData = new ModelHomeData();
                modelHomeData.setBannerId(jo.getString("bannerId"));
                modelHomeData.setBannerImage(jo.getString("image"));
                bannerImagesList.add(modelHomeData);
            }
            mCustomPagerAdapter = new CustomPagerAdapter(context, this, bannerImagesList);
            mViewPager.setAdapter(mCustomPagerAdapter);
            setUiPageViewController();


            JSONArray categories = data.getJSONArray("categories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject jo = categories.getJSONObject(i);
                ModelHomeData modelHomeData = new ModelHomeData();
                modelHomeData.setCategoryId(jo.getString("CategoryId"));
                modelHomeData.setCategoryImage(jo.getString("CategoryImage"));
                modelHomeData.setCategoryName(jo.getString("CategoryName"));
                mCategoriesList.add(modelHomeData);
            }
            AdapterHomeCategories adapterHomeCategories = new AdapterHomeCategories(context, this, mCategoriesList);
            list_categories.setAdapter(adapterHomeCategories);

            JSONArray top_deals = data.getJSONArray("top_deals");
            for (int i = 0; i < top_deals.length(); i++) {
                JSONObject jo = top_deals.getJSONObject(i);
                ModelHomeData modelHomeData = new ModelHomeData();
                modelHomeData.setCategoryId(jo.getString("SubCategoryId"));
                modelHomeData.setCategoryImage(jo.getString("SubCategoryImage"));
                modelHomeData.setCategoryName(jo.getString("SubCategoryName"));
                mDealsList.add(modelHomeData);
            }
            AdapterHomeTopDeals adapterHomeTopDeals = new AdapterHomeTopDeals(context, this, mDealsList);
            list_topdeals.setAdapter(adapterHomeTopDeals);

            JSONArray top_brands = data.getJSONArray("top_brands");
            for (int i = 0; i < top_brands.length(); i++) {
                JSONObject jo = top_brands.getJSONObject(i);
                ModelHomeData modelHomeData = new ModelHomeData();
                modelHomeData.setCategoryId(jo.getString("CategoryId"));
                modelHomeData.setCategoryImage(jo.getString("CategoryImage"));
                modelHomeData.setCategoryName(jo.getString("CategoryName"));
                mBrandsList.add(modelHomeData);
            }
            AdapterHomeTopBrands adapterHomeTopBrands = new AdapterHomeTopBrands(context, this, mBrandsList);
            list_topbrands.setAdapter(adapterHomeTopBrands);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews(View view) {
        mViewPager = view.findViewById(R.id.pager);
        pager_indicator = view.findViewById(R.id.viewPagerCountDots);
        image_search = view.findViewById(R.id.image_search);
        edt_search = view.findViewById(R.id.edt_search);
        list_categories = view.findViewById(R.id.list_categories);
        list_topbrands = view.findViewById(R.id.list_topbrands);
        list_topdeals = view.findViewById(R.id.list_topdeals);
        list_categories.setLayoutManager(new GridLayoutManager(context, 3));
        list_topbrands.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        list_topdeals.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        list_categories.setNestedScrollingEnabled(false);
    }

    private void setUiPageViewController() {

        dotsCount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dotwhite));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //   params.gravity = Gravity.RIGHT;
            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            FragmentProductsAccToCategory fragment = new FragmentProductsAccToCategory();
            Bundle bundle = new Bundle();
            bundle.putString("id", mCategoriesList.get(position).getCategoryId());
            fragment.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
        } else if (flag == 4) {
            FragmentProductsAccToSubCategory fragment = new FragmentProductsAccToSubCategory();
            Bundle bundle = new Bundle();
            bundle.putString("id", mDealsList.get(position).getCategoryId());
            fragment.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
        } else if (flag == 5) {
            FragmentProductsAccToCategory fragment = new FragmentProductsAccToCategory();
            Bundle bundle = new Bundle();
            bundle.putString("id", mBrandsList.get(position).getCategoryId());
            fragment.setArguments(bundle);
            DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragment, true);
        }
    }

    private void pagerMove() {
        try {

            handler = new Handler();
            runabble = new Runnable() {
                @Override
                public void run() {

                    int countPager = 0;
                    int page = mViewPager.getCurrentItem();
                    if (mCustomPagerAdapter != null && mCustomPagerAdapter.getCount() > 0) {
                        countPager = mCustomPagerAdapter.getCount();
                    }
                    //  Toast.makeText(getApplicationContext(),"handler",Toast.LENGTH_SHORT).show();
                    page++;

                    if (page >= countPager) {

                        page = 0;
                    }
                    //countPager--;
                    mViewPager.setCurrentItem(page, true);
                    pagerMove();
                    //  handler.postDelayed(this, 4000);
                }
            };
            handler.postDelayed(runabble, 4000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            if (handler != null) {
                handler.removeCallbacks(runabble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (handler != null) {
                handler.removeCallbacks(runabble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (handler != null) {
                handler.removeCallbacks(runabble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
