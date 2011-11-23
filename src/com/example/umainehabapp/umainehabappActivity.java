package com.example.umainehabapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class umainehabappActivity extends Activity {
	
	private pathingDatabase mDbHelper; //creates a database helper object to be used in accessing the database
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        
        final Button btnhelium = (Button) findViewById(R.id.btnhelium); //button with intent to helium activity
        btnhelium.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent intenthelium = new Intent(umainehabappActivity.this, heliumActivity.class);
        		startActivity(intenthelium);
        	}
        });
        
        
        final Button btnPath = (Button) findViewById(R.id.btnpath); //button with intent to pathing/tracking activity
        btnPath.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
				Intent topathing = new Intent(umainehabappActivity.this, pathing.class);
				startActivity(topathing);
			}
		});
    }
}