package com.example.umainehabapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class umainehabappActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
        
        final Button btnhelium = (Button) findViewById(R.id.btnhelium);
        btnhelium.setOnClickListener(new View.OnClickListener() { //button listener to launch helium activity
        	public void onClick(View v) {
        		Intent intenthelium = new Intent(umainehabappActivity.this, heliumActivity.class);
        		startActivity(intenthelium);
        	}
        });
    }
}

//test string Ian LaForge