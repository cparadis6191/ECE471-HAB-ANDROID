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

<<<<<<< HEAD
=======
//test string Ian LaForge is an idiot
>>>>>>> b3a7c35544b64f42f1a8ace147f986cff73c8eb4
