package code.subcatageory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import code.basic.WishlistAcitvity;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class SubCategoryActivity extends BaseActivity implements  View.OnClickListener {

    //Scorler
    NestedScrollView scrollView;

    //imageView
    ImageView ivback, ivZozima, ivBanner,ivFavourite,ivloader;

    //TextView
    TextView tvNoProduct,tvCount;

    //ArrayList
    private ArrayList<HashMap<String, String>> arrSubcategory = new ArrayList<>();

    //RecyclerView
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sub_category );

        findViewById( );
        setListner();

        if (SimpleHTTPConnection.isNetworkAvailable( mActivity )) {

            arrSubcategory.clear();
            getSubCategory();
        }

        else {
            AppUtils.showToastSort( mActivity, getString( R.string.errorInternet ) );
        }

    }

    //setListner
    private void setListner() {
        ivback.setOnClickListener( this );
        ivFavourite.setOnClickListener( this );

    }

    //FindViewById
    private void findViewById() {

        //RecyCLerView
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        //NestedScrollView
        scrollView = (NestedScrollView) findViewById( R.id.scrollView );

        //ImageView
        ivZozima = (ImageView) findViewById( R.id.zozima );
        /*ivBanner = (ImageView) findViewById( R.id.iv_bannerImage );*/
        ivback = (ImageView) findViewById( R.id.ivback );
        ivFavourite = (ImageView) findViewById( R.id.ivFavourite );
        //Loader
        ivloader = findViewById(R.id.ivloader);
        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);


        //textview
        tvNoProduct = (TextView) findViewById( R.id.tvnoproductt );
        tvCount = (TextView) findViewById( R.id.tvCount );

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivback:
                onBackPressed();
                break;

            case R.id.ivFavourite:
                startActivity( new Intent( mActivity, WishlistAcitvity.class ) );
                break;

        }


    }

    private void getSubCategory() {

        AppUtils.hideSoftKeyboard( mActivity );
        String url = AppUrls.GetSubcategory;
        Log.v( "getSabCategoryApi-URL", url );

        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            Intent intent = getIntent();
            String categoryId = intent.getStringExtra( "categoryId" );
            json.put( AppConstants.projectName, jsonObject );
            jsonObject.put( "categoryId", categoryId);

            Log.v( "findObject", String.valueOf( json ) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post( url )
                .addJSONObjectBody( json )
                .setPriority( Priority.HIGH )
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppUtils.hideDialog();
                        ivloader.setVisibility(View.GONE);
                        Log.v( "getSubCategory", String.valueOf( response ) );
                        try {
                            JSONObject jsonObject1 = response.getJSONObject( AppConstants.projectName );


                            if (jsonObject1.getString( "resCode" ).equals( "1" )) {


                              /*   if (jsonObject1.getString( "featureBanner" ).equalsIgnoreCase( "" )) {
                                    Picasso.get().load( R.mipmap.logo_grey ).into( ivBanner );
                                } else {
                                    Picasso.get().load( jsonObject1.getString( "featureBanner" ) ).into( ivBanner );

                                }  */



                                JSONArray jsonArray = jsonObject1.getJSONArray( "subcategory" );
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject arrayJSONObject = jsonArray.getJSONObject( i );
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put( "subCategoryId", arrayJSONObject.getString( "subcategoryId" ) );
                                    hashlist.put( "subcategoryName", arrayJSONObject.getString( "subcategoryName" ) );
                                    hashlist.put( "categoryId", arrayJSONObject.getString( "categoryId" ) );
                                    hashlist.put( "subcategoryIcon", arrayJSONObject.getString( "subcategoryIcon" ) );
                                    arrSubcategory.add( hashlist );

                                }

                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getApplicationContext(), 3 );
                                Adapter adapter = new Adapter( getApplicationContext(), arrSubcategory );
                                recyclerView.setLayoutManager( layoutManager );
                                recyclerView.setAdapter( adapter );
                            } else {
                                ivloader.setVisibility(View.GONE);
                                recyclerView.setVisibility( View.GONE );
                                tvNoProduct.setVisibility( View.VISIBLE );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v( "ggfh", String.valueOf( anError ) );


                    }

                } );

    }



    private class Adapter extends RecyclerView.Adapter<SubCategoryActivity.Holder> {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapter(Context applicationContext, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public SubCategoryActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.inflate_subcategory, parent, false ) );
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final SubCategoryActivity.Holder holder, final int position) {
            final String subcategoryName = (data.get( position ).get( "subcategoryName" ));

            holder.tvName.setText( subcategoryName );

            Picasso.get().load(data.get(position).get("subcategoryIcon")).placeholder(R.mipmap.logo_grey).into(holder.ivCategory);


            holder.cardVieww.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mActivity, SubCategoryCatlogActivity.class );
                    intent.putExtra( "categoryId", data.get( position ).get( "categoryId" ) );
                    intent.putExtra( "subCategoryId", data.get( position ).get( "subCategoryId" ) );
                    startActivity( intent );
                }
            } );
        }

        public int getItemCount() {
            return data.size();
        }
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView tvName;

        //CardView
        CardView cardVieww;

        //ImageView
        ImageView ivCategory, imageView;

        public Holder(View inflate) {
            super( inflate );

            //cardview
            cardVieww = itemView.findViewById( R.id.cardView );

            //TextView
            tvName = itemView.findViewById( R.id.tvName );

            //ImageView
            ivCategory = itemView.findViewById( R.id.ivCategory );
            //imageView=  itemView.findViewById(R.id.iv_bannerImage);


        }
    }


    @Override
    public void onResume() {

        super.onResume();


            try{
                int count = Integer.parseInt( AppSettings.getString( AppSettings.total_count ) );

                if (count > 0) {
                    tvCount.setVisibility( View.VISIBLE );
                    tvCount.setText( String.valueOf( count ) );
                } else {
                    tvCount.setVisibility( View.GONE );
                }

            } catch(NumberFormatException ex){ // handle your exception

            }


}

}
