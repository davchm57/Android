package edu.wmich.lab03.dcharl5146;
/* 
 *************************************
 * Programmer: David Charles
 * Class ID: dchar5146
 * Lab 03
 * CIS 4700
 * Spring 2014
 * Due date: 3/28/14
 * Date completed: 3/25/14
 *************************************
 Package Tracker app
 Features:
 TAB 1-Lets you add a package:
	-You can add a package entering a tracking number and a name for the package. Also a carrier is needed, by default is UPS but this can be changed in the settings
 TAB 2-Displays added packages: 
  Displays current added packages labeling them with their tracking number and name.
  Pressing a package will show a dialog with a couple of options:
  	   -Show Complete information: Displays more information about the package such as (Location, carrier, status, estimated delivery and Owner (by default a package has no Owner)
       -Remove this package: Removes currently selected package 
       -Add to calendar: Add an estimated delivery date to the calendar
       -Set an owner using contacts book: You can pick someone from your contacts book and set it as owner of this package
       -Cancel 
-Options button: When pressed it will show a preference activity. In this preference activity you can make some changes such as:
	  -Change your profile: Here you can store information about yourself and it will be stored as long as the app is installed
	  -Carrier: You can change the default carrier which is UPS
      -Background Color: You can change the background color
      -Clear packages list: Will clear the packages list
      -Turn on Bluetooth: Turns on the Bluetooth
      
*The app will load the packages from the database everytime it starts up 
*everytime a package is added, removed or edited it will be refelected in the database
*and will save the preferences set on the preference activity 

Tested on and Galaxy S4
*************************************
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PackageInformation extends Activity {
	private int selectedPosition = 0;//selected position in the list fragment
	
	//strings needed to show the information about the package
	private String trackingNumber, estimated, carrier, status, location, name,
			owner;
	
	//textviews to show information about each attribute of the package
	private TextView trackingtv;
	private TextView estimatedtv;
	private TextView carriertv;
	private TextView statustv;
	private TextView locationtv;
	private TextView nametv;
	private TextView ownertv;
	
	private SharedPreferences preferences;//shared preference object to load the background color
	private RelativeLayout layout;//relative layout to set the background color

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.packageinformation);
		
		loadBackgroundPreferences() ;//loads the background color
		
		//instantiates the textviews
		trackingtv = (TextView) findViewById(R.id.trackingtv1);
		estimatedtv = (TextView) findViewById(R.id.estimatedtv1);
		carriertv = (TextView) findViewById(R.id.carriertv1);
		statustv = (TextView) findViewById(R.id.statustv1);
		locationtv = (TextView) findViewById(R.id.locationtv1);
		nametv = (TextView) findViewById(R.id.nametv1);
		ownertv = (TextView) findViewById(R.id.ownertv);
		
		//gets the selected position that is sent by trackedpackages
		Intent intent = getIntent();
		selectedPosition = intent.getIntExtra("selectedPosition", 0); //sets selected position
																		
		//using the selected position retrieves every value from the packages 
		trackingNumber = (MainActivity.packagesArray.get(selectedPosition)
				.getTrackingNumber());
		carrier = (MainActivity.packagesArray.get(selectedPosition)
				.getCarrier());
		estimated = (MainActivity.packagesArray.get(selectedPosition)
				.getEstimated());
		status = (MainActivity.packagesArray.get(selectedPosition).getStatus());
		location = (MainActivity.packagesArray.get(selectedPosition)
				.getLocation());
		name = (MainActivity.packagesArray.get(selectedPosition).getName());
		owner = (MainActivity.packagesArray.get(selectedPosition).getOwner());

		//sets the retreived information to each textview 
		trackingtv.setText("Tracking Number: " + trackingNumber);
		carriertv.setText("Carrier: " + carrier);
		estimatedtv.setText("Estimated Delivery: " + estimated);
		statustv.setText("Status: " + status);
		locationtv.setText("Location: " + location);
		nametv.setText("Name: " + name);
		
		//if owner is not empty it means the user set an owner using the contacts book
		//if is empty it will just say no owner has been set
		if (owner.length() > 0) {
			ownertv.setText("Owner: " + owner);
		} else {
			ownertv.setText("No Owner has been set");
		}
	}
	
	
	
	
	//sets the background color according to the saved preferences 
	private void loadBackgroundPreferences() {
		layout = (RelativeLayout) findViewById(R.id.packgeInformationLayout);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		int color = preferences.getInt("background", 4); // // if it is 4 it means a value has not been set
															
			switch (color) {
			case 0:
				layout.setBackgroundColor(Color.BLUE);
				;
				break;
			case 1:
				layout.setBackgroundColor(Color.GREEN);
				;
				break;

			case 2:
				layout.setBackgroundColor(Color.YELLOW);
				;
				break;
			case 3:
				layout.setBackgroundColor(Color.WHITE);
				;
				break;
			case 4:
				layout.setBackgroundColor(getResources().getColor(R.color.beige));
				;
				break;
			}

		
	}

	
	
	
	
	

}
