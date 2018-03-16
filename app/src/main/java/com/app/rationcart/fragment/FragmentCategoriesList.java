package com.app.rationcart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.rationcart.R;
import com.app.rationcart.activities.DashboardActivity;
import com.app.rationcart.adapter.DrawerListAdapter;
import com.app.rationcart.models.DrawerListModel;
import com.app.rationcart.utils.AppConstant;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/****************************************************************
 * FragmentCategoriesList.java

 ****************************************************************/
public class FragmentCategoriesList extends BaseFragment {

    private ExpandableListView expendableView;
    public static FragmentCategoriesList fragmentCategoriesList;
    private LinkedHashMap<String, List<DrawerListModel>> alldata;
    private ArrayList<String> groupnamelist;
    private ArrayList<String> groupnamelistId;
    private DrawerListAdapter listAdapter;
    private Activity context;

    public static FragmentCategoriesList getInstance() {
        if (fragmentCategoriesList == null)
            fragmentCategoriesList = new FragmentCategoriesList();
        return fragmentCategoriesList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categoris, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.getInstance().manageFooterVisibitlity(true);
        DashboardActivity.getInstance().manageHeaderVisibitlity(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        alldata = new LinkedHashMap<>();
        groupnamelist = new ArrayList<>();
        groupnamelistId = new ArrayList<>();
        expendableView = view.findViewById(R.id.expendableView);
        listAdapter = new DrawerListAdapter(context, groupnamelist, alldata);
        expendableView.setAdapter(listAdapter);
        setData();
        setListener();
    }

    private void setListener() {

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
                DashboardActivity.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, fragmentAvtar_details, true);
                return true;
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
                }
                return false;
            }
        });
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

}
