package com.example.umainehabapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pathing extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathparam);
        
        final Button btnMap= (Button) findViewById(R.id.btntomap);
        btnMap.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent Intenttomap = new Intent(pathing.this, map.class);
				startActivity(Intenttomap);
			}	
        });
	}
}