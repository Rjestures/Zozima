package code.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.orderTrackingActivity.SelectReasonAcitivity;
import code.orderTrackingActivity.TrackingActivity;
import code.database.AppSettings;
import code.main.MainActivity;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class OrderPlaceActivity extends BaseActivity implements View.OnClickListener{
    //TextView
   TextView tvCouponDicount,tv_Productcharge,tvordershipincharge,tvorderTotal,tvorderfinal,tvordernumber,tvodername,tvproductprice,tvdate,tvear, tv_Price, tv_Size,
            tv_Quantity,tvorderdetail,tvcodcharj,tv_SuplierName, tvsuplier,tvProductCharge, tvProductDiscountvalue;

    // ImageView
    ImageView img_product,ivback,ivloader;

    //int Value
    int n;
    RecyclerView rladdtoCart;
    private ArrayList<HashMap<String, String>> OrderPaceList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_place );
        findViewById(  );
        setListener();
        String Array = AppSettings.getString( AppSettings.userarraylist );


        Log.v("userArrayList",Array);
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
              ivloader.setVisibility(View.GONE);
                hashMap.put( "cartId", obj.getString( "cartId" ) );
                if(obj.has("shippingCharges"))
                hashMap.put( "shippingCharges", obj.getString( "shippingCharges" ) );
                else
                hashMap.put( "shippingCharges", "0" );
                hashMap.put( "productId", obj.getString( "productId" ) );
                hashMap.put( "productName", obj.getString( "productName" ) );
                hashMap.put( "productImage", obj.getString( "productImage" ) );
                hashMap.put( "productPrice", obj.getString( "productPrice" ) );
                hashMap.put( "coupn_discount", obj.getString( "coupn_discount" ) );
                hashMap.put( "size", obj.getString( "size" ) );
                hashMap.put( "quantity", obj.getString( "quantity" ) );

                AppSettings.putString( AppSettings.total_count,"" );
                Log.v( "hgvgcfyg", obj.getString( "productImage" ) );

                Log.v("cuzListint", hashMap.toString());
                OrderPaceList.add(hashMap);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager( mActivity, 1 );
                OrderPlaceAdapTar adapter = new OrderPlaceAdapTar( mActivity, OrderPaceList );
                rladdtoCart.setLayoutManager( layoutManager );
                rladdtoCart.setAdapter( adapter );



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setListener() {
        ivback.setOnClickListener( this );

    }

    private void findViewById() {
        //textView
        tv_Productcharge=findViewById( R.id.tv_productcharj );
        tvordershipincharge=findViewById( R.id.tvordershipincharge );
        tvorderTotal=findViewById( R.id.tvorderTotal );
        tvorderfinal=findViewById( R.id.tvorderfinal );
        tvordernumber=findViewById( R.id.tvordernumber );
        ivback=findViewById( R.id.ivback );
        tv_Price =findViewById( R.id.tvShipping );
        tv_Size =findViewById( R.id.size );
        tv_Quantity =findViewById( R.id.quantity );
        tvodername=findViewById( R.id.tvodername );
        tvear =findViewById( R.id.ear );
        rladdtoCart=findViewById( R.id.rladdtoCart );
        tvorderdetail=findViewById( R.id.tvorderdetail );
        tvProductCharge = findViewById( R.id.tv_productcharj );
        tvProductDiscountvalue = findViewById( R.id.tvProductDiscountvalue );

        tvcodcharj = findViewById( R.id.tvcodcharj );
        tvorderfinal = findViewById( R.id.tvorderfinal );
        tvordernumber = findViewById( R.id.tvordernumber );
        tvodername = findViewById( R.id.tvodername );
        tvear = findViewById( R.id.ear );
        tvsuplier = findViewById( R.id.suplier );
        tv_SuplierName = findViewById( R.id.buity );
        tvCouponDicount = findViewById( R.id.tvCouponDicount );
        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);



        //ImageView
        tvproductprice=findViewById( R.id.tvproductprice );
        tvdate=findViewById( R.id.tvdate );

        //LinearLayou
        tvdate.setText( AppSettings.getString(AppSettings.data));
        tv_Price.setText(AppSettings.getString(AppSettings.shippingCharges));
        tv_Productcharge.setText(AppSettings.getString(AppSettings.productCharges));
        tvorderTotal.setText(getString( R.string.rupaye )+AppSettings.getString(AppSettings.orderTotal));
        tvorderfinal.setText( getString( R.string.rupaye )+ AppSettings.getString(AppSettings.edt_totalcount));
        tvorderfinal.setText( getString( R.string.rupaye )+ AppSettings.getString(AppSettings.edt_totalcount));
        tvorderdetail.setText(getString( R.string.id )+""+AppSettings.getString( AppSettings.order_id ) );

        String total_coupon_discount= AppSettings.getString(AppSettings.total_coupon_discount);
        String total_product_discount=AppSettings.getString(AppSettings.total_product_discount);

        if(total_product_discount.equalsIgnoreCase("0"))
        {
            tvProductDiscountvalue.setText("0");
        }

        else
        {
            tvProductDiscountvalue.setText(total_product_discount);
        }


        if(total_coupon_discount.equalsIgnoreCase("0"))

        {
            tvCouponDicount.setText("0");
        }
        else {
            tvCouponDicount.setText(total_coupon_discount);
        }

/*
        tvproductprice.setText(getString( R.string.rupaye )+AppSettings.getString( AppSettings.productPrice ) );
*/
        int total=   Integer.parseInt( AppSettings.getString(AppSettings.orderTotal) );
        int finall=  Integer.parseInt( AppSettings.getString(AppSettings.edt_totalcount));
        n=finall-total;
        Log.v( "msjd", String.valueOf( n ) );
        if(n>0)
        {
            tvear.setText(getString( R.string.rupaye )+ Integer.toString( n  ) );
        }
        else
        {
        }
        tvsuplier.setText( AppSettings.getString( AppSettings.userName ) );
        String type=AppSettings.getString( AppSettings.Type );

        if (type.equals( "4" )) {
            tv_SuplierName.setText( getString(R.string.Manufacture) );
        } else {
            tv_SuplierName.setText( getString(R.string.Supplier) );
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.tvcancel:
                startActivity( new Intent( mActivity, SelectReasonAcitivity.class ) );
                break;

        }
    }





    private class OrderPlaceAdapTar extends RecyclerView.Adapter<OrderPlaceActivity.CatalogueHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public OrderPlaceAdapTar(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public OrderPlaceActivity.CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderPlaceActivity.CatalogueHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.orderplace, parent, false ) );
        }



        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OrderPlaceActivity.CatalogueHolder holder, final int position) {
            holder.tv_productName.setText( data.get( position ).get( "productName" ) );

            holder.tv_size.setText( data.get( position ).get( "size" ) );
            holder.tv_Price.setText("₹ " + data.get( position ).get( "productPrice" ) );
            holder.tv_Quantity.setText( data.get( position ).get( "quantity" ) );

       /*     if(data.get(position).get("coupn_discount").equalsIgnoreCase("0"))
            {
                holder.tvDiscountCopoun.setVisibility(View.GONE);
                holder.tvDiscountvalue.setVisibility(View.GONE);
                holder.rlcopn.setVisibility(View.GONE);
            }
            else {
                holder.tvDiscountCopoun.setText(getString(R.string.rupaye)+" "+data.get(position).get("coupn_discount")+" off");
                holder.tvDiscountvalue.setVisibility(View.VISIBLE);
                holder.tvDiscountCopoun.setVisibility(View.VISIBLE);
                holder.rlcopn.setVisibility(View.VISIBLE);
            }
*/

            Picasso.get().load( data.get( position ).get( "productImage" ) ).placeholder(R.mipmap.logo_grey).resize(400,600).into( holder. ivproductImage);


            holder.tvcancel.setOnClickListener( new View.OnClickListener() {
                @Override
             public void onClick(View v) {
                    Intent intent = new Intent( mActivity, SelectReasonAcitivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productImage", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productImage" ) ) );
                    intent.putExtra( "productId", AppSettings.putString( AppSettings.productId, data.get( position ).get( "productId" ) ) );
                    startActivity( intent );


                  }
               } );
              holder.tvTrack.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent( mActivity, TrackingActivity.class );
                    intent.putExtra( "productName", AppSettings.putString( AppSettings.productName, data.get( position ).get( "productName" ) ) );
                    intent.putExtra( "productPrice", AppSettings.putString( AppSettings.productPrice, data.get( position ).get( "productPrice" ) ) );
                    intent.putExtra( "size", AppSettings.putString( AppSettings.size, data.get( position ).get( "size" ) ) );
                    intent.putExtra( "quantity", AppSettings.putString( AppSettings.quantity, data.get( position ).get( "quantity" ) ) );
                    intent.putExtra( "productImage", AppSettings.putString( AppSettings.productImage, data.get( position ).get( "productImage" ) ) );
                    intent.putExtra( "productId", AppSettings.putString( AppSettings.productId, data.get( position ).get( "productId" ) ) );

                    startActivity( intent );                }
            } );
                }



        public int getItemCount() {
            return data.size();
        }
    }

    public class CatalogueHolder extends RecyclerView.ViewHolder {

        //TextView
        TextView tv_paymentmethod, tv_online, tv_productName, tv_Price, tv_size, tv_Quantity,tvcancel,tvTrack, tvDiscountCopoun,tvDiscountvalue;

        RelativeLayout rlcopn;

        //ImageView
        ImageView ivproductImage;

        public CatalogueHolder(View itemView) {
            super( itemView );

            //TextView
            /*tv_paymentmethod =itemView.findViewById( R.id.paymentmethod );*/
            /*tv_online =itemView.findViewById( R.id.online );*/
            tv_productName =itemView.findViewById( R.id.tvnameee );
            tv_Price =itemView.findViewById( R.id.tvproductprice );
            tv_size =itemView.findViewById( R.id.tvsizee );
            tv_Quantity =itemView.findViewById( R.id.tvqtyy );
            tvcancel=itemView.findViewById( R.id.tvcancel );
            tvTrack=itemView.findViewById( R.id.tvTrack );
           /* tvDiscountCopoun=itemView.findViewById( R.id.tvDiscountCopoun );
            tvDiscountvalue=itemView.findViewById( R.id.tvDiscountvalue );*/
            /*rlcopn=itemView.findViewById( R.id.rlcopn );*/

            //ImageView
            ivproductImage=itemView.findViewById( R.id.ivproductImage );
            img_product=itemView.findViewById( R.id.img_product );




        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( OrderPlaceActivity.this, MainActivity.class );
        intent.putExtra( "pagePath", 1 );
        startActivity( intent );
    }
}

