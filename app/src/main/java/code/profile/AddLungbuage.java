package code.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.zozima.android.R;

import code.database.AppSettings;
import code.main.MainActivity;
import code.utils.AppUtils;
import code.view.BaseActivity;

public class AddLungbuage extends BaseActivity implements View.OnClickListener {

    ImageView ivback;
    RadioButton radiobtn1,radiobtn2;
    RelativeLayout rQrecondLanguage,rrfirstLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lungbuage);
        findViewById();
        setListener();

        Log.v("Mylnaguage","GOof"+AppSettings.getString(AppSettings.language_code));

        if(AppSettings.getString(AppSettings.language_code).isEmpty())
        {
            radiobtn1.setChecked(true);
            radiobtn2.setChecked(false);
            AppSettings.putString(AppSettings.language_code, "En");
            Log.v("english",AppSettings.getString(AppSettings.language_code));
            AppUtils.SettingLanguage(mActivity);
        }
        else if(AppSettings.getString(AppSettings.language_code).equalsIgnoreCase("hi")){

            radiobtn2.setChecked(true);
            radiobtn1.setChecked(false);
            AppSettings.putString(AppSettings.language_code, "hi");
            Log.v("hindi",AppSettings.getString(AppSettings.language_code));
            AppUtils.SettingLanguage(mActivity);

        }
        else
         {
            radiobtn1.setChecked(true);
            radiobtn2.setChecked(false);
            AppSettings.putString(AppSettings.language_code, "En");
            Log.v("english",AppSettings.getString(AppSettings.language_code));
            AppUtils.SettingLanguage(mActivity);
        }



    }

    private void setListener() {
        ivback.setOnClickListener(this);
        radiobtn1.setOnClickListener(this);
        radiobtn2.setOnClickListener(this);
        rrfirstLanguage.setOnClickListener(this);
        rQrecondLanguage.setOnClickListener(this);
    }

    private void findViewById() {
        ivback=findViewById(R.id.ivback);
        radiobtn1=findViewById(R.id.radiobtn1);
        radiobtn2=findViewById(R.id.radiobtn2);
        rQrecondLanguage=findViewById(R.id.rQrecondLanguage);
        rrfirstLanguage=findViewById(R.id.rrfirstLanguage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivback:
                finish();
                break;

            case R.id.radiobtn1:
                radiobtn2.setChecked(false);
                radiobtn1.setChecked(true);
                AppSettings.putString(AppSettings.language_code, "En");
                AppSettings.putString(AppSettings.language, "sailjsa");
                AppUtils.SettingLanguage(mActivity);
                startActivity(new Intent(mActivity, MainActivity.class));
                break;

            case R.id.rrfirstLanguage:
                radiobtn2.setChecked(false);
                radiobtn1.setChecked(true);
                AppSettings.putString(AppSettings.language_code, "En");
                AppSettings.putString(AppSettings.language, "sailjsa");
                AppUtils.SettingLanguage(mActivity);
                startActivity(new Intent(mActivity, MainActivity.class));
                break;




            case R.id.radiobtn2:
                radiobtn1.setChecked(false);
                radiobtn2.setChecked(true);
                AppSettings.putString(AppSettings.language_code, "hi");
                AppSettings.putString(AppSettings.languageValue, "2");
                AppSettings.putString(AppSettings.language, "Princy");
                AppUtils.SettingLanguage(mActivity);
                startActivity(new Intent(mActivity, MainActivity.class));
                break;

            case R.id.rQrecondLanguage:

                radiobtn1.setChecked(false);
                radiobtn2.setChecked(true);
                AppSettings.putString(AppSettings.language_code, "hi");
                AppSettings.putString(AppSettings.languageValue, "2");
                AppSettings.putString(AppSettings.language, "Princy");
                AppUtils.SettingLanguage(mActivity);
                startActivity(new Intent(mActivity, MainActivity.class));
                break;






        }

    }
}
