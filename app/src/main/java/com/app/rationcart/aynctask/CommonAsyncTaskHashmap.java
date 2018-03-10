package com.app.rationcart.aynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.rationcart.R;
import com.app.rationcart.iclasses.WebserviceAPIErrorHandler;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.GlobalConstants;
import com.app.rationcart.models.VolleyErrorModel;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Hemanta on 11/7/2016.
 */
public class CommonAsyncTaskHashmap {

    private ProgressDialog pd;
    private RequestQueue queue;
    private Context context;
    private ApiResponse listener;
    int method;

    public CommonAsyncTaskHashmap(int method, Context context, ApiResponse response) {

        queue = Volley.newRequestQueue(context);
        this.context = context;
        listener = response;
        this.method = method;
        pd = new ProgressDialog(context);
        pd.setMessage("Processing ... ");
        pd.setCancelable(false);

    }


    public void getqueryJsonNoProgress(String url, JSONObject jsonObject, int methodType) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + jsonObject);
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(
                methodType,
                url,
                jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
                try {
                    if (response != null) {

                        if (listener != null)
                            listener.onPostSuccess(method, response);
                    } else {
                        if (listener != null)
                            // listener.onPostRequestFailed(method, "Null data from server.");
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.problem_server),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    if (listener != null) {

                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", AppUtils.getAuthToken(context));
                return params;
            }
        };
        // Adding request to request queue
        queue.add(mJsonRequest);

        mJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void getqueryJsonbjectNoProgress(String url, JSONObject jsonObject, int MethodType) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + "  " + jsonObject);
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(
                MethodType,
                url,
                jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
                try {
                    if (response != null) {

                        if (listener != null)
                            listener.onPostSuccess(method, response);
                    } else {
                        if (listener != null)
                            // listener.onPostRequestFailed(method, "Null data from server.");
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.problem_server),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // hide the progress dialog
                try {
                    if (listener != null) {

                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", AppUtils.getAuthToken(context));
                return params;
            }
        };
        // Adding request to request queue
        queue.add(mJsonRequest);

        mJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void getqueryJsonbject(String url, JSONObject jsonObject, int MethodType) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + "  " + jsonObject);
        pd.show();
        AppUtils.onKeyBoardDown(context);
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(
                MethodType,
                url,
                jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
                if (pd != null)
                    pd.cancel();
                try {
                    if (response != null) {

                        if (listener != null)
                            listener.onPostSuccess(method, response);
                    } else {
                        if (listener != null)
                            // listener.onPostRequestFailed(method, "Null data from server.");
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.problem_server),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // hide the progress dialog
                if (pd != null)
                    pd.cancel();
                try {
                    if (listener != null) {

                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", AppUtils.getAuthToken(context));
                return params;
            }
        };
        // Adding request to request queue
        queue.add(mJsonRequest);

        mJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void getqueryNoProgress(String url) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url);

        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());

                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

                try {
                    if (listener != null) {

                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

// Adding request to request queue
        queue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
