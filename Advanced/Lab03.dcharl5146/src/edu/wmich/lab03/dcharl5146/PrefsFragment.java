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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PrefsFragment extends PreferenceFragment {
	SharedPreferences preferences;//used to save preferences
	SharedPreferences.Editor editor;//used to save preferences
	private DatabaseHandler db;//used to access the database

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		db = new DatabaseHandler(getActivity());//intantiates db object

		//gets the preference with key "background"
		Preference myPref = findPreference("background");
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				AlertDialog.Builder b = new Builder(getActivity());
				b.setTitle("Select a color");
				final String[] types = { "Blue", "Green", "Yellow", "White","Beige(default)" };//this will be shown in screen 
				//the user will choose 1 among these options
				b.setItems(types, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//it will be one option from 0 to 4 and it will send this number to save preferences
						switch (which) {
						case 0:
							saveBackgroundPreferences(0);
							;
							break;
						case 1:
							saveBackgroundPreferences(1);
							;
							break;

						case 2:
							saveBackgroundPreferences(2);
							;
							break;
						case 3:
							saveBackgroundPreferences(3);
							;
							break;
						case 4:
							saveBackgroundPreferences(4);
							;
							break;
						}
					}

				});

				b.show();
				return true;
			}
		});
		//gets the preference with key "carrier"
		Preference myPref2 = findPreference("carrier");
		//same concept uses a string that will be shown to the user 
		//the user will select one of these options
		//the selected number will be sent to saveCarrierPreferences
		myPref2.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				AlertDialog.Builder b = new Builder(getActivity());
				b.setTitle("Select a carrier");
				final String[] types = { "UPS", "Fedex", "USPS", "DHL",
						"Lasership" };
				b.setItems(types, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:
							saveCarrierPreferences(0);
							;
							break;
						case 1:
							saveCarrierPreferences(1);
							;
							break;

						case 2:
							saveCarrierPreferences(2);
							;
							break;
						case 3:
							saveCarrierPreferences(3);
							;
							break;
						case 4:
							saveCarrierPreferences(4);
							;
							break;
						}
					}

				});

				b.show();

				return true;
			}
		});

		//gets the preference with key "profile"
		Preference myPref3 = findPreference("profile");
		//starts a new intent  
		myPref3.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {

				Intent k = new Intent(getActivity(), ProfileActivity.class);
				startActivity(k);
				return true;
			}
		});
		
		//gets the preference with key "clear"
		Preference myPref4 = findPreference("clear");
		myPref4.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				// deletes packages in the db and the list of packages
				clearAndMessage();
				
				return true;
			}
		});
		//gets the preference with key "blue"
		Preference myPref5 = findPreference("blue");
		myPref5.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				//calls the blue message method that will turn on the bluetooth if the users agree
				blueMessage();
			
				return true;
			}
		});

	}

	private void turnOnBluetooth() {
		try {
			
			BluetoothAdapter bluetoothAdapter;
			//gets the default blueetoth adapter 
			//if is null it means the device doesn't support a bluetooth
			if ((bluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter())!=null)
			{
				//it enables the bluetooth
				//for this the user had to click in the OK button in the dialog
				//so is not being turned on without the consent of the user
				bluetoothAdapter.enable();
				Toast.makeText(getActivity(), "Bluetooth has been turned on",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				//is null it doesn't have a bluetooth or is broken
				Toast.makeText(getActivity(), "This device doesn't support bluetooth",
						Toast.LENGTH_LONG).show();
			}
		

		} catch (Exception ex) {
			Toast.makeText(getActivity(), "Bluetooth can't be enabled",
					Toast.LENGTH_LONG).show();
		}
	}

	
	private void clearAndMessage() {
		//creates a dialog
		AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
		ad.setCancelable(false);
		ad.setTitle("Deleting packages");
		ad.setMessage("Are you sure you want to delete all packages?");
		ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				db.clear();//calls the clear method in the database
				MainActivity.packagesArray.clear();//cleras the packages list
				dialog.cancel();//dismisses the dialog
			}
		});

		ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();//dismisses the dialog
			}
		});

		ad.show();
	}

	private void blueMessage() {
		//creates a dialog
		AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
		ad.setCancelable(false);
		ad.setTitle("Turn on bluetooth");
		ad.setMessage("Are you want to turn on the bluetooth?");
		ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				turnOnBluetooth();//calls the method in order to turn on the blueetoth
				dialog.cancel();//dismisses the dialog
			}
		});

		ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();//dismisses the dialog
			}
		});

		ad.show();

	}

	//saves the carrier information 
	private void saveCarrierPreferences(int position) {
		this.preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		editor.putInt("carrier", position);
		editor.commit();

	}
	
	
	//saves the background color information
	private void saveBackgroundPreferences(int color) {
		this.preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		editor.putInt("background", color);
		editor.commit();

	}

}
