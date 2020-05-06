package code.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import code.common.OtpReceivedInterface;
import code.common.SimpleHTTPConnection;
import code.common.SmsBroadcastReceiver;
import code.database.AppSettings;
import code.main.MainActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class OtpVerificationActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OtpReceivedInterface, TextWatcher  {

    //TextView
    TextView tvMobileNo, tvResend;

    //EditText
    EditText etCode1, etCode2, etCode3, etCode4, etCode5, etCode6;

    int check = 0;
   TextView tvTimerText;
    //Button
    Button btnSubmit;
    ImageView Timer;

    SmsBroadcastReceiver mSmsBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_otp_verification );

        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mSmsBroadcastReceiver.setOnOtpListeners(OtpVerificationActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        startSMSListener();

        findViewById();
        hitAddToCart();

    }

    private void findViewById() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( mActivity, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e( "newToken", newToken );

                AppSettings.putString( AppSettings.fcmId, newToken );
            }
        } );




        //TextView
        tvMobileNo = findViewById( R.id.tvMobileNo );
        tvResend = findViewById( R.id.tvResend );
        tvTimerText = (TextView) findViewById(R.id.tvTimer);
        //Button
        btnSubmit = findViewById( R.id.btnSubmit );
        tvTimerText.setVisibility(View.VISIBLE);

        //EditText
        etCode1 = findViewById( R.id.etCode1 );
        etCode2 = findViewById( R.id.etCode2 );
        etCode3 = findViewById( R.id.etCode3 );
        etCode4 = findViewById( R.id.etCode4 );
        etCode5 = findViewById( R.id.etCode5 );
        etCode6 = findViewById( R.id.etCode6 );




        //EditText


        tvTimerText = (TextView) findViewById(R.id.tvTimer);

        Timer = (ImageView) findViewById(R.id.ivTimer);

        //Visibility
        Timer.setVisibility(View.GONE);
        tvTimerText.setVisibility(View.VISIBLE);




        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tvTimerText.setText("" + String.format("%d:%02d",

                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -

                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


            }

            public void onFinish() {
                tvTimerText.setVisibility(View.GONE);
                tvResend.setVisibility(View.VISIBLE);
            }
        }.start();


        tvMobileNo.setText( getString( R.string.countryCode ) + AppSettings.getString( AppSettings.mobile ) );

        etCode1.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode2.requestFocus();
            }
        } );

        etCode2.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode3.requestFocus();
            }
        } );

        etCode3.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode4.requestFocus();
            }
        } );

        etCode4.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode5.requestFocus();
            }
        } );

        etCode5.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode6.requestFocus();
            }
        } );

        etCode6.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        } );

        etCode1.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode1.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    }

                }
                return false;
            }
        } );

        etCode2.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode2.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode3.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode3.getText().toString().trim().isEmpty()) {
                        etCode2.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode4.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode4.getText().toString().trim().isEmpty()) {
                        etCode3.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode5.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode5.getText().toString().trim().isEmpty()) {
                        etCode4.requestFocus();
                    }

                }
                return false;
            }
        } );

        etCode6.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode6.getText().toString().trim().isEmpty()) {
                        etCode5.requestFocus();
                    }

                }
                return false;
            }
        } );




        btnSubmit.setOnClickListener( this );
        tvResend.setOnClickListener( this );



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSubmit:

                String otp = etCode1.getText().toString().trim() +
                        etCode2.getText().toString().trim() +
                        etCode3.getText().toString().trim() +
                        etCode4.getText().toString().trim() +
                        etCode5.getText().toString().trim() +
                        etCode6.getText().toString().trim();

                if (otp.length() != 6) {
                    AppUtils.showToastSort( mActivity, getString( R.string.errorOtp ) );
                } else if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
                    verifyOtpApi( otp );
                } else {
                    AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
                }

                break;

            case R.id.tvResend:

                if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
                    resendOtpApi();
                    startSMSListener();
                } else {
                    AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
                }

                break;

            default:

                break;
        }


    }

    //resendOtpApi
    private void resendOtpApi() {

        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );

        String url = AppUrls.sendOtp;
        Log.v( "sendOtpApi-URL", url );

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {

            jsonData.put( "mobileNumber", AppSettings.getString( AppSettings.mobile ) );

            json.put( AppConstants.projectName, jsonData );

            Log.v( "sendOtpApi", json.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post( url )
                .addJSONObjectBody( json )
                .setPriority( Priority.HIGH )
                .setTag( "sendOtpApi" )
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResendOtpJSON( response );
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort( mActivity, String.valueOf( error.getErrorCode() ) );
                            Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                            Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        } else {
                            AppUtils.showToastSort( mActivity, String.valueOf( error.getErrorDetail() ) );
                        }
                    }
                } );
    }

    //parseResendOtpJSON
    private void parseResendOtpJSON(JSONObject response) {

        AppUtils.hideDialog();

        Log.d( "response ", response.toString() );

        try {
            JSONObject jsonObject = response.getJSONObject( AppConstants.projectName );

            if (jsonObject.getString("resCode").equals("1")) {


                tvTimerText.setVisibility(View.VISIBLE);
                tvResend.setVisibility(View.GONE);
                new CountDownTimer(30000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {
                        tvTimerText.setText("" + String.format("%d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


                    }

                    public void onFinish() {
                        tvTimerText.setVisibility(View.GONE);
                        tvResend.setVisibility(View.VISIBLE);
                    }
                }.start();

            }
            else
            {
                AppUtils.showToastSort(mActivity, jsonObject.getString("resMsg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //verifyOtpApi
    private void verifyOtpApi(String otp) {

        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );

        String url = AppUrls.verifyOtp;
        Log.v( "verifyOtpApi-URL", url );

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {

            jsonData.put( "mobileNumber", AppSettings.getString( AppSettings.mobile ) );
            jsonData.put( "deviceId", AppUtils.getDeviceID( mActivity ) );
            jsonData.put( "fcmId", AppSettings.getString( AppSettings.fcmId ) );
            jsonData.put( "otp", otp );

            json.put( AppConstants.projectName, jsonData );

            Log.v( "verifyOtpApi", json.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post( url )
                .addJSONObjectBody( json )
                .setPriority( Priority.HIGH )
                .setTag( "verifyOtpApi" )
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseVerifyOtpJSON( response );
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort( mActivity, String.valueOf( error.getErrorCode() ) );
                            Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                            Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        } else {
                            AppUtils.showToastSort( mActivity, String.valueOf( error.getErrorDetail() ) );
                        }
                    }
                } );
    }

    //parseVerifyOtpJSON
    private void parseVerifyOtpJSON(JSONObject response) {

        AppUtils.hideDialog();

        Log.d( "response ", response.toString() );

        try {
            JSONObject jsonObject = response.getJSONObject( AppConstants.projectName );

            if (jsonObject.getString( "resCode" ).equals( "1" )) {


                AppSettings.putString( AppSettings.userId, jsonObject.getString( "userId" ) );
                AppSettings.putString( AppSettings.profilename, jsonObject.getString( "userName" ) );
                AppSettings.putString( AppSettings.profilePic, jsonObject.getString( "profilePic" ) );
                AppSettings.putString( AppSettings.loginId, jsonObject.getString( "loginManagerId" ) );
                AppSettings.putString( AppSettings.total_count, jsonObject.getString( "totalItem" ) );
                AppSettings.putString( AppSettings.userMobile, jsonObject.getString( "mobileNumber" ) );
                startActivity( new Intent( mActivity, MainActivity.class ) );
                finish();
            } else {
                AppSettings.putString( AppSettings.total_count,"0" );
                AppUtils.showToastSort(mActivity, jsonObject.getString("resMsg"));
            }

        } catch (JSONException e) {

                AppSettings.putString( AppSettings.total_count,"0" );


            e.printStackTrace();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith( "android.webkit." )) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen( scrcoords );
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService( Context.INPUT_METHOD_SERVICE )).hideSoftInputFromWindow( (this.getWindow().getDecorView().getApplicationWindowToken()), 0 );
        }
        return super.dispatchTouchEvent( ev );
    }

    private void hitAddToCart() {
        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );
        String url = AppUrls.AddToCart;
        Log.v( "getSabCategoryApi-URL", url );
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            json.put( AppConstants.projectName, jsonObject );
            jsonObject.put( "userId", AppSettings.getString( AppSettings.userId ) );
            jsonObject.put( "productId", "" );
            jsonObject.put( "quantity", "" );
            jsonObject.put( "size", "" );
            jsonObject.put( "addedById", "" );
            jsonObject.put( "addedType", "" );
            Log.v( "finddObject", String.valueOf( json ) );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post( url )
                .addJSONObjectBody( json )
                .setPriority( Priority.HIGH )
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.hideDialog();
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );

                            String resCode = jsonObject1.getString( "resCode" );
                            String resMsg = jsonObject1.getString( "resMsg" );
                            int total_count = Integer.parseInt( jsonObject1.getString( "total_count" ) );
                            String total=AppSettings.getString( AppSettings.total );
                            Log.v( "dsdhs", String.valueOf( total_count ) );

                            AppSettings.putString( AppSettings.addedById, jsonObject1.getString( "addedById" ) );
                            AppSettings.putString( AppSettings.addedType, jsonObject1.getString( "addedType" ) );

                            if (resCode.equals( "1" )) {

                                AppSettings.putString( AppSettings.total_count, total);

                            }

                            else

                            {


                                AppSettings.putString( AppSettings.total_count,"" );

                            }

                        } catch (JSONException e) {

                            AppSettings.putString( AppSettings.total_count, "");

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();

                        Log.v( "ggfh", String.valueOf( anError ) );


                    }

                } );


    }


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                layoutInput.setVisibility(View.GONE);
//                layoutVerify.setVisibility(View.VISIBLE);

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }
    @Override
    public void onOtpReceived(String otp) {
        Log.v("", otp);
//        <#> Your Seegro OTP is 561080 tY1fNkdjSSp
        String otpVal= otp.substring(otp.indexOf("is:")+2,otp.lastIndexOf(" "));
        Log.v("otttp",otp);
        System.out.println(otpVal);
        etCode1.setText(otpVal.substring(2,3));
        etCode2.setText(otpVal.substring(3,4));
        etCode3.setText(otpVal.substring(4,5));
        etCode4.setText(otpVal.substring(5,6));
        etCode5.setText(otpVal.substring(6,7));
        etCode6.setText(otpVal.substring(7,8));

//        inputMobileNumber.setText(otp);
    }

    @Override
    public void onOtpTimeout() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
