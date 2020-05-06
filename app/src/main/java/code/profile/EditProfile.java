package code.profile;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.zozima.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import code.database.AppSettings;
import code.fragment.BankDetailsFragment;
import code.fragment.ContactFragment;
import code.fragment.PrasonalFragment;
import code.main.MainActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseActivity;
import code.view.CustomTextView;

public class EditProfile extends BaseActivity {
    //ImageView
    ImageView tvBack,ivloader;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //TextView
   TextView tvSave;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tvSave = (TextView) findViewById(R.id.save);

        tvBack = (ImageView) findViewById(R.id.back);

        ivloader = (ImageView) findViewById(R.id.ivloader);

        Glide.with(mActivity).load(R.drawable.gifimage).into(ivloader);

//        setupViewPager(viewPager);
        GetProfile();

        /// Image Upload
        tvBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
        tvSave.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(AppSettings.getString( AppSettings.profilename ).equals( "" ))
                {
                    Toast.makeText( mActivity, getString(R.string.pleaseenteryourname), Toast.LENGTH_SHORT ).show();
                }
                else if(AppSettings.getString( AppSettings.profileemail ).equals( "" )){

                    Toast.makeText( mActivity, getString(R.string.pleaseentervalidemail), Toast.LENGTH_SHORT ).show();

                }
                else if(AppSettings.getString(AppSettings.profilepicode).length() != 6){
                    Log.v("pindocee", String.valueOf(AppSettings.getString(AppSettings.profilepicode).length()));
                    Toast.makeText( mActivity, getString(R.string.pleaseentervalidepincode), Toast.LENGTH_SHORT ).show();

                }
                else
                {
                    hitEditProfileApi();

                }

            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactFragment(), getString(R.string.contact));
        adapter.addFragment(new PrasonalFragment(), getString(R.string.personal));
        adapter.addFragment(new BankDetailsFragment(), getString(R.string.bankdetals));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);

            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void hitBankApi() {

        AppUtils.showRequestDialog(mActivity);

        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.UpdateAccountNumber;
        Log.v("urlApi", url);

        JSONObject jsonObject = new JSONObject();

        JSONObject json = new JSONObject();

        Log.v("finalObject", String.valueOf(json));

        try {
            jsonObject.put("userID", AppSettings.getString(AppSettings.userId));

            jsonObject.put("accountNumber", AppSettings.getString(AppSettings.accountNumber));

            jsonObject.put("confirmAccountNumber", AppSettings.getString(AppSettings.confirmAccountNumber));

            jsonObject.put("accountHolderName", AppSettings.getString(AppSettings.accountHolderName));

            jsonObject.put("ifscCode", AppSettings.getString(AppSettings.ifscCode));

           json.put(AppConstants.projectName, jsonObject);

           Intent intented = new Intent(mActivity,MainActivity.class);
           intented.putExtra("pagePathh",4);
           startActivity(intented);

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
                        Log.v("bank", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            jsonObject1.getString("resMsg");

                            String msg = jsonObject1.getString("resMsg");
                            AppSettings.putString(AppSettings.accountNumber, jsonObject1.getString("accountNumber"));
                            AppSettings.putString(AppSettings.accountHolderName, jsonObject1.getString("accountHolderName"));
                            AppSettings.putString(AppSettings.ifscCode, jsonObject1.getString("ifscCode"));
                            AppSettings.putString(AppSettings.confirmAccountNumber, jsonObject1.getString("confirmAccountNumber"));

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
    private void GetBankApi() {

        AppUtils.showRequestDialog(mActivity);

        AppUtils.hideSoftKeyboard(mActivity);

        String url = AppUrls.GetAccountNumber;
        Log.v("urlApi", url);

        JSONObject jsonObject = new JSONObject();

        JSONObject json = new JSONObject();

        Log.v("finalObject", String.valueOf(json));

        try {
            jsonObject.put("userID", AppSettings.getString(AppSettings.userId));
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
                        setupViewPager(viewPager);
                        AppUtils.hideDialog();
                        Log.v("bank", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            jsonObject1.getString("resMsg");

                            String msg = jsonObject1.getString("resMsg");
                            AppSettings.putString(AppSettings.accountNumber, jsonObject1.getString("accountNumber"));
                            AppSettings.putString(AppSettings.accountHolderName, jsonObject1.getString("accountHolderName"));
                            AppSettings.putString(AppSettings.ifscCode, jsonObject1.getString("ifscCode"));
                            AppSettings.putString(AppSettings.confirmAccountNumber, jsonObject1.getString("confirmAccountNumber"));

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

    private void hitEditProfileApi() {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.editProfile;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            jsonObject.put("userID", AppSettings.getString(AppSettings.userId));
            jsonObject.put("name", AppSettings.getString(AppSettings.profilename));
            jsonObject.put("mobile", AppSettings.getString(AppSettings.mobile));
            jsonObject.put("email", AppSettings.getString(AppSettings.profileemail));
            jsonObject.put("businessName", AppSettings.getString(AppSettings.profilebusness));
            jsonObject.put("pinCode", AppSettings.getString(AppSettings.profilepicode));
            jsonObject.put("address1", AppSettings.getString(AppSettings.profileaddress1));
            jsonObject.put("address2", AppSettings.getString(AppSettings.profileaddres2));
            jsonObject.put("city", AppSettings.getString(AppSettings.cityid));
            jsonObject.put("stateID", AppSettings.getString(AppSettings.profilestate));
            jsonObject.put("dob", AppSettings.getString(AppSettings.profiledob));
            jsonObject.put("gender", AppSettings.getString(AppSettings.profilegender));
            jsonObject.put("languageSpoken", AppSettings.getString(AppSettings.profilelanguage));
            jsonObject.put("maritalStatus", AppSettings.getString(AppSettings.profilemarital));
            jsonObject.put("numberOfKids", AppSettings.getString(AppSettings.profilenumberofkide));
            json.put(AppConstants.projectName, jsonObject);
            Log.v("finalObject", String.valueOf(json));
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
                        hitBankApi();
                        AppUtils.hideDialog();
                        Log.v("respEditProfile", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            Toast.makeText( mActivity,jsonObject1.getString("resMsg"), Toast.LENGTH_SHORT ).show();
                            AppSettings.putString(AppSettings.profilename, jsonObject1.getString("username"));
                            AppSettings.putString(AppSettings.mobile, jsonObject1.getString("phone_no"));
                            AppSettings.putString(AppSettings.profileemail, jsonObject1.getString("email"));
                            AppSettings.putString(AppSettings.profilebusness, jsonObject1.getString("businessName"));
                            AppSettings.putString(AppSettings.profilepicode, jsonObject1.getString("pinCode"));
                            AppSettings.putString(AppSettings.profileaddress1, jsonObject1.getString("address1"));
                            AppSettings.putString(AppSettings.profileaddres2, jsonObject1.getString("address2"));
                            AppSettings.putString(AppSettings.profilecity, jsonObject1.getString("city"));
                            AppSettings.putString(AppSettings.profilestate, jsonObject1.getString("stateID"));
                            AppSettings.putString(AppSettings.profilelanguage, jsonObject1.getString("languageSpoken"));
                            AppSettings.putString(AppSettings.profiledob, jsonObject1.getString("dob"));
                            AppSettings.putString(AppSettings.profilegender, jsonObject1.getString("gender"));
                            AppSettings.putString(AppSettings.profilemarital, jsonObject1.getString("maritalStatus"));
                            AppSettings.putString(AppSettings.profilenumberofkide, jsonObject1.getString("numberOfKids"));




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("respEditProfile", String.valueOf(anError));


                    }

                });

    }


    private void GetProfile() {
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.GetProfile;
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        try {

            jsonObject.put("userId", AppSettings.getString(AppSettings.userId));

            json.put(AppConstants.projectName, jsonObject);
            Log.v("finalObject", String.valueOf(json));
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

                        GetBankApi();
                       ivloader.setVisibility(View.GONE);
                        AppUtils.hideDialog();
                        Log.v("respGetProfile", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            jsonObject1.getString("resMsg");

                            AppSettings.putString(AppSettings.profilename, jsonObject1.getString("username"));
                            AppSettings.putString(AppSettings.profilenumber, jsonObject1.getString("phone_no"));
                            AppSettings.putString(AppSettings.profilename, jsonObject1.getString("username"));
                            AppSettings.putString(AppSettings.mobile, jsonObject1.getString("phone_no"));
                            AppSettings.putString(AppSettings.profileemail, jsonObject1.getString("email_id"));
                            AppSettings.putString(AppSettings.profilebusness, jsonObject1.getString("businessName"));
                            AppSettings.putString(AppSettings.profilepicode, jsonObject1.getString("pinCode"));
                            AppSettings.putString(AppSettings.profileaddress1, jsonObject1.getString("address1"));
                            AppSettings.putString(AppSettings.profileaddres2, jsonObject1.getString("address2"));
                            AppSettings.putString(AppSettings.profilelanguage, jsonObject1.getString("languageSpoken"));
                            AppSettings.putString(AppSettings.profiledob, jsonObject1.getString("dob"));
                            AppSettings.putString(AppSettings.profilegender, jsonObject1.getString("gender"));
                            AppSettings.putString(AppSettings.profilemarital, jsonObject1.getString("maritalStatus"));
                            AppSettings.putString(AppSettings.profilenumberofkide, jsonObject1.getString("numberOfKids"));

                            String city = jsonObject1.getString("city");
                            AppSettings.putString( AppSettings.city,city );
                            String stateID = jsonObject1.getString("stateID");
                            AppSettings.putString( AppSettings.stateid,stateID );


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("respEditProfile", String.valueOf(anError));


                    }

                });

    }


}
