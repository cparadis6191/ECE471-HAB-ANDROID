package com.example.umainehabapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

public class USBInterfaceActivity extends umainehabappActivity { //class to interface with USB device
	//specific model numbers for connected devices will have to be programmed in manually it seems
	//Android 2.3.2 or 3.1 will have to be used so USB Host Mode can be used
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getAPRSString(); //main function call, might migrate code into a class file and put this function call in umainehabapp
    }
	
	
	
	String getAPRSString() { //This function will return an APRS string. A separate thread will probably be dedicated to this task as the USB device will always transmit strings regardless of the software state
		String coords = "Test"; //Placeholder code
		return coords;
	}
	
	
	private static final String ACTION_USB_PERMISSION = //code to receive permission to communicate with USB device
		    "com.android.example.USB_PERMISSION"; //taken from http://developer.android.com/guide/topics/usb/host.html
		private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        if (ACTION_USB_PERMISSION.equals(action)) {
		            synchronized (this) {
		                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

		                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
		                    if(device != null){
		                      //call method to set up device communication
		                   }
		                } 
		                else {
		                    //Log.d(TAG, "permission denied for device " + device);
		                }
		            }
		        }
		    }
		};
}