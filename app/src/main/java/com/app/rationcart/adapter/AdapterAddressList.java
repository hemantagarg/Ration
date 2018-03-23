package com.app.rationcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelAddressDetail;

import java.util.ArrayList;


public class AdapterAddressList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ModelAddressDetail> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterAddressList(Context context, OnCustomItemClicListener lis, ArrayList<ModelAddressDetail> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_address, parent, false);

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
            progressBar = v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelAddressDetail m1 = detail.get(position);

            ((CustomViewHolder) holder).text_address.setText(m1.getAddress());
            ((CustomViewHolder) holder).mobilenumber.setText(m1.getMobileno());
            ((CustomViewHolder) holder).text_name.setText(m1.getName() + "  " + m1.getMobileno());
            ((CustomViewHolder) holder).text_zipcode.setText(m1.getCity() + "  " + m1.getZipcode());
            if (m1.getSelection_position() == 1) {
                ((CustomViewHolder) holder).radio_button.setImageResource(R.drawable.selected);
            } else {
                ((CustomViewHolder) holder).radio_button.setImageResource(R.drawable.non_selected);
            }
            ((CustomViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 1);
                }
            });

            ((CustomViewHolder) holder).radio_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 2);
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
        TextView text_name, text_address, text_zipcode,mobilenumber;
        ImageView delete, radio_button;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_address = view
                    .findViewById(R.id.address1);
            this.delete = view.findViewById(R.id.delete);
            this.text_name = view.findViewById(R.id.txt_name);
            this.mobilenumber = view.findViewById(R.id.mobilenumber);
            this.text_zipcode = view
                    .findViewById(R.id.zipcode1);
            this.radio_button = view.findViewById(R.id.bubble);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelAddressDetail m1 = detail.get(position);
        if (m1.getRowType() == 1) {
            return VIEW_ITEM;
        } else if (m1.getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;

    }

}
