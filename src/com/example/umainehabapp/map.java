package com.example.umainehabapp;

import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class map extends MapActivity {
	
	protected boolean isRouteDisplayed() {
        return false;
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathmap);
	}
}