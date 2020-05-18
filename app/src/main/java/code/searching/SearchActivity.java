package code.searching;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import code.database.AppSettings;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class SearchActivity extends BaseActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    //SearchView
    SearchView editsearch;
    SharedPreferences sharedPreferences;
    //RelativeLayout
    RelativeLayout rrNoData;
    LinearLayout llNoProducts;
    ProgressBar progresbar;
    //ArrayList
    ImageView IvZozima, ivcart, ivfevriot, iv_menu, ivmice;
    String paging = "0", searchname;
    //NestedScrollView
    NestedScrollView scrollView;
    CatalogueAdapter adapterr;
    private ArrayList<HashMap<String, String>> catalogueList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> searchList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> searchListRev = new ArrayList<>();
    //RecyclerView
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //ImageView
        IvZozima = findViewById(R.id.zozima);
        ivcart = findViewById(R.id.ivcart);
        ivfevriot = findViewById(R.id.ivFavourite);
        iv_menu = findViewById(R.id.iv_menu);
        ivmice = findViewById(R.id.ivmice);

        //RelayTiveLayout
        rrNoData = findViewById(R.id.rrNoData);
        llNoProducts = findViewById(R.id.llNoProducts);

        //EditSearch
        editsearch = findViewById(R.id.search);

        //Recycleview
        recyclerView = findViewById(R.id.recyclerView);

        progresbar = findViewById(R.id.progresbar1);
        scrollView = findViewById(R.id.scrollView);

        Intent intent = getIntent();
        String namee = intent.getStringExtra(AppSettings.productName);
        editsearch.setQuery(namee, false);


        ivmice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });


        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


/*        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                int scrollY = rootScrollView.getScrollY(); // For ScrollView
//                int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
//                // DO SOMETHING WITH THE SCROLL COORDINATES
                if (scrollView != null && !paging.equalsIgnoreCase("0")) {
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        //scroll view is at bottom

                        progresbar.setVisibility(View.VISIBLE);

//                        getSearchData(paging);
                        getSearchData("0");

                    } else {

                    }
                }
            }
        });*/


        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() >= 2) {
                    HashMap<String, String> operators;
                    operators = new HashMap<String, String>();
                    operators.put("id", "");
                    operators.put("catalog_id", "");
                    operators.put("description", "");
                    operators.put("status", "");
                    operators.put("name", String.valueOf(editsearch.getQuery()));
                    operators.put("time", AppUtils.getCurrentDates());
                    for(int i=0;i<searchListRev.size();i++){
                        if(String.valueOf(editsearch.getQuery()).equalsIgnoreCase(searchListRev.get(i).get("name"))){
                            searchListRev.remove(i);
                        }
                    }searchListRev.add(operators);
                    //**************************Saving ArrayList in SharedPref***************************
                    Gson gson = new Gson();
                    String json = gson.toJson(searchListRev);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppSettings.searchArray, json);
                    editor.commit();
                    Intent intent = new Intent(mActivity, SearchListActivity.class);
                    intent.putExtra("name", AppSettings.putString(AppSettings.productname, String.valueOf(editsearch.getQuery())));
                    startActivity(intent);

                } else {

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() >= 2) {
                    catalogueList.clear();
                    getSearchData("0");
                }

                return true;
            }
        });
        setData();
    }

    public int getDays(String startDate) {
        try {
            long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            long begin = dateFormat.parse(startDate).getTime();
            long end = new Date().getTime(); // 2nd date want to compare
            long diff = (end - begin) / (MILLIS_PER_DAY);

            Log.d("Days_Ago", "my " + diff);
            return Math.abs((int) diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setData() {
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        //*********************retrieving arrayList*************************************
        String json = sharedPreferences.getString(AppSettings.searchArray, "");
        Log.v("MyValues", json);
        if (json.isEmpty()) {
            llNoProducts.setVisibility(View.VISIBLE);
        } else {
            llNoProducts.setVisibility(View.GONE);
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject data_object = jsonArray.getJSONObject(i);
                    if (data_object.has("time")) {

                        HashMap<String, String> entries = new HashMap<String, String>();
                        if (getDays(data_object.getString("time")) <= 30) {

                            entries.put("id", data_object.getString("id"));
                            entries.put("catalog_id", data_object.getString("catalog_id"));
                            entries.put("description", data_object.getString("description"));
                            entries.put("status", data_object.getString("status"));
                            entries.put("name", data_object.getString("name"));
                            entries.put("time", data_object.getString("time"));
                            searchList.add(entries);
                            searchListRev.add(entries);

                        }
                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Collections.reverse(searchList);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
            adapterr = new CatalogueAdapter(mActivity, searchList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapterr);

        }
        //************************************************************************************

    }

    private void getSearchData(String pagingLimit) {

        String url = AppUrls.AdvanceSearch;

        Log.v("AdvanceSearch", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("keyword", editsearch.getQuery());
            jsonObject.put("pageindex", pagingLimit);
            Log.v("AdvanceSearch", String.valueOf(json));

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
                        progresbar.setVisibility(View.GONE);
                        catalogueList.clear();
                        Log.v("AdvanceSearch", String.valueOf(response));

                        try {
                            JSONObject jsonObject = response.getJSONObject(AppConstants.projectName);
                            if (jsonObject.getString("resCode").equalsIgnoreCase("1")) {
                                rrNoData.setVisibility(View.GONE);

                                JSONArray jsonArray = jsonObject.getJSONArray("SearchList");
                                paging = jsonObject.getString("pageindex");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashlist = new HashMap();
                                    hashlist.put("id", arrayJSONObject.getString("id"));
                                    hashlist.put("catalog_id", arrayJSONObject.getString("catalog_id"));
                                    hashlist.put("description", arrayJSONObject.getString("description"));
                                    hashlist.put("status", arrayJSONObject.getString("status"));
                                    hashlist.put("name", arrayJSONObject.getString("name"));
                                    catalogueList.add(hashlist);
                                }


                            } else {
                                progresbar.setVisibility(View.GONE);
                                rrNoData.setVisibility(View.VISIBLE);

                            }
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                            CatalogueAdapter adapterr = new CatalogueAdapter(mActivity, catalogueList);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapterr);

                        } catch (JSONException e) {
                            AppUtils.showToastSort(rrNoData, String.valueOf(e), mActivity);

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.v("ggfh", String.valueOf(anError));


                    }

                });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.v("speckkkk", String.valueOf(result));
                    editsearch.setQuery(result.get(0), false);
                    /*editsearch.setText(result.get(0));*/
                }
                break;
            }

        }
    }

    private class CatalogueAdapter extends RecyclerView.Adapter<CatalogueHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();


        public CatalogueAdapter(Activity mActivity, ArrayList<HashMap<String, String>> catalogueList) {
            data = catalogueList;
            Log.v("jxnf", String.valueOf(catalogueList));
        }

        public CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CatalogueHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflatesearch, null));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CatalogueHolder holder, final int position) {
            Log.v("nameee", data.get(position).get("name"));

            holder.tvName.setText(data.get(position).get("name"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String, String> operators;
                    operators = new HashMap<String, String>();
                    operators.put("id", data.get(position).get("id"));
                    operators.put("catalog_id", data.get(position).get("catalog_id"));
                    operators.put("description", data.get(position).get("description"));
                    operators.put("status", data.get(position).get("status"));
                    operators.put("name", data.get(position).get("name"));
                    operators.put("time", AppUtils.getCurrentDates());
                    for(int i=0;i<searchListRev.size();i++){
                        if(data.get(position).get("name").equalsIgnoreCase(searchListRev.get(i).get("name"))){
                            searchListRev.remove(i);
                        }
                    }searchListRev.add(operators);
                    //**************************Saving ArrayList in SharedPref***************************
                    Gson gson = new Gson();
                    String json = gson.toJson(searchListRev);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppSettings.searchArray, json);
                    editor.commit();
                    //************************************************************************************

                    Intent intent = new Intent(mActivity, SearchListActivity.class);
                    intent.putExtra("name", AppSettings.putString(AppSettings.productname, data.get(position).get("name")));
                    startActivity(intent);
                }
            });


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class CatalogueHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RelativeLayout ll;

        public CatalogueHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ll = itemView.findViewById(R.id.ll);


        }
    }


}





