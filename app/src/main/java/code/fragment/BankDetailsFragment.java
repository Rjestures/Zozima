package code.fragment;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zozima.android.R;

import code.database.AppSettings;
import code.view.BaseFragment;

public class BankDetailsFragment extends BaseFragment {

    //ImageView
    ImageView imageView;

    //EditText
    EditText edt_accountnumber, edt_confirmAccount, edt_ifsc, edt_holdername;

    //TextInputLayout
    TextInputLayout[] til = new TextInputLayout[4];

    //int  value
    int[] ids_til = {R.id.acount_layout, R.id.c_acount_layout, R.id.holdername_layout, R.id.ifsc_layout};
    int i = 0;

    //EditText
    EditText[] eds = new EditText[4];

    //int value
    int[] ids_eds = {R.id.edt_accountnumber, R.id.edt_confirmAccount, R.id.edt_holdername, R.id.edt_ifsc};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_bank_details, container, false );

        //Imageview
        imageView = view.findViewById( R.id.back );


        //EditText
        edt_accountnumber = view.findViewById( R.id.edt_accountnumber );
        edt_confirmAccount = view.findViewById( R.id.edt_confirmAccount );
        edt_ifsc = view.findViewById( R.id.edt_ifsc );
        edt_holdername = view.findViewById( R.id.edt_holdername );

        for (i = 0; i < til.length; i++) {

            til[i] = view.findViewById( ids_til[i] );
            eds[i] = view.findViewById( ids_eds[i] );
        }


        edt_holdername.setText( AppSettings.getString( AppSettings.accountHolderName ) );
        edt_accountnumber.setText( AppSettings.getString( AppSettings.accountNumber ) );
        edt_confirmAccount.setText( AppSettings.getString( AppSettings.confirmAccountNumber ) );
        edt_ifsc.setText( AppSettings.getString( AppSettings.ifscCode ) );


        edt_accountnumber.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString( AppSettings.accountNumber, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        edt_confirmAccount.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString( AppSettings.confirmAccountNumber, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        edt_holdername.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString( AppSettings.accountHolderName, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        edt_ifsc.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString( AppSettings.ifscCode, String.valueOf( s ) );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        return view;
    }


}
