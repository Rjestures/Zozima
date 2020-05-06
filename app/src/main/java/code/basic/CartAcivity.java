package code.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.product.ProductAddToCardActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;


public class CartAcivity extends BaseActivity implements View.OnClickListener {

    //RecyclerView
    RecyclerView Rlv_Cart_List;

    //Imageview
    ImageView ivback, ivloader;
    int productCharges;


    //CustomTextView
    TextView tvProductcharj, tvProductDiscountvalue, tvShippingCharj, tvTotal_Price, tvProceed, tvcodcharj, tvSuplierName, tvsuplier, tvDiscountCopoun;

    //RedioButton
    RadioButton redioonline, cashredio;

    //LinearLayout
    LinearLayout linearLayout;

    //int Value
    int n1 = 1;
    int n = 1;
    String type;
    Boolean first = false;
    String value = String.valueOf(1);
    String ids;
    String Productprice;
    int shipingcharges;
    int ProductPrice, shipCharge,prod_price,single_product_price,SingleDiscountValue,single_coupn_discount,single_prod_price;
    int DiscountValue,coupn_discount;
    String coupn_discountt;

    ArrayList<String> slelectsize;

    //TextView
    TextView tvCount;

    //LinearLayout
    LinearLayout layoutCartNoItems;


    //ArrayList
    private ArrayList<HashMap<String, String>> arrCartList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrCartUnits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_acivity);

        findViewById();
        setListeners();

        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {

            arrCartList.clear();
            slelectsize.clear();
            getCartList();

        } else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }

    }

    private void setListeners() {
        ivback.setOnClickListener(this);
        redioonline.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        cashredio.setOnClickListener(this);
        tvProceed.setOnClickListener(this);
    }

    //findViewbyId
    private void findViewById() {


        //ImageView
        ivback = findViewById(R.id.ivback);

        //RedioButton
        redioonline = findViewById(R.id.redioonline);
        cashredio = findViewById(R.id.cashredio);

        //LinearLayout
        linearLayout = findViewById(R.id.rl);

        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);
        //RecyclerView
        Rlv_Cart_List = findViewById(R.id.rladdtocart);

        //TextView
        tvProceed = findViewById(R.id.proccedd);
        tvProductcharj = findViewById(R.id.tv_productcharj);
        tvProductDiscountvalue = findViewById(R.id.tvProductDiscountvalue);
        tvShippingCharj = findViewById(R.id.tvShipping);
        tvDiscountCopoun = findViewById(R.id.tvCouponDicount);
        tvTotal_Price = findViewById(R.id.totalprice);
        tvCount = findViewById(R.id.tvCount);
        tvcodcharj = findViewById(R.id.tvcodcharj);
        tvsuplier = findViewById(R.id.suplier);
        tvSuplierName = findViewById(R.id.buity);

        layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        //ArrayList
        slelectsize = new ArrayList<>();

        Button bStartShopping = (Button) findViewById(R.id.bAddNew);
        bStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

     //cliackLisner

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivback:

                onBackPressed();

                break;

            case R.id.proccedd:

                Intent intent = new Intent(mActivity, AddMarginActivity.class);
                startActivity(intent);

                break;

            case R.id.redioonline:

                if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
                    arrCartList.clear();
                    slelectsize.clear();
                    getCartList();

                } else {

                    AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                }


                break;

            case R.id.cashredio:

                if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
                    arrCartList.clear();
                    slelectsize.clear();
                    getCartList();

                } else {

                    AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                }

                break;
            case R.id.tv_productcharj:

                break;
            case R.id.tvProceed:

                break;
        }

    }

    //getCartList
    private void getCartList() {
        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.getCartList;
        Log.v("getcartList", url);
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
                        ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();
                        Log.v("GetCartList", String.valueOf(response));
                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");
                            if (resCode.equals("1")) {

                                linearLayout.setVisibility(View.VISIBLE);
                                layoutCartNoItems.setVisibility(View.GONE);

                                AppSettings.putString(AppSettings.userName, jsonObject1.getString("userName"));

                                tvsuplier.setText(jsonObject1.getString("userName"));

                                type = jsonObject1.getString("Type");

                                if (type.equals("4")) {
                                    tvSuplierName.setText("Manufacture");
                                } else {
                                    tvSuplierName.setText("Supplier");
                                }

                                tvProductcharj.setText(jsonObject1.getString("productCharges"));
                                productCharges= Integer.parseInt(jsonObject1.getString("productCharges"));
                                tvcodcharj.setText(jsonObject1.getString("codCharges"));
                                tvTotal_Price.setText(jsonObject1.getString("orderTotal"));
                                String total_coupon_discount = jsonObject1.getString("total_coupon_discount");
                                coupn_discountt= (jsonObject1.getString("total_coupon_discount"));
                                int total_product_discount = Integer.parseInt(jsonObject1.getString("total_product_discount"));

                                /*  if(total_product_discount.equalsIgnoreCase("0"))
                                {
                                    tvProductDiscountvalue.setText("0");
                                }
                                else {
                                    tvProductDiscountvalue.setText(total_product_discount);
                                }*/

                                if (total_coupon_discount.equalsIgnoreCase("0")) {
                                    tvDiscountCopoun.setText("0");
                                }
                                else {

                                    tvDiscountCopoun.setText(total_coupon_discount);
                                }


                                tvShippingCharj.setText(jsonObject1.getString("shippingCharges"));
                                shipCharge= Integer.parseInt(jsonObject1.getString("SingleShippingCharges"));
                                AppSettings.putString(AppSettings.productCharges, jsonObject1.getString("productCharges"));
                                AppSettings.putString(AppSettings.orderTotal, jsonObject1.getString("orderTotal"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("Items");
                                JSONArray jsonArray3 = jsonObject1.getJSONArray("Items");
                                AppSettings.putString(AppSettings.userarraylist, jsonArray3.toString());


                                for (int n = 0; n < jsonArray1.length(); n++) {

                                    HashMap<String, String> hashlist = new HashMap();

                                    JSONObject arrayJSONObject1 = jsonArray1.getJSONObject(n);

                                    hashlist.put("DiscounType", arrayJSONObject1.getString("DiscounType"));

                                    hashlist.put("DiscountValue", arrayJSONObject1.getString("DiscountValue"));

                                    hashlist.put("SingleDiscountValue", arrayJSONObject1.getString("SingleDiscountValue"));

                                    hashlist.put("prod_price", arrayJSONObject1.getString("prod_price"));

                                    hashlist.put("single_prod_price", arrayJSONObject1.getString("single_prod_price"));

                                    hashlist.put("cartId", arrayJSONObject1.getString("cartId"));

                                    hashlist.put("productId", arrayJSONObject1.getString("productId"));

                                    hashlist.put("productName", arrayJSONObject1.getString("productName"));

                                    hashlist.put("productId", arrayJSONObject1.getString("productId"));

                                    hashlist.put("productName", arrayJSONObject1.getString("productName"));

                                    hashlist.put("productImage", arrayJSONObject1.getString("productImage"));

                                    hashlist.put("productPrice", arrayJSONObject1.getString("productPrice"));

                                    hashlist.put("couponId", arrayJSONObject1.getString("couponId"));

                                    hashlist.put("single_product_price", arrayJSONObject1.getString("single_product_price"));

                                    hashlist.put("coupon_type", arrayJSONObject1.getString("coupon_type"));

                                    String chooseSize = arrayJSONObject1.getString("size");
                                    hashlist.put("coupn_discount", arrayJSONObject1.getString("coupn_discount"));
                                    hashlist.put("coupn_discount_value", arrayJSONObject1.getString("coupn_discount_value"));
                                    hashlist.put("single_coupn_discount", arrayJSONObject1.getString("single_coupn_discount"));
                                    hashlist.put("size", arrayJSONObject1.getString("size"));
                                    hashlist.put("quantity", arrayJSONObject1.getString("quantity"));

                                    AppSettings.putString(AppSettings.cartId, arrayJSONObject1.getString("cartId"));
                                    AppSettings.putString(AppSettings.productImage, arrayJSONObject1.getString("productImage"));

                                    AppSettings.putString(AppSettings.productPrice, arrayJSONObject1.getString("productPrice"));

                                    AppSettings.putString(AppSettings.productName, arrayJSONObject1.getString("productName"));

                                    AppSettings.putString(AppSettings.quantity, arrayJSONObject1.getString("quantity"));

                                    AppSettings.putString(AppSettings.size, arrayJSONObject1.getString("size"));

                                    AppSettings.putString(AppSettings.productId, arrayJSONObject1.getString("productId"));

                                    /*hashlist.put("orderTotal", jsonObject1.getString("orderTotal"));*/

                                    hashlist.put("UnitList", arrayJSONObject1.getJSONArray("unitNames").toString());

                                    JSONArray jsonArray2 = arrayJSONObject1.getJSONArray("unitNames");

                                    try {

                                        single_coupn_discount = Integer.parseInt( arrayJSONObject1.getString("single_coupn_discount"));

                                    }
                                    catch (NumberFormatException e) {
                                    }
                                    int sizePos = -1;
                                    arrCartList.add(hashlist);

                                    for (int n1 = 0; n1 < jsonArray2.length(); n1++) {

                                        JSONObject arrayJSONObject2 = jsonArray2.getJSONObject(n1);

                                        String statename = arrayJSONObject2.getString("unit_name");

                                        if (statename.equalsIgnoreCase(chooseSize)) {
                                            Log.v("Size_Checker", statename + Integer.toString(n1));
                                            sizePos = n1;
                                        }

                                        slelectsize.add(statename);


                                    }
                                    hashlist.put("sizePos", Integer.toString(sizePos));


                                }




                            } else {
                                tvProductcharj.setText("");
                                tvShippingCharj.setText("");
                                tvTotal_Price.setText("");
                                tvcodcharj.setText("");
                                AppSettings.putString(AppSettings.total_count, "0");
                                layoutCartNoItems.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                                Button bStartShopping = (Button) findViewById(R.id.bAddNew);
                                bStartShopping.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });

                            }


                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                            Adapter adapter = new Adapter(mActivity, arrCartList);
                            Rlv_Cart_List.setLayoutManager(layoutManager);
                            Rlv_Cart_List.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();


                    }


                });
    }

    //RemoveCart
    private void RemoveCart(String cartId, String productId) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.RemoveProductFromCart;
        Log.v("removeCart", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {

            jsonObject.put("productId", productId);
            jsonObject.put("cartId", cartId);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            json.put(AppConstants.projectName, jsonObject);
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
                        Log.v("remove", String.valueOf(response));
                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");


                            if (resCode.equals("1")) {
                                AppSettings.putString(AppSettings.total_count, jsonObject1.getString("total"));


                                arrCartList.clear();
                                getCartList();
                                tvProductcharj.setText("");
                                tvShippingCharj.setText("");
                                tvTotal_Price.setText("");
                                tvcodcharj.setText("");
                                tvSuplierName.setText("");
                                tvsuplier.setText("");


                            } else {
                                AppSettings.putString(AppSettings.total_count, "");

                                linearLayout.setVisibility(View.GONE);

                                layoutCartNoItems.setVisibility(View.VISIBLE);

                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                Button bStartShopping = (Button) findViewById(R.id.bAddNew);
                                bStartShopping.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });


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

    //Update_cart_quantity
    public void Update_cart_quantity(String quantity, String cartId, String size) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.UpdateCartValue;
        Log.v("removeCart", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("cartId", cartId);
            jsonObject.put("quantity", quantity);
            jsonObject.put("size", size);
            json.put(AppConstants.projectName, jsonObject);
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

                        slelectsize.clear();

                        Log.v("updatequantity", String.valueOf(response));

                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String resCode = jsonObject1.getString("resCode");

                            String resMsg = jsonObject1.getString("resMsg");

                            if (resCode.equals("1")) {

                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                AppSettings.putString(AppSettings.total_count, jsonObject1.getString("total"));


                            } else {

                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                AppSettings.putString(AppSettings.total_count, "");

                                tvProductcharj.setText("");
                                tvShippingCharj.setText("");
                                tvTotal_Price.setText("");
                                tvcodcharj.setText("");
                                layoutCartNoItems.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                                Button bStartShopping = (Button) findViewById(R.id.bAddNew);
                                bStartShopping.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });


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


    public void UpdateSize(final String units, View v, final String cartId, final TextView spVal) {
        Context wrapper = new ContextThemeWrapper(mActivity, R.style.PopupMenuOverlapAnchor);
        PopupMenu popup = new PopupMenu(wrapper, v);
        arrCartUnits.clear();
        popup.getMenu().clear();
        try {
            JSONArray jsonArray8 = new JSONArray(units);
            for (int n11 = 0; n11 < jsonArray8.length(); n11++) {
                JSONObject jsonObject8 = jsonArray8.getJSONObject(n11);
                popup.getMenu().add(jsonObject8.getString("unit_name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Log.v("MenuSelected", item.getTitle().toString());
                Log.v("MenuSelected", item.getTitle().toString());
                spVal.setText(item.getTitle().toString());
                Update_cart_quantityy(cartId, item.getTitle().toString());

                return true;

            }
        });
    }


    //Update_cart_quantity
    public void Update_cart_quantityy(String cartId, String unitName) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.UpdateCartValue;
        Log.v("removeCart", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("cartId", cartId);
            jsonObject.put("quantity", "");
            jsonObject.put("size", unitName);
            json.put(AppConstants.projectName, jsonObject);
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

                        slelectsize.clear();

                        Log.v("updatequantity", String.valueOf(response));

                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");

                            if (resCode.equals("1")) {

                                AppSettings.putString(AppSettings.total_count, jsonObject1.getString("total"));


                            } else {

                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                AppSettings.putString(AppSettings.total_count, "");

                                tvProductcharj.setText("");
                                tvShippingCharj.setText("");
                                tvTotal_Price.setText("");
                                tvcodcharj.setText("");
                                layoutCartNoItems.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);

                                Button bStartShopping = (Button) findViewById(R.id.bAddNew);
                                bStartShopping.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });


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

    private class Adapter extends RecyclerView.Adapter<CartAcivity.Holder> {
        ArrayList<String> units;
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        Context context;

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> arrCartList) {
            data = arrCartList;
            context = applicationContext;
            units = new ArrayList<>();
        }

        public CartAcivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CartAcivity.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addtocard, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CartAcivity.Holder holder, final int position) {



            if (data.get(position).get("DiscounType").equals("1"))

                holder.tvDiscount.setText(data.get(position).get("DiscountValue")+getString(R.string.off) );


            else if (data.get(position).get("DiscounType").equals("2"))
                holder.tvDiscount.setText(data.get(position).get("DiscountValue")+getString(R.string.percent));

            else
                holder.tvDiscount.setText("");

            if (data.get(position).get("productPrice").equalsIgnoreCase(data.get(position).get("prod_price"))) {
                holder.tvOriginalPrice.setText(data.get(position).get("productPrice"));
                holder.rupayee.setVisibility(View.VISIBLE);
                holder.rupaye.setVisibility(View.GONE);
                holder.rupayeee.setVisibility(View.GONE);
                holder.tvDiscount.setVisibility(View.GONE);
                holder.tvPrice.setVisibility(View.GONE);
                holder.tvDiscount.setText("");
                holder.tvPrice.setText("");

            } else {
                holder.tvOriginalPrice.setText(data.get(position).get("productPrice"));
                holder.tvPrice.setText((data.get(position)).get("prod_price"));
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | 16);
            }
            if (data.get(position).get("coupn_discount").equalsIgnoreCase("0")) {
                holder.tvDiscountCopoun.setVisibility(View.GONE);
                holder.tvDiscountvalue.setVisibility(View.GONE);
                holder.rltotalvalue.setVisibility(View.GONE);
                holder.tvDiscounttt.setVisibility(View.GONE);
                holder.tvrupaye.setVisibility(View.GONE);
            }
            else {

                coupn_discount = Integer.parseInt(data.get(position).get("coupn_discount"));
                holder.tvDiscountCopoun.setText(Integer.toString(coupn_discount));
                holder.tvDiscountvalue.setVisibility(View.VISIBLE);
                holder.tvDiscountCopoun.setVisibility(View.VISIBLE);
                holder.rltotalvalue.setVisibility(View.VISIBLE);
                int  productPrice=Integer.parseInt(data.get(position).get("productPrice"));
                int single_cpoun= Integer.parseInt(String.valueOf(productPrice-single_coupn_discount));
                holder.tvtotalvalue.setText(Integer.toString(single_cpoun));
            }

            holder.tv_name.setText(data.get(position).get("productName"));
            holder.tvCountTv.setText(data.get(position).get("quantity"));
            holder.spVal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateSize(data.get(position).get("UnitList"), view, data.get(position).get("cartId").toString(), holder.spVal);
                }
            });


            final String cartId;
            cartId = (data.get(position).get("cartId"));
            final String productId = (data.get(position).get("productId"));
            tvProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, AddMarginActivity.class);
                    intent.putExtra("orderTotal", data.get(position).get("orderTotal"));
                    intent.putExtra("shippingCharges", data.get(position).get("shippingCharges"));
                    startActivity(intent);

                }
            });


            units.clear();

            Log.v("sizeeeListing", data.get(position).get("UnitList"));
            try {
                JSONArray jsonArray8 = new JSONArray(data.get(position).get("UnitList"));
                for (int n11 = 0; n11 < jsonArray8.length(); n11++) {

                    JSONObject jsonObject8 = jsonArray8.getJSONObject(n11);

                    String unitsname = jsonObject8.getString("unit_name");
                    units.add(unitsname);
                    Log.v("sasisad", units.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.spVal.setText(data.get(position).get("size"));
            holder.ivDecrement.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    ProductPrice = Integer.parseInt(data.get(position).get("productPrice"));
                    single_product_price = Integer.parseInt(data.get(position).get("single_product_price"));
                    SingleDiscountValue = Integer.parseInt(data.get(position).get("SingleDiscountValue"));
                    single_prod_price = Integer.parseInt(data.get(position).get("single_prod_price"));
                    prod_price = Integer.parseInt(data.get(position).get("prod_price"));
                    DiscountValue = Integer.parseInt(data.get(position).get("DiscountValue"));
                    single_coupn_discount = Integer.parseInt(data.get(position).get("single_coupn_discount"));
                    coupn_discount = Integer.parseInt(data.get(position).get("coupn_discount"));

                    n = Integer.parseInt(holder.tvCountTv.getText().toString());

                    if (n > 1) {

                        Log.v("MydataPriceList", data.get(position).toString());


                        try {



                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                        tvShippingCharj.setText(Integer.toString(Integer.parseInt(tvShippingCharj.getText().toString()) - shipCharge));
                        tvTotal_Price.setText(Integer.toString(Integer.parseInt(tvTotal_Price.getText().toString()) - shipCharge - single_product_price+single_coupn_discount));

                        n = n - 1;
                        value = String.valueOf(n);
                        holder.tvCountTv.setText(value);

                        tvProductcharj.setText(Integer.toString( Integer.parseInt(tvProductcharj.getText().toString())- single_product_price));
                        tvDiscountCopoun.setText(Integer.toString(Integer.parseInt(tvDiscountCopoun.getText().toString())- single_coupn_discount));
                        holder.tvOriginalPrice.setText(Integer.toString(n * single_product_price));
                        holder.tvPrice.setText(Integer.toString(n * single_prod_price-single_coupn_discount));
                        holder.tvDiscount.setText(Integer.toString(n * SingleDiscountValue)+" off");
                        holder.tvtotalvalue.setText(Integer.toString(n * single_product_price-n*single_coupn_discount));
                        holder.tvDiscountCopoun.setText(Integer.toString(n*single_coupn_discount));

                        if (SimpleHTTPConnection.isNetworkAvailable()) {

                            Update_cart_quantity(String.valueOf(n), data.get(position).get("cartId"), holder.spVal.getText().toString());

                        }
                        else
                            {
                                Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            });

            holder.ivIncreIv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ProductPrice = Integer.parseInt(data.get(position).get("productPrice"));
                    single_product_price = Integer.parseInt(data.get(position).get("single_product_price"));
                    SingleDiscountValue = Integer.parseInt(data.get(position).get("SingleDiscountValue"));
                    single_prod_price = Integer.parseInt(data.get(position).get("single_prod_price"));
                    single_coupn_discount = Integer.parseInt(data.get(position).get("single_coupn_discount"));
                    prod_price = Integer.parseInt(data.get(position).get("prod_price"));

                    n1 = Integer.parseInt(holder.tvCountTv.getText().toString());

                    try {

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    tvShippingCharj.setText(Integer.toString(Integer.parseInt(tvShippingCharj.getText().toString()) + shipCharge));
                    tvTotal_Price.setText(Integer.toString(Integer.parseInt(tvTotal_Price.getText().toString()) + shipCharge + single_product_price-single_coupn_discount));

                    n1 = n1 + 1;

                    value = String.valueOf(n1);
                    holder.tvCountTv.setText(value);
                    tvProductcharj.setText(Integer.toString( Integer.parseInt(tvProductcharj.getText().toString())+ single_product_price));
                    tvDiscountCopoun.setText(Integer.toString(Integer.parseInt(tvDiscountCopoun.getText().toString())+ single_coupn_discount));
                    holder.tvOriginalPrice.setText(Integer.toString(n1 * single_product_price));
                    holder.tvPrice.setText(Integer.toString(n1 * single_prod_price));
                    holder.tvDiscount.setText(Integer.toString(n1 * SingleDiscountValue)+" off");
                    holder.tvtotalvalue.setText(Integer.toString(n1 * single_product_price-n1*single_coupn_discount));
                    holder.tvDiscountCopoun.setText(Integer.toString(n1*single_coupn_discount));


                    if (SimpleHTTPConnection.isNetworkAvailable()) {

                        Update_cart_quantity(String.valueOf(n1), data.get(position).get("cartId"), holder.spVal.getText().toString());

                    }

                    else
                        {

                        Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.ivremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                    alertDialogBuilder.setMessage(getString(R.string.remove));
                    alertDialogBuilder.setPositiveButton(getString(R.string.cancele),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                }
                            });

                    alertDialogBuilder.setNegativeButton(getString(R.string.removee), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RemoveCart(cartId, productId);
                            arrCartList.remove(position);

                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });

            String path = data.get(position).get("productImage");

            if (!path.equals("")) {
                Picasso.get().load(data.get(position).get("productImage")).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.imageView.setImageBitmap(bitmap);

                        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
                holder.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mActivity, ProductAddToCardActivity.class);
                        intent.putExtra("Id", AppSettings.putString(AppSettings.productId, data.get(position).get("productId")));
                        intent.putExtra(AppSettings.coupon_code,data.get(position).get("couponId"));
                        startActivity(intent);

                    }
                });

            }
        }

        public int getItemCount() {
            return data.size();

        }

    }

    public class Holder extends RecyclerView.ViewHolder {
        //ImageView
        ImageView imageView, ivDecrement, ivIncreIv, ivremove;

        //CustomTextview
        TextView tvrupaye,tvDiscounttt,rupayee,rupayeee,rupaye,tv_name, tvPrice, tvDiscountvalue,tvrupayee, tvDiscountCopoun, tvDiscount, tvOriginalPrice, tvtotalvalue,tvDiscountt;

        //Textview
        TextView tvCountTv, spVal;

        RelativeLayout rl, rltotalvalue;

        public Holder(View inflate) {
            super(inflate);

            //ImageView
            imageView = itemView.findViewById(R.id.img_product);
            ivDecrement = itemView.findViewById(R.id.decrIv);
            ivIncreIv = itemView.findViewById(R.id.increIv);
            ivremove = itemView.findViewById(R.id.iv_remove);
            rltotalvalue = itemView.findViewById(R.id.rltotalvalue);
            tvDiscounttt = itemView.findViewById(R.id.tvDiscounttt);
            tvrupaye = itemView.findViewById(R.id.tvrupaye);

            //CUstomtextview
            tv_name = itemView.findViewById(R.id.tv_name);
            tvDiscountCopoun = itemView.findViewById(R.id.tvDiscountCopoun);
            tvDiscountvalue = itemView.findViewById(R.id.tvDiscountvalue);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            /*spinner = itemView.findViewById(R.id.spiner);*/
            tvCountTv = itemView.findViewById(R.id.countTv);
            spVal = itemView.findViewById(R.id.spVal);
            rl = itemView.findViewById(R.id.rl);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvtotalvalue = itemView.findViewById(R.id.tvtotalvalue);
            tvDiscountt = itemView.findViewById(R.id.tvDiscountt);
            rupayee = itemView.findViewById(R.id.rupayee);
            rupaye = itemView.findViewById(R.id.rupaye);
            rupayeee = itemView.findViewById(R.id.rupayeee);
            tvrupayee = itemView.findViewById(R.id.tvrupayee);

            //spinner
            /*spinner.setVisibility(View.VISIBLE);*/

        }
    }

}


