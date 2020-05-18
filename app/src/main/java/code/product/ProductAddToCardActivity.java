package code.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
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
import java.util.List;

import code.address.ShippingAddress;
import code.basic.BannerAddapter;
import code.basic.CartAcivity;
import code.basic.CheckAvailabilityAcivity;
import code.basic.WishlistAcitvity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.entity.ProductImage;
import code.fragment.HomeFragment;
import code.profile.EditAddressActivity;
import code.searching.SearchActivity;
import code.subcatageory.SubCategoryActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;
import de.hdodenhof.circleimageview.CircleImageView;

import static code.common.AppSignatureHashHelper.TAG;
import static code.utils.AppConstants.addressNewList;


public class ProductAddToCardActivity extends BaseActivity {

    //ImageView
    ImageView ivbackk, ivImage, decrIvv, ivIncreIvv, ivwhatsp, decrIv, increIv, ivcart, ivshipping, ivFavourite, ivSearch, ivloader;

    //CustomTextView
    TextView tvPincode, tvName, tvPrice, tvAdtocart, tvSharenow, tvPincodedelivery, tvShipincharge, tvCopy;

    //TextView
    TextView tvsku, tvskuu, ivZozima, tvNameee, tvCount, tvPricee, tvOriginalPricee, tvDiscountt, countTvv, tvproductetails, layout_shiping, tvDeliverymsg;

    //LinearLayout
    LinearLayout lladdTocart, llshare, llcheckout, llShsaDare, llShsaDarre, llchek, llShippingCharge, llreturn;

    //RecyclerView
    RecyclerView recv_size, recv_bottomsize;
    String name;
    LinearLayout llcontoniue;


    Dialog dialog;

    RecyclerView recyclerViewColor;

    //ArrayList
    private ArrayList<HashMap<String, String>> arrSelectSize = new ArrayList<>();


    RecyclerView.Adapter<ViewHolder> adapter;


    //ProgresDailog
    ProgressDialog mpProgress;
    ProgressDialog mProgressDialog;


    Intent intGetVal;
    //String Value
    String value = String.valueOf(1);
    String size, image;
    String prodct_id;
    String Id, addedById, addedType, description;

    //int value
    int n1 = 1;
    int row_index;
    int pos = 0,p = 0,a=0,other=0;


    String discountValues = "", finalPrices = "", couponId = "", copouncode = "";
    TextView tvDiscountvalue;


    RelativeLayout tvavailable;

    TextView tvcopouncode;
    CirclePageIndicator indicator;
    ViewPager viewPager;
    private ArrayList<HashMap<String, String>> arrColorList = new ArrayList<>();

    private ArrayList<HashMap<String, String>> arrImagList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrImagFaList = new ArrayList<>();

    //ArrayList
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add_to_card);

        //IamgeView
        ivbackk = findViewById(R.id.back);
        ivImage = findViewById(R.id.ivImage);
        ivwhatsp = findViewById(R.id.ivwhatsp);
        ivZozima = findViewById(R.id.ivZozima);
        decrIvv = findViewById(R.id.decrIv);
        ivIncreIvv = findViewById(R.id.increIv);
        ivshipping = findViewById(R.id.ivshipping);
        ivcart = findViewById(R.id.ivcart);
        ivFavourite = findViewById(R.id.ivFavourite);
        ivSearch = findViewById(R.id.ivSearch);
        tvavailable = findViewById(R.id.tvavailable);
        tvDiscountvalue = findViewById(R.id.tvDiscountvalue);
        tvsku = findViewById(R.id.tvsku);
        tvskuu = findViewById(R.id.tvskuu);
        //ViewPager
        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        recyclerViewColor=findViewById(R.id.rvColor);



        intGetVal = getIntent();

        //Loader
        ivloader = findViewById(R.id.ivloader);


        //RecyclerView
        recv_size = findViewById(R.id.recv_size);


        //TextView
        tvproductetails = findViewById(R.id.tvproductetails);
        tvCount = findViewById(R.id.tvCount);
        tvPricee = findViewById(R.id.tvPrice);
        tvOriginalPricee = findViewById(R.id.tvOriginalPrice);
        tvDiscountt = findViewById(R.id.tvDiscount);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvNameee = findViewById(R.id.tvName);
        countTvv = findViewById(R.id.countTv);
        tvCopy = findViewById(R.id.copy);
        tvDeliverymsg = findViewById(R.id.deliverymsg);
        tvPincode = findViewById(R.id.pincode);
        tvShipincharge = findViewById(R.id.shipincharge);
        tvAdtocart = findViewById(R.id.addtocart);
        tvSharenow = findViewById(R.id.sharenow);
        tvPincodedelivery = findViewById(R.id.pincodedelivery);
        tvcopouncode = findViewById(R.id.tvcopouncode);

        //LinearLayout
        lladdTocart = findViewById(R.id.lladdToCard);
        llshare = findViewById(R.id.lldShare);
        llchek = findViewById(R.id.llchek);

        llShsaDare = findViewById(R.id.llShsaDare);
        llShsaDarre = findViewById(R.id.llShsaDarre);

        llcheckout = findViewById(R.id.checkout);
        llShippingCharge = findViewById(R.id.layout_shiping);
        llreturn = findViewById(R.id.llreturn);
        couponId = AppSettings.getString(AppSettings.couponChoosen);
        Log.v("value", couponId);

        AppSettings.putString(AppSettings.pincodedelivery, tvPincodedelivery.getText().toString());
        Log.v("djadj", AppSettings.getString(AppSettings.pincodedelivery));


        llchek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, CartAcivity.class));
            }
        });

        ivcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, CartAcivity.class));
            }
        });
        ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, WishlistAcitvity.class));
            }
        });

        tvavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, ApplyCoupon.class);
                intent.putExtra("qty", value);
                intent.putExtra("Id", Id);
                intent.putExtra("addedType", addedType);
                intent.putExtra("addedById", addedById);
                intent.putExtra(AppSettings.couponChoosen, couponId);
                startActivity(intent);
            }
        });


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra(AppSettings.productName, name);
                startActivity(intent);
            }
        });
        ivbackk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CheckAvailabilityAcivity.class);
                intent.putExtra("qty", value);
                intent.putExtra("Id", Id);
                intent.putExtra("addedType", addedType);
                intent.putExtra("addedById", addedById);
                startActivity(intent);
            }
        });
        tvPincodedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CheckAvailabilityAcivity.class);
                intent.putExtra("qty", value);
                intent.putExtra("Id", Id);
                intent.putExtra("addedType", addedType);
                intent.putExtra("addedById", addedById);
                startActivity(intent);
            }
        });


      /*  ivZozima.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mActivity, MainActivity.class );
                intent.putExtra( "pagePath", 1 );
                startActivity( intent );
            }
        } );*/
        llreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mActivity);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setCancelable(true);


                dialog.setContentView(R.layout.return_policy);

                dialog.show();

                TextView tvok = (TextView) dialog.findViewById(R.id.tvok);


                tvok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        ///////////Intent///////////////////////////////////
        Intent intent = getIntent();
        if (intent.hasExtra("resCode")) {

            String pincodedeliveryy = intent.getStringExtra("pincode");

            value = intent.getStringExtra("qty");
            Id = intent.getStringExtra("Id");
            addedById = intent.getStringExtra("addedById");
            addedType = intent.getStringExtra("addedType");

            Log.v("ghgh", value);

            countTvv.setText(value);

            tvPincodedelivery.setText(pincodedeliveryy);

            String resCode = intent.getStringExtra("resCode");

            if (resCode.equals("1")) {

                Log.v("zndjnsd", resCode);


                tvDeliverymsg.setText("   " + "Product Delivery is Available ");

                tvDeliverymsg.setTextColor(getResources().getColor(R.color.green));

                tvPincode.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {

                tvPincode.setTextColor(getResources().getColor(R.color.colorAccent));
                tvDeliverymsg.setText("   " + "Delivery not Available at pincode ");
                tvDeliverymsg.setTextColor(getResources().getColor(R.color.red));

            }
        }


        llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = 0;
                imge( pos );
                sharednowApi();
                //   Toast.makeText(this, ""+imageList.get(0).get("tvName"), Toast.LENGTH_SHORT).show();
            }
        });
        llShsaDare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = 0;
                imge( pos );
                sharednowApi();
                //   Toast.makeText(this, ""+imageList.get(0).get("tvName"), Toast.LENGTH_SHORT).show();

            }
        });
        llShsaDarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//   Toast.makeText(this, ""+imageList.get(0).get("tvName"), Toast.LENGTH_SHORT).show();
                other = 0;
                other( other );

            }
        });


        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", description);
                Toast.makeText(mActivity, getString(R.string.catalogedescriptioncopied), Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);

            }
        });


        decrIvv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                n1 = Integer.parseInt(countTvv.getText().toString());

                if (n1 > 1) {
                    n1 = n1 - 1;
                    value = String.valueOf(n1);
                    countTvv.setText(value);

                }

            }
        });
        ivIncreIvv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                n1 = Integer.parseInt(countTvv.getText().toString());

                n1 = n1 + 1;

                value = String.valueOf(n1);

                countTvv.setText(value);

            }
        });

        lladdTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity);
                bottomSheetDialog.setContentView(R.layout.addtocartdailogbox);
                /*decrIv = (ImageView) bottomSheetDialog.findViewById( R.id.decrIv );
                increIv = (ImageView) bottomSheetDialog.findViewById( R.id.increIv );
                countTv = (TextView) bottomSheetDialog.findViewById( R.id.countTv );*/
                llcontoniue = (LinearLayout) bottomSheetDialog.findViewById(R.id.llcontoniue);
                recv_bottomsize = (RecyclerView) bottomSheetDialog.findViewById(R.id.recv_bottomsize);
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mActivity);
                layoutManager.setFlexWrap(FlexWrap.WRAP);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setAlignItems(AlignItems.STRETCH);
                recv_bottomsize.setLayoutManager(layoutManager);
                recv_bottomsize.setAdapter(adapter);
/*
                decrIv.setOnClickListener( new View.OnClickListener() {

                    public void onClick(View v) {
                        n = Integer.parseInt( countTv.getText().toString() );
                        if (n > 1) {
                            n = n - 1;
                            value = String.valueOf( n );
                            countTv.setText( value );
                            countTvv.setText( value );

                        }

                    }
                } );
*/
/*
                increIv.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        n = n + 1;
                        value = String.valueOf( n );
                        countTv.setText( value );
                        countTvv.setText( value );


                    }
                } );
*/

                bottomSheetDialog.show();
                llcontoniue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llcheckout.setVisibility(View.VISIBLE);


                        if (cartcheck())
                            hitAddToCart();
                        else
                            showSupplier();
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });


        adapter = new RecyclerView.Adapter<ViewHolder>() {
            private int selectedPosition = -1;

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selectsize, null));
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
                Log.v("arraysize", Integer.toString(i) + " " + arrSelectSize.get(i).get("unit_name"));
                viewHolder.tvselectsize.setText(arrSelectSize.get(i).get("unit_name"));

                viewHolder.tvselectsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        row_index = i;

                        notifyDataSetChanged();
                    }

                });
                if (row_index == i) {
                    size = arrSelectSize.get(i).get("unit_name");

                    viewHolder.tvselectsize.setBackgroundResource(R.drawable.selectsizebackground);
                } else {
                    viewHolder.tvselectsize.setBackgroundResource(R.drawable.background);
                }

            }


            @Override
            public int getItemCount() {
                return arrSelectSize.size();
            }
        };


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvselectsize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvselectsize = itemView.findViewById(R.id.selectsize);
            this.setIsRecyclable(false);
        }
    }


    //hitAddToCart()
    private void hitAddToCart() {
        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);
        String url = AppUrls.AddToCart;
        Log.v("getSabCategoryApi-URL", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            jsonObject.put("productId", AppSettings.getString(AppSettings.productId));
            jsonObject.put("couponId", couponId);
            jsonObject.put("quantity", value);
            jsonObject.put("size", size);
            jsonObject.put("addedById", addedById);
            jsonObject.put("addedType", addedType);
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
                        Log.v("ndnsdfsd", response.toString());
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            String resCode = jsonObject1.getString("resCode");
                            String resMsg = jsonObject1.getString("resMsg");
                            int total_count = Integer.parseInt(jsonObject1.getString("total_count"));

                            Log.v("dsdhs", String.valueOf(total_count));

                            AppSettings.putString(AppSettings.addedById, jsonObject1.getString("addedById"));
                            AppSettings.putString(AppSettings.addedType, jsonObject1.getString("addedType"));

                            if (resCode.equals("1")) {
                                tvCount.setVisibility(View.VISIBLE);
                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                                tvCount.setText(jsonObject1.getString("total_count"));
                                AppSettings.putString(AppSettings.total_count, String.valueOf(total_count));


                            } else {


                                AppSettings.putString(AppSettings.total_count, "0");

                            }

                        } catch (JSONException e) {

                            AppSettings.putString(AppSettings.total_count, "0");

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

    //OnResume
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
        if (SimpleHTTPConnection.isNetworkAvailable()) {
            arrSelectSize.clear();
            GetProductDetail();


        } else {
            Toast.makeText(mActivity, R.string.errorInternet, Toast.LENGTH_SHORT).show();
        }


    }


    //pop up updatepopup
    public void showSupplier() {

        dialog = new Dialog(mActivity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);


        dialog.setContentView(R.layout.inflate_supplier);

        dialog.show();

        TextView tvcancel = (TextView) dialog.findViewById(R.id.tvcancel);
        TextView tvyes = (TextView) dialog.findViewById(R.id.tvyes);


        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tvyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hitAddToCart();
                dialog.dismiss();

            }
        });


    }

    public Boolean cartcheck() {

        String addedid = AppSettings.getString(AppSettings.addedById);
        String addedtype = AppSettings.getString(AppSettings.addedType);
        Log.v("checkMyCart", "a " + addedid);
        Log.v("checkMyCart", "b " + addedtype);
        Log.v("checkMyCart", "c " + addedById);
        Log.v("checkMyCart", "d " + addedType);
        if (addedid.equalsIgnoreCase("") && addedtype.equalsIgnoreCase("")) {

            return true;
        } else if (addedid.equalsIgnoreCase(addedById) && addedtype.equalsIgnoreCase(addedType)) {
            return true;
        } else
            return false;

    }


    class DownloadFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (pos == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog( ProductAddToCardActivity.this );
                // Set progressdialog title
                mProgressDialog.setTitle(getString(R.string.pleasewait));

                mProgressDialog.setMessage(getString(R.string.loadingimage));
                // Show progressdialog
                mProgressDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v( "justify", "i" );
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File( root + "/Zozima" );
                String file = String.valueOf( new File( file1, "name" + p++ + ".jpeg" ) );
//                String file = String.valueOf( new File( file1, "name" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL( fileUrl[0] );
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream( url.openStream(), 8192 );
                OutputStream outputStream = new FileOutputStream( file );

                byte data[] = new byte[1024];
                int total=0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / fileLength));

                    // writing data to file
                    outputStream.write(data, 0, count);
                }

                // flushing output
                outputStream.flush();

                // closing streams
                outputStream.close();
                inputStream.close();
                return "Downloaded at: " + root + file1;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }



        @Override
        protected void onPostExecute(String file_url) {

//            Toast.makeText( mActivity, "Sharing Images...", Toast.LENGTH_SHORT ).show();
//            mProgressDialog.dismiss();
            File imageFileToShare = new File( Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + Integer.toString( a++ ) + ".jpeg" );
            imageUriArray.add( Uri.fromFile( new File( String.valueOf( imageFileToShare ) ) ) );
            Log.v( "MYImagesSize", String.valueOf( imageUriArray.size() ) );

            imageFileToShare.length();
            Log.v( "mdf", "image= " + imageFileToShare );

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {

                    try {
                        Method m = StrictMode.class.getMethod( "disableDeathOnFileUriExposure" );
                        m.invoke( null );
                        Intent intent = new Intent();
                        intent.setAction( Intent.ACTION_SEND );
                        intent.setType( "text/plain" );
                        intent.putParcelableArrayListExtra( Intent.EXTRA_STREAM, imageUriArray );
                        intent.setType( "image/jpeg" );
                        intent.setPackage( "com.whatsapp" );
                        Toast.makeText( mActivity, getString(R.string.sharingimage), Toast.LENGTH_SHORT ).show();
                        mProgressDialog.dismiss();
                        startActivity( intent );
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }
                }
//


            }

            pos = pos + 1;

            imge( pos );


        }

    }





    class DownloadFromURLl extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (other == 0) {
                // Create a progressdialog
                mpProgress = new ProgressDialog( ProductAddToCardActivity.this );
                // Set progressdialog title
                mpProgress.setTitle(getString(R.string.pleasewait));

                mpProgress.setMessage(getString(R.string.loadingimage));
                // Show progressdialog
                mpProgress.show();
            }

        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {
                Log.v( "justify", "i" );
                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File( root + "/Zozima" );
                String file = String.valueOf( new File( file1, "name" + p++ + ".jpeg" ) );
//                String file = String.valueOf( new File( file1, "name" + ".jpeg" ) );
                file1.mkdirs();
                URL url = new URL( fileUrl[0] );
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream( url.openStream(), 8192 );
                OutputStream outputStream = new FileOutputStream( file );

                byte data[] = new byte[1024];
                int total=0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / fileLength));

                    // writing data to file
                    outputStream.write(data, 0, count);
                }

                // flushing output
                outputStream.flush();

                // closing streams
                outputStream.close();
                inputStream.close();
                return "Downloaded at: " + root + file1;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mpProgress.setProgress(Integer.parseInt(progress[0]));
        }



        @Override
        protected void onPostExecute(String file_url) {

//            Toast.makeText( mActivity, "Sharing Images...", Toast.LENGTH_SHORT ).show();
//            mProgressDialog.dismiss();
            File imageFileToShare = new File( Environment.getExternalStorageDirectory().toString() + "/Zozima", "name" + Integer.toString( a++ ) + ".jpeg" );
            imageUriArray.add( Uri.fromFile( new File( String.valueOf( imageFileToShare ) ) ) );
            Log.v( "MYImagesSize", String.valueOf( imageUriArray.size() ) );

            imageFileToShare.length();
            Log.v( "mdf", "image= " + imageFileToShare );

            if (arrImagFaList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {

                    try {
                        Method m = StrictMode.class.getMethod( "disableDeathOnFileUriExposure" );
                        m.invoke( null );
                        Intent intent = new Intent();
                        intent.setAction( Intent.ACTION_SEND_MULTIPLE );
                        intent.putParcelableArrayListExtra( Intent.EXTRA_STREAM, imageUriArray );
                        intent.setType("image/jpeg");
                        startActivity(Intent.createChooser(intent, "send"));
                        mpProgress.dismiss();
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                        mpProgress.dismiss();
                    }
                }
//


            }

            other = other + 1;

            other( other );


        }

    }



    private void GetProductDetail() {
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);
        String url = AppUrls.ProductDetails;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("productId", AppSettings.getString(AppSettings.productId));
            Log.v("findObjectt", String.valueOf(json));
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
                        arrColorList.clear();
                        arrImagFaList.clear();
                        imageUriArray.clear();
                        AppConstants.imageList.clear();
                        Log.v("responsproductDetail", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);

                            if (jsonObject.getString("resCode").equalsIgnoreCase("1")) {
                                String Type = jsonObject.getString("Type");
                                String shipping_charge = jsonObject.getString("shipping_charge");
                                String productPrice = jsonObject.getString("productPrice");
                                String discounType = jsonObject.getString("discounType");

                                String discountValue = jsonObject.getString("discountValue");
                                String finalPrice = jsonObject.getString("finalPrice");
                                String sku_code = jsonObject.getString("sku_code");

                                if (sku_code.equalsIgnoreCase("")) {
                                    tvsku.setVisibility(View.GONE);
                                    tvskuu.setVisibility(View.GONE);
                                } else {
                                    tvsku.setText(sku_code);
                                    tvsku.setVisibility(View.VISIBLE);
                                    tvskuu.setVisibility(View.VISIBLE);

                                }


                                if (intGetVal.hasExtra(AppSettings.discount)) {
                                    discountValues = intGetVal.getStringExtra(AppSettings.discount);
                                    finalPrices = intGetVal.getStringExtra(AppSettings.finalPrice);
                                    couponId = intGetVal.getStringExtra(AppSettings.coupon_code);
                                    copouncode = intGetVal.getStringExtra(AppSettings.coupon_codee);
                                    tvDiscountvalue.setText(getString(R.string.copundiscount) + getString(R.string.rupaye) + " " + intGetVal.getStringExtra(AppSettings.discount));
                                    tvDiscountvalue.setVisibility(View.VISIBLE);
                                    tvcopouncode.setText(copouncode);

                                    discountValues = discountValues;
                                    finalPrice = finalPrice;
                                    Log.v("letsCheck", "got" + discountValues);
                                } else {
                                    tvcopouncode.setText(getString(R.string.applycoupon));
                                    tvDiscountvalue.setText("Coupon Discount : " + intGetVal.getStringExtra(AppSettings.discount) + "%");

                                }

                                Log.v("letsCheck", "notGot" + discountValues);
                                AppSettings.putString(AppSettings.addedByIdd, jsonObject.getString("addedById"));
                                AppSettings.putString(AppSettings.finalPrice, jsonObject.getString("finalPrice"));
                                AppSettings.putString(AppSettings.addedTypee, jsonObject.getString("addedType"));
                                String Typee = jsonObject.getString("Type");
                                AppSettings.putString(AppSettings.Type, jsonObject.getString("Type"));




                                JSONArray jsonArray = jsonObject.getJSONArray("unitNames");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    HashMap<String, String> HasList = new HashMap<>();
                                    HasList.put("id", jsonObject1.getString("id"));
                                    HasList.put("unit_name", jsonObject1.getString("unit_name"));
                                    arrSelectSize.add(HasList);

                                }
                                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mActivity);
                                layoutManager.setFlexWrap(FlexWrap.WRAP);
                                layoutManager.setFlexDirection(FlexDirection.ROW);
                                layoutManager.setAlignItems(AlignItems.STRETCH);
                                recv_size.setLayoutManager(layoutManager);
                                recv_size.setAdapter(adapter);

                                JSONArray ProductColor = jsonObject.getJSONArray("colorNames");

                                for (int k1 = 0; k1 < ProductColor.length(); k1++) {
                                    JSONObject jsonObjectcolor = ProductColor.getJSONObject(k1);
                                    HashMap<String, String> hmcolor = new HashMap<>();
                                    hmcolor.put("id", jsonObjectcolor.getString("id"));
                                    hmcolor.put("name", jsonObjectcolor.getString("name"));
                                    hmcolor.put("product_id", jsonObjectcolor.getString("product_id"));
                                    hmcolor.put("image", jsonObjectcolor.getString("image"));
                                    arrColorList.add(hmcolor);

                                    Log.v("imageee", arrColorList.toString());
                                }

                                recyclerViewColor.setLayoutManager(new LinearLayoutManager(
                                        mActivity, LinearLayoutManager.HORIZONTAL, false));
                                ProductColoAdapter adapterr = new ProductColoAdapter(mActivity, arrColorList);
                                recyclerViewColor.setAdapter(adapterr);


                                JSONArray images = jsonObject.getJSONArray("productImages");

                                for (int k = 0; k < images.length(); k++) {
                                    JSONObject jsonObject2 = images.getJSONObject(k);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("productImage", jsonObject2.getString("productImage"));
                                    image = jsonObject2.getString("productImage");
                                    AppConstants.imageList.add(hm);
                                    arrImagFaList.add(hm);

                                }


                                if (Type.equals("1")) {
                                    tvShipincharge.setText(getString(R.string.shipping) + " " + getString(R.string.rupaye) + shipping_charge);

                                } else {
                                    llShippingCharge.setVisibility(View.VISIBLE);

                                }

                                description = jsonObject.getString("description");
                                description = description.replaceAll("//", "\n");
                                description = description.replaceAll("##", "\n \u2022");
                                Log.v("My_description", description);
                                tvproductetails.setText(description);

                                addedById = jsonObject.getString("addedById");
                                addedType = jsonObject.getString("addedType");

                                name = jsonObject.getString("productName");
                                tvName.setText(name.trim());
                                AppSettings.putString(AppSettings.productName, jsonObject.getString("productName"));
                                ivZozima.setText(jsonObject.getString("productName"));


                                if (discounType.equals("1"))
                                    tvDiscountt.setText("₹" + discountValue + " " + getString(R.string.off));


                                else if (discounType.equals("2"))

                                    tvDiscountt.setText(discountValue + " " + getString(R.string.percent));

                                else

                                    tvDiscountt.setText("");

                                if (finalPrice.equalsIgnoreCase(productPrice)) {
                                    tvPricee.setText("₹" + productPrice);
                                    tvDiscountt.setText("");
                                    tvOriginalPricee.setText("");



                                } else {
                                    tvOriginalPricee.setText("₹" + finalPrice);
                                    tvPricee.setText("₹" + productPrice);
                                    tvPricee.setPaintFlags(tvPricee.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
                                    tvPricee.setPaintFlags(tvPricee.getPaintFlags() | 16);
                                }


                            }
                            viewPager.setAdapter(new ProductBannerAdapter(ProductAddToCardActivity.this, AppConstants.imageList) {
                                @Override
                                protected void viewClick(View view, String str) {
                                    ProductGalleryActivity.url = str;
                                    startActivity(new Intent(getApplicationContext(), ProductGalleryActivity.class));
                                }
                            });
                            indicator.setViewPager(viewPager);
                            final float density = getResources().getDisplayMetrics().density;
                            indicator.setRadius(6 * density);

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
                        Log.v("errormsg", String.valueOf(anError));
                    }
                });

    }

    private class ProductColoAdapter extends RecyclerView.Adapter<Holoder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public ProductColoAdapter(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public Holoder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_color, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holoder Holoder, final int position) {


            if (!data.get(position).get("image").isEmpty()) {
                Picasso.get().load(data.get(position).get("image")).placeholder(R.mipmap.logo_grey).resize(400, 600).into(Holoder.ivWhite);
            }


            Holoder.Rlimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = position;
                    AppSettings.putString(AppSettings.productId, data.get(position).get("product_id"));
                    notifyDataSetChanged();
                    arrSelectSize.clear();
                    GetProductDetail();

                }

            });

            if(AppSettings.getString(AppSettings.productId).equalsIgnoreCase(data.get(position).get("product_id")))
            {
                Holoder.ivRed.setVisibility(View.VISIBLE);

            }
            else {
                Holoder.ivRed.setVisibility(View.GONE);
            }
          /*  if (row_index == position) {

                Holoder.ivRed.setVisibility(View.VISIBLE);

            }
            else {

                Holoder.ivRed.setVisibility(View.GONE);
            }*/







        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holoder extends RecyclerView.ViewHolder {
       ImageView ivWhite,ivRed;
       RelativeLayout Rlimage;

        public Holoder(View itemView) {
            super(itemView);
            //ImageView
            ivRed = itemView.findViewById(R.id.ivRed);
            ivWhite = itemView.findViewById(R.id.ivWhite);
            Rlimage = itemView.findViewById(R.id.Rlimage);

        }

/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(mActivity,ProductDetailsActivity.class);
        intent.putExtra("description",description);
        intent.putExtra("status","status");
        startActivity(intent);
    }
*/


    }
    private void sharednowApi() {

        String url = AppUrls.ShareCalog;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("catalogId", AppSettings.getString(AppSettings.catalogId));
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


    public void imge(int pos) {

        if (arrImagFaList.size() > pos) {

            new DownloadFromURL().execute( arrImagFaList.get( pos ).get( "productImage" ) );
            Log.v( "imageesss", String.valueOf( arrImagFaList.size() ) );
            /*Log.v( "cdsjf", imageList.get( pos ).get( "image" ) );*/
            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }

////OtherShareNow sharing////////////
    }

    public void other(int other) {

        if (arrImagFaList.size() > other) {

            new DownloadFromURLl().execute( arrImagFaList.get( other ).get( "productImage" ) );
            Log.v( "imageesss", String.valueOf( arrImagFaList.size() ) );
            /*Log.v( "cdsjf", imageList.get( pos ).get( "image" ) );*/
            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }

////OtherShareNow sharing////////////
    }


    }