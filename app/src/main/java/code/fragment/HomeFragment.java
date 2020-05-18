package code.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
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
import java.util.Timer;
import java.util.TimerTask;

import code.basic.BannerAddapter;
import code.basic.CartAcivity;
import code.common.CustomLoader;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.product.ProductDetailsActivity;
import code.subcatageory.SubCategoryActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static int currentPage = 0;
    ProgressBar simpleProgressBar;
    CustomLoader loader;

    //String Value
    String paging = "0";
    String catalog;
//    String paging = "0";


    //BottomSheetDialog
    BottomSheetDialog bottomSheetDialog;

    Typeface typeface;

    String typeDiscount;

    int radioIndex = -1;

    //ProgressDailod///
    ProgressDialog mProgressDialog;

    Boolean canScroll = false;
    Boolean canFilter = false;


    int pos = 0, other = 0, downlod = 0;
    int p = 0, n = 0, dd = 0, f = 0, a = 0, facebook = 0;

    //Indicator
    CirclePageIndicator indicator;

    TextView tvFilter, tvreset, tvnofilter;

    //ImageView
    ImageView ivcart, ivloader;
    CatalogueAdapter adapterr;

    //RelaytiveLayout
    RelativeLayout RlFilter, rllfilter;

    //ScrollView;
    NestedScrollView llDicount, scrollable;

    //CheckBox
    CheckBox priceCB1, priceCB2, priceCB3, priceCB4, priceCB5, priceCB6;

    //RadioButton
    RadioButton rbone, rbtwo, rbthree, rbfour, rbfive, rvsix, rvseven, rveight, rvnine, rvone;

    //RadioGroup
    RadioGroup radioGroup;
    Dialog dialog;
    int fileLength;
    //ArrayList
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();

    ArrayList<String> arrPricelist = new ArrayList<String>();
    JSONArray price = new JSONArray();
    JSONArray category = new JSONArray();
    JSONArray color = new JSONArray();
    JSONObject maindata = new JSONObject();
    JSONObject result = new JSONObject();
    ProgressBar downProgress;
    //NestedScrollView
    NestedScrollView scrollView;
    SwipeRefreshLayout simpleSwipeRefreshLayout;
    private ArrayList FilterArrayList = new ArrayList<>();
    private ArrayList arrColorList = new ArrayList<>();
    //Hasmap ArrayList
    private ArrayList<HashMap<String, String>> categoryList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> categoryList1 = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrGetcolorList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> topBannerList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> catalogueList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrImagList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrImagFaList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> checkBoxAray = new ArrayList<>();
    private ArrayList<HashMap<String, String>> Raaay = new ArrayList<>();
    private ArrayList<HashMap<String, String>> priceArray = new ArrayList<>();
    //RecyclerView
    private RecyclerView recyclerViewCat, recyclerView, rvCategory, rvColor;
    //ViewPager
    private ViewPager viewPager;
    //LinearLayout
    private LinearLayout llCategoryy, llPrice, lllPrice, llCategory, llDiscount, llCollor, llColllor, llColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViewById(view);
        listner();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
        adapterr = new CatalogueAdapter(mActivity, catalogueList);
        recyclerView.setLayoutManager(layoutManager);
        //getCategoryApi
        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {


            categoryList.clear();
            getCategoryApi();
            catalogueList.clear();
            getCatlogCollection();// first Time Calling

        } else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }


        return view;
    }

    private void listner() {
        RlFilter.setOnClickListener(this);
    }

    //findViewById
    private void findViewById(View view) {

        //LinearLayout
        ivcart = view.findViewById(R.id.ivcart);
        scrollView = view.findViewById(R.id.scrollView);
        downProgress = view.findViewById(R.id.progresbar1);


        loader = new CustomLoader(mActivity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //recyclerView
        recyclerViewCat = view.findViewById(R.id.recyclerViewCat);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvnofilter = view.findViewById(R.id.tvnofilter);
        simpleProgressBar = view.findViewById(R.id.progresbar);


        //ViewPager
        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);

        //Loader
        ivloader = view.findViewById(R.id.ivloader);


        //RelayTiveLayout
        RlFilter = view.findViewById(R.id.RlFilter);

        //BannerApi
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                int scrollY = rootScrollView.getScrollY(); // For ScrollView
//                int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
//                // DO SOMETHING WITH THE SCROLL COORDINATES
                if (scrollView != null && !paging.equalsIgnoreCase("0")) {
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        //scroll view is at bottom

                        simpleProgressBar.setVisibility(View.GONE);
                        downProgress.setVisibility(View.VISIBLE);
                        if (canScroll) {
                            if (canFilter)
                                hitFilterApi();
                            else
                                getCatlogCollection();
                        }
                           /* if(canFilter)
                                hitFilterApi();
                            else if(canScroll)
                            getCatlogCollection();// Pagination Api*/


                    } else {
                        downProgress.setVisibility(View.GONE);
                    }
                }
            }
        });

        hitgetBanners();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rlBottom:

                //displayView(1);

                break;

            case R.id.ivcart:
                startActivity(new Intent(mActivity, CartAcivity.class));
                break;

            case R.id.RlFilter:
                filter();
                break;

            default:

                break;

        }
    }

    //HitBannerApi
    private void hitgetBanners() {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        final String url = AppUrls.getBanners;
        Log.v("getbanner", url);
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();

                        Log.v("getbanner", String.valueOf(response));

                        try {

                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);


                            JSONArray jsonArray = jsonObject1.getJSONArray("banners");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String id = jsonObject2.getString("id");
                                String banner_image = jsonObject2.getString("banner_image");
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("banner_image", jsonObject2.getString("banner_image"));
                                topBannerList.add(hm);

                            }


                            if (viewPager != null && indicator != null) {
                                viewPager.setAdapter(new BannerAddapter(mActivity, topBannerList));
                                indicator.setViewPager(viewPager);
                            }
                            Timer timer = new Timer();
                            timer.schedule(new RemindTask(topBannerList.size(), viewPager), 0, 5000);


                            // Pager listener over indicator

                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override

                                public void onPageScrolled(int i, float v, int i1) {

                                }

                                @Override

                                public void onPageSelected(int i) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {

                                }
                            });


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();

                        Log.v("gebanner", String.valueOf(anError));
                    }
                });

    }

    //getCategoryApi
    private void getCategoryApi() {

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.getCategory;

        Log.v("getCategoryApi-URL", url);

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .setTag("getCategoryApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseGetCategoryJSON(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    //parseGetCategoryJSON
    private void parseGetCategoryJSON(JSONObject response) {

        AppUtils.hideDialog();

        Log.d("getCategoryApi ", response.toString());

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);

            if (jsonObject.getString("resCode").equals("1")) {

                JSONArray jsonArray = jsonObject.getJSONArray("category");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> hashlist = new HashMap();

                    hashlist.put("categoryId", arrayJSONObject.getString("categoryId"));
                    hashlist.put("categoryName", arrayJSONObject.getString("categoryName"));
                    hashlist.put("categoryIcon", arrayJSONObject.getString("categoryIcon"));
                    categoryList.add(hashlist);
                }

                recyclerViewCat.setLayoutManager(new LinearLayoutManager(
                        mActivity, LinearLayoutManager.HORIZONTAL, false));

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);

                CategoryAdapter adapterr = new CategoryAdapter(mActivity, categoryList);

                recyclerViewCat.setAdapter(adapterr);

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //getCatlogCollection
    private void getCatlogCollection() {
        canScroll = false;
        simpleProgressBar.setVisibility(View.VISIBLE);
        downProgress.setVisibility(View.VISIBLE);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.GetCatalogs;
        Log.v("GetCatalogs1", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("collectionId", AppSettings.getString(AppSettings.collectionId));
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("pageindex", paging);
            jsonObject.put("categoryId", "");
            jsonObject.put("subCategoryId", "");
            Log.v("GetCatalogs1", String.valueOf(json));

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
                        Log.v("GetCatalogs1", String.valueOf(response));
                        try {

                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);

                            if (jsonObject.getString("resCode").equals("1")) {
                                canScroll = true;
                                simpleProgressBar.setVisibility(View.GONE);
//                                ivloader.setVisibility(View.GONE);
                                paging = jsonObject.getString("pageindex");


                                JSONArray jsonArray = jsonObject.getJSONArray("catalogs");


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("catalogId", arrayJSONObject.getString("catalogId"));
                                    catalog = arrayJSONObject.getString("catalogId");
                                    Log.v("catlogggg", catalog);
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
                                    catalogueList.add(hashlist);

                                    JSONArray jsonArray1 = arrayJSONObject.getJSONArray("thumbnails");

                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        JSONObject jsonArrayJSONObject = jsonArray1.getJSONObject(j);

                                        HashMap<String, String> hashlist1 = new HashMap();

                                        hashlist1.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                                        arrImagList.add(hashlist1);

                                    }


                                }
                                adapterr = new CatalogueAdapter(mActivity, catalogueList);
                                recyclerView.setAdapter(adapterr);
                                adapterr.notifyDataSetChanged();
                            } else {
                                canScroll = false;
                                tvnofilter.setVisibility(View.GONE);
                                simpleProgressBar.setVisibility(View.GONE);
                                downProgress.setVisibility(View.GONE);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            canScroll = false;
                            downProgress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));
                        downProgress.setVisibility(View.GONE);

                    }

                });
    }

    //Sharenow method
    public void Sharenow(int pos) {

        if (arrImagFaList.size() > pos) {

            new DownloadFromURL().execute(arrImagFaList.get(pos).get("images"));
            Log.v("image", String.valueOf(arrImagFaList.size()));
            Log.v("cdsjf", arrImagFaList.get(pos).get("images"));

        }

    }

    //Facebook method
    private void Facebook(int facebook) {
        if (arrImagFaList.size() > facebook) {

            new DownloadFromURLFaceBook().execute(arrImagFaList.get(facebook).get("images"));
            Log.v("image", String.valueOf(arrImagFaList.size()));
            Log.v("cdsjf", arrImagFaList.get(facebook).get("images"));

        }
    }

    //OtherShare
    public void OtherShare(int other) {

        if (arrImagFaList.size() > other) {
            new DownloadFromURLOther().execute(arrImagFaList.get(other).get("images"));
            Log.v("cdsjf", arrImagFaList.get(other).get("images"));


        }
    }

    //Download method
    public void Download(int downlod) {

        if (arrImagFaList.size() > downlod) {
            new DownloadFromURLDownloader().execute(arrImagFaList.get(downlod).get("images"));
            Log.v("cdsjf", arrImagFaList.get(downlod).get("images"));

        }

    }

    //AddFavApi
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
                            AppUtils.showToastSort(getActivity(), String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(getActivity(), String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    private void parseFavdata(JSONObject response, ImageView ivFavourite) {
       /* catalogueList.clear();
        getCatlogCollection();*/
        AppUtils.hideDialog();
        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

            String resMasg = jsonObject1.getString("resMsg");

            if (jsonObject1.getString("resCode").equals("1")) {


                if (jsonObject1.getString("status").equals("1")) {

                    ivFavourite.setImageResource(R.drawable.ic_fav_selected);
                    AppUtils.showToastSort(getActivity(), getString(R.string.catalogseveddmsg));

                } else if (jsonObject1.getString("status").equals("2")) {

                    AppUtils.showToastSort(getActivity(), getString(R.string.catalogremovemasg));
                    ivFavourite.setImageResource(R.drawable.ic_fav_unselected);
                }


            }

        } catch (Exception e) {
            AppUtils.showToastSort(getActivity(), String.valueOf(e));
        }
        AppUtils.hideDialog();

    }

    //Filter
    public void filter() {

        price = new JSONArray();
        category = new JSONArray();
        color = new JSONArray();
        maindata = new JSONObject();
        result = new JSONObject();

        bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(R.layout.filter_layout);
        rllfilter = bottomSheetDialog.findViewById(R.id.rllfilter);
        rvCategory = bottomSheetDialog.findViewById(R.id.rvCategory);
        rvColor = bottomSheetDialog.findViewById(R.id.rvColor);
        llCategoryy = bottomSheetDialog.findViewById(R.id.llCategoryy);
        llColor = bottomSheetDialog.findViewById(R.id.llColor);
        llPrice = bottomSheetDialog.findViewById(R.id.llPrice);
        lllPrice = bottomSheetDialog.findViewById(R.id.lllPrice);
        llCategory = bottomSheetDialog.findViewById(R.id.llCategory);
        llDiscount = bottomSheetDialog.findViewById(R.id.llDiscount);
        llCollor = bottomSheetDialog.findViewById(R.id.llCollor);

        scrollable = bottomSheetDialog.findViewById(R.id.scrollable);
        llDicount = bottomSheetDialog.findViewById(R.id.scrollable1);

        // PriceCeckBox
        priceCB1 = bottomSheetDialog.findViewById(R.id.priceCB1);
        priceCB2 = bottomSheetDialog.findViewById(R.id.priceCB2);
        priceCB3 = bottomSheetDialog.findViewById(R.id.priceCB3);
        priceCB4 = bottomSheetDialog.findViewById(R.id.priceCB4);
        priceCB5 = bottomSheetDialog.findViewById(R.id.priceCB5);
        priceCB6 = bottomSheetDialog.findViewById(R.id.priceCB6);
        radioGroup = bottomSheetDialog.findViewById(R.id.radioGroup);

        //RedioButton
        rbone = bottomSheetDialog.findViewById(R.id.rbone);
        rbtwo = bottomSheetDialog.findViewById(R.id.rvtwo);
        rbthree = bottomSheetDialog.findViewById(R.id.rbthree);
        rbfour = bottomSheetDialog.findViewById(R.id.rvfour);
        rbfive = bottomSheetDialog.findViewById(R.id.rvfive);
        rvsix = bottomSheetDialog.findViewById(R.id.rvsix);
        rvseven = bottomSheetDialog.findViewById(R.id.rvseven);
        rveight = bottomSheetDialog.findViewById(R.id.rveight);
        rvnine = bottomSheetDialog.findViewById(R.id.rvnine);
        rvone = bottomSheetDialog.findViewById(R.id.rvone);


        //TextView
        tvFilter = bottomSheetDialog.findViewById(R.id.tvFilter);
        tvreset = bottomSheetDialog.findViewById(R.id.tvreset);

        Log.v("dkmxdkjf", String.valueOf(categoryList1));

        Log.v("priceArray", String.valueOf(priceArray));


        for (int nn = 0; nn < priceArray.size(); nn++) {
            try {
                Log.v("priceiddd", priceArray.get(nn).get("price_id"));

                int checked = Integer.parseInt(priceArray.get(nn).get("price_id"));

                if (checked == 1) {
                    priceCB1.setChecked(true);
                }

                if (checked == 2) {
                    priceCB2.setChecked(true);
                }

                if (checked == 3) {
                    priceCB3.setChecked(true);
                }

                if (checked == 4) {
                    priceCB4.setChecked(true);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        /*int dis = -1;
        try {


            Log.v("nsdfh", "help" + typeDiscount);
                        if (!typeDiscount.equalsIgnoreCase("")) {
                dis = Integer.parseInt(typeDiscount);
                ((RadioButton) radioGroup.getChildAt(dis)).setChecked(true);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/
/*
        String discount=AppSettings.getString( AppSettings.discount );
         Log.v( "nsdfh",discount );*/
        /* if(!discount.equalsIgnoreCase( "" )){
             dis=Integer.parseInt( discount );
             ((RadioButton)radioGroup.getChildAt( dis )).setChecked(true);
         }*/
        priceCB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string1));

                } else {
                    arrPricelist.remove(getString(R.string.string1));
                }
            }
        });
        priceCB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string2));

                } else {
                    arrPricelist.remove(getString(R.string.string2));
                }
            }
        });
        priceCB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string3));
                } else {
                    arrPricelist.remove(getString(R.string.string3));
                }
            }
        });
        priceCB4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string4));
                } else {
                    arrPricelist.remove(getString(R.string.string4));
                }
            }
        });

        priceCB5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string5));
                } else {
                    arrPricelist.remove(getString(R.string.string5));
                }
            }
        });
        priceCB6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrPricelist.add(getString(R.string.string6));
                } else {
                    arrPricelist.remove(getString(R.string.string6));
                }
            }
        });


        //FilterBitton Apply
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SimpleHTTPConnection.isNetworkAvailable()) {
                    //RadioGruopListner
                    int selectedIdBoundaryWall = radioGroup.getCheckedRadioButtonId();
                    View radioButtonBoundaryWall = bottomSheetDialog.findViewById(selectedIdBoundaryWall);
                    int idx = radioGroup.indexOfChild(radioButtonBoundaryWall);
                    radioIndex = idx;
                    Log.d("dsnfsd", Integer.toString(idx));
                    categoryList1.clear();
                    checkBoxAray.clear();
                    arrGetcolorList.clear();
                    Raaay.clear();
                    priceArray.clear();

                    hitFilterApi();

                    bottomSheetDialog.dismiss();
                } else {
                    AppUtils.showToastSort(getActivity(), getString(R.string.errorInternet));
                }
            }
        });


        bottomSheetDialog.show();
        llCollor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llColor.setVisibility(View.VISIBLE);
                llCollor.setVisibility(View.VISIBLE);
                llCategory.setVisibility(View.GONE);
                scrollable.setVisibility(View.VISIBLE);
                lllPrice.setVisibility(View.GONE);
                llDicount.setVisibility(View.GONE);
                llPrice.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llDiscount.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCategoryy.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCollor.setBackgroundColor(getResources().getColor(R.color.white));


            }
        });


        if (SimpleHTTPConnection.isNetworkAvailable()) {
            arrGetcolorList.clear();
            getColorApi();
        } else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }
        if (SimpleHTTPConnection.isNetworkAvailable()) {
            categoryList1.clear();
            llCategory.setVisibility(View.VISIBLE);
            scrollable.setVisibility(View.VISIBLE);

            getCategoryApiFilter();
        } else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }


        llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lllPrice.setVisibility(View.VISIBLE);
                llCategory.setVisibility(View.GONE);
                llCollor.setVisibility(View.VISIBLE);
                llColor.setVisibility(View.GONE);
                scrollable.setVisibility(View.VISIBLE);
                llDicount.setVisibility(View.GONE);
                llCategoryy.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llDiscount.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCollor.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llPrice.setBackgroundColor(getResources().getColor(R.color.white));


            }
        });

        llDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llDicount.setVisibility(View.VISIBLE);
                scrollable.setVisibility(View.GONE);
                lllPrice.setVisibility(View.GONE);
                llColor.setVisibility(View.GONE);
                llCategory.setVisibility(View.GONE);
                llCollor.setVisibility(View.VISIBLE);
                llPrice.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCollor.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCategoryy.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llDiscount.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });


        llCategoryy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llCategory.setVisibility(View.VISIBLE);
                llColor.setVisibility(View.GONE);
                scrollable.setVisibility(View.VISIBLE);
                lllPrice.setVisibility(View.GONE);
                llDicount.setVisibility(View.GONE);
                llCollor.setVisibility(View.VISIBLE);
                llPrice.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llDiscount.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCollor.setBackgroundColor(getResources().getColor(R.color.lightgrayy));
                llCategoryy.setBackgroundColor(getResources().getColor(R.color.white));


            }
        });

        tvreset.setTextColor(getResources().getColor(R.color.colorAccent));

        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                priceCB1.setChecked(false);
                priceCB2.setChecked(false);
                priceCB3.setChecked(false);
                priceCB4.setChecked(false);
                priceCB5.setChecked(false);
                priceCB6.setChecked(false);
                arrPricelist.clear();
                radioGroup.clearCheck();
                checkBoxAray.clear();
                Raaay.clear();
                priceArray.clear();

                FilterArrayList.clear();
                getCategoryApiFilter();
                arrGetcolorList.clear();
                arrColorList.clear();
                getColorApi();
                catalogueList.clear();
                paging = "0";

                getCatlogCollection();//Reset Filter Api
                categoryList1.clear();


            }
        });

        rllfilter.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
            }
        });


    }

    //getCategoryApiFilter
    private void getCategoryApiFilter() {

        AppUtils.hideSoftKeyboard(mActivity);
//        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.getCategory;
        Log.v("getCategoryApiFilterURL", url);

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .setTag("getCategoryApiFilter")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseGetCategoryJSONN(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    //parseGetCategoryJSON
    private void parseGetCategoryJSONN(JSONObject response) {
        categoryList1.clear();

        AppUtils.hideDialog();

        Log.d("getCategoryApiFilter", response.toString());

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
            if (jsonObject.getString("resCode").equals("1")) {

                JSONArray jsonArray = jsonObject.getJSONArray("category");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> hashlist = new HashMap();

                    hashlist.put("categoryId", arrayJSONObject.getString("categoryId"));
                    hashlist.put("categoryName", arrayJSONObject.getString("categoryName"));
                    hashlist.put("categoryIcon", arrayJSONObject.getString("categoryIcon"));

                    categoryList1.add(hashlist);
                }

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);

                CategoryFilterAdapter adapterr = new CategoryFilterAdapter(mActivity, categoryList1);

                rvCategory.setLayoutManager(layoutManager);

                rvCategory.setAdapter(adapterr);
                adapterr.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //getColorApi
    private void getColorApi() {

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.GetAllColors;
        Log.v("getColorApiFilterURL", url);

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .setTag("getColorApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseGetCategoryJSONNN(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    //parseGetCategoryJSON
    private void parseGetCategoryJSONNN(JSONObject response) {

        AppUtils.hideDialog();

        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
            if (jsonObject.getString("resCode").equals("1")) {

                JSONArray jsonArray = jsonObject.getJSONArray("colorList");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> hashlist = new HashMap();
                    hashlist.put("colorId", arrayJSONObject.getString("colorId"));
                    hashlist.put("name", arrayJSONObject.getString("name"));
                    hashlist.put("image", arrayJSONObject.getString("image"));

                    arrGetcolorList.add(hashlist);
                }

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);

                ColorAdapTer adapterr = new ColorAdapTer(mActivity, arrGetcolorList);

                rvColor.setLayoutManager(layoutManager);

                rvColor.setAdapter(adapterr);
                adapterr.notifyDataSetChanged();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //FilterApi
    private void hitFilterApi() {
        canFilter = true;
        canScroll = false;
        AppUtils.showRequestDialog(mActivity);
        Log.v("GetfiltListApi", AppUrls.FilterData);


        try {

            for (int ll = 0; ll < FilterArrayList.size(); ll++) {
                JSONObject jsonObjectCategory = new JSONObject();
                jsonObjectCategory.put("categoryId", FilterArrayList.get(ll));
                category.put(jsonObjectCategory);

            }

            for (int k = 0; k < arrPricelist.size(); k++) {
                JSONObject jsonObjectPrice = new JSONObject();
                jsonObjectPrice.put("price_id", arrPricelist.get(k));
                price.put(jsonObjectPrice);

            }

            for (int Color = 0; Color < arrColorList.size(); Color++) {
                JSONObject jsonObjectcolor = new JSONObject();
                jsonObjectcolor.put("colorId", arrColorList.get(Color));
                color.put(jsonObjectcolor);

            }

            if (radioIndex == -1) {
                maindata.put("discount", "");

            } else {
                maindata.put("discount", radioIndex);

            }


            maindata.put("userId", AppSettings.getString(AppSettings.userId));
            maindata.put("category", category);
            maindata.put("colors", color);
            maindata.put("price", price);
            maindata.put("pageindex", paging);
            result.put(AppConstants.projectName, maindata);
            Log.v("currentData", maindata.toString());

            Log.v("GetfiltListApi", String.valueOf(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppUrls.FilterData)
                .addJSONObjectBody(result)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        AppUtils.hideDialog();

                        Log.v("GetfiltListApi", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
                            if (jsonObject.getString("resCode").equals("1")) {
                                canScroll = true;


                                tvnofilter.setVisibility(View.GONE);


                                JSONArray jsonArray = jsonObject.getJSONArray("catalogs");

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
                                    catalogueList.add(hashlist);

                                    JSONArray jsonArray1 = arrayJSONObject.getJSONArray("thumbnails");

                                    for (int j = 0; j < jsonArray1.length(); j++) {

                                        JSONObject jsonArrayJSONObject = jsonArray1.getJSONObject(j);

                                        HashMap<String, String> hashlist1 = new HashMap();

                                        hashlist1.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                                        arrImagList.add(hashlist1);

                                    }
                                }
                                if (jsonObject.has("pageindex")) {
                                    paging = jsonObject.getString("pageindex");
                                }

                                JSONObject jsonObject1 = jsonObject.getJSONObject("FilterData");


                                AppSettings.putString(AppSettings.discount, jsonObject1.getString("discount"));


                                JSONArray jsonArray9 = jsonObject1.getJSONArray("category");


                                for (int p12 = 0; p12 < jsonArray9.length(); p12++) {

                                    JSONObject jsonObject2 = jsonArray9.getJSONObject(p12);
                                    HashMap<String, String> hashlis2 = new HashMap();
                                    hashlis2.put("categoryId", jsonObject2.getString("categoryId"));
                                    checkBoxAray.add(hashlis2);

                                }
                                JSONArray colors = jsonObject1.getJSONArray("colors");

                                for (int llo = 0; llo < colors.length(); llo++) {
                                    JSONObject jsonObject8 = colors.getJSONObject(llo);
                                    HashMap<String, String> hashlis6 = new HashMap();
                                    hashlis6.put("colorId", jsonObject8.getString("colorId"));
                                    Raaay.add(hashlis6);
                                }

                                Log.v("Mychecks", checkBoxAray.toString());
                                typeDiscount = jsonObject1.getString("discount");
                                JSONArray jsonArray3 = jsonObject1.getJSONArray("price");


                                for (int k = 0; k < jsonArray3.length(); k++) {
                                    JSONObject jsonObject2 = jsonArray3.getJSONObject(k);
                                    HashMap<String, String> hashlis3 = new HashMap();
                                    hashlis3.put("price_id", jsonObject2.getString("price_id"));
                                    priceArray.add(hashlis3);
                                }


                                adapterr = new CatalogueAdapter(mActivity, catalogueList);
                                recyclerView.setAdapter(adapterr);
                                adapterr.notifyDataSetChanged();
                                AppUtils.hideDialog();
                            } else {
                                canScroll = false;

                                JSONObject jsonObject4 = jsonObject.getJSONObject("FilterData");

                                AppSettings.putString(AppSettings.discount, jsonObject4.getString("discount"));


                                JSONArray jsonArray4 = jsonObject4.getJSONArray("category");

                                for (int p13 = 0; p13 < jsonArray4.length(); p13++) {

                                    JSONObject jsonObject2 = jsonArray4.getJSONObject(p13);
                                    HashMap<String, String> hashlis4 = new HashMap();
                                    hashlis4.put("categoryId", jsonObject2.getString("categoryId"));
                                    checkBoxAray.add(hashlis4);

                                }


                                Log.v("Mychecks", checkBoxAray.toString());

                                JSONArray colors = jsonObject4.getJSONArray("colors");

                                for (int lo = 0; lo < colors.length(); lo++) {
                                    JSONObject jsonObject8 = colors.getJSONObject(lo);
                                    HashMap<String, String> hashlis6 = new HashMap();
                                    hashlis6.put("colorId", jsonObject8.getString("colorId"));
                                    Raaay.add(hashlis6);
                                }


                                typeDiscount = jsonObject4.getString("discount");
                                JSONArray jsonArray5 = jsonObject4.getJSONArray("price");


                                for (int l = 0; l < jsonArray5.length(); l++) {
                                    JSONObject jsonObject6 = jsonArray5.getJSONObject(l);
                                    HashMap<String, String> hashlis5 = new HashMap();
                                    hashlis5.put("price_id", jsonObject6.getString("price_id"));
                                    priceArray.add(hashlis5);
                                }

                                Toast.makeText(mActivity, "No Data Found", Toast.LENGTH_LONG).show();
                                tvnofilter.setVisibility(View.VISIBLE);
                                simpleProgressBar.setVisibility(View.GONE);

                                downProgress.setVisibility(View.GONE);

                                AppUtils.hideDialog();
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
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }

    class RemindTask extends TimerTask {
        private int numberOfPages;
        private ViewPager mViewPager;
        private int page = 0;

        public RemindTask(int numberOfPages, ViewPager mViewPager) {
            this.numberOfPages = numberOfPages;
            this.mViewPager = mViewPager;
        }

        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (page > numberOfPages) { // In my case the number of pages are 5
                        mViewPager.setCurrentItem(0);
                        page = 0;
                    } else {
                        mViewPager.setCurrentItem(page++);
                    }
                }
            });

        }
    }

    //CategoryFilterAdapter
    private class ColorAdapTer extends RecyclerView.Adapter<holderColor> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> dataCheck = new ArrayList<HashMap<String, String>>();

        public ColorAdapTer(Activity mActivity, ArrayList<HashMap<String, String>> arrGetcolorList) {
            data = arrGetcolorList;
        }

        public holderColor onCreateViewHolder(ViewGroup parent, int viewType) {
            return new holderColor(LayoutInflater.from(parent.getContext()).inflate(R.layout.colorlayout, parent, false));
        }


        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final holderColor holderColor, final int position) {


            if (arrColorList.contains(data.get(position).get("colorId")))

                holderColor.checkBox.setChecked(true);

            else {

                holderColor.checkBox.setChecked(false);

            }


            holderColor.checkBox.setText(data.get(position).get("name"));
            Picasso.get().load(data.get(position).get("image")).resize(400, 600).into(holderColor.ivcircle);

            holderColor.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.v("CheckBoxTester", buttonView.getText().toString());

                        arrColorList.add(data.get(position).get("colorId"));


                    } else {

                        arrColorList.remove(data.get(position).get("colorId"));


                    }
                    Log.v("CheckBoxTester", arrColorList.toString());
                }
            });
        }


        public int getItemCount() {
            return data.size();
        }
    }

    public class holderColor extends RecyclerView.ViewHolder {


        //TextView
        TextView tvfilter;
        // CheckBox
        CheckBox checkBox;
        ImageView ivcircle;


        public holderColor(View itemView) {
            super(itemView);

            //TextView
            tvfilter = itemView.findViewById(R.id.tvfilter);
            checkBox = itemView.findViewById(R.id.checkBox);
            ivcircle = itemView.findViewById(R.id.ivcircle);


        }
    }

    //ShareNowWhatspDownloader
    class DownloadFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (pos == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(getActivity());
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

                byte[] data = new byte[1024];
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
            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + a++ + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("MYImagesSize", String.valueOf(imageUriArray.size()));
            Log.v("MYImagesSize", String.valueOf(arrImagList.size()));
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

            Sharenow(pos);


        }

    }

    //DownloadFromURLOther
    class DownloadFromURLOther extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (other == 0) {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage(getString(R.string.pleasewait));
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
                String file = String.valueOf(new File(file1, "name" + n++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] data = new byte[1024];
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


            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + n++ + ".jpeg");

            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));

            Log.v("MYImagesSize", String.valueOf(imageUriArray.size()));

            Log.v("MYImagesSize", String.valueOf(arrImagList.size()));

            imageFileToShare.length();

            Log.v("mdf", "image= " + imageFileToShare);

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        Log.v("imageUriCheck", imageUriArray.toString());
                        m.invoke(null);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        intent.setType("text/plain");
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                        intent.setType("image/jpeg");
                        startActivity(Intent.createChooser(intent, "send"));
                        mProgressDialog.dismiss();
                        Toast.makeText(mActivity, getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }


                }
            }
            other = other + 1;
            OtherShare(other);


        }


    }

    //DownloadFromURLDownloader
    class DownloadFromURLDownloader extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (downlod == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage(getString(R.string.imageDownload));
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
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
                String file = String.valueOf(new File(file1, "home" + dd++ + ".jpeg"));
                /*String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );*/
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{(file)}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                byte[] data = new byte[1024];
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

                /*if (imageUriArray.size() > 0) {

                    int result = (100 /imageUriArray.size())*(imageUriArray.size()+1);

                    Log.d("result", String.valueOf(result));
                    publishProgress(""+result);
                    //mProgressDialog.setProgress(result);
                }*/

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(0);
            Log.v("dqkjbdq", String.valueOf(imageUriArray.size()));
            Log.v("dqkjbdq2", String.valueOf(arrImagFaList.size()));
            int incProgress = 100 / arrImagFaList.size() + 1;
            int imageUriArrayy = imageUriArray.size() + 1;

            for (int i = 0; i < imageUriArrayy; i++) {
                // Do stuff
                mProgressDialog.incrementProgressBy(incProgress);
            }

            if (imageUriArray != null) {

                mProgressDialog.setMessage(getString(R.string.downloading) + (imageUriArrayy + 0) + "/" + arrImagFaList.size());
            }
        }

        @Override
        protected void onPostExecute(String file_url) {

            File imageFileToShare = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Zozima", "home" + dd++ + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("MYImagesSize", String.valueOf(imageUriArray.size()));
            Log.v("MYImagesSize", String.valueOf(arrImagList.size()));
            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);
            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                        mProgressDialog.incrementProgressBy(100);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                mProgressDialog.dismiss();
                                Toast.makeText(mActivity, getString(R.string.catlogeimagedownlod), Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);

                    } catch (Exception e) {

                        e.printStackTrace();

                        mProgressDialog.dismiss();
                    }


                }
            }

            downlod = downlod + 1;
            Download(downlod);


        }


    }

    //DownloadFromURLFaceBook
    class DownloadFromURLFaceBook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (facebook == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(getActivity());
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
                String file = String.valueOf(new File(file1, "name" + f++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] data = new byte[1024];
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
            File imageFileToShare = new File(Environment.getExternalStorageState() + "/Zozima", "name" + f++ + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("MYImagesSize", String.valueOf(imageUriArray.size()));
            Log.v("MYImagesSize", String.valueOf(arrImagList.size()));
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

    //CategoryFilterAdapter
    private class CategoryFilterAdapter extends RecyclerView.Adapter<holdercat> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> dataCheck = new ArrayList<HashMap<String, String>>();

        public CategoryFilterAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public holdercat onCreateViewHolder(ViewGroup parent, int viewType) {
            return new holdercat(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatecategory, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final holdercat holdercat, final int position) {


            if (FilterArrayList.contains(data.get(position).get("categoryId")))

                holdercat.checkBox.setChecked(true);
            else {
                holdercat.checkBox.setChecked(false);
            }
            holdercat.checkBox.setText(data.get(position).get("categoryName"));

            holdercat.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.v("CheckBoxTester", buttonView.getText().toString());

                        FilterArrayList.add(data.get(position).get("categoryId"));


                    } else {

                        FilterArrayList.remove(data.get(position).get("categoryId"));


                    }
                    Log.v("ArrayListCheck", FilterArrayList.toString());
                }
            });


        }


        public int getItemCount() {
            return data.size();
        }
    }

    public class holdercat extends RecyclerView.ViewHolder {

        //TextView
        TextView tvfilter;
        // CheckBox
        CheckBox checkBox;


        public holdercat(View itemView) {
            super(itemView);

            //TextView
            tvfilter = itemView.findViewById(R.id.tvfilter);
            checkBox = itemView.findViewById(R.id.checkBox);


        }
    }

    //CategoryAdapter
    private class CategoryAdapter extends RecyclerView.Adapter<HolderCatlog> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public CategoryAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public HolderCatlog onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HolderCatlog(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_category, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final HolderCatlog holderCatlog, final int position) {
            final String categoryId = (data.get(position).get("categoryId"));

            holderCatlog.tvName.setText(data.get(position).get("categoryName"));
            if (!data.get(position).get("categoryIcon").isEmpty()) {
                Picasso.get().load(data.get(position).get("categoryIcon")).placeholder(R.mipmap.logo_grey).resize(400, 600).into(holderCatlog.ivCategory);
            }

            holderCatlog.cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SubCategoryActivity.class);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("status", data.get(position).get("status"));
                    startActivity(intent);


                }
            });


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class HolderCatlog extends RecyclerView.ViewHolder {
        //TextView
        TextView tvName;
        //CardView
        CardView cardView;
        //ImageView
        ImageView ivCategory, ic_fav_unselected;

        public HolderCatlog(View itemView) {
            super(itemView);
            //CardView
            cardView = itemView.findViewById(R.id.cardView);

            //TextView
            tvName = itemView.findViewById(R.id.tvName);

            //ImageView
            ivCategory = itemView.findViewById(R.id.ivCategory);
            ic_fav_unselected = itemView.findViewById(R.id.ivFavourite);

        }
    }
/*
    @Override
    public void onResume() {

        super.onResume();
        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {

            catalogueList.clear();
            getCatlogCollection();

        } else {

            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }

    }*/

    //CatalogueAdapter
    private class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.CatalogueHolder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();


        public CatalogueAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;

        }

        public CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new CatalogueAdapter.CatalogueHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_catalogue, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CatalogueAdapter.CatalogueHolder holder, final int position) {
            final String statusu = (data.get(position).get("status"));
            String name = (data.get(position).get("name"));


            if (name.contains("Combo Pack")) {
                String camboname = name.substring(0, 10);
                String namevalue = name.substring(11);
                holder.tvName.setText(namevalue.trim());
                holder.tvCambo.setText(camboname);
            } else {
                holder.tvCambo.setVisibility(View.GONE);
                holder.tvName.setText(name.trim());
            }


            Log.v("fkdf", data.get(position).get("status"));

            if (statusu.equals("1")) {

                holder.ivFavourite.setImageResource(R.drawable.ic_fav_selected);

            } else if (statusu.equals("2")) {

                holder.ivFavourite.setImageResource(R.drawable.ic_fav_unselected);
            }

            String prod_price = data.get(position).get("prod_price");
            Log.v("fkdsfk", prod_price);

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

                holder.shipincharge.setText(getString(R.string.shipping) + " " + getString(R.string.rupaye) + shipingCharge);

            } else {

                holder.llShippingCharge.setVisibility(View.VISIBLE);

            }


            int counter = Integer.parseInt(data.get(position).get("productCount"));
            if (counter <= 2) {
                holder.tvCount.setVisibility(View.INVISIBLE);
            } else
                holder.tvCount.setVisibility(View.VISIBLE);

            holder.tvCount.setText("+" + (Integer.parseInt(data.get(position).get("productCount")) - 2));


            try {

                JSONArray jsonArray = new JSONArray(data.get(position).get("imageArray"));

                for (int ii = 0; ii < jsonArray.length(); ii++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(ii);

                    if (ii == 0) {

                        Picasso.get().load(jsonObject.getString("productThumbnil")).placeholder(R.mipmap.logo_grey).resize(400, 600).into(holder.ivImage);

                    }
                    if (ii == 1) {

                        Picasso.get().load(jsonObject.getString("productThumbnil")).placeholder(R.mipmap.logo_grey).resize(400, 600).into(holder.ivImage2);


                    }

                    if (ii == 2) {
                        Picasso.get().load(jsonObject.getString("productThumbnil"))
                                .placeholder(R.mipmap.logo_grey).resize(400, 600).into(holder.ivImage3);
                    }

                }

            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.v("sasd", String.valueOf(AppSettings.getString(AppSettings.status).equals("1")));

            holder.rlFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SimpleHTTPConnection.isNetworkAvailable(getActivity())) {

                        addFavApi(data.get(position).get("catalogId"), holder.ivFavourite);

                    } else {
                        AppUtils.showToastSort(getActivity(), getString(R.string.errorInternet));
                    }

                }
            });

            //sharing multiple image button////////
            holder.lllWhatsapp.setOnClickListener(new View.OnClickListener() {

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
                    Sharenow(pos);
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
                    OtherShare(other);
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

                            HashMap<String, String> hashlist4 = new HashMap();

                            hashlist4.put("images", jsonArrayJSONObject.getString("productThumbnil"));

                            arrImagFaList.add(hashlist4);

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
                    Log.v("sdjfsdj", statusu);
                    Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                    intent.putExtra("catalogId", AppSettings.putString(AppSettings.catalogId, data.get(position).get("catalogId")));

                    intent.putExtra("name", data.get(position).get("name"));
                    intent.putExtra("description", data.get(position).get("description"));
                    intent.putExtra("status", data.get(position).get("status"));
                    startActivity(intent);

                }
            });

        }


        public int getItemCount() {
            return data.size();
        }

        public class CatalogueHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvCount, tvPrice, tvOriginalPrice, tvDiscount, tvCambo;
            //CardView
            CardView cardView;
            //ImageView
            ImageView ivImage, ivImage2, ivImage3, ivFavourite;
            //RelativeLayout
            RelativeLayout rlFavourite;
            //LinearLayout
            LinearLayout llDownload, llFacebook, llShare, lllWhatsapp;
            LinearLayout llShippingCharge;
            TextView shipincharge;
            LinearLayout layout_shiping;
            ImageView ivshipping;
            /////////////
            private Bitmap bitmap = null;

            public CatalogueHolder(View inflate) {

                super(inflate);

                cardView = itemView.findViewById(R.id.cardView);
                //TextView
                tvName = itemView.findViewById(R.id.tvName);

                tvPrice = itemView.findViewById(R.id.tvPrice);

                tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
                layout_shiping = itemView.findViewById(R.id.layout_shiping);
                ivshipping = itemView.findViewById(R.id.ivshipping);
                shipincharge = itemView.findViewById(R.id.shipincharge);
                tvDiscount = itemView.findViewById(R.id.tvDiscount);
                tvCambo = itemView.findViewById(R.id.tvCambo);
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

                lllWhatsapp = itemView.findViewById(R.id.llWhatsapp);

                llShippingCharge = itemView.findViewById(R.id.layout_shiping);


            }

        }


    }

}






