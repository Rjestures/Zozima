package code.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;
import com.zozima.android.R;

import code.utils.AppConstants;
import code.view.BaseActivity;

public class ProductGalleryActivity extends BaseActivity {

    //Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private Matrix matrix = new Matrix();
    ViewPager ivProductPic;
    public static String url="";
    ImageView ivcross;
    CirclePageIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_gallery);
        ivProductPic=findViewById(R.id.imageView40);
        ivcross=findViewById(R.id.ivCross);
        indicator = findViewById(R.id.indicator);
        ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,ProductAddToCardActivity.class));

            }
        });

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override

            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override

            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ivProductPic.setAdapter(new PhotoGalleryAddapter(ProductGalleryActivity.this, AppConstants.imageList) {
            @Override
            protected void viewClick(View view, String str) {

            }
        });


        indicator.setViewPager(ivProductPic);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(6 * density);


    }

   /* public void usingSimpleImage(ImageView imageView) {
        ImageAttacher mAttacher = new ImageAttacher(imageView);
        ImageAttacher.MAX_ZOOM = 4.0f; // times the current Size
        ImageAttacher.MIN_ZOOM = 1.0f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
    }
    private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            //ivProductPic.setImageMatrix(matrix);
            return true;
        }
    */


}
