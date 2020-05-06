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
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.entity.ProductData;
import code.product.ProductAddToCardActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class CheckAvailabilityAcivity extends BaseActivity implements View.OnClickListener {

    //ImageView
    ImageView ivback;

    //TextInputEditText
    EditText enterpincode;

    //Button
    Button btncheck;

    //TextView
    TextView tvmsg;

    //ProductData
    ProductData productData;

    String value;
    String Id;
    String addedType, addedById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability_acivity);
        findViewById();

        ivback.setOnClickListener(this);
        enterpincode.setOnClickListener(this);
        btncheck.setOnClickListener(this);

    }


    private void findViewById() {

        //ImageView
        ivback = (ImageView) findViewById(R.id.back);

        //Edittext
        enterpincode = (EditText) findViewById(R.id.enterpincode);

        //Button
        btncheck = (Button) findViewById(R.id.btncheck);

        //TextView
        tvmsg = (TextView) findViewById(R.id.tvmsg);

        //InputEdiText
        /*enterpincode.setText(AppSettings.getString(AppSettings.pincodedelivery));*/
        //Log.v("djadj", AppSettings.getString(AppSettings.pincodedelivery));
        Intent intent = getIntent();
        productData = (ProductData) intent.getSerializableExtra("productlist");
        value = intent.getStringExtra("qty");
        Id = intent.getStringExtra("Id");
        addedById = intent.getStringExtra("addedById");
        addedType = intent.getStringExtra("addedType");


        Log.v("ds", String.valueOf((ProductData) intent.getSerializableExtra("productlist")));
        Log.v("product", value);


        enterpincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (enterpincode.getText().length() > 0)

                {
                    btncheck.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    enterpincode.setCursorVisible(true);

                } else {
                    btncheck.setBackgroundColor(getResources().getColor(R.color.grey));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btncheck:

                if (enterpincode.length() != 6) {
                    tvmsg.setVisibility(View.VISIBLE);
                } else {
                    if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
                        Hit_Check_Availability_picode();
                    } else {
                        AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                    }
                    break;
                }
        }

    }

        public void Hit_Check_Availability_picode() {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.CheckPinAvaility;
        Log.v("getSabCategoryApi-URL", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("pincode", enterpincode.getText().toString().trim());
            Log.v("findObject", String.valueOf(json));
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
                        Log.v("jkhjkdsf", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");
                            String pincode = jsonObject1.getString("pincode");

                            Intent intent = new Intent(mActivity, ProductAddToCardActivity.class);
                            intent.putExtra("productlist", (Serializable) productData);
                            intent.putExtra("resCode", resCode);
                            intent.putExtra("pincode", pincode);
                            intent.putExtra("qty", value);
                            intent.putExtra("Id", Id);
                            intent.putExtra("addedType", addedType);
                            intent.putExtra("addedById", addedById);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });

    }


}
