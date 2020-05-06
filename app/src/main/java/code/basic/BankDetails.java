package code.basic;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import code.database.AppSettings;
import code.profile.ListBankDetailActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class BankDetails extends BaseActivity {
    //ImageView
    ImageView imageView;

    //EditText
    EditText edt_accountnumber, edt_confirmAccount, edt_ifsc, edt_holdername;

    //Button
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        imageView = (ImageView) findViewById(R.id.back);

        edt_accountnumber = (EditText) findViewById(R.id.edt_accountnumber);

        edt_confirmAccount = (EditText) findViewById(R.id.edt_confirmAccount);

        edt_ifsc = (EditText) findViewById(R.id.edt_ifsc);

        edt_holdername = (EditText) findViewById(R.id.edt_holdername);

        edt_holdername.setText(AppSettings.getString(AppSettings.accountHolderName));

        // submit = (Button) findViewById(R.id.submit);
        edt_accountnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.accountNumber, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_confirmAccount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.confirmAccountNumber, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
          });

         edt_holdername.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppSettings.putString(AppSettings.accountHolderName, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

          });
         edt_ifsc.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                AppSettings.putString(AppSettings.ifscCode, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edt_accountnumber.getText().toString().isEmpty() || edt_accountnumber.getText().toString().length() != 16) {

                    edt_accountnumber.setError("Required!");

                    edt_accountnumber.requestFocus();

                } else {
                    String account = edt_accountnumber.getText().toString().trim();

                    String confirmaccount = edt_confirmAccount.getText().toString().trim();

                    hitEditProfileApi();

                    if (account.equals(confirmaccount)) {

                        edt_confirmAccount.setError("Required!");

                        edt_confirmAccount.requestFocus();

                    } else if (edt_ifsc.getText().toString().isEmpty() || edt_ifsc.getText().toString().length() != 11) {

                        edt_ifsc.setError("Required!");

                        edt_ifsc.requestFocus();

                    }

                }

            }

            private void hitEditProfileApi() {

                AppUtils.showRequestDialog(mActivity);

                AppUtils.hideSoftKeyboard(mActivity);

                String url = AppUrls.addAccountNumber;

                Log.v("urlApi", url);

                JSONObject jsonObject = new JSONObject();

                JSONObject json = new JSONObject();

                Log.v("finalObject", String.valueOf(json));

                try {
                    jsonObject.put("userID", AppSettings.getString(AppSettings.userId));

                    jsonObject.put("accountNumber", AppSettings.getString(AppSettings.accountNumber));

                    jsonObject.put("confirmAccountNumber", AppSettings.getString(AppSettings.confirmAccountNumber));

                    jsonObject.put("accountHolderName", AppSettings.getString(AppSettings.accountHolderName));

                    jsonObject.put("ifscCode", AppSettings.getString(AppSettings.ifscCode));

                    json.put(AppConstants.projectName, jsonObject);

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

                                Log.v("bank", String.valueOf(response));

                                try {
                                    JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                                    jsonObject1.getString("resMsg");

                                    String msg = jsonObject1.getString("resMsg");

                                    AppSettings.putString(AppSettings.accountNumber, jsonObject1.getString("accountNumber"));

                                    AppSettings.putString(AppSettings.accountHolderName, jsonObject1.getString("accountHolderName"));

                                    AppSettings.putString(AppSettings.ifscCode, jsonObject1.getString("ifscCode"));

                                    Toast.makeText(mActivity, msg + "", Toast.LENGTH_SHORT).show();

                                    Intent I = new Intent(BankDetails.this, ListBankDetailActivity.class);

                                    startActivity(I);

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onError(ANError anError) {

                                Log.v("bank", String.valueOf(anError));

                            }

                        });

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}

