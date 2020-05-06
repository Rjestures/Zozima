package code.slider;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zozima.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CardPagerAdapter extends PagerAdapter
{
    public OnClickListener clickListener;

    //Context
    Context context;

    //ArrayList
    ArrayList<HashMap<String, String>> data;

    //List
    private List<String> mData = new ArrayList();
    private List<CardView> mViews = new ArrayList();

    protected abstract void onCategoryClick(View view, String str);

    public CardPagerAdapter(Context context, ArrayList<HashMap<String, String>> banner_list) {

        this.context = context;

        this.data = banner_list;

        for (int i = 0; i < this.data.size(); i++) {
            this.mData.add("");
            this.mViews.add(null);
        }

        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClick(v, String.valueOf(v.getTag()));
            }
        };
    }

    public int getCount() {
        return this.data.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.inflate_pager_adapter, container, false);

        container.addView(view);

        CardView cardView =  view.findViewById(R.id.cardView);

        ImageView imageView =  view.findViewById(R.id.imageView);

        if (data.get(position).get("bannerImage").equalsIgnoreCase("")) {

            Picasso.get().load(R.mipmap.logo_grey).resize(300,300).into(imageView);
        } else {
            Picasso.get().load(data.get(position).get("bannerImage")).resize(300,300).into(imageView);
            //Picasso.with(this.context).load((String) ((HashMap) this.data.get(position)).get("banner_image")).resize(300,300).into(iv);
        }

        view.setTag(position);

        view.setOnClickListener(clickListener);

        this.mViews.set(position, cardView);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        this.mViews.set(position, null);
    }
}
