package sg.edu.np.tracknshare.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import sg.edu.np.tracknshare.models.MyLatLng;

public class TrackingDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tracking.db";
    public static final String TABLE_USERS = "points";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LONG = "long";

    public TrackingDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    public TrackingDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LAT + " REAL,"
                + COLUMN_LONG + " REAL"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    public void delelteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();

        Log.e("Location", "delete");
        ArrayList<MyLatLng> pList = getAllPoints();
        Log.e("Location", "delete "+pList.size());
    }
    public void addRun(double lat, double longd) {
        Log.e("Location", "add"+lat+"-"+longd);
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LONG, longd);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public ArrayList<MyLatLng> getAllPoints(){
        String query = "SELECT * FROM "+ TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<MyLatLng> pList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                Log.e("Location", cursor.getString(0)+"-"+cursor.getDouble(1)+"-"+cursor.getDouble(2));
                pList.add(new MyLatLng(cursor.getDouble(1), cursor.getDouble(2)));
            }while(cursor.moveToNext());
            pList.remove(0);
        }
        cursor.close();
        db.close();
        return pList;
    }
}
