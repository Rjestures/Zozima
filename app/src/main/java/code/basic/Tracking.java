package code.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class Tracking extends BaseActivity {
    //Textview
    TextView tvproductname, tvSize, tvQuantity;

    //imageview
    ImageView ivproductImage, ivback;

    //ArrayList
    private ArrayList<HashMap<String, String>> TrackingList = new ArrayList<>();

    //RecyclerView
    RecyclerView recyclerViewTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tracking2 );
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
        recyclerViewTracking=findViewById( R.id.recyclerViewTracking );

        //setData
        tvproductname.setText( AppSettings.getString( AppSettings.getproductName ) );
        tvSize.setText( AppSettings.getString( AppSettings.getsize ) );
        tvQuantity.setText( AppSettings.getString( AppSettings.getQuantity ) );
        Picasso.get().load( AppSettings.getString( AppSettings.productThumbnil ) ).into( ivproductImage );

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
        Log.v( "urlApi", url );

        JSONObject jsonObject = new JSONObject();

        final JSONObject json = new JSONObject();


        try {
            jsonObject.put( "orderId", AppSettings.getString( AppSettings.order_number ) );
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
                            String resMsg = jsonObject1.getString( "resMsg" );

                            if(resCode.equals( "1" ))
                            {
                                JSONArray jsonArray1 = jsonObject1.getJSONArray( "OrderTrack" );
                                for(int i=0;i<jsonArray1.length();i++) {
                                    HashMap<String, String> hashlist = new HashMap();
                                    JSONObject arrayJSONObject1 = jsonArray1.getJSONObject( i );
                                    hashlist.put( "status", arrayJSONObject1.getString( "status" ) );
                                    hashlist.put( "remark", arrayJSONObject1.getString( "remark" ) );
                                    hashlist.put( "date", arrayJSONObject1.getString( "date" ) );
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
    private class TrackingAdapter extends RecyclerView.Adapter<Tracking.Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public TrackingAdapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public Tracking.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Tracking.Holder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.tracking_layout, parent, false ) );
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Tracking.Holder holder, final int position) {
            holder.tvDate.setText( data.get( position ).get( "date" ) );
            holder.tvReason.setText( data.get( position ).get( "remark" ) );
            String status=data.get( position ).get( "status" );

            if(status.equals( "1" ))

            {
                holder.tvOrdercancelle.setText( "Pending" );



            }
            else if(status.equals( "2" ))
            {
                holder.tvOrdercancelle.setText( "Shipped" );


            }
            else if(status.equals( "3" ))
            {
                holder.tvOrdercancelle.setText( "Delivered" );

            }
            else if(status.equals( "4" ))
            {
                holder.tvOrdercancelle.setText( "Canceled" );

            }

        }

        public int getItemCount() {
            return data.size();

        }

    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvOrdercancelle,tvReason,tvDate;


        public Holder(View inflate) {
            super( inflate );
            tvOrdercancelle=inflate.findViewById( R.id.tvOrdercancelle );
            tvReason=inflate.findViewById( R.id.tvReason );
            tvDate=inflate.findViewById( R.id.tvDate );



        }
    }
}
