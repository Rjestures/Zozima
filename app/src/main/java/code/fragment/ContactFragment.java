package code.fragment;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseFragment;

public class ContactFragment extends BaseFragment {

    //TextInputEditText
    TextInputEditText edt_gmail, edt_fullname, edt_mobilenumber, edt_business, edt_pincode, edt_address1, edt_address2, edt_city;

    Spinner sp_state, sp_city;

    //ArrayList
    ArrayList<String> AddaddressList;
    ArrayList<String> addresslistId;
    ArrayList<HashMap<String, String>> addresslist;


    ArrayList<String> city_list;
    ArrayList<String> city_listId;
    ArrayList<HashMap<String, String>> cityList;

    String state;
    String city;
    String ids = "";
    String address;
    String cityidd = "";



    CitySpinner spinnerr;
    spinnerAdapter spinner;


    public ContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_contact, container, false );
        findViewById( view );

        setListeners();
        hitGetSatate();

        edt_mobilenumber.setText( AppSettings.getString( AppSettings.mobile ) );

        if (!AppSettings.getString( AppSettings.profilename ).equals( "" ))
            edt_fullname.setText( AppSettings.getString( AppSettings.profilename ) );

        if (!AppSettings.getString( AppSettings.profilename ).equals( "" ))
            edt_gmail.setText( AppSettings.getString( AppSettings.profileemail ) );

        if (!AppSettings.getString( AppSettings.profileemail ).equals( "" ))
            edt_mobilenumber.setText( AppSettings.getString( AppSettings.profilenumber ) );

        if (!AppSettings.getString( AppSettings.profilebusness ).equals( "" ))
            edt_business.setText( AppSettings.getString( AppSettings.profilebusness ) );

        if (!AppSettings.getString( AppSettings.profilepicode ).equals( "" ))
            edt_pincode.setText( AppSettings.getString( AppSettings.profilepicode ) );

        if (!AppSettings.getString( AppSettings.profileaddress1 ).equals( "" ))
            edt_address1.setText( AppSettings.getString( AppSettings.profileaddress1 ) );

        if (!AppSettings.getString( AppSettings.profileaddres2 ).equals( "" ))
            edt_address2.setText( AppSettings.getString( AppSettings.profileaddres2 ) );
        if(!AppSettings.getString( AppSettings.profilecity ).equals( "" ))

        city=AppSettings.getString( AppSettings.cityid );
        state=AppSettings.getString( AppSettings.profilestate );
       /* Log.v( "idddd",city );
        Log.v( "idddd",state );*/



        edt_fullname.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                AppSettings.putString( AppSettings.profilename, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        } );
        edt_gmail.addTextChangedListener( new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString( AppSettings.profileemail, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );


        edt_mobilenumber.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AppSettings.putString( AppSettings.profilenumber, String.valueOf( s ) );

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        edt_business.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AppSettings.putString( AppSettings.profilebusness, String.valueOf( s ) );

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        edt_pincode.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppSettings.putString( AppSettings.profilepicode, edt_pincode.getText().toString() );

            }
        } );
        edt_address1.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AppSettings.putString( AppSettings.profileaddress1, String.valueOf( s ) );

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

          edt_address2.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AppSettings.putString( AppSettings.profileaddres2, String.valueOf( s ) );

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        } );

        sp_state.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition( position ).toString() == "select state") {

                } else
                {
                    ids = addresslistId.get( sp_state.getSelectedItemPosition() );
                    /*address = AddaddressList.get( sp_state.getSelectedItemPosition() );*/
                    Log.v( "add",ids );
                    AppSettings.putString( AppSettings.profilestate, ids);

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

                    cityidd = city_listId.get( sp_city.getSelectedItemPosition() );
                    AppSettings.putString( AppSettings.cityid, cityidd);

                    Log.v( "easd", cityidd );


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        return view;
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

                    spinner = new spinnerAdapter( getActivity(), R.layout.spinner, (ArrayList<String>) AddaddressList );
                    sp_state.setAdapter( spinner );
                    Log.v( "statefoundAt", state );

                    for (int i=0;i<addresslist.size();i++)
                    {
                        if(addresslist.get( i ).get( "id" ).equalsIgnoreCase( state )) {

                            Log.v( "statefound", addresslist.get( i ).toString() );
                            sp_state.setSelection( i );

                        }

                    }


                } else {


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();

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

                    spinnerr = new CitySpinner( getActivity(), R.layout.spinner, (ArrayList<String>) city_list );

                    sp_city.setAdapter( spinnerr );

                    Log.v( "cityfound", city );

                    for (int i=0;i<cityList.size();i++)
                    {
                        if( cityList.get( i ).get( "id" ).equalsIgnoreCase( city )) {

                            Log.v( "cityfound", cityList.get( i ).toString() );
                            sp_city.setSelection( i );

                        }

                    }



                } else {


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
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

    private void setListeners() {

    }

    private void findViewById(View view) {
        edt_fullname = view.findViewById( R.id.edt_fullname );
        edt_mobilenumber = view.findViewById( R.id.edt_mobilenumber );
        edt_business = view.findViewById( R.id.edt_busniess );
        edt_pincode = view.findViewById( R.id.edt_pincode );
        edt_address1 = view.findViewById( R.id.edt_address );
        edt_address2 = view.findViewById( R.id.edt_address2 );
        edt_gmail = view.findViewById( R.id.edt_gmail );

        sp_state = view.findViewById( R.id.sp_state );
        sp_city = view.findViewById( R.id.sp_city );

        AddaddressList = new ArrayList<>();
        addresslistId = new ArrayList<>();
        addresslist = new ArrayList<>();
        city_list = new ArrayList<>();
        city_listId = new ArrayList<>();
        cityList = new ArrayList<>();




    }


}
