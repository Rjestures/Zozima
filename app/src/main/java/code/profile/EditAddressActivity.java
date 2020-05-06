package code.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

import code.address.ShippingAddress;
import code.common.SimpleHTTPConnection;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class EditAddressActivity extends BaseActivity  implements View.OnClickListener {
    //EditText
    TextInputEditText edtCutomerName, edt_phonenumber, edt_flat, edt_colony, edtLandmark, edt_pincode,edtStreet;

    //View
    TextView tvName,tvnumber,tvflat,tvstreet,tvlandmark,tvpincode,tvstate,tvcity;

    LinearLayout tvSave;

    ArrayList<String> city_list;
    ArrayList<String> city_listId;
    ArrayList<HashMap<String, String>> cityList;

    //////////////////
    String city;
    String state;

    //ImageView
    ImageView ivback;
    //String
    String ids = "";
    String cityid = "";

    //SpinnerAdapter
    SpinnerAdapter spinner;

    //CitySpinnerAdapter
    CitySpinner spinnerr;

    //ArrayList
    ArrayList<String> AddaddressList;
    ArrayList<String> addresslistId;
    ArrayList<HashMap<String, String>> addresslist;

    //spiner
    Spinner sp_state, sp_city;

    String address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_address );
        findViewById();
        setListerner();
        hitGetSatate();
    }

    private void setListerner() {
        tvSave.setOnClickListener( this );
        ivback.setOnClickListener( this );
    }

    private void findViewById() {

        //EditText
        edtCutomerName = findViewById( R.id.edtCutomerName );
        edt_phonenumber = findViewById( R.id.edtPhoneNymber );
        edt_flat = findViewById( R.id.edt_busniess );
        edtStreet = findViewById( R.id.edtStreet );
        edtLandmark = findViewById( R.id.edtLandmark );
        edt_pincode = findViewById( R.id.edt_pincode );
        //TextView
        tvSave = findViewById( R.id.save );
        //TetxtView
        tvName = findViewById( R.id.tvName );
        tvnumber = findViewById( R.id.tvnumber );
        tvflat = findViewById( R.id.tvflat );
        tvstreet = findViewById( R.id.tvstreet );
        tvcity = findViewById( R.id.tvcity );
        tvstate = findViewById( R.id.tvstate );
        tvlandmark = findViewById( R.id.tvlandmark );
        tvpincode = findViewById( R.id.tvpincode );

        //Spinner
        sp_city = findViewById( R.id.sp_city );
        sp_state = findViewById( R.id.sp_state );

        //ImageView
        ivback = findViewById( R.id.ivback );





        AddaddressList = new ArrayList<>();
        addresslistId = new ArrayList<>();
        addresslist = new ArrayList<>();
        city_list = new ArrayList<>();
        city_listId = new ArrayList<>();
        cityList = new ArrayList<>();

        Intent intent = getIntent();
        String cutomername = intent.getStringExtra( "customerName" );
        edtCutomerName.setText( cutomername );

        String flatHouseBulding = intent.getStringExtra( "flatHouseBulding" );
        edt_flat.setText( flatHouseBulding );

        String streetColony = intent.getStringExtra( "streetColony" );
        edtStreet.setText( streetColony );

        String landmark = intent.getStringExtra( "landmark" );
        edtLandmark.setText( landmark );

        String pincode = intent.getStringExtra( "pincode" );
        edt_pincode.setText( pincode );

        state=intent.getStringExtra( "state" );

        city = intent.getStringExtra( "city" );

        String phoneNumber = intent.getStringExtra( "phoneNumber" );
        edt_phonenumber.setText( phoneNumber );


        sp_state.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition( position ).toString() == "All Packages") {

                } else
                    {
                    ids = addresslistId.get( sp_state.getSelectedItemPosition() );
                    address = AddaddressList.get( sp_state.getSelectedItemPosition() );

                    if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
                        hitCity();
                    } else {
                        AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );


                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        sp_city.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_city.getSelectedItem().toString().trim() == "select city") {
                }
                else {

                    cityid = city_listId.get( sp_city.getSelectedItemPosition() );


                    Log.v( "easd", cityid );


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
                onBackPressed();
                break;

            case R.id.save:

                    if (edtCutomerName.getText().toString().isEmpty()) {
                        tvName.setVisibility( View.VISIBLE );


                    } else if (edt_phonenumber.getText().toString().length() != 10) {

                        Toast.makeText( mActivity, getString(R.string.pleaseaddentervalidphonenumber), Toast.LENGTH_SHORT ).show();
                        tvnumber.setVisibility( View.VISIBLE );
                    } else if (sp_state.getSelectedItem() == "select state") {

                        ids = (String) sp_state.getSelectedItem();
                        tvstate.setVisibility( View.VISIBLE );

                    } else if (sp_city.getSelectedItem() == "select city") {

                        cityid = (String) sp_city.getSelectedItem();
                        tvcity.setVisibility( View.VISIBLE );

                    } else if (edt_pincode.getText().toString().length() != 6) {
                        Toast.makeText( mActivity, getString(R.string.pleaseentervalidpincode), Toast.LENGTH_SHORT ).show();
                        tvpincode.setVisibility( View.VISIBLE );


                    } else {
                        tvName.setVisibility( View.GONE );
                        tvpincode.setVisibility( View.GONE );
                        tvstate.setVisibility( View.GONE );
                        tvcity.setVisibility( View.GONE );
                        tvnumber.setVisibility( View.GONE );


                        UpdateAddress();


                    }


                    break;


                }
        }




    private void UpdateAddress() {
        AppUtils.hideSoftKeyboard( mActivity );
        AppUtils.showRequestDialog( mActivity );
        String url = AppUrls.UpdateAddress;
        Log.v( "UpDateAddress", url );
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            Intent I = getIntent();
            String address = I.getStringExtra( "addressId" );
            jsonObject.put( "addressId", address );
            jsonObject.put( "customerName", edtCutomerName.getText().toString() );
            jsonObject.put( "phoneNumber", edt_phonenumber.getText().toString() );
            jsonObject.put( "flatHouseBulding", edt_flat.getText().toString() );
            jsonObject.put( "streetColony", edtStreet.getText().toString() );
            jsonObject.put( "city", cityid );
            jsonObject.put( "landmark", edtLandmark.getText().toString() );
            jsonObject.put( "state", ids );
            jsonObject.put( "pincode", edt_pincode.getText().toString() );
            json.put( AppConstants.projectName, jsonObject );
            Log.v( "findobjectUpdate", String.valueOf( json ) );

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
                        Log.v( "UpdateAddress", String.valueOf( response ) );
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );
                            String Msg = jsonObject1.getString( "resMsg" );
                            if(jsonObject1.getString( "resCode" ).equals( "1" ))
                            {

                                String city=jsonObject1.getString( "city" );
                                String state=jsonObject1.getString( "state" );


                                Log.v( "selectedcity",addresslistId. get( 5 ));

/*
                                for(int j=0;j<city_listId)
*/
                                Log.v( "selectedcity",state );
                                Toast.makeText( mActivity, Msg + "", Toast.LENGTH_SHORT ).show();
                                startActivity( new Intent( mActivity, ShippingAddress.class ) );
                            }

                            else
                            {

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


    public void hitCity() {
        String url = AppUrls.GetCity;
        Log.v( "#####", url );

        Log.v( "urlApi", url );
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonObject.put( "stateId", ids );
            json.put( AppConstants.projectName, jsonObject );
            Log.v( "finddObject", String.valueOf( json ) );
        } catch (JSONException e) {

            e.printStackTrace();
        }
        AndroidNetworking.post( url ).addJSONObjectBody( json )
                .setPriority( Priority.HIGH ).build().getAsJSONObject( new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonnn( response );
                    Log.d( "ihddss1", ids );
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(ANError error) {

                // handle error
                if (error.getErrorCode() != 0) {
                    AppUtils.hideDialog();

                    Log.v( "tyu", error.getMessage() );
                    Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                    Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                } else {
                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                }
            }
        } );
    }


    private void parseJsonnn(JSONObject response) {

        Log.v( "responseGetCat", response.toString() );


        try {

            if (response.has( "zozima" )) {
                JSONObject phone = response.getJSONObject( "zozima" );
                String res_code = phone.getString( "resCode" );
                String res_msg = phone.getString( "resMsg" );
                cityList.clear();
                city_list.clear();
                city_listId.clear();

              /*  city_listId.add( "select city" );
                city_list.add( "select city" );*/

                if (res_code.equals( "1" )) {
                    JSONArray data_array = phone.getJSONArray( "CityList" );

                    //DatabaseController.removeTable(TableCity.city);

                    for (int i = 0; i < data_array.length(); i++) {


                        JSONObject block_object = data_array.getJSONObject( i );


                        HashMap<String, String> map = new HashMap<String, String>();

                        //ids=block_object.get("id").toString();

                        ;
                        map.put( "id", block_object.get( "id" ).toString() );
                        map.put( "name", block_object.get( "name" ).toString() );
                        cityList.add( map );
                        city_list.add( cityList.get( i ).get( "name" ) );
                        city_listId.add( cityList.get( i ).get( "id" ) );
                        Log.d( "CategorylistID11", cityList.get( i ).get( "id" ) );


                    }

                    spinnerr = new CitySpinner( getApplicationContext(), R.layout.spinner, (ArrayList<String>) city_list );

                    sp_city.setAdapter( spinnerr );

                    Log.v( "cityfound", city );

                    for (int i=0;i<cityList.size();i++)
                    {
                        if(cityList.get( i ).get( "name" ).equalsIgnoreCase( city )) {

                            Log.v( "cityfound", cityList.get( i ).toString() );
                            sp_city.setSelection( i );

                        }

                    }



                } else {


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();
    }


    public static class CitySpinner extends ArrayAdapter<String> {

        ArrayList<String> data;
        ArrayList<String> units;

        public CitySpinner(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

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

            View row = inflater.inflate( R.layout.spinner, parent, false );

            TextView labell = (TextView) row.findViewById( R.id.tvName );

            labell.setText( data.get( position ) );

            return row;
        }

    }


    public static class spinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public spinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

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
            View row = inflater.inflate( R.layout.spinner, parent, false );
            TextView label = (TextView) row.findViewById( R.id.tvName );
            label.setText( data.get( position ) );
            return row;
        }
    }

    public void hitGetSatate() {
        String url = AppUrls.GetState;
        Log.v( "#####", url );


        AndroidNetworking.get( url ).setPriority( Priority.HIGH ).build().getAsJSONObject( new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonn( response );
                    Log.d( "ihddss1", ids );
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(ANError error) {

                // handle error
                if (error.getErrorCode() != 0) {
                    AppUtils.hideDialog();

                    Log.v( "tyu", error.getMessage() );
                    Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                    Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                } else {
                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                }
            }
        } );
    }


    private void parseJsonn(JSONObject response) {

        Log.v( "responseGetCat", response.toString() );


        try {

            if (response.has( "zozima" )) {
                JSONObject phone = response.getJSONObject( "zozima" );
                String res_code = phone.getString( "resCode" );
                String res_msg = phone.getString( "resMsg" );


                addresslist.clear();
                AddaddressList.clear();
                addresslistId.clear();



                if (res_code.equals( "1" )) {
                    JSONArray data_array = phone.getJSONArray( "states" );

                    //DatabaseController.removeTable(TableCity.city);

                    for (int i = 0; i < data_array.length(); i++) {


                        JSONObject block_object = data_array.getJSONObject( i );


                        HashMap<String, String> map = new HashMap<String, String>();

                        //ids=block_object.get("id").toString();

                        ;
                        map.put( "id", block_object.get( "id" ).toString() );
                        map.put( "name", block_object.get( "name" ).toString() );
                        addresslist.add( map );
                        AddaddressList.add( addresslist.get( i ).get( "name" ) );
                        addresslistId.add( addresslist.get( i ).get( "id" ) );
                        Log.d( "CategorylistID11", addresslist.get( i ).get( "id" ) );


                    }



/*
                    Log.v("spinner", String.valueOf(Locationlist));
*/
                    spinner = new spinnerAdapter( getApplicationContext(), R.layout.spinner, (ArrayList<String>) AddaddressList );
                    //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                    sp_state.setAdapter( spinner );
                    Log.v( "statefoundAt", state );

                    for (int i=0;i<addresslist.size();i++)
                    {
                        if(addresslist.get( i ).get( "name" ).equalsIgnoreCase( state )) {

                            Log.v( "statefound", addresslist.get( i ).toString() );
                            sp_state.setSelection( i );

                        }

                                   /* if(addresslistId.get(i ).toString().equalsIgnoreCase( "state" ))
                                    {
                                        Log.v( "statefound",addresslistId.get( i ).toString() );
                                    }
*/
                    }


                } else {


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();

    }
}