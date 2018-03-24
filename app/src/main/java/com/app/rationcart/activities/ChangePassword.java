package com.app.rationcart.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.rationcart.R;
import com.app.rationcart.aynctask.CommonAsyncTaskHashmap;
import com.app.rationcart.fragment.BaseFragment;
import com.app.rationcart.iclasses.HeaderViewManager;
import com.app.rationcart.interfaces.ApiResponse;
import com.app.rationcart.interfaces.HeaderViewClickListener;
import com.app.rationcart.interfaces.JsonApiHelper;
import com.app.rationcart.utils.AppUtils;

import org.json.JSONObject;

/**
 * Created by hemanta on 02-08-2017.
 */

public class ChangePassword extends BaseFragment implements ApiResponse {

    public static ChangePassword changePassword;
    private Button btnSubmit;
    private EditText edtold_password, edt_newpassword, edtconfirmpassword;
    private Activity mActivity;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_change_password, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        init(view);
        //  setListener();
        //    manageHeaderView();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                        submitRequest();
                    } else {
                        Toast.makeText(mActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
            }
        });
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(mActivity, null, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, getResources().getString(R.string.change_password));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);
        HeaderViewManager.getInstance().setLogoView(false);
        HeaderViewManager.getInstance().setProgressLoader(false, false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            //http://stackmindz.com/dev/rationcart/api/change-password?
            // user_id=12&current_pwd=123456&new_pwd=12345&confirm_pwd=12345
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CHANGE_PASSWORD + "user_id=" +
                    AppUtils.getUserId(mActivity) + "&new_pwd=" + edt_newpassword.getText().toString()
                    + "&current_pwd=" + edt_newpassword.getText().toString() + "&confirm_pwd=" + edtconfirmpassword.getText().toString();
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    private void init(View view) {

        edtold_password = view.findViewById(R.id.edtold_password);
        edt_newpassword = view.findViewById(R.id.edt_newpassword);
        edtconfirmpassword = view.findViewById(R.id.edtconfirmpassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                        //  submitRequest();
                    } else {
                        Toast.makeText(mActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
            }
        });

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    mActivity.onBackPressed();
                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}


