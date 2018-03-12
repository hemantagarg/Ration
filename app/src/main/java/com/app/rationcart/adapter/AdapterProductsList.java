package com.app.rationcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterProductsList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ModelProducts> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    ArrayAdapter<String> product_adapter;

    public AdapterProductsList(Context context, OnCustomItemClicListener lis, ArrayList<ModelProducts> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_product_list, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ((CustomViewHolder) holder).product_name.setText(detail.get(position).getProductName());
            ((CustomViewHolder) holder).product_price.setText(detail.get(position).getProductPrice());

            ((CustomViewHolder) holder).text_price.setText(detail.get(position).getProductPrice() + "");

            if (detail.get(position).isCustomoption()) {
                ((CustomViewHolder) holder).spinner_item.setBackgroundResource(R.drawable.list_bg);
            } else {
                ((CustomViewHolder) holder).spinner_item.setBackgroundColor(Color.argb(28, 204, 204, 204));
            }

            HashMap<String, String> hm = detail.get(position).getSetCustomOption().get(detail.get(position).getCustomPosition());

            try {
                Picasso.with(mContext).load(detail.get(position).getImage())
                        //    .placeholder(R.drawable.placeholder)
                        .into(((CustomViewHolder) holder).item_image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((CustomViewHolder) holder).spiner.setAdapter(product_adapter);
            ((CustomViewHolder) holder).spiner.setSelection(detail.get(position).getSp_position());
            ((CustomViewHolder) holder).spinner_item.setText(hm.get("unit"));

            ((CustomViewHolder) holder).spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    ((CustomViewHolder) holder).spinner_item.setText(((CustomViewHolder) holder).spiner.getSelectedItem().toString());

                    detail.get(position).setSp_position(pos);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ((CustomViewHolder) holder).spinner_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (detail.get(position).isCustomoption()) {
                        listener.onItemClickListener(position, 511);
                    }


                }
            });


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView product_name, product_type, product_price, text_price, spinner_item;
        ImageView add_price, sub_price, item_image;
        Spinner spiner;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.product_name = (TextView) view
                    .findViewById(R.id.teaName);
            this.item_image = (ImageView) view.findViewById(R.id.image_product);
            this.product_type = (TextView) view.findViewById(R.id.teaType);
            this.product_price = (TextView) view
                    .findViewById(R.id.price);
            this.spinner_item = (TextView) view.findViewById(R.id.spinner_text);
            this.text_price = (TextView) view.findViewById(R.id.text_price);
            this.add_price = (ImageView) view.findViewById(R.id.add);
            this.sub_price = (ImageView) view.findViewById(R.id.subtract);
            this.spiner = (Spinner) view.findViewById(R.id.spinner1);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelProducts m1 = (ModelProducts) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;

    }

}
