package com.example.umainehabapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class umainehabappActivity extends Activity {
	
	private pathingDatabase mDbHelper = new pathingDatabase(this); //creates a database helper object to be used in accessing the database
    	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        mDbHelper.open(); //opens the database
        
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
        		Intenttomap.putExtra("flightnumber", getspnFNvalue());
				startActivity(Intenttomap);
			}
        });
        
        

        final Button btnNewFlight = (Button) findViewById(R.id.buttonNF); //button with intent
        btnNewFlight.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		mDbHelper.incrementFlightNumber(); //increments the flight number
        		populatespnFlightNumber();
			}
		});
        
        populatespnFlightNumber();

    }


    void populatespnFlightNumber() { //populates the spinner
    	final Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
        Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
        startManagingCursor(FNcur);

        String[] from = new String[] {pathingDatabase.KEY_ROWID}; // create an array to specify which fields we want to display
	    int[] to = new int[] {android.R.id.text1}; // create an array of the display item we want to bind our data to

	    SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter
	    FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    spnFlightNumber.setAdapter(FNadapter);
	}
    
    String getspnFNvalue() {
    	final Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
        Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
        startManagingCursor(FNcur);

        String[] from = new String[] {pathingDatabase.KEY_ROWID}; // create an array to specify which fields we want to display
	    int[] to = new int[] {android.R.id.text1}; // create an array of the display item we want to bind our data to

	    SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter
	    FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Cursor cursor = (Cursor) FNadapter.getItem(spnFlightNumber.getSelectedItemPosition());
        return cursor.getString(cursor.getColumnIndex(pathingDatabase.KEY_ROWID));
    }
}