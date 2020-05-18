package code.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.senderInformation.SenderListingActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

import static code.database.AppSettings.total_coupon_discount;

public class OderSummary extends BaseActivity implements View.OnClickListener {

    //textview
    TextView tvProductDiscountvalue, tvCouponDicount, tvProductCharge, tvShipping, tvOrderTotal, tvorderfinal, tvear, tvordernumber, tvodername, tvcodcharj, tv_SuplierName, tvsuplier, tvShippingName, tvshippingaddres, tvsSippingPhoneNumber, tvchnage;

    //imageview
    ImageView ivback, ivloader;

    //LinearLayout
    LinearLayout llproceed;

    Double n;
    RecyclerView RlvOrderSummary;
    Intent intent = getIntent();
    private ArrayList<HashMap<String, String>> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_summary);
        findViewById();
        setListener();


        String Array = AppSettings.getString(AppSettings.userarraylist);


        try {
            JSONArray array = new JSONArray(Array);
            parseList(array);
            System.out.println(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void parseList(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            HashMap hashMap = new HashMap();

            try {
                JSONObject obj = array.getJSONObject(i);

                hashMap.put("cartId", obj.getString("cartId"));

                hashMap.put("productId", obj.getString("productId"));
                hashMap.put("productName", obj.getString("productName"));
                hashMap.put("productImage", obj.getString("productImage"));
                hashMap.put("productPrice", obj.getString("productPrice"));
                hashMap.put("size", obj.getString("size"));
                hashMap.put("coupn_discount", obj.getString("coupn_discount"));
                hashMap.put("quantity", obj.getString("quantity"));
                hashMap.put("DiscounType", obj.getString("DiscounType"));
                hashMap.put("DiscountValue", obj.getString("DiscountValue"));
                hashMap.put("prod_price", obj.getString("prod_price"));

                Log.v("hgvgcfyg", obj.getString("productImage"));

                Log.v("cuzListint", hashMap.toString());
                categoryList.add(hashMap);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                CatalogueAdapter adapter = new CatalogueAdapter(mActivity, categoryList);
                RlvOrderSummary.setLayoutManager(layoutManager);
                RlvOrderSummary.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener() {

        //imageview
        ivback.setOnClickListener(this);

        //linearlayout
        llproceed.setOnClickListener(this);

        //textview
        tvchnage.setOnClickListener(this);
    }

    private void findViewById() {

        ////CustomtextView
        tvProductCharge = findViewById(R.id.tv_productcharj);
        tvShipping = findViewById(R.id.tvShipping);
        tvcodcharj = findViewById(R.id.tvcodcharj);
        tvOrderTotal = findViewById(R.id.tvorderTotal);
        tvorderfinal = findViewById(R.id.tvorderfinal);
        tvordernumber = findViewById(R.id.tvordernumber);
        tvodername = findViewById(R.id.tvodername);
        tvear = findViewById(R.id.ear);
        tvsuplier = findViewById(R.id.suplier);
        tv_SuplierName = findViewById(R.id.buity);
        tvsSippingPhoneNumber = findViewById(R.id.tvsSippingPhoneNumber);
        tvshippingaddres = findViewById(R.id.tvshippingaddres);
        tvShippingName = findViewById(R.id.tvShippingName);
        tvchnage = findViewById(R.id.tvchnage);
        tvCouponDicount = findViewById(R.id.tvCouponDicount);

        RlvOrderSummary = findViewById(R.id.rladdtoCart);
        tvProductDiscountvalue = findViewById(R.id.tvProductDiscountvalue);

        //ImageView
        ivback = findViewById(R.id.ivback);


        //LinearLayout
        llproceed = findViewById(R.id.lproceed);

        String total_coupon_discount = AppSettings.getString(AppSettings.total_coupon_discount);
        String total_product_discount = AppSettings.getString(AppSettings.total_product_discount);



        if(total_product_discount.equalsIgnoreCase("0"))
        {
            tvProductDiscountvalue.setText("0");
        }

        else
        {
            tvProductDiscountvalue.setText(total_product_discount);
        }


        if (total_coupon_discount.equalsIgnoreCase("0")) {
            tvCouponDicount.setText("0");
        } else {
            tvCouponDicount.setText(total_coupon_discount);
        }


    }

     @Override
     public void onResume() {
        super.onResume();
        tvordernumber.setText(getString(R.string.countryCode) + AppSettings.getString(AppSettings.mobilee));
        tvodername.setText(AppSettings.getString(AppSettings.profilename));
        tvShipping.setText(AppSettings.getString(AppSettings.shippingCharges));
        tvProductCharge.setText(AppSettings.getString(AppSettings.productCharges));
        tvOrderTotal.setText(getString(R.string.rupaye) + AppSettings.getString(AppSettings.orderTotal));
        tvorderfinal.setText(getString(R.string.rupaye) + AppSettings.getString(AppSettings.edt_totalcount));
        Double total = Double.parseDouble(AppSettings.getString(AppSettings.orderTotal));
        Double finall = Double.parseDouble(AppSettings.getString(AppSettings.edt_totalcount));

        n = finall - total;
        Log.v("msjd", String.valueOf(n));
        if (n > 0) {
            tvear.setText(getString(R.string.rupaye) + Double.toString(n));
        } else {
        }


        tvsuplier.setText(AppSettings.getString(AppSettings.userName));

        String type = AppSettings.getString(AppSettings.Type);

        if (type.equals("4")) {
            tv_SuplierName.setText(getString(R.string.Manufacture));
        } else {
            tv_SuplierName.setText(getString(R.string.Supplier));
        }

        String adress = AppSettings.getString(AppSettings.flatHouseBulding) + ", " + AppSettings.getString(AppSettings.landmark) + ", "
                + AppSettings.getString(AppSettings.streetColony) + ", " + AppSettings.getString(AppSettings.city) + ", "
                + AppSettings.getString(AppSettings.state) + ", " + AppSettings.getString(AppSettings.pincode);
        tvshippingaddres.setText(adress);
        tvsSippingPhoneNumber.setText(AppSettings.getString(AppSettings.phoneNumber));
        tvShippingName.setText(AppSettings.getString(AppSettings.customerName));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //ImageView
            case R.id.ivback:
                onBackPressed();
                break;

            //LinearLayout
            case R.id.lproceed:
                
                if(tvodername.getText().toString().trim().equals( "" ))
                {
                    Toast.makeText( mActivity, getString(R.string.addsenderinformation), Toast.LENGTH_SHORT ).show();
                }else if (AppSettings.getString(AppSettings.profilename).equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, getString(R.string.pleaseupdateprofile), Toast.LENGTH_SHORT).show();
                } else {

                    if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
                        hitplaceorder();

                    } else {

                        AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                    }

                }
                break;

            case R.id.tvchnage:

                Intent intent = new Intent(mActivity, SenderListingActivity.class);
                intent.putExtra("mobile", AppSettings.getString(AppSettings.mobilee));
                intent.putExtra("name", AppSettings.getString(AppSettings.namee));
                startActivity(intent);

                break;
        }

    }


    /// Api Hit_placeorder
    private void hitplaceorder() {

        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.placeOrder;
        Log.v("placeOrder", url);

        JSONObject jsonObject = new JSONObject();

        final JSONObject json = new JSONObject();


        try {
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("addressId", AppSettings.getString(AppSettings.addressId));
            jsonObject.put("customerPrice", AppSettings.getString(AppSettings.edt_totalcount));
            jsonObject.put("senderId", AppSettings.getString(AppSettings.sender_id));
            jsonObject.put("paymentType", "1");
            json.put(AppConstants.projectName, jsonObject);
            Log.v("placeOrder", String.valueOf(json));

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
                        Log.v("placeOrder", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");
                            String order_id = jsonObject1.getString("order_id");
                            String dataa = jsonObject1.getString("add_date");

                            Log.v("drata", dataa);
                            AppSettings.putString(AppSettings.data, jsonObject1.getString("add_date"));
                            if (resCode.equals("1")) {
                                AppUtils.hideDialog();
                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();

                                AppSettings.putString(AppSettings.order_id, order_id);
                                startActivity(new Intent(mActivity, OrderPlaceActivity.class));
                                AppSettings.putString(AppSettings.total_count, "");
                            } else {

                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                AppSettings.putString(AppSettings.total_count, "");

                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                            AppUtils.hideDialog();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.v("bank", String.valueOf(anError));

                    }

                });

    }

    private class CatalogueAdapter extends RecyclerView.Adapter<OderSummary.CatalogueHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public CatalogueAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public OderSummary.CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OderSummary.CatalogueHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addmarginlayout, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OderSummary.CatalogueHolder holder, final int position) {


            if (data.get(position).get("DiscounType").equals("1"))

                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") + getString(R.string.off));


            else if (data.get(position).get("DiscounType").equals("2"))
                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") +  getString(R.string.percent));

            else
                holder.tvDiscount.setText("");

            if (data.get(position).get("productPrice").equalsIgnoreCase(data.get(position).get("prod_price"))) {
                holder.tvOriginalPrice.setText("₹" + (data.get(position)).get("productPrice"));
                holder.tvDiscount.setText("");
                holder.tvPrice.setText("");

            } else {
                holder.tvOriginalPrice.setText("₹" + (data.get(position)).get("productPrice"));
                holder.tvPrice.setText("₹" + (data.get(position)).get("prod_price"));
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | 16);
            }
            holder.productName.setText(data.get(position).get("productName"));
            holder.size.setText(data.get(position).get("size"));
            holder.quantity.setText(data.get(position).get("quantity"));
            Picasso.get().load(data.get(position).get("productImage")).placeholder(R.mipmap.logo_grey).resize(400,600).into(holder.img_product);

            if(data.get(position).get("coupn_discount").equalsIgnoreCase("0"))
            {
                holder.tvDiscountCopoun.setVisibility(View.GONE);
                holder.tvDiscountvalue.setVisibility(View.GONE);
                holder.rlcopn.setVisibility(View.GONE);
                holder.rltotalvalue.setVisibility(View.GONE);

            }
            else {

                holder.tvDiscountCopoun.setText("- "+getString(R.string.rupaye)+" "+data.get(position).get("coupn_discount")+ getString(R.string.off));
                holder.tvDiscountvalue.setVisibility(View.VISIBLE);
                holder.tvDiscountCopoun.setVisibility(View.VISIBLE);


                int dicount=Integer.parseInt(data.get(position).get("coupn_discount")) ;
                int product= Integer.parseInt(data.get(position).get("productPrice"));
                String abc= String.valueOf(product-dicount);
                holder.tvtotalvalue.setText( abc);

            }
        }

        public int getItemCount() {
            return data.size();
        }

    }

    public class CatalogueHolder extends RecyclerView.ViewHolder {
        TextView productName, tvtotalvalue,tvPrice, size, quantity, tvDiscountCopoun, tvDiscountvalue, tvDiscount, tvOriginalPrice;
        ImageView img_product;
        RelativeLayout rlcopn,rltotalvalue;


        public CatalogueHolder(View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.productName);
            size = itemView.findViewById(R.id.size);
            quantity = itemView.findViewById(R.id.quantity);
            tvDiscountCopoun = itemView.findViewById(R.id.tvDiscountCopoun);
            tvDiscountvalue = itemView.findViewById(R.id.tvDiscountvalue);
            rlcopn = itemView.findViewById(R.id.rlcopn);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvtotalvalue = itemView.findViewById( R.id.tvtotalvalue );
            rltotalvalue = itemView.findViewById(R.id.rltotalvalue);


        }
    }
}
