package code.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zozima.android.R;

import code.basic.CartAcivity;
import code.basic.WishlistAcitvity;
import code.common.GetBackFragment;
import code.database.AppSettings;
import code.fragment.CollectionsFragment;
import code.fragment.HomeFragment;
import code.fragment.AccountFragment;
import code.notification.NotificationList;
import code.searching.SearchActivity;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Fragment fragment;

    //LinearLayout
    LinearLayout llHome,llCollections,llCart, llAccount,llNotification;

    //ImageView
    ImageView ivHome,ivCollections,ivCart,ivYou,ivNotification,ivSearch,ivcart,ivFavourite;

    //TextView
    TextView tvHome,tvCollections,tvCart,tvYou,tvNotification,tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
        Intent intent=getIntent();

        if(intent.hasExtra("pagePathh")){
            displayView(4);
        }
        else if(intent.hasExtra("pagePath"))
        {
            displayView(1);
        }


    }

    private void findViewById() {

        //LinearLayout
        llHome  =   findViewById(R.id.llHome);
        llCollections  =   findViewById(R.id.llCollections);
        llCart  =   findViewById(R.id.llCart);
        llAccount =   findViewById(R.id.llAccount);
        llNotification =   findViewById(R.id.llNotification);

        //ImageView
        ivHome  =   findViewById(R.id.ivHome);
        ivCollections  =   findViewById(R.id.ivCollections);
        ivCart  =   findViewById(R.id.ivCart);
        ivcart  =   findViewById(R.id.ivcart);
        ivYou  =   findViewById(R.id.ivAccount);
        ivNotification  =   findViewById(R.id.ivNotification);
        ivSearch  =   findViewById(R.id.ivSearch);
        ivFavourite  =   findViewById(R.id.ivFavourite);

        //TextView
        tvHome =   findViewById(R.id.tvHome);
        tvCollections =   findViewById(R.id.tvCollections);
        tvCart =   findViewById(R.id.tvCart);
        tvYou =   findViewById(R.id.tvAccount);
        tvNotification =   findViewById(R.id.tvNotification);
        tvCount =   findViewById(R.id.tvCount);

        //setOnClickListener
        llHome.setOnClickListener(this);
        llCollections.setOnClickListener(this);
        llCart.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivcart.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);

        displayView(1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.llHome:

                displayView(1);

                break;

            case R.id.llCollections:

                displayView(2);

                break;

            case R.id.llCart:

                startActivity( new Intent( mActivity, CartAcivity.class ) );

                break;

            case R.id.ivFavourite:

                startActivity( new Intent( mActivity, WishlistAcitvity.class ) );

                break;

            case R.id.llAccount:

                displayView(4);


                break;
            case R.id.llNotification:

                startActivity( new Intent( mActivity, NotificationList.class ) );

                break;
            case R.id.ivSearch:

                startActivity( new Intent( mActivity, SearchActivity.class ) );

                break;
            case R.id.ivcart:

                startActivity( new Intent( mActivity, CartAcivity.class ) );

                break;


            default:

                break;
        }
    }

    private void setDefaults() {
        AppUtils.changeIconColor(ivHome,mActivity,R.color.black);
        AppUtils.changeIconColor(ivCollections,mActivity,R.color.black);
        AppUtils.changeIconColor(ivCart,mActivity,R.color.black);
        AppUtils.changeIconColor(ivYou,mActivity,R.color.black);
        AppUtils.changeIconColor(ivNotification,mActivity,R.color.black);

        AppUtils.changeTextColor(tvHome,mActivity,R.color.black);
        AppUtils.changeTextColor(tvCollections,mActivity,R.color.black);
        AppUtils.changeTextColor(tvCart,mActivity,R.color.black);
        AppUtils.changeTextColor(tvYou,mActivity,R.color.black);
        AppUtils.changeTextColor(tvNotification,mActivity,R.color.black);
    }

    private void displayView(int position){

        switch (position){

            case 1:

                setDefaults();
                AppUtils.changeIconColor(ivHome,mActivity,R.color.colorAccent);
                AppUtils.changeTextColor(tvHome,mActivity,R.color.colorAccent);

                GetBackFragment.Addpos(position);
                fragment = new HomeFragment();

                break;

            case 2:

                setDefaults();
                AppUtils.changeIconColor(ivCollections,mActivity,R.color.colorAccent);
                AppUtils.changeTextColor(tvCollections,mActivity,R.color.colorAccent);

                GetBackFragment.Addpos(position);
                fragment = new CollectionsFragment();

                break;

            case 3:

                setDefaults();
                AppUtils.changeIconColor(ivCart,mActivity,R.color.colorAccent);
                AppUtils.changeTextColor(tvCart,mActivity,R.color.colorAccent);
                startActivity( new Intent( mActivity, CartAcivity.class ) );



                break;

            case 4:

                setDefaults();
                AppUtils.changeIconColor(ivYou,mActivity,R.color.colorAccent);
                AppUtils.changeTextColor(tvYou,mActivity,R.color.colorAccent);

                GetBackFragment.Addpos(position);
                fragment = new AccountFragment();

                break;
            case 5:

                setDefaults();
                AppUtils.changeIconColor(ivNotification,mActivity,R.color.colorAccent);
                AppUtils.changeTextColor(tvNotification,mActivity,R.color.colorAccent);
                startActivity( new Intent( mActivity, NotificationList.class ) );



                break;


            default:
                break;
        }


        if (fragment != null) {

            AppUtils.hideSoftKeyboard(mActivity);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.popBackStack(fragment.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

            FragmentTransaction tx = fragmentManager.beginTransaction();

            // tx.add(R.id.container,fragment).addToBackStack(fragment.toString());
            tx.replace(R.id.container, fragment).addToBackStack(fragment.toString());

            tx.commit();
            // ====to clear unused memory==

            System.gc();

        } else {
            // error in creating fragment
            Log.e("ImageDataActivity", "Error in creating fragment");
        }
    }
    boolean doubleBackToExitPressedOnce = false;
    Handler myHandler;
    Runnable myRunnable;
    Toast myToast;

    @Override
    public void onBackPressed() {
      super.onBackPressed();

}
    @Override
    public void onResume() {

        super.onResume();

        try {
            int count = Integer.parseInt( AppSettings.getString( AppSettings.total_count ) );

            if (count > 0) {
                tvCount.setVisibility( View.VISIBLE );
                tvCount.setText( String.valueOf( count ) );
            } else {
                tvCount.setVisibility( View.GONE );
            }

        } catch (NumberFormatException ex) { // handle your exception
            tvCount.setVisibility( View.GONE );

        }
    }


        @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}