package code.basic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.address.ShippingAddress;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class AddMarginActivity extends BaseActivity implements View.OnClickListener {

    //CustomeTextview
    TextView tvProceed, tvcodcharj,tvorderPrice, tvproductcharge, tvshipincharge,tvProductDiscountvalue, tvCouponDicount,tvorderTotal, tv_Message, tv_ShowEarnAmount,tv_SuplierName, tvsuplier;

    ;

    // EditText
    EditText edt_totalcount;


    //ImageView
    ImageView ivback,ivloader;

    // String Variable
    String val;
    String margin;
    String type;

    //integere Values
    double a;
    double b;
    RecyclerView Rlv_CategoryList;

    private ArrayList<HashMap<String, String>> categoryList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_margin );
        findViewById();

        if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
            categoryList.clear();
            AddMarginHitApi();


        } else {

            AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
        }

        setListeners();

        edt_totalcount.addTextChangedListener( new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                val = edt_totalcount.getText().toString();
                String orderPrice = tvorderPrice.getText().toString();

                try {
                    margin = String.valueOf( Integer.parseInt( val ) - Integer.parseInt( orderPrice ) );
                    tv_ShowEarnAmount.setText( margin );

                } catch (NumberFormatException ex) { // handle your exception
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v("MYSTringVal",Integer.toString( edt_totalcount.getText().toString().length() ));
                if(edt_totalcount.getText().toString().length()<=0){
                    tv_ShowEarnAmount.setText( "0" );
                }
                // TODO Auto-generated method stub
            }
        } );


    }



    private void setListeners() {
        ivback.setOnClickListener( this );
        tvProceed.setOnClickListener( this );

    }

    ////////////findViewbyId/////////////
    private void findViewById() {

        //TextView
        tvProceed = findViewById( R.id.tvProceed );
        tv_ShowEarnAmount = findViewById( R.id.showthetotalprice );
        tvorderPrice = findViewById( R.id.tv_orderPrice );
        tvproductcharge = findViewById( R.id.tv_productcharj );
        tvCouponDicount = findViewById( R.id.tvCouponDicount );
        tvshipincharge = findViewById( R.id.tvShipping );
        tvorderTotal = findViewById( R.id.oderTotal);
        tv_Message = findViewById( R.id.msg );
        tvsuplier = findViewById( R.id.suplier );
        tv_SuplierName = findViewById( R.id.buity );
        tvcodcharj = findViewById( R.id.tvcodcharj );
        tvProductDiscountvalue = findViewById( R.id.tvProductDiscountvalue );

        Rlv_CategoryList = findViewById( R.id.rladdtoCart );
        ivloader = findViewById( R.id.ivloader );
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);

        //EditText
        edt_totalcount = findViewById( R.id.edt_totalcount );

        //ImageView
        ivback = findViewById( R.id.ivback );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.tvProceed:
                try {
                    b = Integer.parseInt( edt_totalcount.getText().toString() );

                    AppSettings.putString( AppSettings.edt_totalcount, edt_totalcount.getText().toString() );
                } catch (NumberFormatException ex) { // handle your exception
                }

                double c=b-a;
                Log.v("ValueATa",Double.toString( b )+"b");
                Log.v("ValueATa",Double.toString( a )+"a");
                Log.v("ValueATa",Double.toString( c )+"c");

                if (a > b) {

                    tv_Message.setVisibility( View.VISIBLE );

                    Log.v( "valueee", String.valueOf( c ) );

                } else if(b>(2*a)){
                    tv_Message.setVisibility( View.VISIBLE );
                    tv_Message.setText(getString(R.string.pleaseenteramount)+"("+getResources().getString( R.string.rupaye )+" "+a+")"+getString(R.string.getlestthen)+"("+getResources().getString( R.string.rupaye )+2*a+" )");
/*
                    tv_Message.setText( "Please, Enter an amount greater than or equal to order total("+getResources().getString( R.string.rupaye )+" "+a+") and less than double of it ("+getResources().getString( R.string.rupaye )+2*a+" ) " );
*/

                }
                else
                {
                    tv_Message.setVisibility( View.GONE );
                    Intent I = new Intent( mActivity, ShippingAddress.class );
                    startActivity( I );
                    AppSettings.putString( AppSettings.addedById, ""  );
                    AppSettings.putString( AppSettings.addedType, "" );

                }


                break;


        }

    }

    private void AddMarginHitApi() {
        AppUtils.hideSoftKeyboard( mActivity );
        String url = AppUrls.getCartList;

        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put( AppConstants.projectName, jsonObject );
            jsonObject.put( "userId", AppSettings.getString( AppSettings.userId ) );
            Log.v( "findObject", String.valueOf( json ) );

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
                        ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();
                        Log.v( "Addmargin", String.valueOf( response ) );
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );
                            String resMsg = jsonObject1.getString( "resMsg" );

                            jsonObject1.getString( "shippingCharges" );
                            AppSettings.putString( AppSettings.shiping, jsonObject1.getString( "shippingCharges" ) );
                            Log.v( "ndfbdn", jsonObject1.getString( "shippingCharges" ) );
                            tvproductcharge.setText( jsonObject1.getString( "productCharges" ) );
                            tvshipincharge.setText( jsonObject1.getString( "shippingCharges" ) );
                            tvorderTotal.setText( jsonObject1.getString( "orderTotal" ) );
                            a= Double.parseDouble( jsonObject1.getString( "orderTotal" ) );

                            String total_coupon_discount=jsonObject1.getString("total_coupon_discount");
                            AppSettings.putString( AppSettings.total_coupon_discount, total_coupon_discount );



                            String total_product_discount=jsonObject1.getString("total_product_discount");
                            AppSettings.putString( AppSettings.total_product_discount, total_product_discount );

                            if(total_product_discount.equalsIgnoreCase("0"))
                            {
                                tvProductDiscountvalue.setText("0");
                            }
                            else {
                                tvProductDiscountvalue.setText(total_product_discount);
                            }


                            if(total_coupon_discount.equalsIgnoreCase("0"))
                            {
                                tvCouponDicount.setText("0");
                            }
                            else {
                                tvCouponDicount.setText(total_coupon_discount);
                            }
                            tvorderPrice.setText( jsonObject1.getString( "orderTotal" ) );
                            tvsuplier.setText(jsonObject1.getString( "userName" )  );
                            tvcodcharj.setText( jsonObject1.getString( "codCharges" ) );

                            type=jsonObject1.getString( "Type" );

                            if (type.equals( "4" )) {

                                tv_SuplierName.setText( getString(R.string.Manufacture) );
                            } else {
                                tv_SuplierName.setText( getString(R.string.Supplier) );
                            }
                             AppSettings.putString( AppSettings.productCharges, jsonObject1.getString( "productCharges" ) );
                             AppSettings.putString( AppSettings.orderTotal, jsonObject1.getString( "orderTotal" ) );

                            JSONArray jsonArray1 = jsonObject1.getJSONArray( "Items" );
                            JSONArray jsonArray3 = jsonObject1.getJSONArray( "Items" );
                            AppSettings.putString( AppSettings.userarraylist, jsonArray3.toString() );


                            for (int n = 0; n < jsonArray1.length(); n++) {
                                HashMap<String, String> hashlist = new HashMap();
                                JSONObject arrayJSONObject1 = jsonArray1.getJSONObject( n );
                                hashlist.put( "cartId", arrayJSONObject1.getString( "cartId" ) );
                                hashlist.put( "coupn_discount", arrayJSONObject1.getString( "coupn_discount" ) );
                                hashlist.put( "productId", arrayJSONObject1.getString( "productId" ) );
                                hashlist.put( "productImage", arrayJSONObject1.getString( "productImage" ) );
                                hashlist.put( "quantity", arrayJSONObject1.getString( "quantity" ) );
                                hashlist.put( "productPrice", arrayJSONObject1.getString( "productPrice" ) );
                                hashlist.put( "size", arrayJSONObject1.getString( "size" ) );
                                hashlist.put( "productName", arrayJSONObject1.getString( "productName" ) );
                                hashlist.put( "DiscounType", arrayJSONObject1.getString( "DiscounType" ) );
                                hashlist.put( "DiscountValue", arrayJSONObject1.getString( "DiscountValue" ) );
                                hashlist.put( "prod_price", arrayJSONObject1.getString( "prod_price" ) );
                                AppSettings.putString( AppSettings.productImage, arrayJSONObject1.getString( "productImage" ) );
                                Log.v( "mdsdnjfksd", arrayJSONObject1.getString( "productImage" ) );
                                AppSettings.putString( AppSettings.productPrice, arrayJSONObject1.getString( "productPrice" ) );
                                AppSettings.putString( AppSettings.productName, arrayJSONObject1.getString( "productName" ) );
                                AppSettings.putString( AppSettings.quantity, arrayJSONObject1.getString( "quantity" ) );
                                AppSettings.putString( AppSettings.size, arrayJSONObject1.getString( "size" ) );
                                AppSettings.putString( AppSettings.productId, arrayJSONObject1.getString( "productId" ) );


                                categoryList.add( hashlist );
                                JSONArray jsonArray2 = arrayJSONObject1.getJSONArray( "unitNames" );

                                for (int n1 = 0; n1 < jsonArray2.length(); n1++) {

                                    JSONObject arrayJSONObject2 = jsonArray2.getJSONObject( n1 );
                                    String statename = arrayJSONObject2.getString( "unit_name" ); }
                                hashlist.put( "orderTotal", jsonObject1.getString( "orderTotal" ) );


                            }

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager( mActivity, 1 );
                            CatalogueAdapter adapter = new CatalogueAdapter( mActivity, categoryList );
                            Rlv_CategoryList.setLayoutManager( layoutManager );
                            Rlv_CategoryList.setAdapter( adapter );

                        } catch (JSONException e) {
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


    private class CatalogueAdapter extends RecyclerView.Adapter<AddMarginActivity.CatalogueHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public CatalogueAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public AddMarginActivity.CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AddMarginActivity.CatalogueHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.addmarginlayout, parent, false ) );
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final AddMarginActivity.CatalogueHolder holder, final int position) {


            if (data.get(position).get("DiscounType").equals("1"))

                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") + getString(R.string.off));


            else if (data.get(position).get("DiscounType").equals("2"))
                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") +getString(R.string.percent));

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



            holder.tv_Product_Name.setText( data.get( position ).get( "productName" ) );
            /*holder.tv_Online.setText( data.get( position ).get( "userName" ) );*/
            holder.tv_Size.setText(data.get( position ).get( "size" ) );
            holder.tv_Quantity.setText( data.get( position ).get( "quantity" ) );
            Picasso.get().load( data.get( position ).get( "productImage" ) ).placeholder(R.mipmap.logo_grey).resize(400,600).into( holder.img_product );

            if(data.get(position).get("coupn_discount").equalsIgnoreCase("0"))
               {
                holder.tvDiscountCopoun.setVisibility(View.GONE);
                holder.tvDiscountvalue.setVisibility(View.GONE);
                holder.rlcopn.setVisibility(View.GONE);
                holder.rltotalvalue.setVisibility(View.GONE);

            }
            else {
                holder.tvDiscountCopoun.setText(getString(R.string.rupayeee)+" "+data.get(position).get("coupn_discount")+ " off");
                holder.tvDiscountvalue.setVisibility(View.VISIBLE);
                holder.tvDiscountCopoun.setVisibility(View.VISIBLE);
                int dicount=Integer.parseInt(data.get(position).get("coupn_discount"));
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

        TextView tvDiscount, tvTotal,tvOriginalPrice, tv_Product_Name, tvPrice, tv_Size, tv_Quantity,tvDiscountCopoun,tvDiscountvalue,tvtotalvalue;
        ImageView img_product;

        RelativeLayout rlcopn,rltotalvalue;

        public CatalogueHolder(View itemView) {

            super( itemView );

            tv_Product_Name = itemView.findViewById( R.id.productName );

            tv_Size = itemView.findViewById( R.id.size );
            tv_Quantity = itemView.findViewById( R.id.quantity );
            tvDiscountCopoun = itemView.findViewById( R.id.tvDiscountCopoun );
            tvDiscountvalue = itemView.findViewById( R.id.tvDiscountvalue );
            rlcopn = itemView.findViewById( R.id.rlcopn );
            tvOriginalPrice = itemView.findViewById( R.id.tvOriginalPrice );
            tvDiscount = itemView.findViewById( R.id.tvDiscount );
            tvPrice = itemView.findViewById( R.id.tvPrice );
            tvtotalvalue = itemView.findViewById( R.id.tvtotalvalue );
            rltotalvalue = itemView.findViewById( R.id.rltotalvalue );
            tvTotal = itemView.findViewById( R.id.tvTotal );

            //ImageView
            img_product = itemView.findViewById( R.id.img_product );


        }
    }



}