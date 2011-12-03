package com.example.umainehabapp;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
import android.widget.TextView;


public class heliumActivity extends Activity implements View.OnClickListener, OnKeyListener {
	
	//private pathingDatabase mDbHelper; //creates a database helper object to be used in accessing the database

	 /** Called when the activity is first created. */
    final static double gasdensity = .1786; 
    final static double airdensity = 1.205;
    final static double densitymodel = 7238.3;
    int selection;
    String launchdiameter;
    NumberFormat nf = NumberFormat.getInstance();
	double  freeliftkg,freeliftn, acentrate, burstheight, burstvolume, grosslift,balloonsize,area,neckliftdub;
    double launchvolumenum,payloadnum,launchdiameternum,burstdiameter,burstvolumeratio,totallift,l1,l2,launchdiameterf;
	String launchvolume;
    String payload;
    double[] cdarray = {.25, .25, .25, .25, .25, .3, .3, .3, .3, .25, .25, .25, .25};
    double[] burstdiameterarray = {3.00,3.78,4.12,4.72,4.99,6.02,6.53,7.00,7.86,8.63,9.44,10.54,13.0};
    double[] balloonsizearray = {200,300,350,450,500,600,700,800,1000,1200,1500,2000,3000};
    double[] launchdiameterarray = {1.18872, 1.24968, 1.28016, 1.34112, 1.3716, 1.43256, 1.49352, 1.52400, 1.58496, 1.8288, 1.88976, 1.9812, 2.16408};
    private Button calculate;
	private EditText payloadtext,diameter;
    private TextView necklift,Ascentrate,Burstheight;
    

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helium);
        

        
        payloadtext= (EditText) findViewById(R.id.payloadweight);
        diameter= (EditText) findViewById(R.id.BalloonDiameter);
        calculate = (Button) findViewById(R.id.HeliumCalc);
        necklift = (TextView) findViewById(R.id.necklift);
        Ascentrate= (TextView) findViewById(R.id.ascentrate);
        Burstheight= (TextView) findViewById(R.id.burst);
        calculate.setOnClickListener(this);
        payloadtext.setOnKeyListener(this);
        diameter.setOnKeyListener(this);

        final Spinner spinner = (Spinner) findViewById(R.id.heliumspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		this, R.array.helium_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
   
    public class MyOnItemSelectedListener implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			
    	selection=parent.getSelectedItemPosition();
    }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    }

	@Override
public void onClick(View v) {

		Bundle bundle = this.getIntent().getExtras(); //the current selected flight number passed via intent 
	        String flightnumber = bundle.getString("flightnumber");
		if(v == this.calculate){
			launchdiameter=diameter.getText().toString();
			payload=payloadtext.getText().toString();
			payloadnum=Double.parseDouble(payload);
			launchdiameternum= Double.parseDouble(launchdiameter);
			launchvolumenum=(((4.0/3.0)*Math.PI)*Math.pow(launchdiameternum/2.0, 3));
			grosslift=launchvolumenum*(airdensity-gasdensity);
			freeliftkg=grosslift-((payloadnum+balloonsizearray[selection])/1000.0);
			freeliftn=freeliftkg*9.81;
			area=Math.PI*Math.pow(launchdiameternum/2.0, 2);
			acentrate=Math.sqrt(freeliftn/(.5*cdarray[selection]*airdensity*area));
			String acentratestring=Double.toString(roundTwoDecimals(acentrate)); 
			if(Double.isNaN(acentrate))
			Ascentrate.setText("Balloon Configuration Not Possible");
			else
			Ascentrate.setText("Ascent Rate: "+acentratestring+" Meters/S");
			
			burstdiameter=burstdiameterarray[selection];
			burstvolume=(((4.0/3.0)*Math.PI)*Math.pow(burstdiameter/2.0, 3));
			burstvolumeratio=burstvolume/launchvolumenum;
			burstheight=-(densitymodel*Math.log(1/burstvolumeratio));
			String burstheightstring=Double.toString(roundTwoDecimals(burstheight));
			Burstheight.setText("Burstheight: " +burstheightstring+" Meters");
			
			l1=.02905*(Math.PI/6.0);
			launchdiameterf=launchdiameternum*3.280839895;
			l2=(l1*(Math.pow(launchdiameterf, 3)))*1000;
			neckliftdub=l2-payloadnum;
			String neckliftstring=Double.toString(roundTwoDecimals(neckliftdub));
			if(neckliftdub<0)
			necklift.setText("Negative Necklift: Number not attainable");	
			else
			necklift.setText("Necklift: "+ neckliftstring+ " Grams");
			
			pathingDatabase mDbHelper = new pathingDatabase(this); //creates a database helper object to be used in accessing the database
			mDbHelper.open();
			mDbHelper.setpayloadData(flightnumber, payloadnum, neckliftdub, burstheight, acentrate);
		}	
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

double roundTwoDecimals(double d) {
    DecimalFormat twoDForm = new DecimalFormat("#.##");
return Double.valueOf(twoDForm.format(d));
}
}