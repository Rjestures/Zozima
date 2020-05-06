package code.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zozima.android.R;

import code.common.SmsBroadcastReceiver;
import code.login.LoginActivity;
import code.view.BaseActivity;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{

    //ViewPager
    private ViewPager viewPager;

    //MyViewPagerAdapter
     MyViewPagerAdapter myViewPagerAdapter;

    //LinearLayout
    private LinearLayout dotsLayout;

    //TextView
    private TextView[] dots;

    private int[] layouts;

    //Button
    private Button btnNext;

    //TextView
    private TextView tvSkip;

    int page=0;

    SmsBroadcastReceiver mSmsBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*mSmsBroadcastReceiver = new SmsBroadcastReceiver();*/

        //viewPager
        viewPager    =  findViewById(R.id.viewPagers);

        //LinearLayout
        dotsLayout   =  findViewById(R.id.llDots);

        //Button
        btnNext         =  findViewById(R.id.btnNext);

        //TextView
        tvSkip         =  findViewById(R.id.tvSkip);

        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnNext.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
    }

    private void addBottomDots(int currentPage) {

        page = currentPage;

        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            if(i==currentPage)
            {
                dots[i] = new TextView(this);
                dots[i].setTextSize(35);
                dots[i].setText(Html.fromHtml("\u25C9"));
                dots[i].setTextColor(getResources().getColor(R.color.colorAccent));
                dots[i].setGravity(Gravity.CENTER);
                dots[i].setPadding(5,-14,5,0);
                dotsLayout.addView(dots[i]);
            }
            else
            {
                dots[i] = new TextView(this);
                dots[i].setTextSize(25);
                dots[i].setText(Html.fromHtml("\u25CF"));
                dots[i].setTextColor(getResources().getColor(R.color.grey));
                dots[i].setGravity(Gravity.CENTER);
                dots[i].setPadding(5,0,5,0);
                dotsLayout.addView(dots[i]);
            }
        }

        btnNext.setText(getString(R.string.next));
        if(currentPage==2)
        {
            btnNext.setText(getString(R.string.getStarted));
        }

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(mActivity, LoginActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnNext:

                if(page==2)
                {
                    launchHomeScreen();
                }
                else
                {
                    viewPager.setCurrentItem(page+1);
                }

                break;

            case R.id.tvSkip:

                launchHomeScreen();

                break;

            default:

                break;
        }

    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
