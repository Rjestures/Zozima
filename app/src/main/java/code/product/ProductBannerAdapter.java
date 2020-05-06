package code.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zozima.android.R;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

public abstract class ProductBannerAdapter extends PagerAdapter {

    private ArrayList<HashMap<String, String>> images;

    private LayoutInflater inflater;
    public View.OnClickListener viewClickListener;
    private Context context;


    public ProductBannerAdapter(Context context, ArrayList<HashMap<String,String>> images) {

        this.context = context;

        this.images=images;

        inflater = LayoutInflater.from(context);

        viewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClick(v, String.valueOf(v.getTag()));
            }
        };
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View myImageLayout = inflater.inflate(R.layout.inflate_productgallery, view, false);

        final ImageView myImage = (ImageView) myImageLayout

                .findViewById(R.id.img_banner);

        Picasso.get().load(images.get(position).get("productImage")).placeholder(R.mipmap.logo_grey).into(myImage);
   /*     PhotoViewAttacher photoAttacher;
        photoAttacher= new PhotoViewAttacher(myImage);
        photoAttacher.update();*/

       /*{


            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                myImage.setImageBitmap(bitmap);
                myImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });*/
        view.addView(myImageLayout);

        myImageLayout.setOnClickListener(viewClickListener);

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return  view.equals(o);
    }


    protected abstract void viewClick(View view, String str);



}
