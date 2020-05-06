package code.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zozima.android.R;

import code.view.BaseActivity;

public class Abouts extends BaseActivity implements View.OnClickListener {

    ImageView ivback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);
        findViewById();
        setListener();
    }

    private void setListener() {
        ivback.setOnClickListener(this);
    }

    private void findViewById() {
        ivback=findViewById(R.id.ivback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivback:
                finish();
                break;
        }

    }
}
