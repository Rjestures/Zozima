package code.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import code.common.SimpleHTTPConnection;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class NotificationDetail extends BaseActivity {
    //TextView
    TextView tvDate;
    TextView tvName;
    TextView tvDesc;


    //RelativeLayout
    RelativeLayout rrView;

    //ImageView
    ImageView ivIcon;
    ImageView ivback;
    ImageView ivloader;

    //String
    String message;
    String title;
    String image;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        ivIcon = findViewById(R.id.imageView21);
        ivback = findViewById(R.id.ivback);

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);
        //TextView

        tvName = findViewById(R.id.tvName);
        tvDesc = findViewById(R.id.tvDesc);
        tvDate = findViewById(R.id.tvDate);
        rrView = findViewById(R.id.rrView);

        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
            NotificationDetailApi();
        } else {
            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    //***************************** Notification Detail Api ******************************//

    private void NotificationDetailApi() {

        Log.v("LoginApi", AppUrls.NotificationDetails);

        JSONObject json = new JSONObject();
        JSONObject json_data = new JSONObject();

        try {
            json_data.put("notification_id", id);

            json.put(AppConstants.projectName, json_data);
            Log.v("LoginApi", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppUrls.NotificationDetails)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJsondata(response);
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
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorCode()));
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            //Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    private void parseJsondata(JSONObject response) {

        ivloader.setVisibility(View.GONE);

        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
            if (jsonObject.getString("resCode").equals("1")) {
                title = jsonObject.getString("title");
                message = jsonObject.getString("push_msg");
                image = jsonObject.getString("image");

                tvName.setText(title);
                tvDesc.setText(message);
                if (!jsonObject.getString("image").isEmpty()) {
                    Picasso.get().load(jsonObject.getString("image")).placeholder(R.mipmap.logo_grey) .into(ivIcon);
                }
            } else {
                AppUtils.showToastSort(mActivity, String.valueOf(jsonObject.getString("res_msg")));
            }
        } catch (Exception e) {
            AppUtils.showToastSort(mActivity, String.valueOf(e));
        }
        AppUtils.hideDialog();
    }

}
