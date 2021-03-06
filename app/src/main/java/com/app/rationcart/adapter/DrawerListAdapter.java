package com.app.rationcart.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.models.DrawerListModel;

import java.util.HashMap;
import java.util.List;


public class DrawerListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles

    private HashMap<String, List<DrawerListModel>> _listDataChild;

    public DrawerListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<DrawerListModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final DrawerListModel model = (DrawerListModel) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawerlistitem, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.lblListItem);
        Log.e("childclick adapter", groupPosition + childPosition + "*" + model.getName());
        text.setText(model.getName());

      /*  convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dashboard.getInstance().pushFragments(AppConstant.CURRENT_SELECTED_TAB, new Fragment_AvtarMyTeam(), true);
            }
        });*/

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            Log.e("childlidtsize : ", "&&" + childList.size());
            return childList.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawerlistgroup, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView image = (ImageView) convertView
                .findViewById(R.id.image_arrow);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        if (isExpanded) {
            List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
            if (childList != null && !childList.isEmpty()) {
                image.setImageResource(R.drawable.arrow_down);
            }
        } else {
            image.setImageResource(R.drawable.arrow_right);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


