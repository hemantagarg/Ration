package com.app.rationcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.interfaces.OnCustomItemClicListener;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterCustomList extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<String> weight;
    OnCustomItemClicListener listener;
    int selectedPosition = 0;
    ArrayList<String> price;
    ArrayList<String> sprice;

    public AdapterCustomList(Context mContext,
                             OnCustomItemClicListener lis, ArrayList<String> list, ArrayList<String> price, ArrayList<String> sprice, int pos) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        selectedPosition = pos;
        this.weight = list;
        this.price = price;
        this.sprice = sprice;
        this.listener = lis;
    }


    public class ViewHolder {
        TextView text_name, text_price, text_sprice;
    }

    @Override
    public int getCount() {

        return weight.size();

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_customlistweight, null, false);
            holder.text_name = (TextView) convertView.findViewById(R.id.text_name);
            holder.text_price = (TextView) convertView.findViewById(R.id.price);
            holder.text_sprice = (TextView) convertView.findViewById(R.id.specialprice);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.e("selectedPosition","**"+selectedPosition);
        Log.e("position","**"+position);
        if (selectedPosition == position) {
            holder.text_name.setTextColor(Color.argb(255, 246, 152, 121));
        } else {
            holder.text_name.setTextColor(Color.argb(255, 85, 85, 85));
        }
        holder.text_name.setText(weight.get(position) + "");

        if (!sprice.get(position).equalsIgnoreCase("") && !sprice.get(position).equalsIgnoreCase("0") && !sprice.get(position).equalsIgnoreCase("0.00")) {


            String dataspan = "Rs " + price.get(position);
            Spannable wordtoSpan = new SpannableString(dataspan);

            wordtoSpan.setSpan(new StrikethroughSpan(), 0, wordtoSpan.length(), 0);
            holder.text_price.setText(wordtoSpan);
            holder.text_sprice.setText(("Rs " + sprice.get(position)));

        } else {

            holder.text_price.setText("Rs " + price.get(position));
            holder.text_sprice.setText("");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.onItemClickListener(position, 11);
            }
        });

        return convertView;
    }
}