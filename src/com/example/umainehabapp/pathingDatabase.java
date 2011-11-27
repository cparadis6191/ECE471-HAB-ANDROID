package com.example.umainehabapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

//this draws heavily from notepadv3 tutorial
//PLEASE READ!
//There will three separate tables (as of now).
//One will contain flight numbers and flight information
//The others will contain GPS data for each flight: predicted flights and tracked flights
//things will be divided by flight numbers
//a flight number will be chosen (or a new one created) at the main activity screen

public class pathingDatabase { 	
	public final static String DATABASE_NAME = "habappDatabase";

    private final static String TAG = "PathingDatabase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    /**
     * Database creation sql statement
     */
    
    public final static String DATABASE_TABLE1 = "payload_data"; //table containing general flight data
    public final static String KEY_ROWID = "_id";
    public final static String PAYLOAD_WEIGHT = "weight";
    public final static String ASCENT_RATE = "ascent_rate";
    public final static String NECK_WEIGHT = "neck_weight"; //"counterbalance" weight when filling the balloon
    public final static String BURST_ALTITUDE = "burst_altitude";
    public final static String DATE = "date";
    
    public final static String DATABASE_TABLE2 = "gps_data"; //table that will have tracked gps data stored in it
    public final static String TRACKED_LONG = "tracked_longitude";
    public final static String TRACKED_LAT = "tracked_latitude";
    public final static String PREDICTED_LONG = "predicted_longitude";
    public final static String PREDICTED_LAT = "predicted_latitude";
    public final static String TIME = "time_stamp";

    
    public final static int DATABASE_VERSION = 6; //change this when updating methods and data structure
    
    public final static String TABLE_CREATE1 = //payload_data
    		"CREATE TABLE " + DATABASE_TABLE1 + //creates table 2
    		" (" +
    		KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //this should correspond to the KEY_ROWID of table 1
    		PAYLOAD_WEIGHT + " DOUBLE, " + 
    		ASCENT_RATE + " DOUBLE, " + 
    		BURST_ALTITUDE + " DOUBLE, " +
    		NECK_WEIGHT + " DOUBLE, " +
    		TIME + " TEXT);";
    
    public final static String TABLE_CREATE2 = //gps_data
    		"CREATE TABLE " + DATABASE_TABLE2 + //creates table 1
    		" (" +
    		KEY_ROWID + " INT, " + //these are the different fields
    		TRACKED_LONG + " TEXT, " + //tracked gps data
    		TRACKED_LAT + " TEXT, " + //tracked gps data
    		PREDICTED_LONG + " TEXT, " + //predicted gps data
    		PREDICTED_LAT + " TEXT, " + //predicted gps data
    		TIME + " TEXT);";
    		
    
    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(DATABASE_CREATE);
            
            /* Create a Table in the Database. */
            db.execSQL(TABLE_CREATE1);
            
            db.execSQL(TABLE_CREATE2);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1); //deletes the old database if i upgrade the methods
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
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
    
    public void incrementFlightNumber() { //starts a new flight, adds a new unique record to payload_data
    	ContentValues initialValues = new ContentValues();
    	
    	Time now = new Time();
    	now.setToNow();
    	
    	initialValues.put(TIME, now.toString());
    	mDb.insert(DATABASE_TABLE1, null, initialValues);
    }
    
    public Cursor fetchFlightNumbers() { //fetches all flight numbers
    	return mDb.query(DATABASE_TABLE1, new String[] {KEY_ROWID}, null, null, null, null, null);
    }
    
    public Cursor test() {
	    ContentValues initialValues = new ContentValues();
	    double weight = 450;
	    initialValues.put(TRACKED_LONG, weight);
	    mDb.insert(DATABASE_TABLE1, null, initialValues);
	    
	    return mDb.query(DATABASE_TABLE1, new String[] {KEY_ROWID, TRACKED_LONG}, null, null, null, null, null);
    }
}
