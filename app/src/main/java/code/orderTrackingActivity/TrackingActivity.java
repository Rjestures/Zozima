package code.orderTrackingActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TrackingActivity extends BaseActivity {
    //Textview
    TextView tvproductname, tvSize, tvQuantity, tvDatee;

    //imageview
    ImageView ivproductImage, ivback;
    //RecyclerView
    RecyclerView recyclerViewTracking;
    String date;

    RelativeLayout rl_ordered, rl_shipped, rl_deliver, rl_cancel, rl_retReq, rl_retAccept, rl_pickUp, rl_return;
    ImageView iv_oderCircle, iv_oderLine,
            iv_shippedCircle, iv_shippedLine,
            iv_deliverCircle, iv_deliverLine,
            iv_cancelCircle, iv_cancelLine,
            iv_retReqCircle, iv_retReqLine,
            iv_retAcceptCircle, iv_retAcceptLine,
            iv_pickUpCircle, iv_pickUpLine,
            iv_returnCircle, iv_returnLine;
    TextView tv_oderTitle, tv_oderDate, tv_oderReason,
            tv_deliverTitle, tv_deliverDate, tv_deliverReason,
            tv_shippedReason, tv_shippedTitle, tv_shippedDate,
            tv_cancelTitle, tv_cancelDate, tv_cancelReason,
            tv_retReqTitle, tv_retReqDate, tv_retReqReason,
            tv_retAcceptTitle, tv_retAcceptDate, tv_retAcceptReason,
            tv_pickUpTitle, tv_pickUpDate, tv_pickUpReason,
            tv_returnTitle, tv_returnDate, tv_returnReason;


    //ArrayList
    private ArrayList<HashMap<String, String>> TrackingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        findViewById();
        hitTrackApi();

    }

    private void findViewById() {

        //ImageView
        ivproductImage = findViewById(R.id.ivproductImage);
        ivback = findViewById(R.id.ivback);

        //TextView
        tvSize = findViewById(R.id.tvSize);
        tvQuantity = findViewById(R.id.tvqtyy);
        tvproductname = findViewById(R.id.tvproductname);
        rl_ordered = findViewById(R.id.rl_ordered);
        rl_shipped = findViewById(R.id.rl_shipped);
        rl_deliver = findViewById(R.id.rl_deliver);
        iv_oderCircle = findViewById(R.id.iv_oderCircle);
        iv_shippedCircle = findViewById(R.id.iv_shippedCircle);
        iv_oderLine = findViewById(R.id.iv_oderLine);
        tv_oderTitle = findViewById(R.id.tv_oderTitle);
        tv_oderDate = findViewById(R.id.tv_oderDate);
        tv_oderReason = findViewById(R.id.tv_oderReason);
        iv_deliverLine = findViewById(R.id.iv_deliverLine);
        tv_deliverTitle = findViewById(R.id.tv_deliverTitle);
        tv_deliverDate = findViewById(R.id.tv_deliverDate);
        tv_deliverReason = findViewById(R.id.tv_deliverReason);
        iv_deliverCircle = findViewById(R.id.iv_deliverCircle);
        iv_shippedLine = findViewById(R.id.iv_shippedLine);
        tv_shippedTitle = findViewById(R.id.tv_shippedTitle);
        tv_shippedDate = findViewById(R.id.tv_shippedDate);
        iv_cancelCircle = findViewById(R.id.iv_cancelCircle);
        iv_cancelLine = findViewById(R.id.iv_cancelLine);
        tv_shippedReason = findViewById(R.id.tv_shippedReason);
        tv_cancelTitle = findViewById(R.id.tv_cancelTitle);
        tv_cancelDate = findViewById(R.id.tv_cancelDate);
        tv_cancelReason = findViewById(R.id.tv_cancelReason);
        rl_cancel = findViewById(R.id.rl_cancel);
        rl_retReq = findViewById(R.id.rl_retReq);
        rl_retAccept = findViewById(R.id.rl_retAccept);
        rl_pickUp = findViewById(R.id.rl_pickUp);
        rl_return = findViewById(R.id.rl_return);
        iv_retReqCircle = findViewById(R.id.iv_retReqCircle);
        iv_retAcceptCircle = findViewById(R.id.iv_retAcceptCircle);
        iv_retReqLine = findViewById(R.id.iv_retReqLine);
        iv_retAcceptLine = findViewById(R.id.iv_retAcceptLine);
        iv_pickUpCircle = findViewById(R.id.iv_pickUpCircle);
        iv_pickUpLine = findViewById(R.id.iv_pickUpLine);
        iv_returnCircle = findViewById(R.id.iv_returnCircle);
        iv_returnLine = findViewById(R.id.iv_returnLine);
        tv_retReqTitle = findViewById(R.id.tv_retReqTitle);
        tv_retReqDate = findViewById(R.id.tv_retReqDate);
        tv_retReqReason = findViewById(R.id.tv_retReqReason);
        tv_retAcceptDate = findViewById(R.id.tv_retAcceptDate);
        tv_retAcceptTitle = findViewById(R.id.tv_retAcceptTitle);
        tv_retAcceptReason = findViewById(R.id.tv_retAcceptReason);
        tv_pickUpTitle = findViewById(R.id.tv_pickUpTitle);
        tv_pickUpDate = findViewById(R.id.tv_pickUpDate);
        tv_pickUpReason = findViewById(R.id.tv_pickUpReason);
        tv_returnTitle = findViewById(R.id.tv_returnTitle);
        tv_returnDate = findViewById(R.id.tv_returnDate);
        tv_returnReason = findViewById(R.id.tv_returnReason);


        //RecyclerView
        recyclerViewTracking = findViewById(R.id.recyclerViewTracking);

        //setData
        tvproductname.setText(AppSettings.getString(AppSettings.productName));
        tvSize.setText(AppSettings.getString(AppSettings.size));
        tvQuantity.setText(AppSettings.getString(AppSettings.quantity));
        Picasso.get().load(AppSettings.getString(AppSettings.productImage)).placeholder(R.mipmap.logo_grey).resize(400, 600).into(ivproductImage);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void hitTrackApi() {

        AppUtils.showRequestDialog(mActivity);

        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.TrackOrder;
        Log.v("TrackOrder_url", url);

        final JSONObject jsonObject = new JSONObject();

        final JSONObject json = new JSONObject();

        try {

            jsonObject.put("orderId", AppSettings.getString(AppSettings.order_id));
            jsonObject.put("productId", AppSettings.getString(AppSettings.productId));
            jsonObject.put("size", AppSettings.getString(AppSettings.size));
            json.put(AppConstants.projectName, jsonObject);
            Log.v("TrackOrder_json", json.toString());

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
                        Log.v("Tracking", String.valueOf(response));
                        Log.v("TrackOrder_resp", response.toString());

                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resCode = jsonObject1.getString("resCode");
                            String finStats = jsonObject1.getString("status");
                            int finalStats = Integer.parseInt(finStats);

                            if (resCode.equals("1")) {

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("OrderTrack");
                                for (int i = 0; i < jsonArray1.length(); i++) {

                                    HashMap<String, String> hashlist = new HashMap();
                                    JSONObject arrayJSONObject1 = jsonArray1.getJSONObject(i);
                                    hashlist.put("finalstatus", jsonObject1.getString("status"));
                                    hashlist.put("remark", arrayJSONObject1.getString("remark"));
                                    hashlist.put("date", arrayJSONObject1.getString("date"));
                                    hashlist.put("msg", arrayJSONObject1.getString("msg"));
                                    hashlist.put("status", arrayJSONObject1.getString("status"));
                                    TrackingList.add(hashlist);
                                    switch (arrayJSONObject1.getString("status")) {
                                        case "1":
                                            rl_ordered.setVisibility(View.VISIBLE);
                                            if(finalStats==4) {
                                                rl_shipped.setVisibility(View.GONE);
                                                rl_deliver.setVisibility(View.GONE);
                                            }else {
                                                rl_shipped.setVisibility(View.VISIBLE);
                                                rl_deliver.setVisibility(View.VISIBLE);
                                            }if (finalStats >= 1) {
                                                applycolor(iv_oderCircle, iv_oderLine, tv_oderTitle, tv_oderDate, tv_oderReason, "2");
                                                tv_oderTitle.setText("Odered");
                                                tv_oderDate.setText(arrayJSONObject1.getString("date"));
                                                tv_oderReason.setText("");
                                                tv_shippedTitle.setText("Shipping Expected");
                                                tv_shippedDate.setText(AppUtils.addDates(2,arrayJSONObject1.getString("date")));
                                                tv_shippedReason.setText("");
                                                tv_deliverTitle.setText("Delivery Expected");
                                                tv_deliverDate.setText(AppUtils.addDates(4,arrayJSONObject1.getString("date")));
                                                tv_deliverReason.setText("");
                                            } else {
                                                applycolor(iv_oderCircle, iv_oderLine, tv_oderTitle, tv_oderDate, tv_oderReason, "1");
                                                tv_oderTitle.setText("");
                                                tv_oderDate.setText("");
                                                tv_oderReason.setText("");
                                                tv_shippedTitle.setText("Shipping Expected");
                                                tv_shippedDate.setText(AppUtils.addDates(2,arrayJSONObject1.getString("date")));
                                                tv_shippedReason.setText("");
                                                tv_deliverTitle.setText("Delivery Expected");
                                                tv_deliverDate.setText(AppUtils.addDates(4,arrayJSONObject1.getString("date")));
                                                tv_deliverReason.setText("");

                                            }
                                            break;
                                        case "2":
                                            rl_shipped.setVisibility(View.VISIBLE);
                                            if (finalStats >= 2) {
                                                applycolor(iv_shippedCircle, iv_shippedLine, tv_shippedTitle, tv_shippedDate, tv_shippedReason, "2");
                                                tv_shippedTitle.setText("Shipped");
                                                tv_shippedDate.setText(arrayJSONObject1.getString("date"));
                                                tv_shippedReason.setText("");
                                                tv_deliverTitle.setText("Delivery Expected");
                                                tv_deliverDate.setText(AppUtils.addDates(2,arrayJSONObject1.getString("date")));
                                                tv_deliverReason.setText("");

                                            } else {
                                                applycolor(iv_shippedCircle, iv_shippedLine, tv_shippedTitle, tv_shippedDate, tv_shippedReason, "1");
                                            }
                                            break;

                                        case "3":
                                            rl_deliver.setVisibility(View.VISIBLE);

                                            if (finalStats >= 3) {
                                                if(finalStats>3){
                                                    iv_deliverLine.setVisibility(View.VISIBLE);
                                                }
                                                applycolor(iv_deliverCircle, iv_deliverLine, tv_deliverTitle, tv_deliverDate, tv_deliverReason, "2");
                                                tv_deliverTitle.setText("Delivered");
                                                tv_deliverDate.setText(arrayJSONObject1.getString("date"));
                                                tv_deliverReason.setText(arrayJSONObject1.getString("msg"));
                                            } else {
                                                applycolor(iv_deliverCircle, iv_deliverLine, tv_deliverTitle, tv_deliverDate, tv_deliverReason, "1");
                                            }
                                            break;

                                        case "4":
                                            rl_cancel.setVisibility(View.VISIBLE);
                                            rl_deliver.setVisibility(View.GONE);
                                            applycolor(iv_cancelCircle, iv_cancelLine, tv_cancelTitle, tv_cancelDate, tv_cancelReason, "3");
                                            tv_cancelTitle.setText("Cancelled");
                                            tv_cancelDate.setText(arrayJSONObject1.getString("date"));
                                            tv_cancelReason.setText(arrayJSONObject1.getString("msg"));
                                            break;
                                        case "5":
                                            rl_retReq.setVisibility(View.VISIBLE);
                                            rl_retAccept.setVisibility(View.VISIBLE);
                                            rl_pickUp.setVisibility(View.VISIBLE);
                                            rl_return.setVisibility(View.VISIBLE);
                                            if (finalStats >= 5) {
                                                applycolor(iv_retReqCircle, iv_retReqLine, tv_retReqTitle, tv_retReqDate, tv_retReqReason, "2");
                                                tv_retReqTitle.setText("Return Requested");
                                                tv_retReqDate.setText(arrayJSONObject1.getString("date"));
                                                tv_retReqReason.setText(arrayJSONObject1.getString("msg"));
                                                tv_retAcceptTitle.setText("Expected Approval");
                                                tv_retAcceptDate.setText(AppUtils.addDates(1,arrayJSONObject1.getString("date")));
                                                tv_retAcceptReason.setText("");
                                                tv_pickUpTitle.setText("PickUp Expected");
                                                tv_pickUpDate.setText(AppUtils.addDates(3,arrayJSONObject1.getString("date")));
                                                tv_pickUpReason.setText("");
                                                tv_returnTitle.setText("Return Expected");
                                                tv_returnDate.setText(AppUtils.addDates(5,arrayJSONObject1.getString("date")));
                                                tv_returnReason.setText("");

                                            } else {
                                                applycolor(iv_retReqCircle, iv_retReqLine, tv_retReqTitle, tv_retReqDate, tv_returnReason, "1");
                                            }break;
                                        case "6":
                                            rl_retAccept.setVisibility(View.VISIBLE);
                                            if (finalStats >= 6) {
                                                applycolor(iv_retAcceptCircle, iv_retAcceptLine, tv_retAcceptTitle, tv_retAcceptDate, tv_retAcceptReason, "2");
                                                tv_retAcceptTitle.setText("Request Approved");
                                                tv_retAcceptDate.setText(arrayJSONObject1.getString("date"));
                                                tv_retAcceptReason.setText(arrayJSONObject1.getString("msg"));
                                                tv_pickUpTitle.setText("PickUp Expected");
                                                tv_pickUpDate.setText(AppUtils.addDates(2,arrayJSONObject1.getString("date")));
                                                tv_pickUpReason.setText("");
                                                tv_returnTitle.setText("Return Expected");
                                                tv_returnDate.setText(AppUtils.addDates(4,arrayJSONObject1.getString("date")));
                                                tv_returnReason.setText("");

                                            } else {
                                                applycolor(iv_retAcceptCircle, iv_retAcceptLine, tv_retAcceptTitle, tv_retAcceptDate, tv_retAcceptReason, "1");
                                            }break;
                                        case "7":
                                            rl_pickUp.setVisibility(View.VISIBLE);
                                            if (finalStats >= 7) {
                                                applycolor(iv_pickUpCircle, iv_pickUpLine, tv_pickUpTitle, tv_pickUpDate, tv_pickUpReason, "2");
                                                tv_pickUpDate.setText(arrayJSONObject1.getString("date"));
                                                tv_pickUpReason.setText(arrayJSONObject1.getString("msg"));
                                                tv_pickUpTitle.setText("PickUp Completed & Refund Processed");
                                                tv_returnDate.setText(AppUtils.addDates(2,arrayJSONObject1.getString("date")));
                                                tv_returnReason.setText("");
                                            } else {
                                                applycolor(iv_pickUpCircle, iv_pickUpLine, tv_pickUpTitle, tv_pickUpDate, tv_pickUpReason, "1");
                                            }break;
                                        case "8":
                                            rl_return.setVisibility(View.VISIBLE);
                                            if (finalStats >= 8) {
                                                rl_shipped.setVisibility(View.GONE);
                                                rl_retReq.setVisibility(View.GONE);
                                                rl_retAccept.setVisibility(View.GONE);
                                                rl_pickUp.setVisibility(View.GONE);
                                                tv_returnDate.setText(arrayJSONObject1.getString("date"));
                                                tv_returnReason.setText(arrayJSONObject1.getString("msg"));
                                                tv_returnTitle.setText("Returned & Refund Completed");
                                                applycolor(iv_returnCircle, iv_returnLine, tv_returnTitle, tv_returnDate, tv_returnReason, "2");
                                            } else {
                                                applycolor(iv_returnCircle, iv_returnLine, tv_returnTitle, tv_returnDate, tv_returnReason, "1");
                                            }break;
                                    }

                                }

                            }
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                            TrackingAdapter trackingAdapter = new TrackingAdapter(mActivity, TrackingList);
                            recyclerViewTracking.setLayoutManager(layoutManager);
                            recyclerViewTracking.setAdapter(trackingAdapter);


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

    public void applycolor(ImageView imgCircle, ImageView imgLine, TextView tvTitle, TextView tvDatee, TextView tvReason, String choice) {
        switch (choice) {
            case "1":
                imgCircle.setBackground(getResources().getDrawable(R.drawable.ic_circle_grey));
                imgLine.setBackground(getResources().getDrawable(R.mipmap.greyline));
                tvTitle.setTextColor(getColor(R.color.grey));
                tvDatee.setTextColor(getColor(R.color.grey));
                tvReason.setTextColor(getColor(R.color.grey));
                break;
            case "2":
                imgCircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                imgLine.setBackground(getResources().getDrawable(R.mipmap.green));
                tvTitle.setTextColor(getColor(R.color.green));
                tvDatee.setTextColor(getColor(R.color.green));
                tvReason.setTextColor(getColor(R.color.green));
                break;
            case "3":
                imgCircle.setBackground(getResources().getDrawable(R.drawable.ic_circle));
                imgLine.setBackground(getResources().getDrawable(R.mipmap.red));
                tvTitle.setTextColor(getColor(R.color.red));
                tvDatee.setTextColor(getColor(R.color.red));
                tvReason.setTextColor(getColor(R.color.red));
                break;
        }
    }


    private class TrackingAdapter extends RecyclerView.Adapter<TrackingActivity.Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public TrackingAdapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
            Log.v("MyCatList", data.toString());
        }

        public TrackingActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TrackingActivity.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_layout, parent, false));
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final TrackingActivity.Holder holder, final int position) {

            holder.tvDate.setText(data.get(position).get("date"));
            holder.tvOrderedShippedDate.setText(data.get(position).get("date"));
            holder.tvOrderedDelivereDate.setText(data.get(position).get("date"));
            holder.tvOrderReturnDate.setText(data.get(position).get("date"));
            holder.tvReason.setText(data.get(position).get("remark"));
            String status = data.get(position).get("status");
            String msg = data.get(position).get("msg");
            String finalstatus = data.get(position).get("finalstatus");
            Log.v("satutss", status);
            Log.v("satutss", data.get(position).get("date"));
            Log.v("finalstatuss", data.get(position).get("finalstatus"));

            holder.rlordered.setVisibility(View.GONE);
            holder.rlshipped.setVisibility(View.GONE);
            holder.rldelivered.setVisibility(View.GONE);
            holder.rlreturn.setVisibility(View.GONE);


            if (finalstatus.equalsIgnoreCase("8")) {
                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));
            }
            holder.tvReasn.setVisibility(View.VISIBLE);
            holder.tvReason.setVisibility(View.VISIBLE);
            if (status.equals("1")) {

                holder.rlordered.setVisibility(View.VISIBLE);

                if (!msg.equals("")) {
                    holder.tvOrdercancelle.setText(" " + msg);

                } else {
                    holder.tvOrdercancelle.setText(getString(R.string.placed));
                }

                holder.tvReasn.setVisibility(View.GONE);
                holder.tvReason.setVisibility(View.GONE);
                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("2")) {
                holder.rlshipped.setVisibility(View.VISIBLE);

                Log.v("statattat", status);

                if (!msg.equals("")) {
                    holder.tvOrderShipped.setText(getString(R.string.shiped) + getString(R.string.comma) + " " + msg);

                } else {
                    holder.tvOrderShipped.setText(getString(R.string.shiped));
                }
                holder.ivOrderShipped.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line2.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvOrderedShippedDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrderShipped.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("3")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.deliverd));
                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("4")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.cancelled));

                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.red));
                holder.tvDate.setTextColor(getResources().getColor(R.color.red));
                holder.tvReason.setTextColor(getResources().getColor(R.color.red));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.red));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.red));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.red));
                holder.tvReasn.setVisibility(View.VISIBLE);
                holder.tvReason.setVisibility(View.VISIBLE);


            }
            if (status.equals("5")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.returnpending));

                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("6")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.returnrequest));

                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("7")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.pickuped));

                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));


            }
            if (status.equals("8")) {
                holder.rlordered.setVisibility(View.VISIBLE);
                holder.tvOrdercancelle.setText(getString(R.string.returnrequestcomple));
                holder.ivcircle.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                holder.line1.setBackground(getResources().getDrawable(R.mipmap.green));
                holder.tvDate.setTextColor(getResources().getColor(R.color.green));
                holder.tvReason.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelle.setTextColor(getResources().getColor(R.color.green));
                holder.tvReasn.setTextColor(getResources().getColor(R.color.green));
                holder.tvOrdercancelled.setTextColor(getResources().getColor(R.color.green));

            }
        }

        public int getItemCount() {
            return data.size();

        }

    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvOrdercancelle, tvReason, tvDate, tvReasn, tvOrdercancelled, tvOrderShipped, tvOrderedShippedDate, tvOrderedDelivereDate, tvOrderReturnDate;

        //ImageView
        ImageView ivcircle, line1, line2, line4, ivOrderShipped, ivOrderedDelivered, line3, ivOrderReturn;

        //RelativeLayout
        RelativeLayout rlordered, rlshipped, rldelivered, rlreturn;

        public Holder(View inflate) {
            super(inflate);
            tvOrdercancelle = inflate.findViewById(R.id.tvOrdercancelle);
            tvReason = inflate.findViewById(R.id.tvReason);
            tvDate = inflate.findViewById(R.id.tvDate);
            tvReasn = inflate.findViewById(R.id.tvReasn);
            tvOrdercancelled = inflate.findViewById(R.id.tvOrdercancelled);
            tvOrderShipped = inflate.findViewById(R.id.tvOrderShipped);

            tvOrderedShippedDate = inflate.findViewById(R.id.tvOrderedShippedDate);
            tvOrderedDelivereDate = inflate.findViewById(R.id.tvOrderedDelivereDate);
            tvOrderReturnDate = inflate.findViewById(R.id.tvOrderReturnDate);

            //ImageView
            ivOrderShipped = inflate.findViewById(R.id.ivOrderShipped);
            ivOrderedDelivered = inflate.findViewById(R.id.ivOrderedDelivered);
            ivOrderReturn = inflate.findViewById(R.id.ivOrderReturn);
            ivcircle = inflate.findViewById(R.id.ivcircle);
            line1 = inflate.findViewById(R.id.line1);
            line2 = inflate.findViewById(R.id.line2);
            line3 = inflate.findViewById(R.id.line3);
            line4 = inflate.findViewById(R.id.line4);
            ivOrderReturn = inflate.findViewById(R.id.ivOrderReturn);

            //RelaytiveLayout
            rlordered = inflate.findViewById(R.id.rlordered);
            rlshipped = inflate.findViewById(R.id.rlshipped);
            rldelivered = inflate.findViewById(R.id.rldelivered);
            rlreturn = inflate.findViewById(R.id.rlreturn);


        }
    }
}