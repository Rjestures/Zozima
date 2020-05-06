package code.orderTrackingActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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

import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class TrackingActivity extends BaseActivity {
    //Textview
    TextView tvproductname, tvSize, tvQuantity,tvDatee;

    //imageview
    ImageView ivproductImage, ivback;
    //RecyclerView
    RecyclerView recyclerViewTracking;
    String date;
    //ArrayList
    private ArrayList<HashMap<String, String>> TrackingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tracking );
        findViewById();
        hitTrackApi();

    }

    private void findViewById() {

        //ImageView
        ivproductImage = findViewById( R.id.ivproductImage );
        ivback = findViewById( R.id.ivback );

        //TextView
        tvSize = findViewById( R.id.tvSize );
        tvQuantity = findViewById( R.id.tvqtyy );
        tvproductname = findViewById( R.id.tvproductname );



        //RecyclerView
        recyclerViewTracking = findViewById( R.id.recyclerViewTracking );

        //setData
        tvproductname.setText( AppSettings.getString( AppSettings.productName ) );
        tvSize.setText( AppSettings.getString( AppSettings.size ) );
        tvQuantity.setText( AppSettings.getString( AppSettings.quantity ) );
        Picasso.get().load( AppSettings.getString( AppSettings.productImage ) ).placeholder(R.mipmap.logo_grey).resize(400,600).into( ivproductImage );
        ivback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

    }

        private void hitTrackApi() {

        AppUtils.showRequestDialog( mActivity );

        AppUtils.hideSoftKeyboard( mActivity );

        String url = AppUrls.TrackOrder;

        final JSONObject jsonObject = new JSONObject();

        final JSONObject json = new JSONObject();

        try {

            jsonObject.put( "orderId", AppSettings.getString( AppSettings.order_id ) );
            jsonObject.put( "productId", AppSettings.getString( AppSettings.productId ) );
            jsonObject.put( "size", AppSettings.getString( AppSettings.size ) );
            json.put( AppConstants.projectName, jsonObject );
            Log.v( "finalObject", String.valueOf( json ) );

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
                        Log.v( "Tracking", String.valueOf( response ) );
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );
                            String resCode = jsonObject1.getString( "resCode" );


                            if (resCode.equals( "1" )) {

                                JSONArray jsonArray1 = jsonObject1.getJSONArray( "OrderTrack" );
                                for (int i = 0; i < jsonArray1.length(); i++) {

                                    HashMap<String, String> hashlist = new HashMap();
                                    JSONObject arrayJSONObject1 = jsonArray1.getJSONObject( i );
                                    hashlist.put( "finalstatus", jsonObject1.getString( "status" ) );
                                    hashlist.put( "remark", arrayJSONObject1.getString( "remark" ) );
                                    hashlist.put( "date", arrayJSONObject1.getString( "date" ) );
                                    hashlist.put( "msg", arrayJSONObject1.getString( "msg" ) );
                                    hashlist.put( "status", arrayJSONObject1.getString( "status" ) );

                                    TrackingList.add( hashlist );


                                }

                            }
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager( mActivity, 1 );
                            TrackingAdapter trackingAdapter = new TrackingAdapter( mActivity, TrackingList );
                            recyclerViewTracking.setLayoutManager( layoutManager );
                            recyclerViewTracking.setAdapter( trackingAdapter );


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.v( "bank", String.valueOf( anError ) );

                    }

                } );
    }

    private class TrackingAdapter extends RecyclerView.Adapter<TrackingActivity.Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public TrackingAdapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
            Log.v("MyCatList",data.toString());
        }

        public TrackingActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TrackingActivity.Holder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.tracking_layout, parent, false ) );
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final TrackingActivity.Holder holder, final int position) {

            holder.tvDate.setText( data.get( position ).get( "date" ) );
            holder.tvOrderedShippedDate.setText( data.get( position ).get( "date" ) );
            holder.tvOrderedDelivereDate.setText( data.get( position ).get( "date" ) );
            holder.tvOrderReturnDate.setText( data.get( position ).get( "date" ) );
            holder.tvReason.setText( data.get( position ).get( "remark" ) );
            String status = data.get( position ).get( "status" );
            String msg = data.get( position ).get( "msg" );
            String finalstatus = data.get( position ).get( "finalstatus" );
            Log.v("satutss",status);
            Log.v("satutss",data.get( position ).get( "date" ));
            Log.v("finalstatuss",data.get( position ).get( "finalstatus" ));


            if(finalstatus.equalsIgnoreCase( "8" )){
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );
            }

            if (status.equals( "1" )) {

                if(!msg.equals( "" ) )
                {
                    holder.tvOrdercancelle.setText( " "+ msg );

                }
                else
                {
                    holder.tvOrdercancelle.setText( getString(R.string.placed) );
                }

                holder.tvReasn.setVisibility( View.GONE );
                holder.tvReason.setVisibility( View.GONE );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );


            } else if (status.equals( "" )) {
                Log.v("statattat",status);

                if(!msg.equals( "" ) )
                {
                    holder.tvOrderShipped.setText( getString(R.string.shiped)+getString( R.string.comma )+" "+ msg );

                }
                else
                {
                    holder.tvOrderShipped.setText( getString(R.string.shiped) );
                }
                holder.ivOrderShipped.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line2.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvOrderedShippedDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrderShipped.setTextColor( getResources().getColor( R.color.green ) );


            }

            else if (status.equals( "3" )) {
                holder.tvOrdercancelle.setText(getString(R.string.deliverd) );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setVisibility( View.GONE );
                holder.tvReason.setVisibility( View.GONE );

            } 
            
            else if (status.equals( "4" )) {
                holder.tvOrdercancelle.setText(getString(R.string.cancelled) );
                holder.tvOrdercancelle.setText(getString(R.string.deliverd) );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.red ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.red ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.red ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.red ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.red ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.red ) );
                holder.tvReasn.setVisibility( View.VISIBLE );
                holder.tvReason.setVisibility( View.VISIBLE );
            



            }

            else if (status.equals( "5" )) {
                holder.tvOrdercancelle.setText( getString(R.string.returnpending) );
                holder.tvReasn.setVisibility( View.GONE );
                holder.tvReason.setVisibility( View.GONE );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );


            }



            else if (status.equals( "6" )) {
                holder.tvOrdercancelle.setText( getString(R.string.returnrequest) );
                holder.tvReasn.setVisibility( View.GONE );
                holder.tvReason.setVisibility( View.GONE );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );


            }


            else if (status.equals( "7" )) {

                holder.tvOrdercancelle.setText( getString(R.string.pickuped));
                holder.tvReasn.setVisibility( View.GONE );
                holder.tvReason.setVisibility( View.GONE );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );


            }



            else if (status.equals( "8" )) {
                holder.tvOrdercancelle.setText( getString(R.string.returnrequestcomple) );
                holder.ivcircle.setBackground( getResources().getDrawable( R.drawable.ic_green_circle ) );
                holder.line1.setBackground( getResources().getDrawable( R.mipmap.green ) );
                holder.tvDate.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReason.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelle.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvReasn.setTextColor( getResources().getColor( R.color.green ) );
                holder.tvOrdercancelled.setTextColor( getResources().getColor( R.color.green ) );

            }
        }

        public int getItemCount() {
            return 1;

        }

    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvOrdercancelle, tvReason, tvDate, tvReasn, tvOrdercancelled,tvOrderShipped,tvOrderedShippedDate,tvOrderedDelivereDate,tvOrderReturnDate;

        //ImageView
        ImageView ivcircle, line1,line2,line4,ivOrderShipped,ivOrderedDelivered,line3,ivOrderReturn;

        //RelativeLayout
        RelativeLayout rlordered,rlshipped,rldelivered,rlreturn;

        public Holder(View inflate) {
            super( inflate );
            tvOrdercancelle = inflate.findViewById( R.id.tvOrdercancelle );
            tvReason = inflate.findViewById( R.id.tvReason );
            tvDate = inflate.findViewById( R.id.tvDate );
            tvReasn = inflate.findViewById( R.id.tvReasn );
            tvOrdercancelled = inflate.findViewById( R.id.tvOrdercancelled );
            tvOrderShipped = inflate.findViewById( R.id.tvOrderShipped );
            
            tvOrderedShippedDate = inflate.findViewById( R.id.tvOrderedShippedDate );
            tvOrderedDelivereDate = inflate.findViewById( R.id.tvOrderedDelivereDate );
            tvOrderReturnDate = inflate.findViewById( R.id.tvOrderReturnDate );
            
            //ImageView
            ivOrderShipped = inflate.findViewById( R.id.ivOrderShipped );
            ivOrderedDelivered = inflate.findViewById( R.id.ivOrderedDelivered );
            ivOrderReturn = inflate.findViewById( R.id.ivOrderReturn );
            ivcircle = inflate.findViewById( R.id.ivcircle );
            line1 = inflate.findViewById( R.id.line1 );
            line2 = inflate.findViewById( R.id.line2 );
            line3 = inflate.findViewById( R.id.line3 );
            line4 = inflate.findViewById( R.id.line4 );
            ivOrderReturn = inflate.findViewById( R.id.ivOrderReturn );

            //RelaytiveLayout
            rlordered = inflate.findViewById( R.id.rlordered );
            rlshipped = inflate.findViewById( R.id.rlshipped );
            rldelivered = inflate.findViewById( R.id.rldelivered );
            rlreturn = inflate.findViewById( R.id.rlreturn );


        }
    }
}