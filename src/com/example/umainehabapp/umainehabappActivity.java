package com.example.umainehabapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class umainehabappActivity extends Activity {
	
	private pathingDatabase mDbHelper; //creates a database helper object to be used in accessing the database
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        mDbHelper = new pathingDatabase(this);
        mDbHelper.open();
        
        
        final Button btnhelium = (Button) findViewById(R.id.btnhelium); //button with intent to helium activity
        btnhelium.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intenthelium = new Intent(umainehabappActivity.this, heliumActivity.class);
        		startActivity(intenthelium);
        	}
        });
        
        final Button btnMap= (Button) findViewById(R.id.btnmap);
        btnMap.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent Intenttomap = new Intent(umainehabappActivity.this, map.class);
				startActivity(Intenttomap);
			}
        });
        
        

        final Button btnNewFlight = (Button) findViewById(R.id.buttonNF); //button with intent to pathing/tracking activity
        btnNewFlight.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		mDbHelper.incrementFlightNumber();
        		
        		Cursor cur = mDbHelper.fetchGPSData("1"); //fills the cursor from the database
        		startManagingCursor(cur);
        		
        		//String[] from = new String[]{pathingDatabase.TRACKED_LONG}; // create an array to specify which fields we want to display
        		//int[] to = new int[]{android.R.id.text1}; // create an array of the display item we want to bind our data to
        		
        		//CharSequence text = cur.getString(cur.getColumnIndex("tracked_longitude"));
        		
        		String str = "It doesn't work";
        		
        		if(cur.moveToFirst()) {
        			str = cur.getString(cur.getColumnIndex(pathingDatabase.PREDICTED_LAT)); //prints the latest entry for testing
        		}
        		
        		//CharSequence text = cur.getString(1);
        		int duration = Toast.LENGTH_SHORT;

        		Toast toast = Toast.makeText(getApplicationContext(), str, duration);
        		toast.show();
        		//on click, query database for highest flight number, increment and commit to database the new flight number
			}
		});
        
        
       Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
	   startManagingCursor(FNcur);
	   
	    String[] from = new String[]{pathingDatabase.KEY_ROWID}; // create an array to specify which fields we want to display
	    int[] to = new int[]{android.R.id.text1}; // create an array of the display item we want to bind our data to
	    
	    Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
	
	    SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter
	    FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    spnFlightNumber.setAdapter(FNadapter);
	    
    }
}