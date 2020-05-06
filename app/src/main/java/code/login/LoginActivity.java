package code.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import code.common.SimpleHTTPConnection;
import code.common.SmsBroadcastReceiver;
import code.database.AppSettings;
import code.main.MainActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    //EditText
    EditText etMobile;

    //TextView
    Button btnSubmit;
    SmsBroadcastReceiver mSmsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*mSmsBroadcastReceiver = new SmsBroadcastReceiver();*/

        findViewById();
        /*etMobile.setText("8423055442");*/
    }

    private void findViewById() {

        AppUtils.checkPermissions(mActivity);

        //EditText
        etMobile = findViewById(R.id.etMobile);

        //Button
        btnSubmit = findViewById(R.id.btnSubmit);

        //setOnClickListener
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSubmit:

                if(!AppUtils.isValidMobileNo(etMobile.getText().toString().trim()))
                {
                    AppUtils.showToastSort(mActivity, getString(R.string.errorMobile));
                }
                else if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
                    sendOtpApi();
                } else {
                    AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                }

                break;

            default:

                break;
        }

    }


    //sendOtpApi
    private void sendOtpApi() {

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.sendOtp;
        Log.v("sendOtpApi-URL", url);

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {

            jsonData.put("mobileNumber", etMobile.getText().toString().trim());
            json.put(AppConstants.projectName, jsonData);
            Log.v("sendOtpApi", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .setTag("sendOtpApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseSendOtpJSON(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(mActivity,String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    //parseSendOtpJSON
    private void parseSendOtpJSON(JSONObject response){

        AppUtils.hideDialog();

        Log.d("response ", response.toString());

        try {

            JSONObject jsonObject= response.getJSONObject(AppConstants.projectName);
            jsonObject.getString( "mobileNumber" );
            Log.d( "mobileNumberll",jsonObject.getString( "mobileNumber" ) );

            if(jsonObject.getString("resCode").equals("1"))

               {

                   AppSettings.putString(AppSettings.mobile,etMobile.getText().toString().trim());
                   startActivity(new Intent(mActivity, OtpVerificationActivity.class));
                   finish();
               }


            else
            {
                AppUtils.showToastSort(mActivity, jsonObject.getString("resMsg"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
