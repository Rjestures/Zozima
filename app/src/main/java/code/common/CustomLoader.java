package code.common;

import android.app.Dialog;
import android.content.Context;

import com.zozima.android.R;


/**
 * Created by admin on 4/9/2016.
 */


public class CustomLoader extends Dialog {

    public CustomLoader(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomLoader(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.custom_dialog);
    }

    public CustomLoader(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

}
