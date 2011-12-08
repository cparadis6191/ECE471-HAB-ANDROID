package com.example.umainehabapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class umainehabappActivity extends Activity {
	
	private pathingDatabase mDbHelper = new pathingDatabase(this); //creates a database helper object to be used in accessing the database

	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mainview);
	    
	    mDbHelper.open(); //opens the database
	    
	    populatespnFlightNumber(); //populate the spinner
	    
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
			}
		});
	    
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
	    		Cursor cur = mDbHelper.fetchForPrediction(getspnFNvalue());
	    		startManagingCursor(cur);
	    		cur.moveToFirst();
	    		
	    		String launchlong = cur.getString(cur.getColumnIndex(pathingDatabase.LAUNCH_LONG));
	    		String launchlat = cur.getString(cur.getColumnIndex(pathingDatabase.LAUNCH_LAT));
	    		String burstalt = cur.getString(cur.getColumnIndex(pathingDatabase.BURST_ALTITUDE));
	    		String time = "2011120712";
	    		String FCST = "48";
	    		
	    		String URL = "http://weather.uwyo.edu/cgi-bin/balloon_traj?TIME=" + time + "&FCST=" + FCST + "&POINT=none&LAT=" + launchlat + "&LON=" + launchlong + "&TOP=" + burstalt + "&OUTPUT=kml&Submit=Submit&.cgifields=POINT&.cgifields=FCST&.cgifields=TIME&.cgifields=OUTPUT";
	    		String KML = DownloadFromUrl(URL, "blah.txt");
	    		String CSV = "";
	    		
				try {
					CSV = parseKML(KML);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (!CSV.contentEquals("")) {
					parseCSV(CSV);
				}
				
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
	
	
	public String DownloadFromUrl(String KMLURL, String fileName) {  //this is the downloader method
		String RAW_KML = "";
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
			RAW_KML = new String(baf.toByteArray());
			
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
	    
		return RAW_KML;
	}
	
	
	String parseKML(String KML) throws ParserConfigurationException, SAXException, IOException { 
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(new InputSource(new StringReader(KML))); 
		// normalize the document 
		doc.getDocumentElement().normalize(); 
		// get the root node
		NodeList Document = doc.getElementsByTagName("coordinates");
		
		return Document.item(0).getChildNodes().item(0).getNodeValue();
	}
	
	
	void parseCSV(String CSV) {
		TextView debug = (TextView) findViewById(R.id.debug);
				
		StringTokenizer tokens = new StringTokenizer(CSV, "\n");
		while (tokens.hasMoreTokens()) {
			StringTokenizer temp = new StringTokenizer(tokens.nextToken(), ",");
			if (temp.countTokens() == 3) {
				mDbHelper.setpredictedGPS(getspnFNvalue(), temp.nextToken(), temp.nextToken(), temp.nextToken());
			}	
			tokens.nextToken();
			//if (tokens.hasMoreTokens()) {
			//	tokens.nextToken();
			//}
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
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		helpBuilder.setTitle("Add New Flight");
		helpBuilder.setMessage("Please provide some additional information for this new flight");
		
		final Context mContext = getApplicationContext();
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.newflight, (ViewGroup) findViewById(R.id.editTextlaunchlat));
		
		helpBuilder.setView(layout);
		
		final EditText editTextlong = (EditText) layout.findViewById(R.id.editTextlaunchlong);
		final EditText editTextlat = (EditText) layout.findViewById(R.id.editTextlaunchlat);
		
		helpBuilder.setPositiveButton("Add new flight", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mDbHelper.newFlight(editTextlong.getText().toString(), editTextlat.getText().toString());
				populatespnFlightNumber();
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
