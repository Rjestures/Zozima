package code.searching;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import code.basic.CartAcivity;
import code.basic.WishlistAcitvity;
import code.product.ProductAddToCardActivity;
import code.product.ProductDetailsActivity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;

import code.searching.SearchActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;


public class SearchListActivity extends BaseActivity implements View.OnClickListener {

    //ArrayList
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
    private ArrayList<HashMap<String, String>> arrSearchList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrImagList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrImagFaList = new ArrayList<>();


    //ProgressDailod///
    ProgressDialog mProgressDialog;
    ProgressDialog mProgressDialog1;
    ProgressDialog mProgressDialog2;
    ProgressBar progresbar;


    int pos = 0, other = 0, downlod = 0;
    int a = 0, p = 0, n = 0, download = 0, facebook = 0;

    //ImageView
    ImageView ivback, ivZozima, ivcart, ivfevriot, ivSearch, ivloader, ivFavourite;

    //RecyclerView
    private RecyclerView recyclerView;

    //RelativeLayout
    RelativeLayout rrNoData;

    //TextView
    TextView tvCount;

    //String
    String paging = "0";


    //NestedScrollView
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        findViewById();
        setLisnere();

        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
            arrSearchList.clear();
            getSearchList();
        } else {
            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }

    }

    //setLisnere
    private void setLisnere() {
        ivSearch.setOnClickListener(this);
        ivback.setOnClickListener(this);
        ivfevriot.setOnClickListener(this);
        ivcart.setOnClickListener(this);

    }

    //findViewById
    private void findViewById() {

        //ImageViee
        ivZozima = findViewById(R.id.zozima);
        ivloader = findViewById(R.id.ivloader);
        ivcart = findViewById(R.id.ivcart);
        ivfevriot = findViewById(R.id.ivFavourite);
        ivSearch = findViewById(R.id.ivSearch);
        ivback = findViewById(R.id.ivback);
        ivFavourite = (ImageView) findViewById(R.id.ivFavourite);

        //Relaytivelayout
        rrNoData = findViewById(R.id.rrNoData);

        //TextView
        tvCount = findViewById(R.id.tvCount);

        //ScrollView
        scrollView = findViewById(R.id.scrollView);

        //Progresbar
        progresbar = findViewById(R.id.progresbar1);

        //REcyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);


/*
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                int scrollY = rootScrollView.getScrollY(); // For ScrollView
//                int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
//                // DO SOMETHING WITH THE SCROLL COORDINATES
                if (scrollView != null && !paging.equalsIgnoreCase("0")) {
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        //scroll view is at bottom

                        progresbar.setVisibility(View.VISIBLE);
                        arrSearchList.clear();
                        getCatlogCollection();
                    } else {
                    }
                }
            }
        });
*/


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.ivFavourite:
                startActivity(new Intent(mActivity, WishlistAcitvity.class));
                break;

            case R.id.ivcart:
                Intent intent = new Intent(mActivity, CartAcivity.class);
                startActivity(intent);
                break;

            case R.id.ivSearch:
                Intent intent1 = new Intent(mActivity, SearchActivity.class);
                startActivity(intent1);
                break;


        }

    }


    private void getSearchList() {
        AppUtils.hideSoftKeyboard(mActivity);


        String url = AppUrls.AdvanceSearchList;
        Log.v("AdvanceSearchList", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
           
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("name", AppSettings.getString(AppSettings.productname));
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("pageindex", paging);
          
            Log.v("AdvanceSearchList", String.valueOf(json));
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
                        ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();
                        progresbar.setVisibility(View.GONE);
                        Log.v("AdvanceSearchList", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);


                            if (jsonObject.getString("resCode").equals("1")) {



                                paging = jsonObject.getString("pageindex");
                                JSONArray jsonArray = jsonObject.getJSONArray("SearchList");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("catalogId", arrayJSONObject.getString("catalogId"));
                                    hashlist.put("Type", arrayJSONObject.getString("Type"));
                                    hashlist.put("shipping_charge", arrayJSONObject.getString("shipping_charge"));
                                    hashlist.put("status", arrayJSONObject.getString("status"));
                                    hashlist.put("name", arrayJSONObject.getString("name"));
                                    hashlist.put("productCount", arrayJSONObject.getString("productCount"));
                                    hashlist.put("stattingPrice", arrayJSONObject.getString("stattingPrice"));
                                    hashlist.put("DiscountValue", arrayJSONObject.getString("DiscountValue"));
                                    hashlist.put("description", arrayJSONObject.getString("description"));
                                    hashlist.put("DiscounType", arrayJSONObject.getString("DiscounType"));
                                    hashlist.put("prod_price", arrayJSONObject.getString("prod_price"));
                                    hashlist.put("imageArray", arrayJSONObject.getJSONArray("thumbnails").toString());
                                    arrSearchList.add(hashlist);
                                }


                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                                SearchAdapter adapterr = new SearchAdapter(mActivity, arrSearchList);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapterr);
                            } else {
                                rrNoData.setVisibility(View.VISIBLE);
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

    public void imge(int pos) {

        if (arrImagFaList.size() > pos) {

            new DownloadFromURLShareNow().execute(arrImagFaList.get(pos).get("images"));
            Log.v("image", String.valueOf(arrImagFaList.size()));
            Log.v("cdsjf", arrImagFaList.get(pos).get("images"));

        }

    }

    private void Facebook(int facebook) {
        if (arrImagFaList.size() > facebook) {

            new DownloadFromURLDFaceBookownloaderl().execute(arrImagFaList.get(facebook).get("images"));

        }
    }

    public void imgee(int other) {

        if (arrImagFaList.size() > other) {

            new DownloadFromURLlOther().execute(arrImagFaList.get(other).get("images"));


        }
    }

    public void Download(int downlod) {

        if (arrImagFaList.size() > downlod) {

            new DownloadFromURLDownloader().execute(arrImagFaList.get(downlod).get("images"));

        }

    }


    //addFavApi
    private void addFavApi(String CatalogId, final ImageView ivFavourite) {

        AppUtils.showRequestDialog(mActivity);

        Log.v("addFavApi", AppUrls.WishList);

        JSONObject json = new JSONObject();
        JSONObject json_data = new JSONObject();

        try {
            json_data.put("userId", AppSettings.getString(AppSettings.userId));
            json_data.put("catalogId", CatalogId);

            json.put(AppConstants.projectName, json_data);
            Log.v("addFavApi", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppUrls.WishList)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseFavdata(response, ivFavourite);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(getApplicationContext(), String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(getApplicationContext(), String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    private void parseFavdata(JSONObject response, ImageView ivFavourite) {
        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
            String resMasg = jsonObject1.getString("resMsg");
            if (jsonObject1.getString("resCode").equals("1")) {

                if (jsonObject1.getString("status").equals("1")) {
                    Toast.makeText(mActivity, resMasg, Toast.LENGTH_SHORT).show();

                    ivFavourite.setImageResource(R.drawable.ic_fav_selected);
                    AppUtils.showToastSort(getApplicationContext(), getString(R.string.catalogseveddmsg));

                } else if (jsonObject1.getString("status").equals("2")) {
                    AppUtils.showToastSort(getApplicationContext(), getString(R.string.catalogremovemasg));
                    ivFavourite.setImageResource(R.drawable.ic_fav_unselected);

                }
            }

        } catch (Exception e) {
            AppUtils.showToastSort(getApplicationContext(), String.valueOf(e));
        }
        AppUtils.hideDialog();


    }


    private class SearchAdapter extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public SearchAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_catalogue, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            final String statusu = (data.get(position).get("status"));
            final String name = (data.get(position).get("name"));

            if (name.contains("Combo Pack")) {
                String camboname = name.substring(0, 10);
                String namevalue = name.substring(11);
                holder.tvName.setText(namevalue);
                holder.tvCambo.setText(camboname);
            } else
                {
                    holder.tvCambo.setVisibility(View.GONE);

                holder.tvName.setText(name);

            }

            if (data.get(position).get("DiscounType").equals("1"))

                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") + " " + getString(R.string.off));

            else if (data.get(position).get("DiscounType").equals("2"))
                holder.tvDiscount.setText("₹" + data.get(position).get("DiscountValue") + " " + getString(R.string.percent));

            else
                holder.tvDiscount.setText("");

            if (data.get(position).get("stattingPrice").equalsIgnoreCase(data.get(position).get("prod_price"))) {
                holder.tvPrice.setText("₹" + (data.get(position)).get("prod_price"));
                holder.tvDiscount.setText("");
                holder.tvOriginalPrice.setText("");

            } else {
                holder.tvOriginalPrice.setText("₹" + (data.get(position)).get("stattingPrice"));
                holder.tvPrice.setText("₹" + (data.get(position)).get("prod_price"));
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | 16);
            }


            final String type = (data.get(position).get("Type"));

            Log.v("fndjf", data.get(position).get("Type"));

            final String shipingCharge = (data.get(position).get("shipping_charge"));


            if (type.equals("1")) {

                holder.shipincharge.setText(getString(R.string.shipping)+" " + getString(R.string.rupaye) + shipingCharge);

            } else {
                holder.llShippingCharge.setVisibility(View.GONE);

            }

            if (statusu.equals("1")) {

                holder.ivFavourite.setImageResource(R.drawable.ic_fav_selected);
            } else if (statusu.equals("2")) {
                holder.ivFavourite.setImageResource(R.drawable.ic_fav_unselected);

            }
            holder.tvName.setText(data.get(position).get("name"));

            int counter = Integer.parseInt(data.get(position).get("productCount"));

            if (counter <= 2) {

                holder.tvCount.setVisibility(View.INVISIBLE);
            } else

                holder.tvCount.setVisibility(View.VISIBLE);

            holder.tvCount.setText("+" + Integer.toString(Integer.parseInt(data.get(position).get("productCount")) - 2));


            try {
                JSONArray jsonArray = new JSONArray(data.get(position).get("imageArray"));

                for (int ik = 0; ik < jsonArray.length(); ik++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(ik);

                    if (ik == 0) {
                        Picasso.get().load(jsonObject.getString("productThumbnil"))
                                .placeholder(R.mipmap.logo_grey).resize(400,600).into(holder.ivImage);
                    }

                    if (ik == 1) {
                        Picasso.get().load(jsonObject.getString("productThumbnil"))
                                .placeholder(R.mipmap.logo_grey).resize(400,600).into(holder.ivImage2);
                    }

                    if (ik == 2) {

                        Picasso.get().load(jsonObject.getString("productThumbnil"))

                                .placeholder(R.mipmap.logo_grey).resize(400,600).into(holder.ivImage3);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.rlFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SimpleHTTPConnection.isNetworkAvailable(getApplicationContext())) {

                        addFavApi(data.get(position).get("catalogId"), holder.ivFavourite);
                    } else {
                        AppUtils.showToastSort(getApplicationContext(), getString(R.string.errorInternet));
                    }
                }
            });
            holder.llWhatsapp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    try {
                        arrImagFaList.clear();
                        imageUriArray.clear();
                        sharednowApi(data.get(position).get("catalogId"));
                        JSONArray jsonArray2 = new JSONArray(data.get(position).get("imageArray"));
                        Log.v("nadsklsadk", jsonArray2.toString());
                        for (int j = 0; j < jsonArray2.length(); j++) {

                            JSONObject jsonArrayJSONObject = jsonArray2.getJSONObject(j);

                            Log.v("helloimage", jsonArrayJSONObject.getString("productThumbnil"));

                            HashMap<String, String> hashlist2 = new HashMap();

                            hashlist2.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                            arrImagFaList.add(hashlist2);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pos = 0;
                    imge(pos);
                    Log.v("imagePositions", "position= " + data.get(position).get("imageArray"));


                }
            });


            holder.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        arrImagFaList.clear();
                        imageUriArray.clear();
                        JSONArray jsonArray2 = new JSONArray(data.get(position).get("imageArray"));
                        Log.v("nadsklsadk", jsonArray2.toString());
                        for (int j = 0; j < jsonArray2.length(); j++) {

                            JSONObject jsonArrayJSONObject = jsonArray2.getJSONObject(j);

                            Log.v("helloimage", jsonArrayJSONObject.getString("productThumbnil"));

                            HashMap<String, String> hashlist2 = new HashMap();

                            hashlist2.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                            arrImagFaList.add(hashlist2);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    other = 0;
                    imgee(other);
                    Log.v("imagePositions", "position= " + data.get(position).get("imageArray"));

                }
            });
            holder.llDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        arrImagFaList.clear();
                        imageUriArray.clear();
                        JSONArray jsonArray2 = new JSONArray(data.get(position).get("imageArray"));
                        Log.v("nadsklsadk", jsonArray2.toString());
                        for (int j = 0; j < jsonArray2.length(); j++) {

                            JSONObject jsonArrayJSONObject = jsonArray2.getJSONObject(j);

                            Log.v("helloimage", jsonArrayJSONObject.getString("productThumbnil"));

                            HashMap<String, String> hashlist3 = new HashMap();

                            hashlist3.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                            arrImagFaList.add(hashlist3);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    downlod = 0;
                    Download(downlod);
                    Log.v("imagePositions", "position= " + data.get(position).get("imageArray"));

                }
            });
            holder.llFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        arrImagFaList.clear();
                        imageUriArray.clear();
                        JSONArray jsonArray2 = new JSONArray(data.get(position).get("imageArray"));
                        Log.v("nadsklsadk", jsonArray2.toString());
                        for (int j = 0; j < jsonArray2.length(); j++) {

                            JSONObject jsonArrayJSONObject = jsonArray2.getJSONObject(j);

                            Log.v("helloimage", jsonArrayJSONObject.getString("productThumbnil"));

                            HashMap<String, String> hashlist3 = new HashMap();

                            hashlist3.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                            arrImagFaList.add(hashlist3);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    facebook = 0;
                    Facebook(facebook);
                    Log.v("imagePositions", "position= " + data.get(position).get("imageArray"));

                }
            });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  if (data.get( position ).get( "status" ).equalsIgnoreCase( "1" )) {
                        Intent intent=new Intent( mActivity, ProductAddToCardActivity.class );
                        intent.putExtra( "id", AppSettings.putString( AppSettings.productId, data.get( position ).get( "id" ) ) );
                        startActivity( intent );
                    }
                    else

                    {*/
                        Intent intent=new Intent( mActivity, ProductDetailsActivity.class );
                        intent.putExtra( "catalogId", AppSettings.putString( AppSettings.catalogId, data.get( position ).get( "catalogId" ) ) );
                        intent.putExtra( "status",data.get( position ).get( "status" ) );
                        intent.putExtra( "description",data.get( position ).get( "description" ) );
                        startActivity( intent );



                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvName, tvCount, tvPrice, tvOriginalPrice, tvDiscount, tvCambo;

        //CardView
        CardView cardView;

        //ImageView
        ImageView ivImage, ivImage2, ivImage3, ivFavourite;

        //RelativeLayout
        RelativeLayout rlFavourite;

        //LinearLayout
        LinearLayout llDownload, llFacebook, llShare, llWhatsapp;
        LinearLayout llShippingCharge;
        TextView shipincharge;
        LinearLayout layout_shiping;
        ImageView ivshipping;

        public Holder(View itemView) {
            super(itemView);

            llShippingCharge = itemView.findViewById(R.id.layout_shiping);
            ivshipping = itemView.findViewById(R.id.ivshipping);
            shipincharge = itemView.findViewById(R.id.shipincharge);
            tvCambo = itemView.findViewById(R.id.tvCambo);

            //CardView
            cardView = itemView.findViewById(R.id.cardView);

            //TextView
            tvName = itemView.findViewById(R.id.tvName);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);

            //ImageView
            ivImage = itemView.findViewById(R.id.ivImage);
            ivImage2 = itemView.findViewById(R.id.ivImage2);
            ivImage3 = itemView.findViewById(R.id.ivImage3);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            tvCount = itemView.findViewById(R.id.tvCount);

            //RelativeLayout
            rlFavourite = itemView.findViewById(R.id.rlFavourite);

            llDownload = itemView.findViewById(R.id.llDownload);
            llFacebook = itemView.findViewById(R.id.llFacebook);
            llShare = itemView.findViewById(R.id.llShare);
            llWhatsapp = itemView.findViewById(R.id.llWhatsapp);

        }
    }

    //DownloadFromURLShareNow
    class DownloadFromURLShareNow extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (pos == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(SearchListActivity.this);
                // Set progressdialog title
                mProgressDialog.setTitle(getString(R.string.pleasewait));
                // Set progressdialog message
                mProgressDialog.setMessage(getString(R.string.loadingimage));

                // Show progressdialog
                mProgressDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v("justify", "i");
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "name" + p++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "name" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();


                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    outputStream.write(data, 0, count);
                }
                // flushing output
                outputStream.flush();
                // closing streams
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                // Log.e("Error: ", e.getMessage());
                //  kProgressHUD.dismiss();
                System.out.println(e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

//            Toast.makeText( mActivity, "Sharing Images...", Toast.LENGTH_SHORT ).show();
//            mProgressDialog.dismiss();
            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + Integer.toString(a++) + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("wjhjbcb", String.valueOf(imageUriArray.size()));
            Log.v("wjhjbcb", String.valueOf(arrImagList.size()));
            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                        intent.setType("image/jpeg");
                        intent.setPackage("com.whatsapp");
                        Toast.makeText(mActivity, getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }

                }
//


            }

            pos = pos + 1;

            imge(pos);


        }

    }

    //DownloadFromURLlOther
    class DownloadFromURLlOther extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (other == 0) {
                // Create a progressdialog
                mProgressDialog1 = new ProgressDialog(SearchListActivity.this);
                // Set progressdialog title
                // Set progressdialog message
                mProgressDialog1.setMessage(getString(R.string.pleasewait));

                // Show progressdialog
                mProgressDialog1.show();
            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v("justify", "i");
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "name" + p++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "name" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    outputStream.write(data, 0, count);
                }
                // flushing output
                outputStream.flush();
                // closing streams
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                // Log.e("Error: ", e.getMessage());
                //  kProgressHUD.dismiss();
                System.out.println(e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {


            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + Integer.toString(n++) + ".jpeg");

            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));

            imageFileToShare.length();

            Log.v("mdf", "image= " + imageFileToShare);

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");

                        m.invoke(null);

                        Intent shareIntent1 = new Intent();
                        shareIntent1.setAction(Intent.ACTION_SEND);
                        shareIntent1.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                        shareIntent1.setType("image/jpeg");
                        shareIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent1, "send"));
                        Toast.makeText(mActivity, getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
                        mProgressDialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog1.dismiss();
                    }


                }
            }
            other = other + 1;
            imgee(other);


        }


    }

    //DownloadFromURLDownloader
    class DownloadFromURLDownloader extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (downlod == 0) {
                mProgressDialog2 = new ProgressDialog(SearchListActivity.this);
                // Set progressdialog title
                mProgressDialog2.setMessage(getString(R.string.imageDownload));
                mProgressDialog2.setIndeterminate(false);
                mProgressDialog2.setMax(100);
                mProgressDialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog2.setCancelable(true);
                mProgressDialog2.show();


            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v("justify", "i");
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "collection" + download++ + ".jpeg"));
                /*String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );*/
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);
                MediaScannerConnection.scanFile(mActivity,
                        new String[]{(file)}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = inputStream.read(data)) != -1) {

                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    if (fileLength > 0) {
                        publishProgress("" + (total * 100 / fileLength));
                    }
                    outputStream.write(data, 0, count);
                }


                // flushing output
                outputStream.flush();

                // closing streams

                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            mProgressDialog2.setProgress(0);
            int incProgress = 100 / arrImagFaList.size() + 1;
            int imageUriArrayy = imageUriArray.size() + 1;

            for (int i = 0; i < imageUriArrayy; i++) {
                // Do stuff
                mProgressDialog2.incrementProgressBy(incProgress);
            }

            if (imageUriArray != null) {

                mProgressDialog2.setMessage(getString(R.string.downloading) + (imageUriArrayy + 0) + "/" + arrImagFaList.size());
            }
        }

        @Override
        protected void onPostExecute(String file_url) {

            File imageFileToShare = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Zozima", "collection" + Integer.toString(download++) + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));

            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");

                        m.invoke(null);

                        mProgressDialog2.incrementProgressBy(100);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                mProgressDialog2.dismiss();
                                Toast.makeText(mActivity, getString(R.string.catlogeimagedownlod), Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog2.dismiss();
                    }


                }
            }
            downlod = downlod + 1;
            Download(downlod);


        }


    }

    //DownloadFromURLDFaceBookownloaderl
    class DownloadFromURLDFaceBookownloaderl extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (facebook == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(SearchListActivity.this);
                // Set progressdialog title// Set progressdialog message
                mProgressDialog.setMessage(getString(R.string.pleasewait));

                // Show progressdialog
                mProgressDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v("justify", "i");
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "name" + p++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "name" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    outputStream.write(data, 0, count);
                }
                // flushing output
                outputStream.flush();
                // closing streams
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                // Log.e("Error: ", e.getMessage());
                //  kProgressHUD.dismiss();
                System.out.println(e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + Integer.toString(a++) + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("imageUriArray", String.valueOf(imageUriArray.size()));
            Log.v("arrImagList", String.valueOf(arrImagList.size()));
            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                        intent.setType("image/jpeg");
                        intent.setPackage("com.facebook.katana");
                        mProgressDialog.dismiss();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }

                }
//


            }

            facebook = facebook + 1;

            Facebook(facebook);


        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        try {
            int count = Integer.parseInt(AppSettings.getString(AppSettings.total_count));

            if (count > 0) {
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(String.valueOf(count));
            } else {
                tvCount.setVisibility(View.GONE);
            }

        } catch (NumberFormatException ex) { // handle your exception
            tvCount.setVisibility(View.GONE);

        }

    }


    private void sharednowApi(String catalogId) {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.ShareCalog;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("catalogId", catalogId);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            Log.v("finddObject", String.valueOf(json));

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

                        Log.v("getsharedCatalog", String.valueOf(response));
                        try {

                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);


                            if (jsonObject.getString("resCode").equals("1")) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("erroorr", String.valueOf(anError));


                    }

                });
    }

}

