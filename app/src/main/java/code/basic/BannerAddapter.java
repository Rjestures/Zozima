package code.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zozima.android.R;

import java.util.ArrayList;
import java.util.HashMap;

import code.utils.AppUtils;

public class BannerAddapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<HashMap<String, String>> images;

    public BannerAddapter(Context context, ArrayList<HashMap<String,String>> images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.bannerlayout, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_banner);
        Picasso.get().load(images.get(position).get("banner_image")).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}