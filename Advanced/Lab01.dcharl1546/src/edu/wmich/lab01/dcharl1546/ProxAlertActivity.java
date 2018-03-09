package edu.wmich.lab01.dcharl1546;
/* 
*************************************
* Programmer: David Charles
* Class ID: dchar5146
* Lab 01
* CIS 4700
* Spring 2014
* Due date: 2/12/14
* Date completed: 2/11/14
*************************************
Simple wmu app
Features:
Marks on a map:
Current location with address and coordinates
WMU Location
Proximity Alert:
This app lets you set a proximity alert to the following places:
Schenaider hall
Waldo library
The recreation center
The Bernhard center
If you click the main icon it will show a picture of wmu using a fragment

*************************************
*/

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;


public class ProxAlertActivity extends Activity {
	private static final int FM_NOTIFICATION_ID = 0;
	private static final long POINT_RADIUS = 300; // in Meters
	private final String PROX_ALERT = "edu.wmich.lab01.PROXIMITY_ALERT";
	private LocationManager locationManager = null;
	private PendingIntent proximityIntent = null;
	private float currentLatitude;
	private float currentLongitude;
	private String selectedPlace="";
	private ProximityIntentReceiver proximityReceiver=null;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.proxalertactivity);
		//it will check for the selected place
		//sets current latitude and longitude according to the selected place
		selectedPlace= getIntent().getStringExtra("place");
		if (selectedPlace.equalsIgnoreCase("Schneider Hall"))
        {
        	currentLatitude=42.2860832f;
        	currentLongitude=-85.618408f;
        }
        else if (selectedPlace.equalsIgnoreCase("Waldo Library"))
        {
        	currentLatitude=42.2820243f;
        	currentLongitude=-85.613586f;
        	
        }
        else if (selectedPlace.equalsIgnoreCase("The Recreation Center"))
        {
        	currentLatitude=42.2845382f;
        	currentLongitude=-85.608726f;
        	
        }
        else if (selectedPlace.equalsIgnoreCase("The Bernhard Center"))
        {
        	currentLatitude=42.2850265f;
        	currentLongitude=-85.612007f;
        	
        }
	        Intent intent = new Intent(PROX_ALERT);
	        intent.putExtra("message", selectedPlace);//sends selected place to the intent
	        proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
	            PendingIntent.FLAG_CANCEL_CURRENT);

	        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
	        locationManager.addProximityAlert(currentLatitude,  currentLongitude , POINT_RADIUS, -1, proximityIntent);
	        //adds the proximity alerts sending the current latitude and longitude, the radius, the expiration time and the pending intent
	        //that it fires up when the location is reached
	        proximityReceiver = new ProximityIntentReceiver();

	        IntentFilter iFilter = new IntentFilter(PROX_ALERT);
	        registerReceiver(proximityReceiver, iFilter);//register the receiver
	        //sets information so the user knows what is going on
	        TextView alertMessage=(TextView) findViewById(R.id.messageTv);
	        alertMessage.setText("Proximity Alert is set:\nPlace: "+selectedPlace+"\nDefault Radius: 300meters\n\nIt will fire up a notification when the device enters to the area\n\nIf the back button is pressed, the alert will be disabled");
	    }
	 @Override
	 protected void onDestroy() {
		 unregisterReceiver(proximityReceiver);
	      locationManager.removeProximityAlert(proximityIntent);
	      removeNotification();
	      super.onDestroy();
	    }
	 private void removeNotification() {  
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		    manager.cancel(FM_NOTIFICATION_ID);  
		}
	
    
}
