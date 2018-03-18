package com.app.rationcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.interfaces.OnCustomItemClicListener;
import com.app.rationcart.models.ModelCartDetail;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class OrderSummary extends Activity implements ApiResponse, OnCustomItemClicListener {

    private Activity context;
    private RelativeLayout rl_discount;
    private TextView text_product_quantity, text_sub_total, text_shipping_charges, text_total_amount,
            text_addressname, text_addresscity, text_addresszipcode, txt_delivery_location;
    private String total_items, total_quantity, sub_total, grand_total, shipping_charges, addressid;
    private TextView text_date, text_deliverydate, text_total, text_total_product, text_paytm, text_discount, text_discount_amt;
    private RecyclerView list_time;
    //  AdapterDeliverytime adapterDeliverytime;
    private ImageView debit, cash_ondelivery, card_ondelivery, paytm_image;
    private ArrayList<ModelCartDetail> listDataServices;
    private RelativeLayout rl_checkout;
    private int selectedAddressPosition = 0;
    private int paymentMode = 0;
    private JSONObject commandResult;
    private ArrayList<HashMap<String, String>> transactionDetail;
    private String paymentModeKey = "";
    private boolean isCouponApplied;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_order_summary);

        context = this;
        init();
        listDataServices = new ArrayList<ModelCartDetail>();

//        text_total.setText(AppUtils.convertToTwoDecimalDigit(AppUtils.getTotalprice(context)));
//        text_total_product.setText(AppUtils.getTotalItems(context));
        Intent in = getIntent();
        transactionDetail = new ArrayList<HashMap<String, String>>();

        total_items = in.getExtras().getString("total_items");
        total_quantity = in.getExtras().getString("total_quantity");
        sub_total = in.getExtras().getString("sub_total");
        grand_total = in.getExtras().getString("grand_total");
        shipping_charges = in.getExtras().getString("shipping_charges");
        isCouponApplied = in.getExtras().getBoolean("isCouponApplied");
        addressid = in.getExtras().getString("addressId");

        text_product_quantity.setText(total_quantity);
        text_shipping_charges.setText(shipping_charges);
        text_sub_total.setText(sub_total);
        text_total_amount.setText(grand_total);
        //  text_total.setText(grand_total);
        text_addresscity.setText(in.getExtras().getString("address"));
        text_addressname.setText(in.getExtras().getString("city"));
        text_addresszipcode.setText(in.getExtras().getString("code"));

        if (isCouponApplied == true) {
            rl_discount.setVisibility(View.VISIBLE);
            text_discount.setText("Discount (" + in.getExtras().getString("couponCode") + ") :");
            Double d = in.getDoubleExtra("discountAmt", 0);
            String s = d.toString();
            text_discount_amt.setText(" -" + s);

        }
        setListener();
        text_total.setText(grand_total);
        //  text_total.setText();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        //   getDeliveryTime();

    }

    private void updateDisplay() {
        /*text_deliverydate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(""));*/
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

        text_deliverydate.setText(date);
    }


    private void init() {
        text_discount = findViewById(R.id.text_discount);
        text_discount_amt = findViewById(R.id.text_discount_amt);
        rl_discount = findViewById(R.id.rl_discount);
        text_total_amount = findViewById(R.id.text_total_amount);
        text_product_quantity = findViewById(R.id.text_product_quantity);
        text_shipping_charges = findViewById(R.id.text_shipping_charges);
        text_sub_total = findViewById(R.id.text_sub_total);
        text_addresscity = findViewById(R.id.address1);
        text_addressname = findViewById(R.id.name1);
        text_addresszipcode = findViewById(R.id.zipcode1);
        text_date = findViewById(R.id.btnDate);
        text_deliverydate = findViewById(R.id.text_delivery_date);
        list_time = findViewById(R.id.list_time);
        list_time.setLayoutManager(new LinearLayoutManager(context));
        text_total = findViewById(R.id.total_time);
        paytm_image = findViewById(R.id.bubble6);
        debit = findViewById(R.id.bubble3);
        card_ondelivery = findViewById(R.id.bubble5);
        cash_ondelivery = findViewById(R.id.bubble4);
        rl_checkout = findViewById(R.id.layout_bottom);
        manageHeaderView();

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        HeaderViewManager.getInstance().InitializeHeaderView(context, null, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Order Summary");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);
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
                AppUtils.showLog("onClick", "onClickOfHeaderLeftView");
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void setListener() {
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showDatePicker(1);
            }
        });
        rl_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
             /*   boolean isAddressSelected = false;
                for (int i = 0; i < listDataServices.size(); i++) {

                    if (listDataServices.get(i).getSelection_position() == 1) {
                        isAddressSelected = true;

                        selectedAddressPosition = i;
                        break;
                    }

                }
                if (isAddressSelected) {
                    if (paymentMode == 0) {
                        Toast.makeText(OrderSummary.this, "Please select atleast one Payment Mode", Toast.LENGTH_SHORT).show();
                    } else {

                     *//*   List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("quote_id", quote_id));
                        params.add(new BasicNameValuePair("address_id", addressid));
                        params.add(new BasicNameValuePair("payment_method", paymentModeKey));
                        params.add(new BasicNameValuePair("uid", AppUtils.getUserId(context)));
                        params.add(new BasicNameValuePair("delivery_date", listDataServices.get(selectedAddressPosition).getSelecteddate()));
                        //   params.add(new BasicNameValuePair("delivery_slot", listDataServices.get(selectedAddressPosition).getDeliverySlot()));
                        params.add(new BasicNameValuePair("delivery_slot", listDataServices.get(selectedAddressPosition).getFromtime() + "-" +
                                listDataServices.get(selectedAddressPosition).getTotime()));
                        String url = getString(R.string.url_base_new)
                                + getString(R.string.url_getcreateorder);
                        new AsyncPostDataResponse(OrderSummary.this, 2, params, url);

                        AppUtils.setTotalItems(context, "0");
                        AppUtils.setTotalprice(context, "0.00");*//*

                    }

                } else {
                    Toast.makeText(OrderSummary.this, "Please select atleast one delivery time", Toast.LENGTH_SHORT).show();
                }
*/
            }
        });

        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMode = 1;
                getPaymentMode();


            }
        });

        card_ondelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMode = 2;
                getPaymentMode();


            }
        });
        cash_ondelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMode = 3;
                getPaymentMode();


            }
        });
        paytm_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMode = 4;
                getPaymentMode();


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        text_total.setText(AppUtils.convertToTwoDecimalDigit(AppUtils.getTotalprice(context)));
//        text_total_product.setText(AppUtils.getTotalItems(context));

    }

    private void placeOrder() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                //http://stackmindz.com/dev/rationcart/api/placeorder?user_id=13
                // &token=355241080144570&total_amount=408&quantity=5&address_id=8
                String url = JsonApiHelper.BASEURL + JsonApiHelper.PLACE_ORDER + "token=" + AppUtils.getImeiNo(context)
                        + "&user_id=" + AppUtils.getUserId(context) + "&total_amount="
                        + grand_total + "&quantity=" + total_quantity + "&address_id=" + addressid;
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPaymentMode() {

        switch (paymentMode) {
            case 1:
                paymentModeKey = "firstdataicici";
                debit.setImageResource(R.drawable.selected);
                card_ondelivery.setImageResource(R.drawable.non_selected);
                cash_ondelivery.setImageResource(R.drawable.non_selected);
                paytm_image.setImageResource(R.drawable.non_selected);

                break;
            case 2:
                paymentModeKey = "checkmo";
                debit.setImageResource(R.drawable.non_selected);
                card_ondelivery.setImageResource(R.drawable.selected);
                cash_ondelivery.setImageResource(R.drawable.non_selected);
                paytm_image.setImageResource(R.drawable.non_selected);
                break;
            case 3:
                paymentModeKey = "cashondelivery";
                debit.setImageResource(R.drawable.non_selected);
                card_ondelivery.setImageResource(R.drawable.non_selected);
                cash_ondelivery.setImageResource(R.drawable.selected);
                paytm_image.setImageResource(R.drawable.non_selected);
                break;
            case 4:
                paymentModeKey = "paytm_cc";
                debit.setImageResource(R.drawable.non_selected);
                card_ondelivery.setImageResource(R.drawable.non_selected);
                cash_ondelivery.setImageResource(R.drawable.non_selected);
                paytm_image.setImageResource(R.drawable.selected);
                break;
        }

    }


    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 2 && !(listDataServices.get(position).getDisabled().equalsIgnoreCase("disabled"))) {
            for (int i = 0; i < listDataServices.size(); i++) {
                Log.e("size_alldetail", listDataServices.size() + "");
                listDataServices.get(i).setSelection_position(0);
            }
            Log.e("clickposition", listDataServices.get(position) + "");

            listDataServices.get(position).setSelection_position(1);

            //   adapterDeliverytime.notifyDataSetChanged();
        }


        //  if (listDataServices.get(position).getDisabled().equalsIgnoreCase("disabled"))


    }


/*
    public void showDatePicker(final int i) {

        // Initializiation

        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog
                .Builder dialogBuilder = new AlertDialog.Builder(
                OrderSummary.this);
        // dialogBuilder.setTitle("Choose Date");
        View customView = inflater.inflate(R.layout.datepicker_layout, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker = (DatePicker) customView
                .findViewById(R.id.dialog_datepicker);
        final TextView dateTextView = (TextView) customView
                .findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                // new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy",
                Locale.ENGLISH);

        // Minimum date

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        try {

            datePicker.setMinDate(maxDate.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();

        }
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        if (i == 1) {
            try {
                Date choosenDateFromUI = formatter.parse(text_date.getText()
                        .toString());
                choosenDate.setTime(choosenDateFromUI);
                year = choosenDate.get(Calendar.YEAR);
                month = choosenDate.get(Calendar.MONTH);
                day = choosenDate.get(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(dateViewFormatter.format(dateToDisplay.getTime()));
        // Buttons
        dialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        final AlertDialog.Builder ok = dialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(datePicker.getYear(), datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        if (i == 1) {
                            text_deliverydate.setText(dateViewFormatter.format(choosen
                                    .getTime()));

                            getDeliveryTime();


                        }


                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = dialogBuilder.create();

        // Initialize datepicker in dialog atepicker
        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(dateViewFormatter
                                .format(choosenDate.getTime()));

                        dateTextView.setTextColor(Color.parseColor("#000000"));
                        ((Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE))
                                .setEnabled(true);
                    }
                });

        dialog.show();
    }
*/


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                 //   JSONArray data = commandResult.getJSONArray("data");

                    Toast.makeText(context, "Your Order placed succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
              /*      listDataServices.removeAll(listDataServices);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        allDetail = new ModelCartDetail();
                        allDetail.setSelecteddate(jo.getString("selecteddate"));
                        allDetail.setDate(jo.getString("date"));
                        allDetail.setFromtime(jo.getString("fromtime"));
                        allDetail.setTotime(jo.getString("totime"));
                        allDetail.setChecked(jo.getString("checked"));
                        allDetail.setDisabled(jo.getString("disabled"));
                        allDetail.setStyle(jo.getString("style"));
                        allDetail.setValue(jo.getString("value"));
                        allDetail.setSelection_position(0);
                        allDetail.setDelivery_date(jo.getString("delivery_date"));
                        allDetail.setDeliverySlot(jo.getString("delivery_slot"));

                        listDataServices.add(allDetail);

                    }
                    adapterDeliverytime = new AdapterDeliverytime(getApplicationContext(), this, listDataServices);
                    list_time.setAdapter(adapterDeliverytime);*/

                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
