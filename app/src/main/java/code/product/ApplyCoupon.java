package code.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.entity.ProductData;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class ApplyCoupon extends BaseActivity {

    RecyclerView rclcopon;
    ImageView ivBack;
    TextView tvnodata, tvbest;
    private ArrayList<HashMap<String, String>> arrcopounlist = new ArrayList<>();
    Intent intent,getIntentVal;
    String selectedCoupon="";
    String value;
    String Id;
    String addedType, addedById;
    ProductData productData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        rclcopon = findViewById(R.id.rclcopon);
        ivBack = findViewById(R.id.ivback);
        tvnodata = findViewById(R.id.tvnodata);
        tvbest = findViewById(R.id.tvbest);
        getIntentVal = getIntent();

        productData = (ProductData) getIntentVal.getSerializableExtra("productlist");

        value = getIntentVal.getStringExtra("qty");
        Id = getIntentVal.getStringExtra("Id");
        addedById = getIntentVal.getStringExtra("addedById");
        addedType = getIntentVal.getStringExtra("addedType");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });

        if (SimpleHTTPConnection.isNetworkAvailable()) {
            getCopon();
        } else {
            Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
        }


        intent = new Intent(mActivity,ProductAddToCardActivity.class);
        if(getIntentVal.hasExtra(AppSettings.couponChoosen)){
            selectedCoupon=getIntentVal.getStringExtra(AppSettings.couponChoosen);
        }
    }


    private class AdapterCoupon extends RecyclerView.Adapter<AdapterCoupon.MyViewHolder> {

        Activity activity;
        ArrayList<HashMap<String, String>> arrayList;
        float minCartValue = 0, cartValue = 0;


        public AdapterCoupon(Activity mActivity, ArrayList<HashMap<String, String>> arrcopounlist) {

            activity = mActivity;
            arrayList = arrcopounlist;

        }

        @NonNull
        @Override
        public AdapterCoupon.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_coupon_list, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterCoupon.MyViewHolder holder, final int position) {

            String coupon_type = arrayList.get(position).get("coupon_type");
            Log.v("vallluuee", coupon_type);

            if (coupon_type.equalsIgnoreCase("1")) {

                holder.tvTitle.setText(getString(R.string.get) + " " + getString(R.string.rupaye) + " " + arrayList.get(position).get("discount"));

            } else if (coupon_type.equalsIgnoreCase("2")) {

                holder.tvTitle.setText(getString(R.string.get) + " " + arrayList.get(position).get("discount") + "%");
            }


            if (selectedCoupon.equalsIgnoreCase(arrayList.get(position).get("id"))) {

                holder.tvremove.setVisibility(View.VISIBLE);
                holder.tvDiscValue.setVisibility(View.GONE);

            }
            else

                {
                holder.tvDiscValue.setVisibility(View.VISIBLE);
                holder.tvremove.setVisibility(View.GONE);

            }

            Log.v("copppn", selectedCoupon);
            holder.tvCouponCode.setText(arrayList.get(position).get("coupon_code"));

            holder.tvDiscValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectedCoupon=arrayList.get(position).get("coupon_code");
                    notifyDataSetChanged();
                    if (SimpleHTTPConnection.isNetworkAvailable()) {

                        ApplyCopuon(arrayList.get(position).get("coupon_code"), holder.tvremove, holder.tvDiscValue);

                    } else {
                        Toast.makeText(activity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
                    }


                }
            });

            holder.tvremove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Remove();
                    holder.tvremove.setVisibility(View.GONE);
                    holder.tvDiscValue.setVisibility(View.VISIBLE);





                }
            });




        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvCouponCode, tvDiscValue, tvTitle, tvremove;

            CardView clMain;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tvCouponCode = itemView.findViewById(R.id.tvCouponCode);
                tvDiscValue = itemView.findViewById(R.id.tvDiscValue);
                clMain = itemView.findViewById(R.id.clMain);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvremove = itemView.findViewById(R.id.tvremove);

            }
        }
    }

    private void getCopon() {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.GetCouponList;

        Log.v("getcpon", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {

            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.addedByIdd));
            jsonObject.put("userType", AppSettings.getString(AppSettings.addedTypee));
            Log.v("findObjectttt", String.valueOf(json));

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

                        AppUtils.hideDialog();
                        Log.v("getAddress", String.valueOf(response));


                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String Msg = jsonObject1.getString("resMsg");


                            if (jsonObject1.getString("resCode").equals("1")) {
                                tvnodata.setVisibility(View.GONE);
                                tvbest.setVisibility(View.VISIBLE);


                                JSONArray jsonArray = jsonObject1.getJSONArray("CouponList");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("id", arrayJSONObject.getString("id"));
                                    hashlist.put("coupon_code", arrayJSONObject.getString("coupon_code"));
                                    hashlist.put("coupon_type", arrayJSONObject.getString("coupon_type"));
                                    hashlist.put("discount", arrayJSONObject.getString("discount"));
                                    arrcopounlist.add(hashlist);

                                }
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                                AdapterCoupon adapterCoupon = new AdapterCoupon(mActivity, arrcopounlist);
                                rclcopon.setLayoutManager(layoutManager);
                                rclcopon.setAdapter(adapterCoupon);
                            } else {
                                tvnodata.setVisibility(View.VISIBLE);
                                tvbest.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppUtils.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });

    }


    private void ApplyCopuon(String coupon_code, final TextView tvremove, final TextView tvDiscValue) {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.ApplyCouponCode;
        Log.v("getcpon", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("user_id", AppSettings.getString(AppSettings.userId));
            jsonObject.put("final_price", AppSettings.getString(AppSettings.finalPrice));
            jsonObject.put("coupon_code", coupon_code);

            Log.v("findObjectttt", String.valueOf(json));

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
                        AppUtils.hideDialog();
                        Log.v("applycopoun", String.valueOf(response));


                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String Msg = jsonObject1.getString("resMsg");


                                if (jsonObject1.getString("resCode").equals("1")) {
                                Toast.makeText(mActivity, Msg, Toast.LENGTH_SHORT).show();
                                String coupon_id = jsonObject1.getString("coupon_id");
                                String coupon_code = jsonObject1.getString("coupon_code");
                                String final_price = jsonObject1.getString("final_price");
                                String discount = jsonObject1.getString("discount");

                                intent.putExtra("productlist", (Serializable) productData);
                                intent.putExtra(AppSettings.finalPrice,final_price);
                                intent.putExtra(AppSettings.coupon_codee,coupon_code);
                                intent.putExtra(AppSettings.discount,discount);
                                intent.putExtra(AppSettings.coupon_code,coupon_id);
                                intent.putExtra("qty", value);
                                intent.putExtra("Id", Id);
                                intent.putExtra("addedType", addedType);
                                intent.putExtra("addedById", addedById);
                                startActivity(intent);


                            } else {
                                Toast.makeText(mActivity, Msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppUtils.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }


    private void Remove() {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.RemoveCoupon;
        Log.v("getcpon", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("productId", AppSettings.getString(AppSettings.productId));
            jsonObject.put("size", AppSettings.getString(AppSettings.size));
            Log.v("findObjectttt", String.valueOf(json));

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
                        AppUtils.hideDialog();
                        Log.v("applycopoun", String.valueOf(response));


                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String Msg = jsonObject1.getString("resMsg");


                            if (jsonObject1.getString("resCode").equals("1")) {

                                intent.putExtra("qty", value);
                                intent.putExtra("Id", Id);
                                intent.putExtra("addedType", addedType);
                                intent.putExtra("addedById", addedById);
                                startActivity(intent);


                            } else {
                                Toast.makeText(mActivity, Msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppUtils.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }



}
