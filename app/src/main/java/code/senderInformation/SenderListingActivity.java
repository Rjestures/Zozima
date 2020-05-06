package code.senderInformation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.order.OderSummary;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

import static code.utils.AppConstants.senderInforMationList;

public class SenderListingActivity extends BaseActivity {


    //adapter
    SenderInforMationAdapter senderInforMationAdapter;

    //RecyclerView
    RecyclerView rlvSenderInforMation;

    //EditText
    EditText edtNumber, edtName;
    EditText edtnumber, edtname;

    //Dailog
    Dialog dialog;

    //STRing value
    String namee, mobile_number;

    //LinearLayout
    LinearLayout llAddnewSender, llproces;

    //ImageView
    ImageView ivback,ivloader;


    String nameee, mobilee;

    //ArrayList
    private ArrayList<HashMap<String, String>> arrSenderInformation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_listing);

        findViewById();

        //hitApi
        GetSenderInformationApi();
    }


    private void findViewById() {

        //RecyclerView
        rlvSenderInforMation = findViewById(R.id.rlvSenderInforMation);
        llAddnewSender = findViewById(R.id.llAddnewSender);
        llproces = findViewById(R.id.llproces);
        ivback = findViewById(R.id.ivback);
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);



        llAddnewSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSender();
            }
        });


        llproces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OderSummary.class);

                intent.putExtra("name", namee);
                intent.putExtra("mobile", mobile_number);
                startActivity(intent);

            }
        });

        Intent intent = getIntent();
        nameee = intent.getStringExtra("name");
        mobilee = intent.getStringExtra("mobile");
        Log.v("nammeee", nameee);
        Log.v("nammeee", mobilee);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    //GetSenderInformationApi
    public void GetSenderInformationApi() {

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.GetSenderInformation;

        Log.v("GetSenderInformation", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            Log.v("findObject", String.valueOf(json));
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
                        ivloader.setVisibility(View.GONE);
                        Log.v("getSenderListing", String.valueOf(response));


                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String Msg = jsonObject1.getString("resMsg");

                            if (jsonObject1.getString("resCode").equals("1")) {

                                JSONArray jsonArray = jsonObject1.getJSONArray("SenderList");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("sender_id", arrayJSONObject.getString("sender_id"));
                                    hashlist.put("user_id", arrayJSONObject.getString("user_id"));
                                    hashlist.put("name", arrayJSONObject.getString("name"));
                                    AppSettings.putString(AppSettings.profilename, arrayJSONObject.getString("name"));
                                    hashlist.put("mobile", arrayJSONObject.getString("mobile"));
                                    AppSettings.putString(AppSettings.mobilee, arrayJSONObject.getString("mobile"));
                                    AppSettings.putString(AppSettings.sender_id, arrayJSONObject.getString("sender_id"));

                                    if (i == 0) {

                                        hashlist.put("primary", "1");
                                        senderInforMationList.add(hashlist);
                                    } else {
                                        hashlist.put("primary", "0");
                                    }

                                    arrSenderInformation.add(hashlist);


                                }
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                                senderInforMationAdapter = new SenderInforMationAdapter(mActivity, arrSenderInformation);
                                rlvSenderInforMation.setLayoutManager(layoutManager);
                                rlvSenderInforMation.setAdapter(senderInforMationAdapter);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ivloader.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }

    public void updatepopup(String name, final String mobile, final String sender_id) {

        dialog = new Dialog(mActivity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.inflate_edit_sender);

        dialog.show();

        edtNumber = (EditText) dialog.findViewById(R.id.edtNumber);
        edtName = (EditText) dialog.findViewById(R.id.edtName);
        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvsubmit);
        final TextView tvnumber = (TextView) dialog.findViewById(R.id.tvnumber);
        final TextView tvnamee = (TextView) dialog.findViewById(R.id.tvnamee);
        final TextView tvrequired = (TextView) dialog.findViewById(R.id.tvrequired);
        final TextView tvrequiredd = (TextView) dialog.findViewById(R.id.tvrequiredd);

        edtName.setText(name);
        edtNumber.setText(mobile);


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtNumber.getText().toString().length() != 10) {
                    tvrequired.setVisibility(View.VISIBLE);
                    Toast.makeText(mActivity, getString(R.string.pleaseaddentervalidphonenumber), Toast.LENGTH_SHORT).show();
                    tvnumber.requestFocus();


                } else if (edtName.getText().toString().isEmpty()) {
                    tvrequiredd.setVisibility(View.VISIBLE);
                    tvnamee.requestFocus();
                } else {
                    if (SimpleHTTPConnection.isNetworkAvailable()) {
                        UpdateSenderInformation(sender_id);

                    } else {
                        Toast.makeText(mActivity, getString(R.string.errorInternet), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        edtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvnumber.setTextColor(getResources().getColor(R.color.darkGrey));
                tvnamee.setTextColor(getResources().getColor(R.color.colorAccent));
                tvrequired.setVisibility(View.GONE);
            }
        });

        edtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvnumber.setTextColor(getResources().getColor(R.color.colorAccent));
                tvnamee.setTextColor(getResources().getColor(R.color.darkGrey));
            }
        });


    }

    public void newSender() {

        dialog = new Dialog(mActivity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.inflate_new_sender);

        dialog.show();

        edtnumber = (EditText) dialog.findViewById(R.id.edtNumber);
        edtname = (EditText) dialog.findViewById(R.id.edtName);
        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvsubmit);
        final TextView tvnumber = (TextView) dialog.findViewById(R.id.tvnumber);
        final TextView tvnamee = (TextView) dialog.findViewById(R.id.tvnamee);
        final TextView tvrequired = (TextView) dialog.findViewById(R.id.tvrequired);
        final TextView tvrequiredd = (TextView) dialog.findViewById(R.id.tvrequiredd);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtnumber.getText().toString().length() != 10) {
                    tvrequired.setVisibility(View.VISIBLE);
                    Toast.makeText(mActivity, getString(R.string.pleaseaddentervalidphonenumber), Toast.LENGTH_SHORT).show();

                    edtnumber.requestFocus();


                } else if (edtname.getText().toString().isEmpty()) {
                    tvrequiredd.setVisibility(View.VISIBLE);
                    edtnumber.requestFocus();
                }
                else
                    {
                    if (SimpleHTTPConnection.isNetworkAvailable()) {
                        AddSenderInformation();

                    } else {
                        Toast.makeText(mActivity, getString(R.string.errorInternet), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        edtname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvnumber.setTextColor(getResources().getColor(R.color.darkGrey));
                tvnamee.setTextColor(getResources().getColor(R.color.colorAccent));

            }
        });

        edtnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvnumber.setTextColor(getResources().getColor(R.color.colorAccent));
                tvnamee.setTextColor(getResources().getColor(R.color.darkGrey));
            }
        });


    }

    //UpdateSenderInformation
    private void UpdateSenderInformation(String sender_id) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.UpdateSenderInformation;
        Log.v("UpdateSenderInformation", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            jsonObject.put("sender_id", sender_id);
            jsonObject.put("name", edtName.getText().toString());
            jsonObject.put("mobile", edtNumber.getText().toString());
            json.put(AppConstants.projectName, jsonObject);

            Log.v("findobjectUpdate", String.valueOf(json));

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
                        Log.v("UpdateSenderInformation", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String Msg = jsonObject1.getString("resMsg");
                            if (jsonObject1.getString("resCode").equalsIgnoreCase("1")) {

                                String name = jsonObject1.getString("name");
                                String mobile_number = jsonObject1.getString("mobile_number");
                                AppSettings.putString(AppSettings.namee, name);
                                AppSettings.putString(AppSettings.mobilee, mobile_number);

                                arrSenderInformation.clear();
                                GetSenderInformationApi();

                                dialog.dismiss();

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

    private void AddSenderInformation() {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.AddSenderInformation;
        Log.v("AddSenderInformation", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("name", edtname.getText().toString());
            jsonObject.put("mobile", edtnumber.getText().toString());
            json.put(AppConstants.projectName, jsonObject);

            Log.v("findobjectUpdate", String.valueOf(json));

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
                        Log.v("UpdateSenderInformation", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String Msg = jsonObject1.getString("resMsg");
                            if (jsonObject1.getString("resCode").equalsIgnoreCase("1")) {


                                arrSenderInformation.clear();
                                GetSenderInformationApi();

                                dialog.dismiss();


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

    //SenderInforMationAdapter
    private class SenderInforMationAdapter extends RecyclerView.Adapter<SenderListingActivity.Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public SenderInforMationAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrSenderInformation) {

            data = arrSenderInformation;

        }

        public SenderListingActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new SenderListingActivity.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_sender, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final SenderListingActivity.Holder holder, final int position) {

            holder.tvname.setText(data.get(position).get("name"));

            holder.tvNumber.setText("+91" + data.get(position).get("mobile"));

            if (data.get(position).get("primary").equals("1")) {

                holder.ivCheck.setImageResource(R.drawable.redioselect);
                mobile_number = data.get(position).get("mobile");
                namee = data.get(position).get("name");
                AppSettings.putString(AppSettings.sender_id, data.get(position).get("sender_id"));
                AppSettings.putString(AppSettings.mobilee, mobile_number);
                AppSettings.putString(AppSettings.namee, namee);


            } else {

                holder.ivCheck.setImageResource(R.drawable.radio);
            }

            holder.ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    senderInforMationList.clear();

                    for (int i = 0; i < data.size(); i++) {


                        HashMap<String, String> addList = new HashMap();

                        addList.put("name", data.get(i).get("name"));
                        addList.put("mobile", data.get(i).get("mobile"));
                        addList.put("sender_id", data.get(i).get("sender_id"));
                        Log.v("djkshf", data.get(i).get("sender_id"));
                        Log.v("addVal", data.get(i).get("primary"));


                        if (data.get(position).get("sender_id").equals(data.get(i).get("sender_id"))) {

                            if (data.get(i).get("primary").equals("1")) {

                                addList.put("primary", "0");

                            } else {
                                addList.put("primary", "1");

                                senderInforMationList.add(addList);


                            }

                        } else {

                            addList.put("primary", "0");

                        }

                        arrSenderInformation.set(i, addList);
                    }

                    senderInforMationAdapter.notifyDataSetChanged();

                }

            });

            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatepopup(data.get(position).get("name"), data.get(position).get("mobile"), data.get(position).get("sender_id"));

                }
            });


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        //Textview
        TextView tvname, tvEdit, tvNumber;

        //ImageView
        ImageView ivCheck;


        public Holder(View inflate) {

            super(inflate);

            //TextView
            tvname = inflate.findViewById(R.id.tvname);
            tvEdit = inflate.findViewById(R.id.tvEdit);
            tvNumber = inflate.findViewById(R.id.tvNumber);

            //ImageView
            ivCheck = inflate.findViewById(R.id.ivCheck);


        }
    }


}
