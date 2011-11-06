package com.example.umainehabapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class heliumActivity extends Activity {
	 /** Called when the activity is first created. */
    double gasdensity=.1786;
    double airdensity=1.205;
    double densitymodel=7238.3;
    double launchdiameter;
    double launchvolume=(4/3)*Math.PI*Math.pow(launchdiameter/2,3);

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helium);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerBalloonType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.helium_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
    }
}
