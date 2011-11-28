package com.example.umainehabapp;

import java.util.List;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class map extends MapActivity {
	LinearLayout linearLayout;
    MapView mapView;
    mapItemizedOverlay itemizedOverlay;
    Drawable drawable;
    List<Overlay> mapOverlays;
	
    private pathingDatabase mDbHelper = new pathingDatabase(this); 
    
	protected boolean isRouteDisplayed() {
        return false;
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathmap);
        
        mDbHelper.open(); //opens the database
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        Bundle bundle = this.getIntent().getExtras();
        String flightnumber=bundle.getString("flightnumber");
        
        Cursor cur = mDbHelper.fetchGPSData(flightnumber); //fills the cursor from the database
		startManagingCursor(cur);
		cur.moveToFirst();
		
		
		while(cur.moveToNext()) {
	        GeoPoint point=new GeoPoint(1000000*cur.getInt(cur.getColumnIndex(pathingDatabase.PREDICTED_LAT)),1000000*cur.getInt(cur.getColumnIndex(pathingDatabase.PREDICTED_LONG)));
	        OverlayItem overlayitem=new OverlayItem(point, "", "");
	        
	        itemizedOverlay.addOverlay(overlayitem);
	        mapOverlays.add(itemizedOverlay);
		}
	}
}