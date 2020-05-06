package code.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

import code.basic.CollectionCategory;
import code.common.SimpleHTTPConnection;
import code.database.AppSettings;
import code.main.MainActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionsFragment extends BaseFragment implements View.OnClickListener {

    //ArrayList
    private ArrayList<HashMap<String, String>> collectionList = new ArrayList<>();


    //RecyclerView
    RecyclerView recyclerView;

    //ImageView
    ImageView ivBanner,ivloader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        findViewById(view);

        if (SimpleHTTPConnection.isNetworkAvailable(mActivity)) {
          collectionList.clear();
            getCallection();
        } else {
            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }
        return view;
    }

    //findViewById
    private void findViewById(View view) {

        //recyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        //ImageView
        ivBanner = view.findViewById(R.id.ivBanner);
        ivloader = view.findViewById(R.id.ivloader);

        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);

    }

    @Override
    public void onClick(View v) {

    }

    private class Adapterr extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public Adapterr(Activity mActivity, ArrayList<HashMap<String, String>> favList) {
            data = favList;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_collections, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            final String id = (data.get(position).get("id"));
            final String collectionName = (data.get(position).get("collectionName"));
            holder.tvName.setText(collectionName);

            Picasso.get().load(data.get(position).get("collectionImage")).placeholder(R.mipmap.logo_grey).resize(400,600).into(holder.imageView);
           /* Picasso.get().load(data.get(position).get("collectionImage")).placeholder(R.mipmap.logo_grey) .into(new Target() {
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
            });*/

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, CollectionCategory.class);
                    intent.putExtra("id", id);
                    startActivity(intent);

                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        //TextView
        TextView tvName;

        //CardView
        CardView cardView;

        //ImageView
        ImageView ivCollections;
        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);

            //CardView
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.ivCategory);

            //TextView
            tvName = itemView.findViewById(R.id.tvName);

            //ImageView
            ivCollections = itemView.findViewById(R.id.ivCollections);

        }
    }


    private void getCallection() {
        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.getCollections;
        Log.v("getSabCategoryApi-URL", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("categoryId", AppSettings.getString(AppSettings.categoryId));

            Log.v("findObject", String.valueOf(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();
                        Log.v("getSubCategory", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            if (jsonObject1.getString("resCode").equals("1")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("allCollectons");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("id", arrayJSONObject.getString("id"));
                                    hashlist.put("categoryId", arrayJSONObject.getString("categoryId"));
                                    hashlist.put("subCategoryId", arrayJSONObject.getString("subCategoryId"));
                                    hashlist.put("collectionName", arrayJSONObject.getString("collectionName"));
                                    hashlist.put("collectionImage", arrayJSONObject.getString("collectionImage"));

                                    collectionList.add(hashlist);
                                }

                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
                                Adapterr adapterr = new Adapterr(mActivity, collectionList);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapterr);
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

    @Override
    public void onDetach() {
        super.onDetach();
        Intent intent = new Intent( getActivity(), MainActivity.class );
        intent.putExtra( "pagePath", 1 );
        startActivity( intent );

    }
}
