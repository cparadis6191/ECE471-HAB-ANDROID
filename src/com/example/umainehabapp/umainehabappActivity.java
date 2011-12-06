package com.example.umainehabapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class umainehabappActivity extends Activity {
	
	private pathingDatabase mDbHelper = new pathingDatabase(this); //creates a database helper object to be used in accessing the database
    	
    /** Called when the activity is first created. */

	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        mDbHelper.open(); //opens the database
        
        final Button btnhelium = (Button) findViewById(R.id.btnhelium); //button with intent to helium activity
        btnhelium.getBackground().setAlpha(175);
        btnhelium.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intenthelium = new Intent(umainehabappActivity.this, heliumActivity.class);
        		intenthelium.putExtra("flightnumber", getspnFNvalue());
        		startActivity(intenthelium);
        	}
        });
        
        final Button btnMap= (Button) findViewById(R.id.btnmap);
        btnMap.getBackground().setAlpha(175);
        btnMap.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intenttomap = new Intent(umainehabappActivity.this, map.class);
        		intenttomap.putExtra("flightnumber", getspnFNvalue());
				startActivity(intenttomap);
			}
        });
        
        final Button btnNewFlight = (Button) findViewById(R.id.btnNF); //button with intent
        btnNewFlight.getBackground().setAlpha(175);
        btnNewFlight.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		mDbHelper.newFlight(); //increments the flight number
        		populatespnFlightNumber(); //updates the spinner so the new flight is shown
			}
		});
        
        populatespnFlightNumber(); //populate the spinner
        
    	final Button btndeleteFlight = (Button) findViewById(R.id.btndeleteFlight);
    	btndeleteFlight.getBackground().setAlpha(175);
    	btndeleteFlight.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Context context = getApplicationContext();
        		CharSequence text = getspnFNvalue();
        		int duration = Toast.LENGTH_SHORT;

        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();        		
        		
        		getspnFNvalue();
        		mDbHelper.deleteFlight(getspnFNvalue());
        		populatespnFlightNumber();
        	}});
    

    	final Button habhubbutton = (Button) findViewById(R.id.habhub);
    	habhubbutton.getBackground().setAlpha(175);
 

    	habhubbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intenttohabhub = new Intent(umainehabappActivity.this, habhub.class);
        		startActivity(intenttohabhub);
        	}});
    }
    	
    	
    void populatespnFlightNumber() { //populates the spinner
    	final Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
    	spnFlightNumber.getBackground().setAlpha(175);
    	Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
        startManagingCursor(FNcur);

        String[] from = new String[] {pathingDatabase.FLIGHT_NUMBER, pathingDatabase.KEY_ROWID}; // create an array to specify which fields we want to display
	    int[] to = new int[] {android.R.id.text1, android.R.id.text2}; // create an array of the display item we want to bind our data to

	    SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter
	    FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    spnFlightNumber.setAdapter(FNadapter);
	}
    
    
    String getspnFNvalue() { // returns the value of the selected item
    	final Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
        Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
        startManagingCursor(FNcur);

        String[] from = new String[] {pathingDatabase.FLIGHT_NUMBER, pathingDatabase.KEY_ROWID}; // create an array to specify which fields we want to display
	    int[] to = new int[] {android.R.id.text1, android.R.id.text2}; // create an array of the display item we want to bind our data to

	    SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter filled with data from the database
	    FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
	    Cursor cursor = (Cursor) FNadapter.getItem(spnFlightNumber.getSelectedItemPosition());
	    
        String selecteditem = cursor.getString(cursor.getColumnIndex(pathingDatabase.KEY_ROWID));
        
        return selecteditem;
    }
}