package com.app.rationcart.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.adapter.DrawerListAdapter;
import com.app.rationcart.fragment.BaseFragment;
import com.app.rationcart.fragment.FragmentHome;
import com.app.rationcart.interfaces.GlobalConstants;
import com.app.rationcart.models.DrawerListModel;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

public class DashboardActivity extends AppCompatActivity {

    private RelativeLayout rl_top;
    private TextView mTvUserName, mTvHome, mTvAccount, mTvCategoies, mTvOffers, mTVNotifications, mTVShare, mTVRate;
    private ExpandableListView expendableView;
    private ScrollView mScrollview;
    private Animation anim;
    private LinearLayout mRlBack;
    private static final String TAG = DashboardActivity.class.getSimpleName();
    private Context context;
    private AppBarLayout appBar;
    private LinkedHashMap<String, List<DrawerListModel>> alldata;
    private ArrayList<String> groupnamelist;
    private ArrayList<String> groupnamelistId;
    private DrawerListAdapter listAdapter;
    private FrameLayout home_container, categories_container, search_container, offers_container, cart_container;
    public static volatile Fragment currentFragment;
    private HashMap<String, Stack<Fragment>> mStacks;
    private static DashboardActivity mInstance;
    private LinearLayout ll_bottom;
    private String mCurrentTab;
    private RelativeLayout rl_home, rl_category, rl_search, rl_offers, rl_cart;
    private ImageView image_home, image_category, image_search, image_offer, image_cart;
    private TextView text_home, text_category, text_search, text_offer, text_cart;

    /***********************************************
     * Function Name : getInstance
     * Description : This function will return the instance of this activity.
     *
     * @return
     */
    public static DashboardActivity getInstance() {
        if (mInstance == null)
            mInstance = new DashboardActivity();
        return mInstance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        mInstance = DashboardActivity.this;
        initViews();
        mStacks = new HashMap<>();
        mStacks.put(GlobalConstants.TAB_HOME_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_CATEGORIES_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_SEARCH_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_OFFERS_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_CART_BAR, new Stack<Fragment>());

        pushFragments(GlobalConstants.TAB_HOME_BAR, new FragmentHome(), true);

        setData();
        setListener();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        alldata = new LinkedHashMap<>();
        groupnamelist = new ArrayList<>();
        groupnamelistId = new ArrayList<>();
        mRlBack = (LinearLayout) findViewById(R.id.mRlBack);
        mTvUserName = (TextView) findViewById(R.id.mTvUserName);
        mTvHome = (TextView) findViewById(R.id.mTvHome);
        mTvAccount = (TextView) findViewById(R.id.mTvAccount);
        mTvCategoies = (TextView) findViewById(R.id.mTvCategoies);
        mTvOffers = (TextView) findViewById(R.id.mTvOffers);
        mTVNotifications = (TextView) findViewById(R.id.mTVNotifications);
        mTVShare = (TextView) findViewById(R.id.mTVShare);
        mTVRate = (TextView) findViewById(R.id.mTVRate);
        mScrollview = (ScrollView) findViewById(R.id.mScrollview);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        expendableView = (ExpandableListView) findViewById(R.id.expendableView);
        listAdapter = new DrawerListAdapter(this, groupnamelist, alldata);
        expendableView.setAdapter(listAdapter);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);

        home_container = (FrameLayout) findViewById(R.id.home_container);
        categories_container = (FrameLayout) findViewById(R.id.categories_container);
        search_container = (FrameLayout) findViewById(R.id.search_container);
        offers_container = (FrameLayout) findViewById(R.id.offers_container);
        cart_container = (FrameLayout) findViewById(R.id.cart_container);

        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_category = (RelativeLayout) findViewById(R.id.rl_category);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        rl_offers = (RelativeLayout) findViewById(R.id.rl_offers);
        image_home = (ImageView) findViewById(R.id.image_home);
        image_category = (ImageView) findViewById(R.id.image_category);
        image_search = (ImageView) findViewById(R.id.image_search);
        image_cart = (ImageView) findViewById(R.id.image_cart);
        image_offer = (ImageView) findViewById(R.id.image_offer);

        text_home = (TextView) findViewById(R.id.text_home);
        text_category = (TextView) findViewById(R.id.text_category);
        text_search = (TextView) findViewById(R.id.text_search);
        text_offer = (TextView) findViewById(R.id.text_offer);
        text_cart = (TextView) findViewById(R.id.text_cart);

    }

    private void unSelectImages() {
        image_cart.setImageResource(R.drawable.cart_grey);
        image_category.setImageResource(R.drawable.category_grey);
        image_offer.setImageResource(R.drawable.offer_grey);
        image_home.setImageResource(R.drawable.home_grey);
        image_search.setImageResource(R.drawable.search_grey);

        text_home.setTextColor(getResources().getColor(R.color.textcolordark));
        text_cart.setTextColor(getResources().getColor(R.color.textcolordark));
        text_category.setTextColor(getResources().getColor(R.color.textcolordark));
        text_offer.setTextColor(getResources().getColor(R.color.textcolordark));
        text_search.setTextColor(getResources().getColor(R.color.textcolordark));
    }


    private void setData() {
        try {
            String mainData = AppUtils.getHomeCategories(context);
            JSONObject data = new JSONObject(mainData);

            JSONArray categories = data.getJSONArray("categories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject headerobj = categories.getJSONObject(i);
                groupnamelist.add(headerobj.getString("CategoryName"));
                groupnamelistId.add(headerobj.getString("CategoryId"));

                ArrayList<DrawerListModel> list = new ArrayList<>();
                JSONArray subCategoriesArray = headerobj.getJSONArray("subCategory");
                Log.e("subCategoriesArray", subCategoriesArray.toString());

                if (subCategoriesArray.length() > 0) {
                    for (int j = 0; j < subCategoriesArray.length(); j++) {
                        JSONObject obj = subCategoriesArray.getJSONObject(j);

                        DrawerListModel model = new DrawerListModel();
                        model.setSubMenu1Id(obj.getString("SubCategoryId"));
                        model.setName(obj.getString("SubCategoryName"));
                        list.add(model);
                    }
                    alldata.put(groupnamelist.get(i), list);
                }
            }
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        mTvCategoies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryAnimation();
            }
        });

        mRlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryAnimation();
            }
        });


        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_home.setImageResource(R.drawable.home_orange);
                text_home.setTextColor(getResources().getColor(R.color.red));
                if (mStacks.get(GlobalConstants.TAB_HOME_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "Home clicked");
                    activeHomeFragment();
                } else
                    pushFragments(GlobalConstants.TAB_HOME_BAR, new FragmentHome(), true);

            }
        });
        rl_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_offer.setImageResource(R.drawable.offer_orange);
                text_offer.setTextColor(getResources().getColor(R.color.red));
                if (mStacks.get(GlobalConstants.TAB_OFFERS_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "Offer clicked");
                    activeOfferFragment();
                } else
                    pushFragments(GlobalConstants.TAB_OFFERS_BAR, new FragmentHome(), true);

            }
        });

        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_category.setImageResource(R.drawable.category_orange);
                text_category.setTextColor(getResources().getColor(R.color.red));
                if (mStacks.get(GlobalConstants.TAB_CATEGORIES_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "Category clicked");
                    activeCategoriesFragment();
                } else
                    pushFragments(GlobalConstants.TAB_CATEGORIES_BAR, new FragmentHome(), true);

            }
        });

        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_search.setImageResource(R.drawable.search_orange);
                text_search.setTextColor(getResources().getColor(R.color.red));
                if (mStacks.get(GlobalConstants.TAB_SEARCH_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "search clicked");
                    activeSearchFragment();
                } else
                    pushFragments(GlobalConstants.TAB_SEARCH_BAR, new FragmentHome(), true);

            }
        });

        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_cart.setImageResource(R.drawable.cart_orange);
                text_cart.setTextColor(getResources().getColor(R.color.red));
                if (mStacks.get(GlobalConstants.TAB_CART_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "cart clicked");
                    activeCartFragment();
                } else
                    pushFragments(GlobalConstants.TAB_CART_BAR, new FragmentHome(), true);

            }
        });
    }

    private void hideCategoryAnimation() {
        anim = AnimationUtils.loadAnimation(context, R.anim.right_out);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mScrollview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expendableView.setVisibility(View.GONE);
                mRlBack.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        expendableView.startAnimation(anim);
    }

    private void showCategoryAnimation() {
        anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mScrollview.setVisibility(View.GONE);
                expendableView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        expendableView.startAnimation(anim);
        mRlBack.setVisibility(View.VISIBLE);
    }


    /*********************************************************************************
     * Function Name - popFragments
     * <p/>
     * Description - this function is used to remove the top fragment of a
     * specific tab on back press
     ********************************************************************************/
    private void popFragments() {
        /*
         * // * Select the last fragment in current tab's stack.. which will be
		 * shown after the fragment transaction given below
		 */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(
                mStacks.get(mCurrentTab).size() - 1);

        // Fragment fragment = getLastElement(mStacks.get(mCurrentTab));
        /* pop current fragment from stack.. */
        mStacks.get(mCurrentTab).remove(fragment);
        if (mStacks != null && mStacks.get(mCurrentTab) != null && !mStacks.get(mCurrentTab).isEmpty())
            currentFragment = mStacks.get(mCurrentTab).lastElement();
           /*
         * Remove the top fragment
		 */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        // ft.add(R.id.realtabcontent, fragment);
        ft.detach(fragment);
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        AppUtils.showLog(TAG, " ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() : " + ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (mStacks.get(mCurrentTab).size() > 0 &&
                    ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false) {
                AppUtils.showErrorLog(TAG, "onBackPressed");
            /*
             * top fragment in current tab doesn't handles back press, we can do
			 * our thing, which is
			 *
			 * if current tab has only one fragment in stack, ie first fragment
			 * is showing for this tab. finish the activity else pop to previous
			 * fragment in stack for the same tab
			 */
                if (mStacks.get(mCurrentTab).size() == 1) {
                    AppUtils.showLog(TAG, "mStacks.get(mCurrentTab).size() == 1");
                    super.onBackPressed();
                    finish();
                } else {
                    AppUtils.showLog(TAG,
                            "mStacks.get(" + mCurrentTab + ").size() not equal to 1 : "
                                    + mStacks.get(mCurrentTab).size());
                    popFragments();
                    if (mStacks.get(mCurrentTab).hashCode() != 0) {
                        // refresh screens
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome) {
                            AppUtils.showLog(TAG, " Current Fragment is Feed Fragment");
                            //  refreshHomeFragment();
                        }
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome) {
                            AppUtils.showLog(TAG, " Current Fragment is Feed Fragment");
                            //  refreshHomeFragment();
                        }
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome) {
                            AppUtils.showLog(TAG, " Current Fragment is Notification Fragment");
                            //  refreshProfileFragment();
                        }

                        if (mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome ||
                                mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome ||
                                mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome ||
                                mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome ||
                                mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome) {
                            manageHeaderVisibitlity(true);
                            manageFooterVisibitlity(true);
                        } else {
                            manageHeaderVisibitlity(false);
                        }
                        refreshFragments();
                    }
                }
            } else {
                // do nothing.. fragment already handled back button press.

            }
        }
    }

    private void refreshFragments() {
        if (currentFragment instanceof FragmentHome) {
            ((FragmentHome) currentFragment).onResume();
        }
    }


    public void manageFooterVisibitlity(boolean isVisible) {
        if (isVisible) {
            //  tabLayout.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            //  tabLayout.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
        }
    }

    public void manageHeaderVisibitlity(boolean isVisible) {
        if (isVisible) {
            appBar.setVisibility(View.VISIBLE);
        } else {
            appBar.setVisibility(View.GONE);
        }
    }

    /*
        * To add fragment to a tab. tag -> Tab identifier fragment -> Fragment to
        * show, false when we switch tabs, or adding first fragment to a tab true
        * when we are pushing more fragment into navigation stack. shouldAdd ->
        * Should add to fragment navigation stack (mStacks.get(tag)). false when we
        * are switching tabs (except for the first time) true in all other cases.
        */
    public void pushFragments(String tag, Fragment fragment, boolean ShouldAdd) {
        if (fragment != null && currentFragment != fragment) {
            currentFragment = fragment;
            mCurrentTab = tag;
            if (ShouldAdd)
                mStacks.get(tag).add(fragment);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (tag.equals(GlobalConstants.TAB_HOME_BAR)) {
                ft.add(R.id.home_container, fragment);
                activeHomeFragment();
            } else if (tag.equals(GlobalConstants.TAB_CATEGORIES_BAR)) {
                ft.add(R.id.categories_container, fragment);
                activeCategoriesFragment();
            } else if (tag.equals(GlobalConstants.TAB_SEARCH_BAR)) {
                ft.add(R.id.search_container, fragment);
                activeSearchFragment();
            } else if (tag.equals(GlobalConstants.TAB_OFFERS_BAR)) {
                ft.add(R.id.offers_container, fragment);
                activeOfferFragment();
            } else if (tag.equals(GlobalConstants.TAB_CART_BAR)) {
                ft.add(R.id.cart_container, fragment);
                activeCartFragment();
            }
            ft.commitAllowingStateLoss();
        }
    }


    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeHomeFragment() {

        mCurrentTab = GlobalConstants.TAB_HOME_BAR;
        AppConstant.CURRENT_SELECTED_TAB = GlobalConstants.TAB_HOME_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        cart_container.setVisibility(View.GONE);
        offers_container.setVisibility(View.GONE);
        home_container.setVisibility(View.VISIBLE);
        search_container.setVisibility(View.GONE);
        categories_container.setVisibility(View.GONE);
    }


    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeCategoriesFragment() {

        mCurrentTab = GlobalConstants.TAB_CATEGORIES_BAR;
        AppConstant.CURRENT_SELECTED_TAB = GlobalConstants.TAB_CATEGORIES_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        cart_container.setVisibility(View.GONE);
        offers_container.setVisibility(View.GONE);
        home_container.setVisibility(View.GONE);
        search_container.setVisibility(View.GONE);
        categories_container.setVisibility(View.VISIBLE);
    }


    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeSearchFragment() {

        mCurrentTab = GlobalConstants.TAB_SEARCH_BAR;
        AppConstant.CURRENT_SELECTED_TAB = GlobalConstants.TAB_SEARCH_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        cart_container.setVisibility(View.GONE);
        offers_container.setVisibility(View.GONE);
        home_container.setVisibility(View.GONE);
        search_container.setVisibility(View.VISIBLE);
        categories_container.setVisibility(View.GONE);
    }

    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeOfferFragment() {

        mCurrentTab = GlobalConstants.TAB_OFFERS_BAR;
        AppConstant.CURRENT_SELECTED_TAB = GlobalConstants.TAB_OFFERS_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        cart_container.setVisibility(View.GONE);
        offers_container.setVisibility(View.VISIBLE);
        home_container.setVisibility(View.GONE);
        search_container.setVisibility(View.GONE);
        categories_container.setVisibility(View.GONE);
    }

    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeCartFragment() {

        mCurrentTab = GlobalConstants.TAB_CART_BAR;
        AppConstant.CURRENT_SELECTED_TAB = GlobalConstants.TAB_CART_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        cart_container.setVisibility(View.VISIBLE);
        offers_container.setVisibility(View.GONE);
        home_container.setVisibility(View.GONE);
        search_container.setVisibility(View.GONE);
        categories_container.setVisibility(View.GONE);
    }


}