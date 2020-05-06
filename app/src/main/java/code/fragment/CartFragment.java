package code.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import code.basic.AddMarginActivity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseFragment;
import code.view.CustomTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private ArrayList<HashMap<String, String>> categoryList = new ArrayList<>();

    RecyclerView rladdtoCart;

    ImageView  ivback;

    CustomTextView procces, tv_productcharj, price, totalprice, tv_codcharj, proccedd;

    RadioButton redioonline, cashredio;

    LinearLayout linearLayout;

    int n = 1;

    String value = String.valueOf(1);

    ArrayList<String> slelectsize;

    ArrayList<String> setSize;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cart, container, false);
        findViewById();
        setListener();
        return view;
    }

    private void setListener() {
        ivback.setOnClickListener(this);
        procces.setOnClickListener(this);
        redioonline.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        cashredio.setOnClickListener(this);
        proccedd.setOnClickListener(this);
    }

    private void findViewById() {


        ivback = view.findViewById(R.id.ivback);
        procces = view.findViewById(R.id.proccedd);

        redioonline = view.findViewById(R.id.redioonline);
        linearLayout = view.findViewById(R.id.rl);
        cashredio = view.findViewById(R.id.cashredio);
        rladdtoCart = view.findViewById(R.id.rladdtocart);
        tv_productcharj = view.findViewById(R.id.tv_productcharj);
        price = view.findViewById(R.id.tvShipping );
        totalprice = view.findViewById(R.id.totalprice);
        tv_codcharj = view.findViewById(R.id.tv_codcharj);
        proccedd = view.findViewById(R.id.proccedd);
        slelectsize = new ArrayList<>();
        setSize = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivback:

                break;

            case R.id.proccedd:
                Intent intent = new Intent(mActivity, AddMarginActivity.class);
                startActivity(intent);
                break;

            case R.id.redioonline:
                linearLayout.setVisibility(View.VISIBLE);
                getCartList();

                break;

            case R.id.cashredio:

                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_productcharj:

                break;
            case R.id.tvProceed:

                break;
        }


    }


    private class Adapter extends RecyclerView.Adapter<CartFragment.Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public CartFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CartFragment.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addtocard, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CartFragment.Holder holder, final int position) {
            holder.tv_name.setText(data.get(position).get("productName"));
            holder.tv_price.setText(data.get(position).get("productPrice"));
            holder.countTv.setText(data.get(position).get("quantity"));

            final String cartId;
            cartId = (data.get(position).get("cartId"));
            final String productId = (data.get(position).get("productId"));
            String quantity = (data.get(position).get("quantity"));
            String size = (data.get(position).get("size"));
            String orderTotal = (data.get(position).get("orderTotal"));
            String productImage = (data.get(position).get("productImage"));
            proccedd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, AddMarginActivity.class);
                    intent.putExtra("orderTotal",data.get(position).get("orderTotal"));
                    intent.putExtra("productPrice",data.get(position).get("productPrice"));
                    intent.putExtra("productName",data.get(position).get("productName"));
                    intent.putExtra("size",data.get(position).get("size"));
                    intent.putExtra("quantity",data.get(position).get("quantity"));
                    intent.putExtra("productImage",data.get(position).get("productImage"));

                    startActivity(intent);

                }
            });



            holder.spinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, slelectsize));

            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            holder.decrIv.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    n = Integer.parseInt(holder.countTv.getText().toString());
                    if (n > 1) {
                        n = n - 1;
                        value = String.valueOf(n);
                        holder.countTv.setText(value);


                    }

                }
            });
            holder.increIv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    n = n + 1;
                    value = String.valueOf(n);
                    holder.countTv.setText(value);


                }
            });
            holder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                    alertDialogBuilder.setMessage("Remove product from cart?");
                    alertDialogBuilder.setPositiveButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                }
                            });

                    alertDialogBuilder.setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RemoveCart(cartId, productId);
                            categoryList.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
            });

            Picasso.get().load(data.get(position).get("productImage")).into( new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.imageView.setImageBitmap(bitmap);

                    holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }

        public int getItemCount() {
            return data.size();

        }

    }

    private void getCartList() {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.getCartList;
        Log.v("getcartList", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put( AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority( Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.hideDialog();
                        Log.v("GetCartList", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            tv_productcharj.setText(jsonObject1.getString("productCharges"));
                            price.setText(jsonObject1.getString("shippingCharges"));
                            totalprice.setText(jsonObject1.getString("orderTotal"));
                            tv_codcharj.setText(jsonObject1.getString("codCharges"));
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("Items");
                            for (int n = 0; n < jsonArray1.length(); n++) {
                                HashMap<String, String> hashlist = new HashMap();
                                JSONObject arrayJSONObject1 = jsonArray1.getJSONObject(n);
                                hashlist.put("cartId", arrayJSONObject1.getString("cartId"));
                                hashlist.put("productId", arrayJSONObject1.getString("productId"));
                                hashlist.put("productName", arrayJSONObject1.getString("productName"));
                                hashlist.put("productImage", arrayJSONObject1.getString("productImage"));
                                hashlist.put("productPrice", arrayJSONObject1.getString("productPrice"));
                                hashlist.put("size", arrayJSONObject1.getString("size"));
                                hashlist.put("quantity", arrayJSONObject1.getString("quantity"));
                                Log.v("hgvgcfyg", arrayJSONObject1.getString("productImage"));
                                categoryList.add(hashlist);

                                JSONArray jsonArray2 = arrayJSONObject1.getJSONArray("unitNames");

                                for (int n1 = 0; n1 < jsonArray2.length(); n1++) {

                                    JSONObject arrayJSONObject2 = jsonArray2.getJSONObject(n1);
                                    String statename = arrayJSONObject2.getString("unit_name");
                                    slelectsize.add(statename);


                                }
                                hashlist.put("orderTotal", jsonObject1.getString("orderTotal"));


                            }

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                           Adapter adapter = new Adapter(mActivity, categoryList);
                            rladdtoCart.setLayoutManager(layoutManager);
                            rladdtoCart.setAdapter(adapter);

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

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView, decrIv, increIv, iv_remove;
        CustomTextView tv_name, tv_price, free_size;
        TextView countTv;
        Spinner spinner;

        public Holder(View inflate) {
            super(inflate);
            imageView = itemView.findViewById(R.id.img_product);
            decrIv = itemView.findViewById(R.id.decrIv);
            increIv = itemView.findViewById(R.id.increIv);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tvShipping );
            /*spinner = itemView.findViewById(R.id.spiner);*/
            countTv = itemView.findViewById(R.id.countTv);

            iv_remove = itemView.findViewById(R.id.iv_remove);
            /*spinner.setVisibility(View.VISIBLE);*/

        }
    }

    private void RemoveCart(String cartId, String productId) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.RemoveProductFromCart;
        Log.v("removeCart", url);
        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {

            jsonObject.put("productId", productId);
            jsonObject.put("cartId", cartId);
            json.put(AppConstants.projectName, jsonObject);
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
                        Log.v("GetCartList", String.valueOf(response));
                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);


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

    @Override
    public void onResume() {
        super.onResume();
        if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {
        getCartList();
        categoryList.clear();

        } else {

            AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
        }
    }


}
