package code.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zozima.android.R;

public class DataBaseHelper extends SQLiteOpenHelper {
    DataBaseHelper(Context context) {
        super(context, context.getString(R.string.app_name), null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableApartment.SQL_CREATE_APARTMENT);
        db.execSQL(TableFavourite.SQL_CREATE_FAVOURITE);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableApartment.apartment);
            db.execSQL("DROP TABLE IF EXISTS " + TableFavourite.favourite);

            onCreate(db);
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
