package code.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zozima.android.R;

import code.view.BaseActivity;
import code.view.CustomTextView;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    //LinearLayout
    LinearLayout lcontactnumber,email;

    //CustomTextView
   TextView tvemail;

    //ImageView
    ImageView ivback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_help );
        findViewById(  );
        listenere();
    }

    private void listenere() {
        lcontactnumber.setOnClickListener( this );
        email.setOnClickListener( this );
        ivback.setOnClickListener( this );
    }

    private void findViewById() {
        //LinearLayout
        lcontactnumber=findViewById( R.id.lcontactnumber );
        email=findViewById( R.id.email );

        //CustomTextView
        tvemail=findViewById( R.id.tvemail );

        //ImageView
        ivback=findViewById( R.id.ivback );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lcontactnumber:
                String phone = "9935825142";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;

            case  R.id.email:
                ClipboardManager clipboard = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE );
                ClipData clip = ClipData.newPlainText( "label",getString(R.string.gmail) );
                Intent intent1=new Intent(Intent.ACTION_SEND);
                String[] recipients={getString(R.string.gmail)};
                intent1.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent1.setType("text/html");
                intent1.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent1, "Send mail"));
                clipboard.setPrimaryClip( clip );
                break;

            case R.id.ivback:
                onBackPressed();
                break;
        }

    }

}
