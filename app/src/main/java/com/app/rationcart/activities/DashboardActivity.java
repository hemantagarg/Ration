package com.app.rationcart.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.app.rationcart.fragment.FragmentCartList;
import com.app.rationcart.fragment.FragmentCategoriesList;
import com.app.rationcart.fragment.FragmentHome;
import com.app.rationcart.fragment.FragmentNotification;
import com.app.rationcart.fragment.FragmentOrderHistory;
import com.app.rationcart.fragment.FragmentProductsAccToCategory;
import com.app.rationcart.fragment.FragmentProductsAccToSubCategory;
import com.app.rationcart.fragment.FragmentProductsDetail;
import com.app.rationcart.fragment.FragmentSelectAddress;
import com.app.rationcart.fragment.Offers;
import com.app.rationcart.fragment.SearchProducts;
import com.app.rationcart.fragment.UserProfile;
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
    private Toolbar toolbar;
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
    private RelativeLayout rl_home, rl_category, rl_search, rl_offers, rl_cart, rlLocationMain;
    private ImageView image_home, image_category, image_search, image_offer, image_cart;
    private TextView text_home, text_category, text_search, text_offer,
            text_cart, mTvLogin, mTVLogout, mTvOrderHistory, mTvAddress, mTChangePassword, mTvLocation,
            mTvHeading, mTvLocation1;
    private ImageView mIvEdit, mIvEdit1;
    private DrawerLayout drawer;
    private Button btn_choose_location;


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
        if (AppUtils.getLoction(context).equalsIgnoreCase("")) {
            rlLocationMain.setVisibility(View.VISIBLE);
        }
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvLocation.setText(AppUtils.getLoction(context));
        mTvLocation1.setText(AppUtils.getLoction(context));
        if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
            mTvLogin.setVisibility(View.VISIBLE);
            mTvUserName.setVisibility(View.GONE);
            mTVLogout.setVisibility(View.GONE);
            mTvOrderHistory.setVisibility(View.GONE);
            mTChangePassword.setVisibility(View.GONE);
            mTvAddress.setVisibility(View.GONE);
        } else {
            mTvLogin.setVisibility(View.GONE);
            mTvUserName.setVisibility(View.VISIBLE);
            mTVLogout.setVisibility(View.VISIBLE);
            mTvOrderHistory.setVisibility(View.VISIBLE);
            mTChangePassword.setVisibility(View.VISIBLE);
            mTvAddress.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
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
        mTvLocation = (TextView) findViewById(R.id.mTvLocation);
        mTvLocation1 = (TextView) findViewById(R.id.mTvLocation1);
        mTvHeading = (TextView) findViewById(R.id.mTvHeading);
        mTVRate = (TextView) findViewById(R.id.mTVRate);
        mScrollview = (ScrollView) findViewById(R.id.mScrollview);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        expendableView = (ExpandableListView) findViewById(R.id.expendableView);
        listAdapter = new DrawerListAdapter(this, groupnamelist, alldata);
        expendableView.setAdapter(listAdapter);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mIvEdit = (ImageView) findViewById(R.id.mIvEdit);
        mIvEdit1 = (ImageView) findViewById(R.id.mIvEdit1);
        home_container = (FrameLayout) findViewById(R.id.home_container);
        categories_container = (FrameLayout) findViewById(R.id.categories_container);
        search_container = (FrameLayout) findViewById(R.id.search_container);
        offers_container = (FrameLayout) findViewById(R.id.offers_container);
        cart_container = (FrameLayout) findViewById(R.id.cart_container);

        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_category = (RelativeLayout) findViewById(R.id.rl_category);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlLocationMain = (RelativeLayout) findViewById(R.id.rlLocationMain);
        rl_offers = (RelativeLayout) findViewById(R.id.rl_offers);
        image_home = (ImageView) findViewById(R.id.image_home);
        image_category = (ImageView) findViewById(R.id.image_category);
        image_search = (ImageView) findViewById(R.id.image_search);
        image_cart = (ImageView) findViewById(R.id.image_cart);
        image_offer = (ImageView) findViewById(R.id.image_offer);
        btn_choose_location = (Button) findViewById(R.id.btn_choose_location);
        text_home = (TextView) findViewById(R.id.text_home);
        text_category = (TextView) findViewById(R.id.text_category);
        text_search = (TextView) findViewById(R.id.text_search);
        text_offer = (TextView) findViewById(R.id.text_offer);
        text_cart = (TextView) findViewById(R.id.text_cart);
        mTvLogin = (TextView) findViewById(R.id.mTvLogin);
        mTVLogout = (TextView) findViewById(R.id.mTVLogout);
        mTvOrderHistory = (TextView) findViewById(R.id.mTvOrderHistory);
        mTChangePassword = (TextView) findViewById(R.id.mTChangePassword);
        mTvAddress = (TextView) findViewById(R.id.mTvAddress);

    }

    private void unSelectImages() {
        image_cart.setImageResource(R.drawable.cart_grey);
        image_category.setImageResource(R.drawable.category_grey);
        image_offer.setImageResource(R.drawable.offer_grey);
        image_home.setImageResource(R.drawable.home_grey);
        image_search.setImageResource(R.drawable.search_grey);

        text_home.setTextColor(ContextCompat.getColor(context, R.color.textcolordark));
        text_cart.setTextColor(ContextCompat.getColor(context, R.color.textcolordark));
        text_category.setTextColor(ContextCompat.getColor(context, R.color.textcolordark));
        text_offer.setTextColor(ContextCompat.getColor(context, R.color.textcolordark));
        text_search.setTextColor(ContextCompat.getColor(context, R.color.textcolordark));
    }


    private void setData() {
        try {
            mTvUserName.setText(AppUtils.getUserName(context));

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

    public void changeMenuHeader(String heading, boolean showLocation) {
        if (heading.equalsIgnoreCase("")) {
            mTvHeading.setText("Ration Cart");
        } else {
            mTvHeading.setText(heading);
        }

        if (showLocation) {
            mTvLocation.setVisibility(View.VISIBLE);
            mIvEdit.setVisibility(View.VISIBLE);
        } else {
            mTvLocation.setVisibility(View.GONE);
            mIvEdit.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        mTvCategoies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryAnimation();
            }
        });


        mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PickLocation.class);
                i.putExtra("lat", AppUtils.getLatitude(context));
                i.putExtra("lng", AppUtils.getLongitude(context));
                startActivityForResult(i, 511);

            }
        });
        mIvEdit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PickLocation.class);
                i.putExtra("lat", AppUtils.getLatitude(context));
                i.putExtra("lng", AppUtils.getLongitude(context));
                startActivityForResult(i, 511);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        mTvLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PickLocation.class);
                i.putExtra("lat", AppUtils.getLatitude(context));
                i.putExtra("lng", AppUtils.getLongitude(context));
                startActivityForResult(i, 511);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        btn_choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PickLocation.class);
                i.putExtra("lat", AppUtils.getLatitude(context));
                i.putExtra("lng", AppUtils.getLongitude(context));
                startActivityForResult(i, 511);
                drawer.closeDrawer(GravityCompat.START);
                rlLocationMain.setVisibility(View.GONE);

            }
        });
        rlLocationMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLocationMain.setVisibility(View.GONE);
            }
        });


        mTVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutBox();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mTvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment instanceof FragmentHome)
                    drawer.closeDrawer(GravityCompat.START);
                else
                    pushFragments(GlobalConstants.TAB_HOME_BAR, new FragmentHome(), true);

                changeMenuHeader("Your Location", true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mTvOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(AppConstant.CURRENT_SELECTED_TAB, new FragmentOrderHistory(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mTvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(AppConstant.CURRENT_SELECTED_TAB, new UserProfile(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mTvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSelectAddress fragmentSelectAddress = new FragmentSelectAddress();
                Bundle bundle = new Bundle();
                bundle.putString("price", "");
                bundle.putString("finalquantity", "");
                bundle.putBoolean("isCheckout", false);
                fragmentSelectAddress.setArguments(bundle);
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragmentSelectAddress, true);

                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mTChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(AppConstant.CURRENT_SELECTED_TAB, new ChangePassword(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mTVNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(AppConstant.CURRENT_SELECTED_TAB, new FragmentNotification(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
        mRlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryAnimation();
            }
        });
        expendableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
                Log.e("group click", "clicked" + position);
                List<DrawerListModel> list = alldata.get(groupnamelist.get(position));
                if (list == null || list.size() == 0) {
                    FragmentProductsAccToCategory fragmentAvtar_details = new FragmentProductsAccToCategory();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", groupnamelistId.get(position));
                    fragmentAvtar_details.setArguments(bundle);
                    DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragmentAvtar_details, true);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
        expendableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Nothing here ever fires
                System.err.println("child clicked");
                List<DrawerListModel> list = alldata.get(groupnamelist.get(groupPosition));
                FragmentProductsAccToSubCategory fragmentAvtar_details = new FragmentProductsAccToSubCategory();
                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(childPosition).getSubMenu1Id());
                fragmentAvtar_details.setArguments(bundle);
                pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragmentAvtar_details, true);
                parent.collapseGroup(groupPosition);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });


        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_home.setImageResource(R.drawable.home_orange);
                text_home.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (mStacks.get(GlobalConstants.TAB_HOME_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome))
                        AppUtils.showErrorLog(TAG, "Home clicked");
                    activeHomeFragment();
                } else
                    pushFragments(GlobalConstants.TAB_HOME_BAR, new FragmentHome(), true);

                refreshFragments();

            }
        });
        rl_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_offer.setImageResource(R.drawable.offer_orange);
                text_offer.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (mStacks.get(GlobalConstants.TAB_OFFERS_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof Offers))
                        AppUtils.showErrorLog(TAG, "Offer clicked");
                    activeOfferFragment();
                } else
                    pushFragments(GlobalConstants.TAB_OFFERS_BAR, new Offers(), true);


                refreshFragments();
            }
        });

        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_category.setImageResource(R.drawable.category_orange);
                text_category.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (mStacks.get(GlobalConstants.TAB_CATEGORIES_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentCategoriesList))
                        AppUtils.showErrorLog(TAG, "Category clicked");
                    activeCategoriesFragment();
                } else
                    pushFragments(GlobalConstants.TAB_CATEGORIES_BAR, new FragmentCategoriesList(), true);

                refreshFragments();
            }
        });

        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_search.setImageResource(R.drawable.search_orange);
                text_search.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (mStacks.get(GlobalConstants.TAB_SEARCH_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof SearchProducts))
                        AppUtils.showErrorLog(TAG, "search clicked");
                    activeSearchFragment();
                } else
                    pushFragments(GlobalConstants.TAB_SEARCH_BAR, new SearchProducts(), true);

                refreshFragments();
            }
        });

        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectImages();
                image_cart.setImageResource(R.drawable.cart_orange);
                text_cart.setTextColor(ContextCompat.getColor(context, R.color.red));
                if (mStacks.get(GlobalConstants.TAB_CART_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof FragmentCartList))
                        AppUtils.showErrorLog(TAG, "cart clicked");
                    activeCartFragment();
                } else
                    pushFragments(GlobalConstants.TAB_CART_BAR, new FragmentCartList(), true);

                refreshCartFragments();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 511 && resultCode == 512) {
            mTvLocation.setText(data.getStringExtra("location"));
            mTvLocation1.setText(data.getStringExtra("location"));
            AppUtils.setLatitude(context, data.getStringExtra("latitude"));
            AppUtils.setLongitude(context, data.getStringExtra("longitude"));
        }
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

    private void showLogoutBox() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                DashboardActivity.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        AppUtils.setUseremail(context, "");
                        AppUtils.setUserName(context, "");
                        AppUtils.setAuthToken(context, "");
                        mTvUserName.setVisibility(View.GONE);
                        mTVLogout.setVisibility(View.GONE);
                        mTvOrderHistory.setVisibility(View.GONE);
                        mTChangePassword.setVisibility(View.GONE);
                        mTvAddress.setVisibility(View.GONE);
                        mTvLogin.setVisibility(View.VISIBLE);
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();

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
                            changeMenuHeader("Your Location", true);
                            //  refreshHomeFragment();
                        }
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof FragmentCategoriesList) {
                            AppUtils.showLog(TAG, " Current Fragment is FragmentCategoriesList");
                            //  refreshHomeFragment();
                        }
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof FragmentCartList) {
                            AppUtils.showLog(TAG, " Current Fragment is Cart Fragment");
                            //  refreshProfileFragment();
                        }

                       /* if (mStacks.get(mCurrentTab).lastElement() instanceof FragmentHome ||
                                mStacks.get(mCurrentTab).lastElement() instanceof FragmentCategoriesList ||
                                mStacks.get(mCurrentTab).lastElement() instanceof Offers) {
                            manageHeaderVisibitlity(true);
                            manageFooterVisibitlity(true);
                        } else {
                            manageHeaderVisibitlity(false);
                        }*/
                        manageHeaderVisibitlity(true);
                        manageFooterVisibitlity(true);
                        refreshFragments();
                    }
                }
            } else {
                // do nothing.. fragment already handled back button press.
            }
        }
    }

    private void refreshFragments() {
        if (currentFragment instanceof FragmentSelectAddress) {
            ((FragmentSelectAddress) currentFragment).onResume();
        }
        if (currentFragment instanceof Offers) {
            ((Offers) currentFragment).onResume();
        }

        if (currentFragment instanceof SearchProducts) {
            ((SearchProducts) currentFragment).onResume();
        }
        if (currentFragment instanceof FragmentCategoriesList) {
            ((FragmentCategoriesList) currentFragment).onResume();
        }
        if (currentFragment instanceof FragmentProductsAccToCategory) {
            ((FragmentProductsAccToCategory) currentFragment).onResume();
        }
        if (currentFragment instanceof FragmentProductsAccToSubCategory) {
            ((FragmentProductsAccToSubCategory) currentFragment).onResume();
        }
        if (currentFragment instanceof FragmentProductsDetail) {
            ((FragmentProductsDetail) currentFragment).onResume();
        }
        if (currentFragment instanceof FragmentHome) {
            ((FragmentHome) currentFragment).onResume();
        }
    }

    private void refreshCartFragments() {
        if (currentFragment instanceof FragmentCartList) {
            ((FragmentCartList) currentFragment).onResume();
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
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
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
