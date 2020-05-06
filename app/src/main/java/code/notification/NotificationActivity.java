package code.notification;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class NotificationActivity extends BaseActivity {


    //Integer
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static String date;
    public static String date1;
    public static String notify_time;
    public static String notify_time1;
    public static String timein24;
    public static String month;
    public static String date_time;

    //ImageView
    ImageView ivback,ivloader;

    //RecyclerView
    RecyclerView recyclerView;

    //GridLayoutManager
    GridLayoutManager mGridLayoutManager;

    //ArrayList
    ArrayList<HashMap<String, String>> PushList = new ArrayList();

    PushAdapter pushAdapter;

    Switch sw;


    private static String getMonth(String date) throws ParseException, java.text.ParseException {
        Date d = new SimpleDateFormat("MMMM/dd/yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
        String month_name = monthName.substring(0, 3);
        return month_name;
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;


        }
        // long now = getCurrentTime(ctx);
        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;


        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            //mins = diff / MINUTE_MILLIS ;
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            if ((diff / HOUR_MILLIS) == 1) {
                return "an hour ago";
            } else {
                return diff / HOUR_MILLIS + " hours ago";
            }
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {


            return date_time;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sw = findViewById(R.id.simpleSwitch);
        ivback = findViewById(R.id.ivback);

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);

        recyclerView = findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(mActivity, 1);
        recyclerView.setLayoutManager(mGridLayoutManager);

        sw.setChecked(false);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void NotificationList() {

        Log.v("GetCatListApi", AppUrls.GetNotification);

        AndroidNetworking.get(AppUrls.GetNotification)
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parsedataMembershipList(response);
                        Log.v("res", response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.hideDialog();
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.hideDialog();
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    private void parsedataMembershipList(JSONObject response) {

        ivloader.setVisibility(View.GONE);
        Log.d("response ", response.toString());
        PushList.clear();

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
            if (jsonObject.getString("resCode").equals("1")) {


                JSONArray jsonArray = jsonObject.getJSONArray("notification_list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", object.getString("id"));
                    hashMap.put("type", object.getString("type"));
                    hashMap.put("title", object.getString("title"));
                    hashMap.put("message", object.getString("push_msg"));
                    hashMap.put("add_date", object.getString("add_date"));
                    hashMap.put("largeIcon", object.getString("image"));


                    PushList.add(hashMap);
                }


                pushAdapter = new PushAdapter(PushList);
                recyclerView.setAdapter(pushAdapter);
                recyclerView.setNestedScrollingEnabled(true);

            } else {
                Toast.makeText(this, String.valueOf(jsonObject.getString("res_msg")), Toast.LENGTH_SHORT);
                //AppUtils.showErrorMessage(edOtp, String.valueOf(jsonObject.getString("res_msg")), mActivity);
            }
        } catch (Exception e) {
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT);
            // AppUtils.showErrorMessage(edOtp, String.valueOf(e), mActivity);
        }


        AppUtils.hideDialog();
    }

    private String getTime(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


    private String getDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("MMMM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


    private String getTime24hr(String time) throws java.text.ParseException {
        String t = "";
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        t = date24Format.format(date12Format.parse(time));
        Log.e("t", t);
        return t;

    }

    @Override
    protected void onResume() {
        super.onResume();
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PushList.clear();
                    NotificationList();
                } else {
                    PushList.clear();
                }
            }
        });
    }

    private class PushAdapter extends RecyclerView.Adapter<FavNameHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public PushAdapter(ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public FavNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FavNameHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_push, parent, false));
        }

        public void onBindViewHolder(FavNameHolder holder, final int position) {

            //largeIcon


            long timestamp = Long.parseLong(data.get(position).get("add_date")) * 1000L;
            notify_time = getTime(timestamp);
            try {
                timein24 = getTime24hr(notify_time);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }


            date = getDate(timestamp);


            try {
                month = getMonth(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            //getting date
            String[] separated = date.split("/");
            separated[0] = separated[0].trim();//month
            separated[1] = separated[1].trim();//date
            separated[2] = separated[2].trim();//year


            date_time = month + " " + separated[1] + ", " + separated[2] + " at " + timein24;


            holder.tvName.setText(data.get(position).get("title"));
            holder.tvDate.setText(getTimeAgo(Long.parseLong(data.get(position).get("add_date"))));
            holder.tvDesc.setText(data.get(position).get("message"));

            if (!data.get(position).get("largeIcon").isEmpty()) {
                Picasso.get().load(data.get(position).get("largeIcon")).placeholder(R.mipmap.logo_grey) .into(holder.ivIcon);
            }

            holder.rrView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(NotificationActivity.this, NotificationDetail.class);
                    i.putExtra("id", data.get(position).get("id"));
                    startActivity(i);

                }
            });


        }

        public int getItemCount() {
            return data.size();
        }
    }

    private class FavNameHolder extends RecyclerView.ViewHolder {
        //ImageView
        ImageView ivIcon;

        //TextView
        TextView tvDate;
        TextView tvName;
        TextView tvDesc;

        //RelativeLayout
        RelativeLayout rrView;

        public FavNameHolder(View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.imageView21);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            rrView = itemView.findViewById(R.id.rrView);
        }
    }
}
