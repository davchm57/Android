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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Contacts extends Activity {
	// AutoCompleteTextView: Need to create an Object in order to Access it.
	private AutoCompleteTextView mContactsAt;
	private Button chooseContact;
	private int position = 0;
	private DatabaseHandler db;
	SharedPreferences preferences;
	LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_contacts);
		
		//load the background color preferences
		loadBackgroundPreferences();
		db = new DatabaseHandler(this);//instantiate database object so the selected package can be updated with the contact
		Intent mIntent = getIntent();
		position = mIntent.getIntExtra("position", 0);//gets the selected position

		chooseContact = (Button) findViewById(R.id.chooseContact);
		// Initialize AutoCompleteTextView object So that it can access
		// at_Contacts
		mContactsAt = (AutoCompleteTextView) findViewById(R.id.at_Contacts);

		// Load the Contact Name from List into the AutoCompleteTextView
		mContactsAt.setAdapter(new ArrayAdapter<String>(
				getApplicationContext(), R.layout.single_contact,
				R.id.tv_ContactName, getAllContactNames()));

		chooseContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//when clicked it will get the contact name from the textview
				if (mContactsAt.getText().toString().length() > 0) {
					MainActivity.packagesArray.get(position).setOwner(
							mContactsAt.getText().toString());
					db.updatePackage(MainActivity.packagesArray.get(position));
					message();

				}
			}

		});
		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//message displayed when the owner is set to the package
	private void message() {
		Toast toast = Toast
				.makeText(this, MainActivity.packagesArray.get(position)
						.getOwner() + " Added as owner of this package",
						Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * Get All the Contact Names
	 * 
	 * @return
	 */
	private List<String> getAllContactNames() {
		List<String> lContactNamesList = new ArrayList<String>();
		try {
			// Get all Contacts
			Cursor lPeople = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, null, null, null,
					null);
			if (lPeople != null) {
				while (lPeople.moveToNext()) {
					// Add Contact's Name into the List
					lContactNamesList
							.add(lPeople.getString(lPeople
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				}
			}
		} catch (NullPointerException e) {
			Log.e("getAllContactNames()", e.getMessage());
		}
		return lContactNamesList;
	}
	
	//loads the background preferences 
	private void loadBackgroundPreferences() {
		layout=(LinearLayout) findViewById(R.id.showContacts);
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
	
	
}