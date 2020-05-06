package code.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.main.MainActivity;
import code.order.OrderDetailActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomButton;

public class FeedBack extends BaseActivity implements View.OnClickListener {
    ImageView ivback;

    CustomButton btnSubmit;

    EditText etfullname,etemail,etmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        findViewById();
         setListenre();
    }

    private void setListenre() {
      ivback.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void findViewById() {
        ivback=findViewById(R.id.ivback);
        btnSubmit=findViewById(R.id.btnSubmit);
        etfullname=findViewById(R.id.etfullname);
        etemail=findViewById(R.id.etemail);
        etmessage=findViewById(R.id.etmessage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.btnSubmit:

                if(etfullname.getText().toString().isEmpty())
                {
                    Toast.makeText(mActivity, getString(R.string.pleaseenteryournam), Toast.LENGTH_SHORT).show();
                }
                else if(etmessage.getText().toString().isEmpty())
                {
                    Toast.makeText(mActivity, getString(R.string.pleaseEnterYourMasg), Toast.LENGTH_SHORT).show();

                }
                else {

                if(SimpleHTTPConnection.isNetworkAvailable())

                {
                    SendFeedbacks();



                }

                else {
                    Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
                }
                }
                break;

        }

    }




    private void SendFeedbacks() {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.SendFeedbacks;
        Log.v("CancelOrder", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("name", etfullname.getText().toString().trim());
            jsonObject.put("email", etemail.getText().toString().trim());
            jsonObject.put("message", etmessage.getText().toString().trim() );

            Log.v("obectfeedback", String.valueOf(json));

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
                        Log.v("feedbackresponse", String.valueOf(response));

                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String Msg = jsonObject1.getString("resMsg");
                            String resCode = jsonObject1.getString("resCode");


                            if (resCode.equals("1")) {
                                Toast.makeText(mActivity,  getString(R.string.feedbacksuccessfull), Toast.LENGTH_SHORT).show();

                                etfullname.getText().clear(); //or you can use editText.setText("");
                                etmessage.getText().clear(); //or you can use editText.setText("");
                                etemail.getText().clear(); //or you can use editText.setText("");
                                  onBackPressed();



                            } else {

                                Toast.makeText(mActivity, Msg + "", Toast.LENGTH_SHORT).show();

                            }


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
