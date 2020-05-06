package code.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.orderTrackingActivity.SelectReasonAcitivity;
import code.orderTrackingActivity.TrackingActivity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class
OrderDetailActivity extends BaseActivity {

    // CustomTextview
   TextView tvProductDiscountvalue,tvCouponDicount, tvQuantityItem, tvorderId, tv_productcharge, tvordershipincharge, tvorderTotal, tvorderfinal, tvpaymentmethod, tvonline, tvproductName, tvPrice, tvsuplier, tvpayment, tvplaced;

    //ImageView
    ImageView ivproductImage, ivcopy, ivback;

    //TextView
    TextView tvname, tvaddress, tvnumber, tvpincode, name, tvsendernumber, tv_SuplierName;

    RecyclerView rlvOrderDetails;

    //Array List
    private ArrayList<HashMap<String, String>> orderDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_detail );

        findViewById();
        //getOrderDetailAPi
        if (SimpleHTTPConnection.isNetworkAvailable()) {
            getOrderDetailAPi();
        } else {
            Toast.makeText( mActivity, R.string.errorInternet, Toast.LENGTH_SHORT ).show();
        }
    }

    private void findViewById() {

        //CustomeTextView
        tvorderId = findViewById( R.id.orderId );
        tv_productcharge = findViewById( R.id.tv_productcharge );
        tvordershipincharge = findViewById( R.id.tvordershipincharge );
        tvorderTotal = findViewById( R.id.tvorderTotal );
        tvorderfinal = findViewById( R.id.tvorderfinal );
        /*tvPrice = findViewById( R.id.pricee );*/
        tvsuplier = findViewById( R.id.tvsuplier );
        tvpayment = findViewById( R.id.tvpayment );
        tv_SuplierName = findViewById( R.id.tv_SuplierName );
        tvname = findViewById( R.id.textView4 );
        tvaddress = findViewById( R.id.textView3 );
        tvnumber = findViewById( R.id.textView5 );
        tvpincode = findViewById( R.id.textView6 );
        name = findViewById( R.id.name );
        tvsendernumber = findViewById( R.id.tvsendernumber );
        tvQuantityItem = findViewById( R.id.tvQuantityItem );
        tvProductDiscountvalue = findViewById( R.id.tvProductDiscountvalue );
        tvCouponDicount = findViewById( R.id.tvCouponDicount );


        //ImageView
        ivcopy = findViewById( R.id.ivcopy );
        ivback = findViewById( R.id.ivback );

        //RecyclerVire
        rlvOrderDetails = findViewById( R.id.rlvOrderDetails );

        tv_SuplierName.setText( AppSettings.getString( AppSettings.userName ) );
        String type = AppSettings.getString( AppSettings.Type );
        if (type.equals( "4" )) {

            tvsuplier.setText( getString(R.string.Manufacture) );

        } else

            {
                tvsuplier.setText( getString(R.string.Supplier) );
        }


        ivcopy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE );
                ClipData clip = ClipData.newPlainText( "label", tvorderId.getText().toString() );
                Toast.makeText( mActivity, " OrderId copied", Toast.LENGTH_SHORT ).show();
                clipboard.setPrimaryClip( clip );

            }
        } );
        ivback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );


    }

    //HitApi
    private void getOrderDetailAPi() {
        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );
        String url = AppUrls.OrderDetails;
        Log.v( "GetOrderDetails", url );
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put( AppConstants.projectName, jsonObject );
            jsonObject.put( "orderId", AppSettings.getString( AppSettings.order_id ) );
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
                        AppUtils.hideDialog();
                        Log.v( "orderDetails", String.valueOf( response ) );
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );

                            if (jsonObject1.getString( "resCode" ).equalsIgnoreCase( "1" )) {
                                tvorderId.setText( jsonObject1.getString( "order_number" ) );
                                tvQuantityItem.setText( jsonObject1.getString( "total_Items" ) + " " + getString(R.string.Item) );

                                AppSettings.putString( AppSettings.order_number, jsonObject1.getString( "order_number" ) );

                                Log.v( "kdfkd", jsonObject1.getString( "order_number" ) );

                                tvordershipincharge.setText( "+ ₹ "+ jsonObject1.getString( "shippment_charge" ) );

                                tvorderfinal.setText( "₹ " + jsonObject1.getString( "customerPrice" ) );

                                tvorderTotal.setText( "₹ "  + jsonObject1.getString( "final_price" ) );

                                tv_productcharge.setText( "+ ₹ " + jsonObject1.getString( "poduct_charges" ) );

                                String total_coupon_discount=jsonObject1.getString( "total_coupon_discount" );
                                String total_product_discount=jsonObject1.getString( "total_product_discount" );


                                if(total_product_discount.equalsIgnoreCase("0"))
                                {
                                    tvProductDiscountvalue.setText("- ₹ 0");
                                }

                                else
                                {
                                    tvProductDiscountvalue.setText("- ₹ "+total_product_discount);
                                }

                                if(total_coupon_discount.equalsIgnoreCase("0"))
                                {
                                    tvCouponDicount.setText("- ₹ 0");
                                }

                                else
                                {
                                    tvCouponDicount.setText("- ₹ "+total_coupon_discount);
                                }


                                JSONArray jsonArray = jsonObject1.getJSONArray( "SenderDetails" );

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject5 = jsonArray.getJSONObject( i );
                                    name.setText( AppSettings.getString(AppSettings.profilename) );
                                    tvsendernumber.setText(AppSettings.getString(AppSettings.mobilee) );

                                }

                                JSONArray jsonArray1 = jsonObject1.getJSONArray( "CustomerDetails" );
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray1.getJSONObject( i );

                                    String address = jsonObject3.get( "flatHouseBulding" ) + ", " + jsonObject3.get( "streetColony" ) + ", " + jsonObject3.get( "landmark" ) + "," + jsonObject3.get( "city" ) + ", " + jsonObject3.get( "state" );

                                    tvaddress.setText( address );

                                    tvname.setText( jsonObject3.getString( "customerName" ) );

                                    tvnumber.setText( jsonObject3.getString( "phoneNumber" ) );

                                    tvpincode.setText( jsonObject3.getString( "pincode" ) );


                                }
                                JSONArray jsonArray3 = jsonObject1.getJSONArray( "ProductList" );

                                for (int i = 0; i < jsonArray3.length(); i++) {

                                    JSONObject jsonObject5 = jsonArray3.getJSONObject( i );

                                    HashMap<String, String> hashlist = new HashMap();

                                    hashlist.put( "product_id", jsonObject5.getString( "product_id" ) );
                                    hashlist.put( "sku_code", jsonObject5.getString( "sku_code" ) );

                                    hashlist.put( "productName", jsonObject5.getString( "productName" ) );

                                    hashlist.put( "productThumbnil", jsonObject5.getString( "productThumbnil" ) );

                                    hashlist.put( "productPrice", jsonObject5.getString( "productPrice" ) );

                                    hashlist.put( "finalPrice", jsonObject5.getString( "finalPrice" ) );

                                    hashlist.put( "size", jsonObject5.getString( "size" ) );

                                    hashlist.put( "status", jsonObject5.getString( "status" ) );

                                    hashlist.put( "quantity", jsonObject5.getString( "quantity" ) );
                                    hashlist.put( "return", jsonObject5.getString( "return" ) );

                                    orderDetails.add( hashlist );

                                }
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager( mActivity, 1 );

                                CatalogueAdapter adapterr = new CatalogueAdapter( mActivity, orderDetails );

                                rlvOrderDetails.setLayoutManager( layoutManager );

                                rlvOrderDetails.setAdapter( adapterr );

                            } else {

                            }

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

    private class CatalogueAdapter extends RecyclerView.Adapter<OrderDetailActivity.CatalogueHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public CatalogueAdapter(Activity mActivity, ArrayList<HashMap<String, String>> orderDetails) {
            data = orderDetails;
        }

        public OrderDetailActivity.CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderDetailActivity.CatalogueHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.order_details, parent, false ) );
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OrderDetailActivity.CatalogueHolder holder, final int position) {
            String statuss = data.get( position ).get( "status" );
            String productName = data.get( position ).get( "productName" ).trim();
            holder.tvproductname.setText( productName.trim() );
            holder.tvproductprice.setText("₹ " + data.get( position ).get( "productPrice" ) );
            holder.tvsize.setText( data.get( position ).get( "size" ) );
            holder.tvQuantity.setText( data.get( position ).get( "quantity" ) );
            final String product = data.get( position ).get( "product_id" );
            final String sku_code = data.get( position ).get( "sku_code" );


            if(sku_code.equalsIgnoreCase(""))

            {
                holder.tvsku.setVisibility(View.GONE);
                holder.tvskuu.setVisibility(View.GONE);
            }
            else {
                holder.tvsku.setText(sku_code);
                holder.tvsku.setVisibility(View.VISIBLE);
                holder.tvskuu.setVisibility(View.VISIBLE);

            }



            if(data.get( position ).get( "return" ).equalsIgnoreCase("No"))
            {
                holder.tvreturn.setVisibility( View.GONE );
            }

            else {
                holder.tvreturn.setVisibility( View.VISIBLE );

            }


           /* if (image.isEmpty()) {
                iview.setImageResource(R.drawable.placeholder);
            } else{
                Picasso.get().load(image).into(iview);
            }*/

            Picasso.get().load( data.get( position ).get( "productThumbnil" ) ).placeholder(R.mipmap.logo_grey).resize(400,600).into( holder.ivproductImage );


            if (statuss.equals( "1" )) {

                holder.tvorderstatuts.setText( getString(R.string.Ordered));
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.green ) );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvreturn.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvcancel.setVisibility( View.VISIBLE );

            } else if (statuss.equals( "2" )) {

                holder.tvorderstatuts.setText( getString(R.string.shiped));
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.yellow ) );
                holder.tvTrack.setVisibility( View.VISIBLE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.GONE );
                holder.tvcancel.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );



            } else if (statuss.equals( "3" )) {

                holder.tvorderstatuts.setText( getString(R.string.deliverd) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.green ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.VISIBLE );
                holder.tvTrackkk.setVisibility( View.GONE );
                holder.tvreturn.setVisibility( View.VISIBLE );

            } else if (statuss.equals( "4" )) {

                holder.tvorderstatuts.setText( getString(R.string.cancelled) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.red ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );

            }

             else if (statuss.equals( "5" )) {

                holder.tvorderstatuts.setText( getString(R.string.returnpending) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.yellow ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );
                

            }


            else if (statuss.equals( "6" )) {

                holder.tvorderstatuts.setText( getString(R.string.returnrequest) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.green ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );


            }


            else if (statuss.equals( "7" )) {

                holder.tvorderstatuts.setText( getString(R.string.pickuped) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.green ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );


            }



            else if (statuss.equals( "8" )) {

                holder.tvorderstatuts.setText( getString(R.string.returnned) );
                holder.tvorderstatuts.setBackgroundColor( getResources().getColor( R.color.green ) );
                holder.tvcancel.setVisibility( View.GONE );
                holder.tvTrack.setVisibility( View.GONE );
                holder.tvTrackk.setVisibility( View.GONE );
                holder.tvTrackkk.setVisibility( View.VISIBLE );
                holder.tvreturn.setVisibility( View.GONE );


            }

            holder.tvTrack.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, TrackingActivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productThumbnil", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productThumbnil" ) ) );
                    intent.putExtra( "order_id", AppSettings.putString( AppSettings.order_id, AppSettings.getString( AppSettings.order_number ) ) );
                    intent.putExtra( "product_id", AppSettings.putString( AppSettings.productId, product ) );
                    Log.v( "productIddd", data.get( position ).get( "product_id" ) );
                    startActivity( intent );

                }
            } );

            holder.tvTrackk.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, TrackingActivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productThumbnil", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productThumbnil" ) ) );
                    intent.putExtra( "order_id", AppSettings.putString( AppSettings.order_id, AppSettings.getString( AppSettings.order_number ) ) );
                    intent.putExtra( "product_id", AppSettings.putString( AppSettings.productId, product ) );
                    Log.v( "productIddd", data.get( position ).get( "product_id" ) );
                    startActivity( intent );

                }
            } );
            holder.tvTrackkk.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, TrackingActivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productThumbnil", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productThumbnil" ) ) );
                    intent.putExtra( "order_id", AppSettings.putString( AppSettings.order_id, AppSettings.getString( AppSettings.order_number ) ) );
                    intent.putExtra( "product_id", AppSettings.putString( AppSettings.productId, product ) );
                    Log.v( "productIddd", data.get( position ).get( "product_id" ) );
                    startActivity( intent );

                }
            } );

            holder.tvcancel.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, SelectReasonAcitivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productThumbnil", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productThumbnil" ) ) );
                    intent.putExtra( "order_id", AppSettings.putString( AppSettings.order_id, AppSettings.getString( AppSettings.order_number ) ) );
                    intent.putExtra( "product_id", AppSettings.putString( AppSettings.productId, product ) );
                    Log.v( "productIddd", data.get( position ).get( "product_id" ) );

                    startActivity( intent );

                }
            } );


            holder.tvreturn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, ReturnDetails.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productThumbnil", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productThumbnil" ) ) );
                    intent.putExtra( "order_id", AppSettings.putString( AppSettings.order_id, AppSettings.getString( AppSettings.order_number ) ) );
                    intent.putExtra( "product_id", AppSettings.putString( AppSettings.productId, product ) );
                    Log.v( "productIddd", data.get( position ).get( "product_id" ) );

                    startActivity( intent );

                }
            } );
        }

        public int getItemCount() {
            return data.size();
        }

    }

    public class CatalogueHolder extends RecyclerView.ViewHolder {

        //ImageView
        ImageView ivproductImage;

        //TextView
        TextView tvTrackkk,tvsku,tvskuu,tvreturn,tvTrackk, tvcancel, tvTrack, tvorderstatuts, tvproductname, tvproductprice, tvsize, tvQuantity;

        public CatalogueHolder(View itemView) {
            super( itemView );

            //TextView
            tvorderstatuts = itemView.findViewById( R.id.tvorderstatuts );
            tvproductname = itemView.findViewById( R.id.tvproductname );
            tvproductprice = itemView.findViewById( R.id.tvproductprice );
            tvsize = itemView.findViewById( R.id.tvsizee );
            tvQuantity = itemView.findViewById( R.id.tvqtyy );
            tvTrack = itemView.findViewById( R.id.tvTrack );
            tvcancel = itemView.findViewById( R.id.tvcancel );
            tvTrackk = itemView.findViewById( R.id.tvTrackk );
            tvTrackkk = itemView.findViewById( R.id.tvTrackkk );
            tvreturn = itemView.findViewById( R.id.tvreturn );
            tvsku = itemView.findViewById( R.id.tvsku );
            tvskuu = itemView.findViewById( R.id.tvskuu );

            //ImageView
            ivproductImage = itemView.findViewById( R.id.ivproductImage );


        }
    }

}
