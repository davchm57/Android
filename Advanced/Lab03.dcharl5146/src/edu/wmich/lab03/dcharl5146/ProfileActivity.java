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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class ProfileActivity extends Activity {
	private Button saveProfile;//button that will trigger the actio to save the profile to the shared preferneces
	private SharedPreferences preferences;//used to save and load shared preferences
	private SharedPreferences.Editor editor;//used to save preferences
	private RelativeLayout layout;//layout used to change the background color
	
	//Edittexts used so the user can write and keep information about them in their profile
	private EditText name;//name
	private EditText city;//city
	private EditText state;//state
	private EditText zip;//zip

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileactivity);
	
		loadBackgroundPreferences();//loads background color
		
		//intantiates the shared preferences objects
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		
		//links the edittexts
		saveProfile = (Button) findViewById(R.id.saveprofilebtn);
		name = (EditText) findViewById(R.id.profilenameed);
		city = (EditText) findViewById(R.id.profilecityed);
		state = (EditText) findViewById(R.id.profilestateed);
		zip = (EditText) findViewById(R.id.profilezipcodeed);

		//calls the methods to load the saved preferences
		loadName();
		loadCity();
		loadState();
		loadZip();

		saveProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//on click it will save whatever the user has entered
				saveName(name.getText().toString());
				saveCity(city.getText().toString());
				saveState(state.getText().toString());
				saveZip(zip.getText().toString());
			}
		});

	}
	
	
	private void loadBackgroundPreferences() {
		layout = (RelativeLayout) findViewById(R.id.profileLayout);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		int color = preferences.getInt("background", 4); // if it is 4 it means a value has not been set
															

		
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

	
	//these methods load the profile settings
	private void loadName() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		name.setText(preferences.getString("name", "")); // if it is "" it means a value has
															// not been set
	}

	private void loadCity() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		city.setText(preferences.getString("city", "")); // if it is "" it means a value has
															// not been set
	}

	private void loadState() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		state.setText(preferences.getString("state", "")); // if it is "" it means a value
															// has not been set
	}

	private void loadZip() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		zip.setText(preferences.getString("zip", "")); // if it is ""it means a value has
														// not been set
	}

	//thgese methods save the profile settings
	private void saveName(String name) {
		editor.putString("name", name);
		editor.commit();
	}

	private void saveCity(String city) {
		editor.putString("city", city);
		editor.commit();

	}

	private void saveState(String state) {
		editor.putString("state", state);
		editor.commit();
	}

	private void saveZip(String zip) {
		editor.putString("zip", zip);
		editor.commit();

	}

}
