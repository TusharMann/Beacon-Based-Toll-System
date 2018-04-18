package org.altbeacon.beaconreference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tushar on 21-06-2016.
 */
public class TT_Sqlite extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Number_Plate_Database";
    public final static String Tname = "NUMDETAILS";
    public final static String rc = "RC";
    public final static String pnum = "NUMBER_PLATE";




    public TT_Sqlite(Context context, int version){
        super(context,DATABASE_NAME,null,version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String query="CREATE TABLE "+Tname+"("+pnum +" VARCHAR(50),"+rc+" VARCHAR(50));";
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
