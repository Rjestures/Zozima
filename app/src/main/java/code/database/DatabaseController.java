package code.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import code.utils.AppUtils;

public class DatabaseController {
    public static SQLiteDatabase myDataBase;
    private static DataBaseHelper myDataBaseHelper;

    public static void openDataBase(Context mContext) {
        if (myDataBaseHelper == null) {
            myDataBaseHelper = new DataBaseHelper(mContext);
        }
        if (myDataBase == null) {
            myDataBase = myDataBaseHelper.getWritableDatabase();
        }
    }

    public static void removeTable(String tableName) {
        myDataBase.delete(tableName, null, null);
    }

    public static long insertData(ContentValues values, String tableName) {
        long id = -1;
        try {
            id = myDataBase.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppUtils.print("====insertData " + id);
        return id;
    }

    public static long insertUpdateData(ContentValues values, String tableName, String columnName, String uniqueId) {
        try {
            if (checkRecordExist(tableName, columnName, uniqueId)) {
                return (long) myDataBase.update(tableName, values, columnName + "='" + uniqueId + "'", null);
            }
            return myDataBase.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean checkRecordExist(String tableName, String columnName, String uniqueId) {
        boolean status = false;
        Cursor cursor = myDataBase.query(tableName, null, columnName + "='" + uniqueId + "'", null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                status = true;
            }
            cursor.close();
        }
        return status;
    }

    public static void updateEqual(ContentValues mContentValues, String tableName, String columnName, String columnValue) {
        myDataBase.update(tableName, mContentValues, columnName + " = '" + columnValue + "'", null);
    }

    public static void updateNotEqual(ContentValues mContentValues, String tableName, String columnName, String columnValue) {
        myDataBase.update(tableName, mContentValues, columnName + " != '" + columnValue + "'", null);
    }

    public static long insertUpdateNotData(ContentValues values, String tableName, String columnName, String uniqueId) {
        try {
            if (checkRecordExist(tableName, columnName, uniqueId)) {
                return (long) myDataBase.update(tableName, values, columnName + "!='" + uniqueId + "'", null);
            }
            return myDataBase.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long insertUpdateDataMultiple(ContentValues values, String tableName, String where) {
        try {
            if (checkRecordExistMultiple(tableName, where)) {
                return (long) myDataBase.update(tableName, values, where, null);
            }
            return myDataBase.insert(tableName, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    public static boolean checkRecordExistWhere(String tableName, String where) {
        boolean status = false;
        Cursor cursor = myDataBase.query(tableName, null, where, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                status = true;
            }
            cursor.close();
        }
        return status;
    }

    public static boolean checkRecordExistMultiple(String tableName, String where) {
        boolean status = false;
        Cursor cursor = myDataBase.query(tableName, null, where, null, null, null, null);
        AppUtils.print("isDataExit-SubCategory" + cursor.getCount());
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                status = true;
            }
            cursor.close();
        }
        return status;
    }

    public static long isDataExit(String tableName) {
        long cnt = DatabaseUtils.queryNumEntries(myDataBase, tableName);
        AppUtils.print("isDataExit " + cnt);
        Log.d("isDataExit", String.valueOf(cnt));
        return cnt;
    }

    public static long isDataExitWhere(String tableName,String where) {
        long cnt = DatabaseUtils.queryNumEntries(myDataBase, tableName,where);
        AppUtils.print("isDataExit " + cnt);
        Log.d("isDataExit", String.valueOf(cnt));
        return cnt;
    }

    public static boolean deleteRow(String tableName, String keyName, String keyValue) {
        try {
            return myDataBase.delete(tableName, new StringBuilder().append(keyName).append("=").append(keyValue).toString(), null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static int delete(String tableName, String where, String[] args) {
        return myDataBase.delete(tableName, where, args);
    }
}
