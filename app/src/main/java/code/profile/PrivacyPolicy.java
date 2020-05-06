package code.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zozima.android.R;

import code.view.BaseActivity;

public class PrivacyPolicy extends BaseActivity implements View.OnClickListener {

    ImageView ivback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        findViewById();
        setLisnere();
    }

    private void setLisnere() {
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
                onBackPressed();
                break;
        }

    }
}
