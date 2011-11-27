package com.example.umainehabapp;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class map extends MapActivity {
	LinearLayout linearLayout;
    MapView mapView;
    mapItemizedOverlay itemizedOverlay;
    Drawable drawable;
    List<Overlay> mapOverlays;
	
	protected boolean isRouteDisplayed() {
        return false;
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathmap);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
	}
}