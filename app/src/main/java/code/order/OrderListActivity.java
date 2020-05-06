package code.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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

public class OrderListActivity extends BaseActivity implements View.OnClickListener {

    //RecyclerView
    RecyclerView Rlv_productlist;

    SearchView search;

    //TextView
    TextView tvorderd, tvshippid, tvdelivered, tvcanceled, tvreturn;

    //ArrayList
    private ArrayList<HashMap<String, String>> categoryList = new ArrayList<>();

    //ImageView
    ImageView ivback, ivloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        findViewById();

        if (SimpleHTTPConnection.isNetworkAvailable()) {

            categoryList.clear();
            hit_OrderList("1");

        }
        else {

            Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
        }
        setLisener();


    }

    private void setLisener() {
        ivback.setOnClickListener(this);
        tvorderd.setOnClickListener(this);
        tvreturn.setOnClickListener(this);
        tvdelivered.setOnClickListener(this);
        tvcanceled.setOnClickListener(this);
        tvshippid.setOnClickListener(this);

    }

    private void findViewById() {

        //RecyclerView
        Rlv_productlist = (findViewById(R.id.productlist));

        //ImageView
        ivback = (findViewById(R.id.ivback));
        tvorderd = (findViewById(R.id.tvorderd));
        tvshippid = (findViewById(R.id.tvshippid));
        tvdelivered = (findViewById(R.id.tvdelivered));
        tvcanceled = (findViewById(R.id.tvcanceled));
        tvreturn = (findViewById(R.id.tvreturn));

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivback:

                finish();
                break;


            case R.id.tvorderd:
                categoryList.clear();
                hit_OrderList("1");

                tvorderd.setBackgroundColor(getColor(R.color.colorAccent));
                tvshippid.setBackgroundColor(getColor(R.color.white));
                tvdelivered.setBackgroundColor(getColor(R.color.white));
                tvcanceled.setBackgroundColor(getColor(R.color.white));
                tvreturn.setBackgroundColor(getColor(R.color.white));


                break;

            case R.id.tvshippid:
                categoryList.clear();

                hit_OrderList("2");
                tvorderd.setBackgroundColor(getColor(R.color.white));
                tvshippid.setBackgroundColor(getColor(R.color.colorAccent));
                tvdelivered.setBackgroundColor(getColor(R.color.white));
                tvcanceled.setBackgroundColor(getColor(R.color.white));
                tvreturn.setBackgroundColor(getColor(R.color.white));

                break;
            case R.id.tvdelivered:
                categoryList.clear();

                hit_OrderList("3");

                tvorderd.setBackgroundColor(getColor(R.color.white));
                tvshippid.setBackgroundColor(getColor(R.color.white));
                tvdelivered.setBackgroundColor(getColor(R.color.colorAccent));
                tvcanceled.setBackgroundColor(getColor(R.color.white));
                tvreturn.setBackgroundColor(getColor(R.color.white));
                break;
            case R.id.tvcanceled:
                categoryList.clear();
                hit_OrderList("4");
                tvorderd.setBackgroundColor(getColor(R.color.white));
                tvshippid.setBackgroundColor(getColor(R.color.white));
                tvdelivered.setBackgroundColor(getColor(R.color.white));
                tvcanceled.setBackgroundColor(getColor(R.color.colorAccent));
                tvreturn.setBackgroundColor(getColor(R.color.white));
                break;
            case R.id.tvreturn:
                categoryList.clear();
                hit_OrderList("5");
                tvorderd.setBackgroundColor(getColor(R.color.white));
                tvshippid.setBackgroundColor(getColor(R.color.white));
                tvdelivered.setBackgroundColor(getColor(R.color.white));
                tvcanceled.setBackgroundColor(getColor(R.color.white));
                tvreturn.setBackgroundColor(getColor(R.color.colorAccent));
                break;
        }


    }

    //Api_HIT
    private void hit_OrderList(String types) {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.OrderPageList;
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("status", types);
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ivloader.setVisibility(View.GONE);
                        categoryList.clear();
                        AppUtils.hideDialog();
                        Log.v("getOrderList", String.valueOf(response));

                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");


                            if (resCode.equals("1")) {
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("OrderList");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    HashMap<String, String> hashlist = new HashMap();
                                    JSONObject arrayJSONObject1 = jsonArray1.getJSONObject(i);
                                    hashlist.put("id", arrayJSONObject1.getString("id"));
                                    hashlist.put("order_id", arrayJSONObject1.getString("order_id"));
                                    hashlist.put("total_price", arrayJSONObject1.getString("total_price"));
                                    hashlist.put("status", arrayJSONObject1.getString("status"));
                                    hashlist.put("add_date", arrayJSONObject1.getString("add_date"));
                                    categoryList.add(hashlist);


                                }


                            }
                            else {
                                Toast mToast= Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT);
                                mToast.show();
                                categoryList.clear();
                            }

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                            OrderListActivity.Adapter adapter = new OrderListActivity.Adapter(mActivity, categoryList);
                            Rlv_productlist.setLayoutManager(layoutManager);
                            Rlv_productlist.setAdapter(adapter);

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


    private class Adapter extends RecyclerView.Adapter<OrderListActivity.Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public OrderListActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderListActivity.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OrderListActivity.Holder holder, final int position) {
            holder.tvorderiddetail.setText(data.get(position).get("order_id"));

            holder.tvpricedetail.setText(getString(R.string.rupaye) + " " + data.get(position).get("total_price"));

            holder.tvdatedetail.setText(data.get(position).get("add_date"));

            holder.tvstatusdetail.setText(data.get(position).get("status"));

            String status = data.get(position).get("status");

            if (status.equals("1")) {
                holder.tvstatusdetail.setText(getString(R.string.Ordered));
                holder.tvstatusdetail.setTextColor(getResources().getColor(R.color.green));
            } else if (status.equals("2")) {
                holder.tvstatusdetail.setText(getString(R.string.shiped));
                holder.tvstatusdetail.setTextColor(getResources().getColor(R.color.yellow));
            } else if (status.equals("3")) {
                holder.tvstatusdetail.setText(getString(R.string.deliverd));
                holder.tvstatusdetail.setTextColor(getResources().getColor(R.color.green));
            } else if (status.equals("4")) {
                holder.tvstatusdetail.setText(getString(R.string.cancelled));
                holder.tvstatusdetail.setTextColor(getResources().getColor(R.color.red));
            }

            else if (status.equals("5")) {
                holder.tvstatusdetail.setText(getString(R.string.returnned));
                holder.tvstatusdetail.setTextColor(getResources().getColor(R.color.yellow));
            }

            holder.orderlist.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);

                    intent.putExtra("order_id", AppSettings.putString(AppSettings.order_id, data.get(position).get("order_id")));

                    startActivity(intent);


                }
            });

        }

        public int getItemCount() {

            return data.size();

        }

    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvorderiddetail, tvpricedetail, tvdatedetail, tvstatusdetail;

        //CardView
        CardView orderlist;

        public Holder(View inflate) {
            super(inflate);
            tvorderiddetail = inflate.findViewById(R.id.orderiddetail);
            tvpricedetail = inflate.findViewById(R.id.tvpricedetail);
            tvdatedetail = inflate.findViewById(R.id.tvdatedetail);
            tvstatusdetail = inflate.findViewById(R.id.tvstatusdetail);
            orderlist = inflate.findViewById(R.id.orderlist);


        }
    }


}
