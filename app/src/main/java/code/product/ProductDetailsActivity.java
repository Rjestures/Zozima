package code.product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import code.basic.CartAcivity;

import code.basic.WishlistAcitvity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.entity.ProductData;
import code.entity.ProductImage;
import code.entity.UnitNameData;
import code.main.MainActivity;
import code.searching.SearchActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class ProductDetailsActivity extends BaseActivity {

    //ImageView
    ImageView ivBack, ivcart, ivFavourite, ivFavouritte, ivSearch, ivZozima, ivloader;

    //RecyclerView
    RecyclerView recyclerView;

    //TextView
    TextView tvdescreption, tvName, tvCount;

    String descriptionn;

    //Listing
    List<ProductData> productDataList;

    String catlogId;

    //LinearLayout
    LinearLayout llWhatsapp, llCopy, llFavourite, llSharee, llDownload, llFacebook;

    //ProgressDaiolg
    ProgressDialog mProgressDialog;
    ProgressDialog mpProgress;
    ProgressDialog mProgressDialog2;

    int down = 100;

    //CustomTextView
    TextView tvWishlist;

    Intent intent;
    int pos = 0, other = 0, downlod = 0, facebook = 0;
    int a = 0, p = 0, d = 0;

    List<ProductImage> productImgList;
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //LinearLayout
        llSharee = (LinearLayout) findViewById(R.id.llSharee);
        llDownload = (LinearLayout) findViewById(R.id.llDownload);
        llCopy = (LinearLayout) findViewById(R.id.copy);
        llFacebook = (LinearLayout) findViewById(R.id.llFacebook);
        llFavourite = (LinearLayout) findViewById(R.id.rlFavourite);
        llWhatsapp = (LinearLayout) findViewById(R.id.llWhatsapp);

        //RecyCLerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerVieww);

        //TextView
        tvName = (TextView) findViewById(R.id.tvnamee);
        tvWishlist = (TextView) findViewById(R.id.wishlist);
        tvdescreption = (TextView) findViewById(R.id.tvdescreption);
        tvCount = (TextView) findViewById(R.id.tvCount);

        //ImageView
        ivFavourite = (ImageView) findViewById(R.id.ivFavourite);
        ivFavouritte = (ImageView) findViewById(R.id.ivFavouritte);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivcart = findViewById(R.id.ivcart);
        ivZozima = findViewById(R.id.ivZozima);
        simpleProgressBar = findViewById(R.id.progresbar);
        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);


        intent = getIntent();

        descriptionn = intent.getStringExtra("description");
        descriptionn = descriptionn.replaceAll("//", "\n");
        descriptionn = descriptionn.replaceAll("##", "\n \u2022 ");
        Log.v("My_description", descriptionn);



        catlogId = intent.getStringExtra("catalogId");
        final String status = intent.getStringExtra("catalogId");


        ivZozima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("pagePath", 1);
                startActivity(intent);
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });


        llWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUriArray.clear();
                sharednowApi();
                pos = 0;
                imge(pos);
                /*Log.v( "imagePositions", "position= " .get( position ).get( "imageArray" ) );*/


            }
        });

        llSharee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUriArray.clear();
                other = 0;
                Other(other);
                /*Log.v( "imagePositions", "position= " .get( position ).get( "imageArray" ) );*/


            }
        });
        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageUriArray.clear();
                downlod = 0;
                Download(downlod);
                // Download( downlod );
                /*Log.v( "imagePositions", "position= " .get( position ).get( "imageArray" ) );*/


            }
        });
        llFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageUriArray.clear();
                facebook = 0;
                Facebook(facebook);
                // Download( downlod );
                /*Log.v( "imagePositions", "position= " .get( position ).get( "imageArray" ) );*/
            }
        });
        ivFavouritte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, WishlistAcitvity.class));
            }
        });

        llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", descriptionn);
                Toast.makeText(mActivity, getString(R.string.catalogedescriptioncopied), Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });
        ivcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, CartAcivity.class));
            }
        });
        ivBack = (ImageView) findViewById(R.id.back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, MainActivity.class));
            }
        });
        llFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SimpleHTTPConnection.isNetworkAvailable(getApplicationContext())) {

                    addFavApi();


                } else {
                    AppUtils.showToastSort(getApplicationContext(), getString(R.string.errorInternet));

                }
            }
        });
        //allStaticCode();
        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
            getCatlogDetails();
        } else {
            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }
        //

    }

    private void getCatlogDetails() {
        /*simpleProgressBar.setVisibility(View.VISIBLE);*/
        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.CatalogDetails;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            Intent intent = getIntent();
            String collectionId = intent.getStringExtra("catalogId");
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("catalogId", AppSettings.getString(AppSettings.catalogId));
            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));
            /*jsonObject.put( "catalogId", collectionId );*/
            /*Log.v("daaa",collectionId);*/

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


                        Log.v("dshdhsdw", String.valueOf(response));
                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
                            simpleProgressBar.setVisibility(View.GONE);
                            ivloader.setVisibility(View.GONE);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {

                                ivFavourite.setImageResource(R.drawable.ic_fav_selected);

                            } else if (status.equals("2")) {

                                ivFavourite.setImageResource(R.drawable.ic_fav_unselected);

                            }
                            String jsonInString = jsonObject.getString("products");
                            productDataList = ProductData.createJsonInList(jsonInString);
                            LinearLayoutManager linearVertical = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            ProductDetails adapterForProduct = new ProductDetails(ProductDetailsActivity.this, productDataList);
                            recyclerView.setLayoutManager(linearVertical);
                            recyclerView.setAdapter(adapterForProduct);

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

    public void imge(int pos) {

        if (productDataList.size() > pos) {
            final List<ProductImage> productImageList = productDataList.get(pos).getProductImages();
            new DownloadFromURLShareNow().execute(productImageList.get(0).getProductImage());
            /*sharednowApi(catlogId);*/
            Log.v("image", String.valueOf(productImageList));

            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }


    }

    public void Other(int other) {

        if (productDataList.size() > other) {
            final List<ProductImage> productImageList = productDataList.get(other).getProductImages();
            new DownloadOther().execute(productImageList.get(0).getProductImage());
            Log.v("image", String.valueOf(productImageList));

            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }


    }

    public void Download(int downlod) {

        if (productDataList.size() > downlod) {
            final List<ProductImage> productImageList = productDataList.get(downlod).getProductImages();
            new DownloadFromURLDownloader().execute(productImageList.get(0).getProductImage());
            Log.v("image", String.valueOf(productImageList));

            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }


    }

    public void Facebook(int facebook) {

        if (productDataList.size() > facebook) {
            final List<ProductImage> productImageList = productDataList.get(facebook).getProductImages();
            new DownloadFromURLFaceBook().execute(productImageList.get(0).getProductImage());
            Log.v("image", String.valueOf(productImageList));

            // Toast.makeText( getActivity(), arrImagList.get( pos ).get( "productThumbnil" ), Toast.LENGTH_SHORT ).show();

        }


    }


    private void addFavApi() {

        AppUtils.showRequestDialog(mActivity);

        Log.v("addFavApi", AppUrls.WishList);

        JSONObject json = new JSONObject();
        JSONObject json_data = new JSONObject();

        try {
            json_data.put("userId", AppSettings.getString(AppSettings.userId));
            json_data.put("catalogId", AppSettings.getString(AppSettings.catalogId));

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
                        parseFavdata(response);
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

    private void parseFavdata(JSONObject response) {
        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
            String resMasg = jsonObject1.getString("resMsg");
            Log.v("dsjfsjd", jsonObject1.getString("status"));

            if (jsonObject1.getString("resCode").equals("1")) {

                if (jsonObject1.getString("status").equals("1")) {
                    ivFavourite.setImageResource(R.drawable.ic_fav_selected);

                    AppUtils.showToastSort(getApplicationContext(), getString(R.string.productaddwishlist));

                }

                else if (jsonObject1.getString("status").equals("2")) {
                    AppUtils.showToastSort(getApplicationContext(), getString(R.string.productremovewishlist));
                    ivFavourite.setImageResource(R.drawable.ic_fav_unselected);


                }
            }

        } catch (Exception e) {
            AppUtils.showToastSort(getApplicationContext(), String.valueOf(e));
        }
        AppUtils.hideDialog();

    }

    //ProductDetails
    private class ProductDetails extends RecyclerView.Adapter<ProductDetails.Holder> {
        Context context;
        List<ProductData> productData;

        public ProductDetails(Activity mActivity, List<ProductData> productData) {
            this.productData = productData;
            this.context = mActivity;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.infilate_productsdetails, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            final String catlogid = (productData.get(position).getId());
            final String type = (productData.get(position).getType());
            final String addbyid = (productData.get(position).getAddedById());
            final String addedtype = (productData.get(position).getAddedType());
            final String shipping_charge = (productData.get(position).getShipping_charge());
            final String Sku_code = (productData.get(position).getSku_code());


            if (Sku_code.equalsIgnoreCase("")) {
                holder.tvsku.setVisibility(View.GONE);
                holder.tvskuu.setVisibility(View.GONE);
            } else {
                holder.tvsku.setText(Sku_code);
                holder.tvsku.setVisibility(View.VISIBLE);
                holder.tvskuu.setVisibility(View.VISIBLE);

            }


            Log.v("sdjfsdj", type);
            if (type.equals("1")) {
                holder.shipincharge.setText(getString(R.string.shipping) + " " + getString(R.string.rupaye) + shipping_charge);

            } else {
                holder.llShippingCharge.setVisibility(View.VISIBLE);

            }

            Log.v("checkImageUrls", productData.toString());
            final List<UnitNameData> unitNameDataList = productData.get(position).getUnitNames();
            final List<ProductImage> productImageList = productData.get(position).getProductImages();
            productImgList = productData.get(position).getProductImages();
            Log.v("hshasd", productImgList.toString());
            // final String productName = (data.get(position).get("productName"));
            final String productName = productData.get(position).getProductName();


            if (productName.contains("Combo Pack")) {
                String camboname = productName.substring(0, 10);
                String namevalue = productName.substring(11);
                Log.v("nammee", namevalue);
                Log.v("nammee", camboname);
                holder.tvName.setText(namevalue);
                holder.tvCambo.setText(camboname);
            } else {
                holder.tvName.setText(productName.trim());
                holder.tvCambo.setVisibility(View.GONE);
            }


            if (productData.get(position).getDiscounType().equals("1"))
                holder.tvDiscount.setText("₹" + productData.get(position).getDiscountValue() + " " + getString(R.string.off));

            else if (productData.get(position).getDiscounType().equals("2"))
                holder.tvDiscount.setText(productData.get(position).getDiscountValue() + " " + getString(R.string.percent));

            else
                holder.tvDiscount.setText("");

            if (productData.get(position).getFinalPrice().equalsIgnoreCase(productData.get(position).getProductPrice())) {
                holder.tvPrice.setText("₹" + (productData.get(position)).getProductPrice());
                holder.tvDiscount.setText("");
                holder.tvOriginalPrice.setText("");



            } else {
                holder.tvOriginalPrice.setText("₹" + (productData.get(position)).getFinalPrice());
                holder.tvPrice.setText("₹" + (productData.get(position)).getProductPrice());
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
                holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | 16);
            }

            holder.rlproductDetailslist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.llProductList.setVisibility(View.VISIBLE);
                    holder.rlproductDetailslistt.setVisibility(View.VISIBLE);
                    holder.rlproductDetailslist.setVisibility(View.GONE);
                    holder.tvdescreption.setText(descriptionn);

                }
            });
            holder.rlproductDetailslistt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.llProductList.setVisibility(View.GONE);
                    holder.rlproductDetailslistt.setVisibility(View.GONE);
                    holder.rlproductDetailslist.setVisibility(View.VISIBLE);
                    holder.tvdescreption.setText(descriptionn);


                }
            });


            if (!productImageList.get(0).getProductImage().equals(""))

                Picasso.get().load(productImageList.get(0).getProductImage()).placeholder(R.mipmap.logo_grey).into(holder.ivImage);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ProductAddToCardActivity.class);
                    intent.putExtra("Id", AppSettings.putString(AppSettings.productId, productData.get(position).getId()));
                    startActivity(intent);

                }
            });


            holder.llWhatsap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("checkImageUrl", String.valueOf(productImageList.size()));

                    if (!productImageList.get(0).getProductImage().equals("")) {
//                        Log.v( "checkImageUrl", productImageList.toString() );
                        Log.v("checkImageUrl", Integer.toString(position));
                        sharednowApi();
                        new DownloadFromURL().execute(productImageList.get(0).getProductImage());
//                        new DownloadFromURL().execute( productImageList.get( 0 ).getProductImage() );
                    }
                }
            });
            holder.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!productImageList.get(0).getProductImage().equals("")) {
                        Log.v("checkImageUrl", productImageList.toString());
                        Log.v("checkImageUrl", Integer.toString(position));
                        new DownloadFromURLl().execute(productImageList.get(0).getProductImage());
                    }
                }
            });

        }

        public int getItemCount() {
            return productData.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            //TextView
            TextView tvName, tvPrice, tvOriginalPrice, tvDiscount, tvCambo;

            //CardView
            CardView cardView;

            //ImageView
            ImageView ivImage;

            //RelativeLayout

            int count = 0;

            TextView shipincharge, tvsku, tvskuu,tvdescreption;

            LinearLayout layout_shiping;

            ImageView ivshipping;

            //LinearLayout
            LinearLayout llShare, llWhatsap, llShippingCharge,llProductList;

            RelativeLayout rlproductDetailslist,rlproductDetailslistt;

            public Holder(View itemView) {
                super(itemView);

                layout_shiping = itemView.findViewById(R.id.layout_shiping);
                ivshipping = itemView.findViewById(R.id.ivshipping);
                shipincharge = itemView.findViewById(R.id.shipincharge);
                tvsku = itemView.findViewById(R.id.tvsku);
                tvCambo = itemView.findViewById(R.id.tvCambo);
                tvskuu = itemView.findViewById(R.id.tvskuu);
                tvdescreption = itemView.findViewById(R.id.tvdescreption);

                //CardView
                cardView = itemView.findViewById(R.id.cardVieww);

                //TextView
                tvName = itemView.findViewById(R.id.tvName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
                tvDiscount = itemView.findViewById(R.id.tvDiscount);

                //ImageView
                ivImage = itemView.findViewById(R.id.ivImage);

                //RelativeLayout
                llShare = itemView.findViewById(R.id.llShare);


                llWhatsap = itemView.findViewById(R.id.llWhatsapp);
                llShippingCharge = itemView.findViewById(R.id.layout_shiping);
                rlproductDetailslist = itemView.findViewById(R.id.rlproductDetailslist);
                llProductList = itemView.findViewById(R.id.llProductList);
                rlproductDetailslistt = itemView.findViewById(R.id.rlproductDetailslistt);


            }

        }

    }

    class DownloadFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mpProgress = new ProgressDialog(ProductDetailsActivity.this);
            // Set progressdialog title
            mpProgress.setTitle(getString(R.string.pleasewait));
            // Set progressdialog message
            mpProgress.setMessage(getString(R.string.loadingimage));
            mpProgress.setIndeterminate(false);
            // Show progressdialog
            mpProgress.show();


        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {

                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "tvName" + ".jpeg"));
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
            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "tvName.jpeg");
            Uri uri = Uri.fromFile(imageFileToShare);
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/jpeg");
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mpProgress.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
        }


    }

    //DownloadFromURLShareNow
    class DownloadFromURLl extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mpProgress = new ProgressDialog(ProductDetailsActivity.this);
            // Set progressdialog title
            mpProgress.setTitle(getString(R.string.pleasewait));
            // Set progressdialog message
            mpProgress.setMessage(getString(R.string.loadingimage));
            mpProgress.setIndeterminate(false);
            // Show progressdialog
            mpProgress.show();


        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {

                String root = Environment.getExternalStorageDirectory().getPath();
                File file1 = new File(root + "/Zozima");
                String file = String.valueOf(new File(file1, "tvName" + ".jpeg"));
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
            File imageFileToShare = new File(Environment.getExternalStorageDirectory().toString() + "/Zozima", "tvName.jpeg");
            Uri uri = Uri.fromFile(imageFileToShare);
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                    Intent shareIntent1 = new Intent();
                    shareIntent1.setAction(Intent.ACTION_SEND);
                    shareIntent1.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent1.setType("image/jpeg");
                    shareIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent1, "send"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mpProgress.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.sharingimage), Toast.LENGTH_SHORT).show();
        }


    }

    //DownloadFromURLShareNow
    class DownloadFromURLShareNow extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pos == 0) {

                // Create a progressdialog
                mProgressDialog = new ProgressDialog(ProductDetailsActivity.this);

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
//                String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );
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
            Log.v("wjhjbcbimageUri", String.valueOf(imageUriArray.size()));

            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (productDataList.size() == imageUriArray.size()) {

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

    //DownloadOther
    class DownloadOther extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (other == 0) {
                // Create a progressdialog
                mProgressDialog2 = new ProgressDialog(ProductDetailsActivity.this);
                // Set progressdialog title
                mProgressDialog2.setMessage(getString(R.string.pleasewait));
                // Set progressdialog message
                // Show progressdialog
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
                String file = String.valueOf(new File(file1, "name" + p++ + ".jpeg"));
//                String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );
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
            Log.v("pdsizechecker", String.valueOf(imageUriArray.size()) + "  " + Integer.toString(productDataList.size()));

            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (productDataList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Log.v("pdsizechecker", "Hello");
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                        intent.setType("image/jpeg");
                        startActivity(Intent.createChooser(intent, "send"));
                        mProgressDialog2.dismiss();
                        Toast.makeText(mActivity, getString(R.string.catlogedescription), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog2.dismiss();
                    }

                }

            } else {

                other = other + 1;
                Other(other);
            }
        }
    }

    //DownloadDownloadUrl
    class DownloadFromURLDownloader extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (downlod == 0) {
                mProgressDialog2 = new ProgressDialog(ProductDetailsActivity.this);
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
                File file2 = new File(file1, "product" + down++ + ".jpeg");
                String file = String.valueOf(new File(file1, "product" + down++ + ".jpeg"));

                if (file2.exists()) {
                    System.out.println("file is already there");
                } else {
                    System.out.println("Not find file ");
                }

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
            // setting progress percentage
            mProgressDialog2.setProgress(0);
            Log.v("dqkjbdq", String.valueOf(imageUriArray.size()));
            Log.v("dqkjbdq2", String.valueOf(productDataList.size()));
            int incProgress = 100 / productDataList.size() + 1;
            int imageUriArrayy = imageUriArray.size() + 1;

            for (int i = 0; i < imageUriArrayy; i++) {
                // Do stuff
                mProgressDialog2.incrementProgressBy(incProgress);
            }

            if (imageUriArray != null) {

                mProgressDialog2.setMessage(getString(R.string.downloading) + (imageUriArrayy + 0) + "/" + productDataList.size());
            }
        }

        @Override
        protected void onPostExecute(String file_url) {
            File imageFileToShare = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Zozima", "product" + Integer.toString(down++) + ".jpeg");
            imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFileToShare))));
            Log.v("wjhjbcb", String.valueOf(imageUriArray.size()));
            Log.v("wjhjbcb", String.valueOf(productDataList.size()));
            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);
            if (productDataList.size() == imageUriArray.size()) {

                if (Build.VERSION.SDK_INT >= 24) {
                    try {
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

    //DownloadFromURLFaceBook
    class DownloadFromURLFaceBook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (facebook == 0) {
                // Create a progressdialog
                mProgressDialog = new ProgressDialog(ProductDetailsActivity.this);
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
//                String file = String.valueOf( new File( file1, "tvName" + ".jpeg" ) );
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
            Log.v("wjhjbcb", String.valueOf(imageUriArray.size()));
            Log.v("wjhjbcb", String.valueOf(productDataList.size()));
            imageFileToShare.length();
            Log.v("mdf", "image= " + imageFileToShare);

            if (productDataList.size() == imageUriArray.size()) {

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

    //OnResume
    @Override
    public void onResume() {

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
   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( mActivity, MainActivity.class );
        intent.putExtra( "pagePath", 1 );
        startActivity( intent );
    }*/


}


