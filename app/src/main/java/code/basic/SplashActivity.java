package code.basic;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import code.common.AppSignatureHelper;
import code.common.SimpleHTTPConnection;
import code.common.SmsBroadcastReceiver;
import code.database.AppSettings;
import code.main.MainActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

 public class SplashActivity extends BaseActivity {

    String versionName="",versionCode="";
     SmsBroadcastReceiver mSmsBroadcastReceiver;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        
         /*mSmsBroadcastReceiver = new SmsBroadcastReceiver();*/
         getFcmToken();

         Log.v("Mylnaguage","GOof"+AppSettings.getString(AppSettings.language_code));

         if(AppSettings.getString(AppSettings.language_code).isEmpty())
         {
             AppSettings.putString(AppSettings.language_code, "En");
             Log.v("english",AppSettings.getString(AppSettings.language_code));
             AppUtils.SettingLanguage(mActivity);
         }
         else if(AppSettings.getString(AppSettings.language_code).equalsIgnoreCase("hi")){

             AppSettings.putString(AppSettings.language_code, "hi");
             Log.v("hindi",AppSettings.getString(AppSettings.language_code));
             AppUtils.SettingLanguage(mActivity);

         }
         else
             {

             AppSettings.putString(AppSettings.language_code, "En");
             Log.v("english",AppSettings.getString(AppSettings.language_code));
             AppUtils.SettingLanguage(mActivity);
         }

        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
            settingApi();

        }
        else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }
       /*  AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
         appSignatureHelper.getAppSignatures();*/


    }

    private void checkLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AppSettings.getString(AppSettings.userId).isEmpty()) {
                    startActivity(new Intent(mActivity, WelcomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(mActivity, MainActivity.class));
                    finish();
                }
            }
        }, 100);

    }




    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dialog.dismiss();
            }
        });
    }

    private void settingApi() {

        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.appVersion;
        Log.v("settingApi-URL", url);

        AndroidNetworking.get(url)

                .setPriority(Priority.HIGH)
                .setTag("settingApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseSettingJSON(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    private void parseSettingJSON(JSONObject response) {

        Log.d("response ", response.toString());

        AppUtils.hideDialog();

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);

            if (jsonObject.getString("resCode").equals("1")) {

                getVersionInfo();

                if (versionName.equalsIgnoreCase(jsonObject.getString("version").toString())) {
                    checkLogin();
                } else {
                    AlertVersion();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));
            AppSettings.putString(AppSettings.versionName,versionName);


            /*
*/
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



     private void getFcmToken() {

         FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
             @Override
             public void onSuccess(InstanceIdResult instanceIdResult) {
                 String newToken = instanceIdResult.getToken();
                 AppSettings.putString(AppSettings.fcmId,newToken);
                 android.util.Log.e("fcmoken",newToken);

             }
         });
     }
}
