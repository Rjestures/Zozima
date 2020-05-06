package code.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class ReturnDetails extends BaseActivity  implements View.OnClickListener{
    //ImageView
    ImageView ivproductImage, ivback;

    //TextView
    TextView tvproductname, tvdetails, tvQuantity,canceleproduct;

    //Spiner
    Spinner spreason;


    String reson;
    String ids="";
    SpinnerAdapter spinner;
    ArrayList<String> ResonList;
    ArrayList<String> ResonListId;
    ArrayList<HashMap<String, String>> arrResonList;

    //EditText
    EditText edt_remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_return_details );
        findViewById();
        setListener();
        Hit_GetAllReason();
    }

    private void setListener() {
        ivback.setOnClickListener( this );
        canceleproduct.setOnClickListener( this );
    }

    private void findViewById() {

        //TextView
        ivproductImage = findViewById( R.id.ivproductImage );
        ivback = findViewById( R.id.ivback );
        tvdetails = findViewById( R.id.tvdetails );
        tvQuantity = findViewById( R.id.tvqtyy );
        tvproductname = findViewById( R.id.tvproductname );
        canceleproduct = findViewById( R.id.canceleproduct );

        //EditText
        edt_remark = findViewById( R.id.edt_remark );


        //Spinner
        spreason = findViewById( R.id.spreason );

        ResonList = new ArrayList<>();
        ResonListId = new ArrayList<>();
        arrResonList = new ArrayList<>();


        tvproductname.setText( AppSettings.getString( AppSettings.productName ) );
        tvdetails.setText( AppSettings.getString( AppSettings.size ) );
        tvQuantity.setText( AppSettings.getString( AppSettings.quantity ) );
        Picasso.get().load( AppSettings.getString( AppSettings.productImage ) ).placeholder(R.mipmap.logo_grey) .into( ivproductImage );

        spreason.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString()=="Select Reason For Return ")

                {
                    canceleproduct.setBackgroundDrawable( getResources().getDrawable( R.color.grey ) );

                }

                else

                {
                    ids = ResonListId.get(spreason.getSelectedItemPosition());
                    reson = ResonList.get(spreason.getSelectedItemPosition());
                    canceleproduct.setBackgroundDrawable( getResources().getDrawable( R.drawable.rectangle_gradient_pink ) );


                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivback:
                finish();
                break;


            case R.id.canceleproduct:

                if(spreason.getSelectedItem() =="Select Reason For Return " ) {

                    ids = (String) spreason.getSelectedItem();

                    Toast.makeText( mActivity, "Select Reason For Return ", Toast.LENGTH_SHORT ).show();

                }

                else

                { if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
                    hit_product_CanceleProduct();
                } else {
                    AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
                }


                }
                break;
        }

    }

    private void hit_product_CanceleProduct() {
        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );
        String url = AppUrls.ReturnOrder;
        Log.v( "CancelOrder", url );
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put( AppConstants.projectName, jsonObject );
            jsonObject.put( "orderId", AppSettings.getString( AppSettings.order_id ) );
            jsonObject.put( "productId",AppSettings.getString( AppSettings.productId ) );
            jsonObject.put( "reasonId" ,ids);
            jsonObject.put( "remark", edt_remark.getText().toString());
            jsonObject.put( "size", AppSettings.getString(AppSettings.size));
            Log.v( "orderCancele", String.valueOf( json ) );

        }  catch (JSONException e) {
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
                        Log.v( "OrderCancele", String.valueOf( response ) );

                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );
                            String Msg = jsonObject1.getString( "resMsg" );
                            String resCode = jsonObject1.getString( "resCode" );

                            AppSettings.putString( AppSettings.order_id,jsonObject1.getString( "order_id" ) );

                            if(resCode.equals( "1" ))
                            {
                                Toast.makeText( mActivity,  "Return Request has been Successfully", Toast.LENGTH_SHORT ).show();

                                Intent intent = new Intent( mActivity, OrderDetailActivity.class );

                                intent.putExtra( "order_id",AppSettings.putString( AppSettings.order_id,jsonObject1.getString( "order_id" ) ) );


                                startActivity( intent );
                            }
                            else
                                {

                                Toast.makeText( mActivity, Msg , Toast.LENGTH_SHORT ).show();

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

    public void Hit_GetAllReason() {
        String url = AppUrls.GetAllReason;
        Log.v("#####", url);


        AndroidNetworking.get(url).setPriority( Priority.HIGH).build().getAsJSONObject( new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonn(response);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(ANError error) {

                // handle error
                if (error.getErrorCode() != 0) {
                    AppUtils.hideDialog();

                    Log.v("tyu", error.getMessage());
                    Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                    Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                    Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                } else {
                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                    Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                }
            }
        });
    }


    private void parseJsonn(JSONObject response) {

        Log.v("responseGetCat", response.toString());


        try {

            if (response.has("zozima")) {
                JSONObject phone = response.getJSONObject("zozima");
                String res_code = phone.getString("resCode");
                String res_msg = phone.getString("resMsg");


                arrResonList.clear();
                ResonList.clear();
                ResonListId.clear();
                ResonListId.add( "Select Reason For Return" );
                ResonList.add( "Select Reason For Return" );


                if (res_code.equals("1")) {
                    JSONArray data_array = phone.getJSONArray("reasonList");

                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject block_object = data_array.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", block_object.get("id").toString());

                        map.put("reason", block_object.get("reason").toString());
                        arrResonList.add(map);

                        ResonList.add( arrResonList.get(i).get("reason"));
                        ResonListId.add( arrResonList.get(i).get("id"));
                        Log.d("CategorylistID11", arrResonList.get(i).get("id"));


                    }




                    spinner = new SpinnerAdapter(getApplicationContext(), R.layout.selectreason, (ArrayList<String>) ResonList );
                    spreason.setAdapter(spinner);


                } else {


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }

    public static class SpinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super( context, textViewResourceId, arraySpinner_time );

            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
            View row = inflater.inflate( R.layout.selectreason, parent, false );
            TextView label = (TextView) row.findViewById( R.id.tvName );
            label.setText( data.get( position ) );
            return row;
        }

    }
}
