package com.example.umainehabapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class heliumActivity extends Activity implements /*View.OnClickListener,*/ OnKeyListener {
	
	private pathingDatabase mDbHelper; //creates a database helper object to be used in accessing the database

	 /** Called when the activity is first created. */
    final static double gasdensity = .1786; //I made these static Ian, because... well... they should be
    final static double airdensity = 1.205;
    final static double densitymodel = 7238.3;
    
    double launchdiameter, balloonweight, freelift, ballooncd, acentrate, burstheight, burstvolume, launchvolume, grosslift;
    double payload;
    
    private Button calculate;
	private EditText payloadtext,burst,ascent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helium);
        payloadtext= (EditText) findViewById(R.id.payloadweight);
        burst= (EditText) findViewById(R.id.BurstAltitude);
        ascent= (EditText) findViewById(R.id.AscentRate);
        calculate = (Button) findViewById(R.id.calculate);
        
 //       calculate.setOnClickListener(this);
        payloadtext.setOnKeyListener(this);
        burst.setOnKeyListener(this);
        ascent.setOnKeyListener(this);
        
        
        
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerBalloonType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.helium_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
   
    public class MyOnItemSelectedListener implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent,
    	        
	    		View view, int pos, long id) {
			
	    	switch (parent.getSelectedItemPosition()) {
	    	case 0:

	    		balloonweight=200;
	    		launchdiameter=1.18872;
	    		launchvolume=(4/3)*Math.PI*Math.pow(launchdiameter/2,3);
	    		grosslift=launchvolume*(airdensity-gasdensity);

	        	break;
	        case 1:

	        	break;
	        case 2:
	        case 3:
	        case 4:
	        case 5:
	        case 6:
	        case 7:
	        case 8:	
	        case 9:
	        case 10:
	        case 11:
	        case 12:
	        case 13:
	    	}
    }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    }

	//@Override
	//public void onClick(View v) {

		
	//	if(v == this.calculate){
	//		payload=this.addItem(this.payloadtext.getText().toString());
	//	}	
//	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
