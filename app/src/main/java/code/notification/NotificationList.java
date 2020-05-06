package code.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class NotificationList extends BaseActivity {

    //ImageView
    ImageView ivback, ivloader;
    //Recyclerview
    RecyclerView recyclerview;
    ///LinearLayout
    LinearLayout llsave;
    //TExtview
    TextView tvUnselecvt;
    //Adapter
    CategoryFilterAdapter adapterr;
    private ArrayList<HashMap<String, String>> arrNotificationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        findViewById();
        if (SimpleHTTPConnection.isNetworkAvailable()) {
            arrNotificationList.clear();
            getNotificationListApi();
        } else {
            Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
        }

    }


    private void findViewById() {
        recyclerview = findViewById(R.id.recyclerview);
        llsave = findViewById(R.id.llsave);
        tvUnselecvt = findViewById(R.id.tvUnselecvt);
        ivback = findViewById(R.id.ivback);

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);

        llsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveNotificationType();
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tvUnselecvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                arrNotificationList.clear();
                hitFilterApii();
                arrNotificationList.clear();
                SaveNotificationType();
                arrNotificationList.clear();
                getNotificationListApi();


            }
        });



    }

    private void getNotificationListApi() {

        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.GetAllNotificationType;

        Log.v("getNotification", url);

        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .setTag("getNotificationListApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseGetCategoryJSONN(response);
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

    //parseGetCategoryJSON
    private void parseGetCategoryJSONN(JSONObject response) {

        AppUtils.hideDialog();
        ivloader.setVisibility(View.GONE);

        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
            if (jsonObject.getString("resCode").equals("1")) {

                JSONArray jsonArray = jsonObject.getJSONArray("NotificationType");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> hashlist = new HashMap();

                    hashlist.put("id", arrayJSONObject.getString("id"));

                    hashlist.put("name", arrayJSONObject.getString("name"));

                    if (arrayJSONObject.getString("status").equalsIgnoreCase("1"))
                        hashlist.put("status", "1");
                    else
                        hashlist.put("status", "0");

                    arrNotificationList.add(hashlist);
                }

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);

                adapterr = new CategoryFilterAdapter(mActivity, arrNotificationList);

                recyclerview.setLayoutManager(layoutManager);

                recyclerview.setAdapter(adapterr);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SaveNotificationType() {
        AppUtils.showRequestDialog(mActivity);
        Log.v("notificationlist", AppUrls.SaveNotificationType);
        JSONArray category = new JSONArray();
        JSONObject maindata = new JSONObject();
        JSONObject result = new JSONObject();

        try {

            for (int ll = 0; ll < arrNotificationList.size(); ll++) {

                if (arrNotificationList.get(ll).get("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObjectCategory = new JSONObject();
                    jsonObjectCategory.put("id", arrNotificationList.get(ll).get("id"));
                    category.put(jsonObjectCategory);
                }


            }

            maindata.put("userId", AppSettings.getString(AppSettings.userId));

            maindata.put("NotificationType", category);

            result.put(AppConstants.projectName, maindata);
            Log.v("currentData", maindata.toString());

            Log.v("jhkg", String.valueOf(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppUrls.SaveNotificationType)
                .addJSONObjectBody(result)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.hideDialog();

                        Log.v("jggghjgh", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
                            if (jsonObject.getString("resCode").equals("1")) {
                             adapterr.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();

                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }

    private void hitFilterApii() {
        AppUtils.showRequestDialog(mActivity);
        Log.v("GetfiltListApi", AppUrls.SaveNotificationType);
        JSONArray category = new JSONArray();
        JSONObject maindata = new JSONObject();
        JSONObject result = new JSONObject();

        try {

            for (int ll = 0; ll < arrNotificationList.size(); ll++) {

                if (arrNotificationList.get(ll).get("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObjectCategory = new JSONObject();
                    jsonObjectCategory.put("id", arrNotificationList.get(ll).get("id"));
                    category.put(jsonObjectCategory);
                }


            }

            maindata.put("userId", AppSettings.getString(AppSettings.userId));

            maindata.put("NotificationType", "");
            result.put(AppConstants.projectName, maindata);
            Log.v("currentData", maindata.toString());

            Log.v("jhkg", String.valueOf(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppUrls.SaveNotificationType)
                .addJSONObjectBody(result)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                              onResume();
                                    arrNotificationList.clear();
                                    hitFilterApii();
                                    arrNotificationList.clear();
                                    SaveNotificationType();
                                    arrNotificationList.clear();
                                    getNotificationListApi();



                        AppUtils.hideDialog();

                        Log.v("jggghjgh", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
                            if (jsonObject.getString("resCode").equals("1")) {


                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();

                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }

    private class CategoryFilterAdapter extends RecyclerView.Adapter<NotificationList.holdercat> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> dataCheck = new ArrayList<HashMap<String, String>>();

        public CategoryFilterAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public NotificationList.holdercat onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NotificationList.holdercat(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_notufication, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final NotificationList.holdercat holdercat, final int position) {


            if (data.get(position).get("status").equalsIgnoreCase("0")) {

                holdercat.checkBox.setImageResource(R.drawable.checkbox);

            } else {

                holdercat.checkBox.setImageResource(R.drawable.selected);

            }

            holdercat.tvfilter.setText(data.get(position).get("name"));

            holdercat.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("FilterArrayList", holdercat.checkBox.getDrawable().toString());

                    if (arrNotificationList.get(position).get("status").equals("0")) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", arrNotificationList.get(position).get("id"));
                        hashMap.put("name", arrNotificationList.get(position).get("name"));
                        hashMap.put("status", "1");
                        arrNotificationList.set(position, hashMap);
                        adapterr.notifyDataSetChanged();

                    }
                    else {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", arrNotificationList.get(position).get("id"));
                        hashMap.put("name", arrNotificationList.get(position).get("name"));
                        hashMap.put("status", "0");
                        arrNotificationList.set(position, hashMap);
                        adapterr.notifyDataSetChanged();

                    }


                }
            });


        }


        public int getItemCount() {
            return data.size();
        }
    }

    public class holdercat extends RecyclerView.ViewHolder {

        //TextView
        TextView tvfilter;
        // CheckBox
        ImageView checkBox, selectedCheckBox;


        public holdercat(View itemView) {
            super(itemView);

            //TextView
            tvfilter = itemView.findViewById(R.id.tvfilter);
            checkBox = itemView.findViewById(R.id.checkBox);
            selectedCheckBox = itemView.findViewById(R.id.selectedCheckBox);
            tvfilter = itemView.findViewById(R.id.tvfilter);


        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        arrNotificationList.clear();
        getNotificationListApi();

        tvUnselecvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrNotificationList.clear();
                hitFilterApii();
                arrNotificationList.clear();
                SaveNotificationType();
                arrNotificationList.clear();
                getNotificationListApi();
            }
        });


    }
}
