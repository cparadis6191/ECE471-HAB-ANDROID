package com.example.umainehabapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
	    		showAddFlightDialog();
	    		
	    		String URL = "http://weather.uwyo.edu/cgi-bin/balloon_traj?TIME=2011120612&FCST=6&POINT=none&LAT=45&LON=45&TOP=30000&OUTPUT=kml&Submit=Submit&.cgifields=POINT&.cgifields=FCST&.cgifields=TIME&.cgifields=OUTPUT";
	    		//DownloadFromUrl(URL, "blah.txt");
			}
		});
	    
	    populatespnFlightNumber(); //populate the spinner
	    
		final Button btndeleteFlight = (Button) findViewById(R.id.btndeleteFlight);
		btndeleteFlight.getBackground().setAlpha(175);
		btndeleteFlight.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		showDeleteFlightConfirmation();
	    		
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

	    String[] from = new String[] {pathingDatabase.FLIGHT_NUMBER}; // create an array to specify which fields we want to display
		int[] to = new int[] {android.R.id.text1}; // create an array of the display item we want to bind our data to

		SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter
		FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spnFlightNumber.setAdapter(FNadapter);
	}
	
	
	String getspnFNvalue() { // returns the value of the selected item
		final Spinner spnFlightNumber = (Spinner) findViewById(R.id.spinnerFN); // get reference to our spinner
	    Cursor FNcur = mDbHelper.fetchFlightNumbers(); //fills the spinner from the database
	    startManagingCursor(FNcur);

	    String[] from = new String[] {pathingDatabase.FLIGHT_NUMBER}; // create an array to specify which fields we want to display
		int[] to = new int[] {android.R.id.text1}; // create an array of the display item we want to bind our data to

		SimpleCursorAdapter FNadapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, FNcur, from, to); // create simple cursor adapter filled with data from the database
		FNadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
		Cursor cursor = (Cursor) FNadapter.getItem(spnFlightNumber.getSelectedItemPosition());
		
	    String selecteditem = cursor.getString(cursor.getColumnIndex(pathingDatabase.KEY_ROWID));
	    
	    return selecteditem;
	}
	
	
	public void DownloadFromUrl(String KMLURL, String fileName) {  //this is the downloader method
		final String PATH = "/data/data/com.example.umainehabapp/";  //put the downloaded file here
	    try {
			URL url = new URL(KMLURL); //you can write here any link
			File file = new File(PATH + fileName);
			
			long startTime = System.currentTimeMillis();
			Log.d("KMLDownloader", "download begining");
			Log.d("KMLDownloader", "download url:" + url);
			Log.d("KMLDownloader", "downloaded file name:" + fileName);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			
			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
			        baf.append((byte) current);
			}
			
			baf.toByteArray();
			
			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("KMLDownloader", "download ready in"
			+ ((System.currentTimeMillis() - startTime) / 1000)
				+ " sec");

	    } catch (IOException e) {
	            Log.d("KMLDownloader", "Error: " + e);
	    }
	}
	
	
	public void showDeleteFlightConfirmation() { // shows a pop up confirmation dialog
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Delete Flight");
		helpBuilder.setMessage("Are you sure you want to delete the current flight record?");
	 
		helpBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mDbHelper.deleteFlight(getspnFNvalue());
				populatespnFlightNumber();
				// Do nothing but close the dialog
		    }
		});
	
		helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() { // don't delete flight
			public void onClick(DialogInterface dialog, int which) {
				 // Do nothing
			}
		});
		 
		 
		 // Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	
		}
	
	public void showAddFlightDialog() { // shows an add flight dialog
		final Dialog helpBuilder = new Dialog(this);
		
		helpBuilder.setCanceledOnTouchOutside(true);
		helpBuilder.setCancelable(true); // cancelable with back button
		
		helpBuilder.setTitle("Please supply additional info");
		
	   	helpBuilder.setContentView(R.layout.newflight);

		final EditText editLaunchLat = (EditText) findViewById(R.id.editTextlaunchlat); // makes the latitude text box show up
		final EditText editLaunchLong = (EditText) findViewById(R.id.editTextlaunchlong); // makes the latitude text box show up
		
	    final Button btnConfirm= (Button) helpBuilder.findViewById(R.id.btnConfirmAddFlight);
	    btnConfirm.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				mDbHelper.newFlight(); // add new flight to the database
			  	populatespnFlightNumber(); // repopulate the spinner to add the new flight
			  	
			  	helpBuilder.dismiss();
			}
	    });
	    
	    final Button btnCancel= (Button) helpBuilder.findViewById(R.id.btnCancelAddFlight);
	    btnCancel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				helpBuilder.dismiss();
			}
	    });
   	 
   	 helpBuilder.show();

   	}
	
	public void showAddFlightDialog2() { // shows an add flight dialog
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		
		//LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//LinearLayout ll = (LinearLayout) this.findViewById(R.id.newflightRelativeLayout);
		
        //View item = inflater.inflate(R.layout.newflight, null);
        //EditText x = (EditText) item.findViewById(R.id.editTextlaunchlat);
        //ll.addView(item,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		helpBuilder.setTitle("Add New Flight");
		helpBuilder.setMessage("Please provide some additional information for this new flight");
		 
		final EditText editLaunchLat = (EditText) findViewById(R.id.editTextlaunchlat); // makes the latitude text box show up
		final EditText editLaunchLong = (EditText) findViewById(R.id.editTextlaunchlong); // makes the latitude text box show up

		helpBuilder.setPositiveButton("Add new flight", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mDbHelper.newFlight();
				populatespnFlightNumber();
				// Do nothing but close the dialog
		   }
		});

		helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // don't delete flight
			public void onClick(DialogInterface dialog, int which) {
				 // Do nothing
			}
		});
		 
		 // Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}
}
