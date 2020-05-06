package code.common;

import android.app.Application;
import android.content.Context;

import code.database.DatabaseController;


public class MyApplication extends Application {

    private static MyApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        DatabaseController.openDataBase(this);
        //Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //MultiDex.install(this);
    }


    public static synchronized MyApplication getInstance() {
        return mainApplication;
    }
}
