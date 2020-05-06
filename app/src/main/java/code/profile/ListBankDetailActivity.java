package code.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zozima.android.R;
import org.json.JSONException;
import org.json.JSONObject;

import code.basic.BankDetails;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class ListBankDetailActivity extends BaseActivity {

    //TextView
    TextView tv_accountHolderName, tv_accountnumber, tv_ifsc;

    //CustomTextView
    CustomTextView edt_edit;

    //ImageView
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bank_detail);

        //TextView
        tv_accountHolderName = (TextView) findViewById(R.id.tv_accountHolderName);
        tv_accountnumber = (TextView) findViewById(R.id.tv_accountnumber);
        tv_ifsc = (TextView) findViewById(R.id.tv_ifsc);

        //CustomeTextView
        edt_edit = (CustomTextView) findViewById(R.id.edt_edtit);

        //ImageView
        back = (ImageView) findViewById(R.id.back);

        //TextViewAddTextChangedListenere
        tv_accountHolderName.addTextChangedListener(new TextWatcher() {

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        edt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I=new Intent(ListBankDetailActivity.this, BankDetails.class);
                startActivity(I);
            }
        });
        hitEditProfileApi();
        tv_accountHolderName.setText(AppSettings.getString(AppSettings.accountHolderName));
        tv_accountnumber.setText(AppSettings.getString(AppSettings.accountNumber));
        tv_ifsc.setText(AppSettings.getString(AppSettings.ifscCode));



    }

    private void hitEditProfileApi() {

        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.getBankList;
        Log.v("urlApi", url);

        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));



            json.put(AppConstants.projectName,jsonObject);
            Log.v("finalObject", String.valueOf(json));
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
                        Log.v("getBanklist", String.valueOf(response));
                        try {
                            JSONObject jsonObject1=response.getJSONObject(AppConstants.projectName);

                            JSONObject jsonObject2=jsonObject1.getJSONObject("banks");
                            jsonObject2.getString("accountId");
                            jsonObject2.getString("accountHolderName");
                            jsonObject2.getString("accountNumber");
                            jsonObject2.getString("ifscCode");
                            AppSettings.putString(AppSettings.accountHolderName,jsonObject1.getString("accountHolderName"));

                            Toast.makeText(mActivity, jsonObject1.getString("resMsg")+"", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("getBanklist", String.valueOf(anError));




                    }

                });
        {

        }

    }

}