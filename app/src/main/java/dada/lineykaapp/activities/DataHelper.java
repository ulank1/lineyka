package dada.lineykaapp.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dastan on 13.05.2016.
 */
public class DataHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydatabase.db";

    public static final String TABLE = "D";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String PRESSURE = "pressure";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE + "(" +
                BaseColumns._ID + " integer primary key autoincrement," +
                NAME + " text," +
                PRESSURE + " text," +
                DATE + " text," +
                DESCRIPTION + " text," +
                IMAGE + " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor getPrice() {
        return getReadableDatabase().query(DataHelper.TABLE,
                null, null, null,
                null, null, null);
    }

    public void deletePrice() {
        getWritableDatabase().delete(TABLE, null, null);
    }

    public void addPrice(D d) {
        ContentValues values = new ContentValues();

        Calendar calendar = Calendar.getInstance();

        String date;
        date = calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR);
        date = date+" : "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);

       values.put(NAME,d.getName());
       values.put(DATE, date);
       values.put(PRESSURE,d.getPressure());
       values.put(DESCRIPTION,d.getDescription());
       values.put(IMAGE,d.getImage());
        getWritableDatabase().insert(TABLE, null, values);
    }




}