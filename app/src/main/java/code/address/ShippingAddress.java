package code.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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

import code.database.AppSettings;
import code.order.OderSummary;
import code.profile.EditAddressActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

import static code.utils.AppConstants.addressNewList;


public class ShippingAddress extends BaseActivity implements View.OnClickListener {

    //CustomTextView
    TextView tvaddnewaddress;

    //RecyclerView
    RecyclerView recViewlistingAddress;

    //ImageView
    ImageView ivback, ivloader;

    //LinearLayout
    LinearLayout llproces;

    Boolean canProcced = false;

    ///
    String customerName, flatHouseBulding, streetColony, landmark, pincode, city, phoneNumber, state, addressId;
    Adapter adapter;
    private ArrayList<HashMap<String, String>> ShippinAddresList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        findViewById();
        setListener();
        getAddress();
    }

    private void setListener() {

        //textView
        tvaddnewaddress.setOnClickListener(this);

        //ImageView
        ivback.setOnClickListener(this);

        //LinearLayout
        llproces.setOnClickListener(this);


    }

    private void findViewById() {
        //TextView
        tvaddnewaddress = findViewById(R.id.addnewaddress);

        //RecyclerView
        recViewlistingAddress = findViewById(R.id.listingaddress);

        //ImageView
        ivback = findViewById(R.id.ivback);

        //LinearLayout
        llproces = findViewById(R.id.proces);

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addnewaddress:
                Intent I = new Intent(mActivity, AddNewAddressActivity.class);
                startActivity(I);
                break;


            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.proces:
                if (canProcced)
                    startActivity(new Intent(mActivity, OderSummary.class));
                else
                    Toast.makeText(mActivity, "Address can't be blank", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void getAddress() {

        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.getAddress;

        Log.v("getAddress", url);
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
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.hideDialog();
                        Log.v("getAddress", String.valueOf(response));
                        ivloader.setVisibility(View.GONE);
                        ShippinAddresList.clear();
                        addressNewList.clear();

                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String Msg = jsonObject1.getString("resMsg");

                            if (jsonObject1.getString("resCode").equals("1")) {

                                canProcced = true;

                                JSONArray jsonArray = jsonObject1.getJSONArray("address");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("addressId", arrayJSONObject.getString("addressId"));
                                    hashlist.put("customerName", arrayJSONObject.getString("customerName"));
                                    AppSettings.putString(AppSettings.customerName, arrayJSONObject.getString("customerName"));
                                    hashlist.put("phoneNumber", arrayJSONObject.getString("phoneNumber"));
                                    AppSettings.putString(AppSettings.phoneNumber, arrayJSONObject.getString("phoneNumber"));
                                    hashlist.put("flatHouseBulding", arrayJSONObject.getString("flatHouseBulding"));
                                    AppSettings.putString(AppSettings.flatHouseBulding, arrayJSONObject.getString("flatHouseBulding"));
                                    hashlist.put("streetColony", arrayJSONObject.getString("streetColony"));
                                    AppSettings.putString(AppSettings.streetColony, arrayJSONObject.getString("streetColony"));
                                    hashlist.put("city", arrayJSONObject.getString("city"));
                                    AppSettings.putString(AppSettings.city, arrayJSONObject.getString("city"));
                                    hashlist.put("landmark", arrayJSONObject.getString("landmark"));
                                    AppSettings.putString(AppSettings.landmark, arrayJSONObject.getString("landmark"));
                                    hashlist.put("state", arrayJSONObject.getString("state"));
                                    AppSettings.putString(AppSettings.state, arrayJSONObject.getString("state"));
                                    hashlist.put("pincode", arrayJSONObject.getString("pincode"));
                                    AppSettings.putString(AppSettings.pincode, arrayJSONObject.getString("pincode"));

                                    if (i == 0) {
                                        hashlist.put("primary", "1");
                                        addressNewList.add(hashlist);
                                    } else {
                                        hashlist.put("primary", "0");
                                    }

                                    ShippinAddresList.add(hashlist);

                                }
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                                adapter = new Adapter(mActivity, ShippinAddresList);
                                recViewlistingAddress.setLayoutManager(layoutManager);
                                recViewlistingAddress.setAdapter(adapter);
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

    @Override
    protected void onResume() {
        super.onResume();


    }

    private class Adapter extends RecyclerView.Adapter<ShippingAddress.Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public ShippingAddress.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.showtheaddress, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ShippingAddress.Holder holder, final int position) {
            final String AddAddressId = (data.get(position).get("addressId"));

            holder.tv_name.setText(data.get(position).get("customerName"));

            holder.tv_pincode.setText(data.get(position).get("pincode"));

            holder.tv_number.setText(data.get(position).get("phoneNumber"));

            String address = data.get(position).get("flatHouseBulding") + ", " + data.get(position).get("streetColony") + ", " + data.get(position).get("landmark") + "," + data.get(position).get("city") + ", " + data.get(position).get("state");

            holder.tv_addnewAddress.setText(address);

            if (data.get(position).get("primary").equals("1")) {
                holder.ivCheck.setImageResource(R.drawable.redioselect);

                customerName = data.get(position).get("customerName");
                flatHouseBulding = data.get(position).get("flatHouseBulding");
                streetColony = data.get(position).get("streetColony");
                landmark = data.get(position).get("landmark");
                pincode = data.get(position).get("pincode");
                city = data.get(position).get("city");
                phoneNumber = data.get(position).get("phoneNumber");
                state = data.get(position).get("state");
                addressId = data.get(position).get("addressId");

                AppSettings.putString(AppSettings.customerName, customerName);
                AppSettings.putString(AppSettings.flatHouseBulding, flatHouseBulding);
                AppSettings.putString(AppSettings.streetColony, streetColony);
                AppSettings.putString(AppSettings.landmark, landmark);
                AppSettings.putString(AppSettings.pincode, pincode);
                AppSettings.putString(AppSettings.city, city);
                AppSettings.putString(AppSettings.phoneNumber, phoneNumber);
                AppSettings.putString(AppSettings.state, state);
                AppSettings.putString(AppSettings.addressId, addressId);


            } else {
                holder.ivCheck.setImageResource(R.drawable.radio);
            }


            holder.ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addressNewList.clear();

                    for (int i = 0; i < data.size(); i++) {

                        HashMap<String, String> addList = new HashMap();
                        addList.put("customerName", data.get(i).get("customerName"));
                        addList.put("flatHouseBulding", data.get(i).get("flatHouseBulding"));
                        addList.put("streetColony", data.get(i).get("streetColony"));
                        addList.put("landmark", data.get(i).get("landmark"));
                        addList.put("pincode", data.get(i).get("pincode"));
                        addList.put("city", data.get(i).get("city"));
                        addList.put("phoneNumber", data.get(i).get("phoneNumber"));
                        addList.put("state", data.get(i).get("state"));
                        addList.put("addressId", data.get(i).get("addressId"));
                        Log.v("djkshf", data.get(i).get("addressId"));
                        Log.v("addVal", data.get(i).get("primary"));

                        if (data.get(position).get("addressId").equals(data.get(i).get("addressId"))) {
                            if (data.get(i).get("primary").equals("1")) {
                                addList.put("primary", "0");
                            } else {
                                addList.put("primary", "1");

                                addressNewList.add(addList);
                            }
                        } else {
                            addList.put("primary", "0");
                        }

                        ShippinAddresList.set(i, addList);
                    }

                    adapter.notifyDataSetChanged();

                }

            });
            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mActivity, EditAddressActivity.class);
                    intent.putExtra("customerName", data.get(position).get("customerName"));
                    intent.putExtra("flatHouseBulding", data.get(position).get("flatHouseBulding"));
                    intent.putExtra("streetColony", data.get(position).get("streetColony"));
                    intent.putExtra("landmark", data.get(position).get("landmark"));
                    intent.putExtra("pincode", data.get(position).get("pincode"));
                    intent.putExtra("city", data.get(position).get("city"));
                    intent.putExtra("phoneNumber", data.get(position).get("phoneNumber"));
                    intent.putExtra("state", data.get(position).get("state"));
                    intent.putExtra("addressId", data.get(position).get("addressId"));
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
        TextView tv_name, tv_pincode, tv_number, tv_addnewAddress, tvEdit;

        //ImageView
        ImageView ivCheck;


        public Holder(View inflate) {

            super(inflate);

            //textView
            tv_name = itemView.findViewById(R.id.textView4);
            tv_number = itemView.findViewById(R.id.textView6);
            tv_pincode = itemView.findViewById(R.id.textView5);
            tv_addnewAddress = itemView.findViewById(R.id.textView3);
            tvEdit = itemView.findViewById(R.id.ivDelete);

            //ImageView
            ivCheck = itemView.findViewById(R.id.imageView5);

        }
    }
}
