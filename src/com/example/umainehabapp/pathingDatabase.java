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
    
    public final static String DATABASE_TABLE1 = "payload_data"; // table containing general flight data
    public final static String KEY_ROWID = "_id"; // unique number
    public final static String FLIGHT_NUMBER = "flight_number";
    public final static String PAYLOAD_WEIGHT = "weight";
    public final static String ASCENT_RATE = "ascent_rate";
    public final static String NECK_WEIGHT = "neck_weight"; // "counterbalance" weight when filling the balloon
    public final static String BURST_ALTITUDE = "burst_altitude";
    public final static String DATE = "date";
    
    public final static String DATABASE_TABLE2 = "gps_data"; // table that will have tracked gps data stored in it
    public final static String TRACKED_LONG = "tracked_longitude";
    public final static String TRACKED_LAT = "tracked_latitude";
    public final static String TRACKED_ALT = "predicted_altitude";
    public final static String PREDICTED_LONG = "predicted_longitude";
    public final static String PREDICTED_LAT = "predicted_latitude";
    public final static String PREDICTED_ALT = "predicted_altitude";
    public final static String TIME = "time_stamp";

    
    public final static int DATABASE_VERSION = 49; // change this when updating methods and data structure
    
    public final static String TABLE_CREATE1 = // payload_data
    		"CREATE TABLE " + DATABASE_TABLE1 + // creates table 2
    		" (" +
    		KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // this should correspond to the KEY_ROWID of table 1
    		FLIGHT_NUMBER + " INTEGER, " +
    		PAYLOAD_WEIGHT + " DOUBLE, " + 
    		ASCENT_RATE + " DOUBLE, " + 
    		BURST_ALTITUDE + " DOUBLE, " +
    		NECK_WEIGHT + " DOUBLE, " +
    		TIME + " TEXT);";
    
    public final static String TABLE_CREATE2 = // gps_data
    		"CREATE TABLE " + DATABASE_TABLE2 + // creates table 1
    		" (" +
    		KEY_ROWID + " INT, " + // these are the different fields
    		TRACKED_LONG + " DOUBLE, " + // tracked gps data
    		TRACKED_LAT + " DOUBLE, " + // tracked gps data
    		PREDICTED_LONG + " DOUBLE, " + // predicted gps data
    		PREDICTED_LAT + " DOUBLE, " + // predicted gps data
    		TIME + " TEXT);";
    		
    
    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE1);
            db.execSQL(TABLE_CREATE2);
            
        	ContentValues initialValues = new ContentValues();
        	
        	Time now = new Time(); // gets the current time
        	now.setToNow();
        	
        	//Cursor cur = db.query(DATABASE_TABLE1, new String[] {FLIGHT_NUMBER}, null, null, null, null, KEY_ROWID + " DESC");
        	
        	initialValues.put(TIME, now.toString());
        	initialValues.put(FLIGHT_NUMBER, 1);
        	db.insert(DATABASE_TABLE1, null, initialValues); // inserts the current time to the database
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1); // deletes the old database if i upgrade the methods
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
    
    
    public void setpayloadData(String flightnumber, Double weight, Double neck_weight, Double burst_height, Double ascent_rate) {
    	ContentValues initialValues = new ContentValues(); //adds some test cases to the database
	    initialValues.put(PAYLOAD_WEIGHT, weight);
	    initialValues.put(NECK_WEIGHT, neck_weight);
	    initialValues.put(BURST_ALTITUDE, burst_height);
	    initialValues.put(ASCENT_RATE, ascent_rate);
	    
    	mDb.update(DATABASE_TABLE1, initialValues, KEY_ROWID + " = " + flightnumber, null);
    }
    

	public void newFlight() { //starts a new flight, adds a new unique record to payload_data
		ContentValues initialValues = new ContentValues();
		Cursor cur = fetchFlightNumbers();
		
		if (cur.moveToFirst()) {
			initialValues.put(FLIGHT_NUMBER, cur.getDouble(cur.getColumnIndex(pathingDatabase.FLIGHT_NUMBER)) + 1);
		}	else initialValues.put(FLIGHT_NUMBER, 1);
			
			Time now = new Time(); //gets the current time
			now.setToNow();
			
			initialValues.put(TIME, now.toString());
			mDb.insert(DATABASE_TABLE1, null, initialValues); //inserts the current time to the database
	}


	public void deleteFlight(String flightnumber) { //deletes flight number (used with the spinner on front page)
    	/*ContentValues initialValues = new ContentValues();
    	
    	Time now = new Time(); //gets the current time
    	now.setToNow();
    	
    	initialValues.put(TIME, now.toString());*/
    	mDb.delete(DATABASE_TABLE1, KEY_ROWID + " = " + flightnumber, null);
    	
    	if (!fetchFlightNumbers().moveToLast()) {
       		newFlight();
    	}
    }
    

    public Cursor fetchFlightNumbers() { //fetches all flight numbers
    	return mDb.query(DATABASE_TABLE1, new String[] {KEY_ROWID, FLIGHT_NUMBER}, null, null, null, null, KEY_ROWID + " DESC");
    }
    
    
    public Cursor fetchGPSData(String flightnumber) { //fetches all flight numbers
    	ContentValues initialValues = new ContentValues(); //adds some test cases to the database
	    double weight = 45.0;
	    initialValues.put(PREDICTED_LONG, weight);
	    double weight1 = 45.1;
	    initialValues.put(PREDICTED_LAT, weight1);
	    double weight2 = 45.2;
	    initialValues.put(TRACKED_LONG, weight2);
	    double weight3 = 45.3;
	    initialValues.put(TRACKED_LAT,  weight3);
	    initialValues.put(KEY_ROWID, 1);
	    
	    ContentValues initialValues2 = new ContentValues(); //adds some test cases to the database
	    double weight22 = 49.4;
	    initialValues2.put(PREDICTED_LONG, weight22);
	    double weight21 = 49.3;
	    initialValues2.put(PREDICTED_LAT, weight21);
	    double weight222 = 49.5;
	    initialValues2.put(TRACKED_LONG, weight222);
	    double weight23 = 49.4;
	    initialValues2.put(TRACKED_LAT,  weight23);
	    initialValues2.put(KEY_ROWID, 1);
	    
    	ContentValues initialValues1 = new ContentValues();
	    double weight10 = -68.52;
	    initialValues1.put(PREDICTED_LONG, weight10);
	    double weight11 = 45.4;
	    initialValues1.put(PREDICTED_LAT, weight11);
	    double weight12 = 46.2;
	    initialValues1.put(TRACKED_LONG, weight12);
	    double weight13 = 46.3;
	    initialValues1.put(TRACKED_LAT,  weight13);
	    initialValues1.put(KEY_ROWID, 1);
	    
	    mDb.insert(DATABASE_TABLE2, null, initialValues1);
	    mDb.insert(DATABASE_TABLE2, null, initialValues2);
	    mDb.insert(DATABASE_TABLE2, null, initialValues);
    	
    	return mDb.query(DATABASE_TABLE2, new String[] {PREDICTED_LONG, PREDICTED_LAT, TRACKED_LONG, TRACKED_LAT}, KEY_ROWID + " = " + flightnumber, null, null, null, null);
    	// picks the four columns based on the flight number
    }
}
