package com.app.rationcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.rationcart.R;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelOrderDetail;

import java.util.ArrayList;


public class AdapterNotificatonList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ModelOrderDetail> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterNotificatonList(Context context, OnCustomItemClicListener lis, ArrayList<ModelOrderDetail> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_order_list, parent, false);

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

            ModelOrderDetail m1 = detail.get(position);

            ((CustomViewHolder) holder).text_message.setText(m1.getMessage());
            ((CustomViewHolder) holder).text_date.setText(m1.getCreateDate());

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_message, text_date;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_message = view
                    .findViewById(R.id.text_message);
            this.text_date = view
                    .findViewById(R.id.text_date);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelOrderDetail m1 = detail.get(position);
        if (m1.getRowType() == 1) {
            return VIEW_ITEM;
        } else if (m1.getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;

    }

}
