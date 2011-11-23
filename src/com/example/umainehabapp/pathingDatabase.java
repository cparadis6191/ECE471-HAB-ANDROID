package com.example.umainehabapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class pathingDatabase { //this draws heavily from notepadv3 tutorial
	//PLEASE READ!
	//There will three separate tables (as of now).
	//One will contain flight numbers and flight information
	//The others will contain GPS data for each flight: predicted flights and tracked flights
	//things will be divided by flight numbers
	//a flight number will be chosen (or a new one created) at the main activity screen
	
	public static final String DATABASE_NAME = "habappDatabase";

    private static final String TAG = "PathingDatabase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table (_flightnumber integer primary key autoincrement, "
        + "title text not null, body text not null);";

    private static final String DATABASE_TABLE1 = "gps_data"; //table that will have tracked gps data stored in it
    private static final String TRACKED_LONG = "tracked_longitude";
    private static final String TRACKED_LAT = "tracked_latitude";
    private static final String PREDICTED_LONG = "predicted_longitude";
    private static final String PREDICTED_LAT = "predicted_latitude";
    private static final String TIME = "time_stamp";
    
    private static final String DATABASE_TABLE2 = "payload_data"; //table containing general flight data
    private static final String KEY_ROWID = "_flightnumber";
    private static final String PAYLOAD_WEIGHT = "weight";
    private static final String ASCENT_RATE = "ascent_rate";    
    private static final String BURST_ALTITUDE = "burst_altitude";
    private static final String DATE = "date";
    
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    
    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(DATABASE_CREATE);
            
            /* Create a Table in the Database. */
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE1 + //creates table 1
            		"(" +
            		KEY_ROWID + " INT, " + //these are the different fields
            		TRACKED_LONG + " TEXT, " + //tracked gps data
            		TRACKED_LAT + " TEXT, " + //tracked gps data
            		PREDICTED_LONG + " TEXT, " + //predicted gps data
            		PREDICTED_LAT + " TEXT, " + //predicted gps data
            		TIME + " TEXT)");
            
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE2 + //creates table 2
            		"(" +
            		KEY_ROWID + " INT, " + 
            		PAYLOAD_WEIGHT + " DOUBLE, " + 
            		ASCENT_RATE + " DOUBLE, " + 
            		BURST_ALTITUDE + " DOUBLE, " +
            		DATE + " DATE)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public pathingDatabase(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public pathingDatabase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public Cursor fetchFlightNumbers(){ //fetches all flight numbers
    	return mDb.query(DATABASE_TABLE2, new String[]{KEY_ROWID, KEY_ROWID}, null, null, null, null, null);
    }
}
