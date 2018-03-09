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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity implements TabListener {
	public static ArrayList<Package> packagesArray = new ArrayList<Package>();
	private DatabaseHandler db;
	public List<Fragment> fragList = new ArrayList<Fragment>();// list of
																// fragments

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//starts the preference activity when the menu is pressed
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SetPreferenceActivity.class);
		startActivityForResult(intent, 0);

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		//inflates the item that is in menu.main 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	// on tab selected
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// it will get the tab selected position
		Fragment f = null;
		if (fragList.size() > tab.getPosition())
			fragList.get(tab.getPosition());

		if (f == null) {
			// if is 0 it means the first tab is selected
			if (tab.getPosition() == 0) {
				TrackPackage tf = null;
				// intantiates tf
				tf = new TrackPackage();
				// adds tf to the fragment list
				fragList.add(tf);
				// replace fragment
				ft.replace(android.R.id.content, tf);
			}
			// same thing happens down here but
			// here information network information is sent to the fragment
			else {
				TrackedPackages tf = null;
				tf = new TrackedPackages();
				fragList.add(tf);
				ft.replace(android.R.id.content, tf);
			}
		}
	}

	// removes the current fragment
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragList.size() > tab.getPosition()) {
			ft.remove(fragList.get(tab.getPosition()));
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			packagesArray.clear();
			// loading for the first time, load database
			db = new DatabaseHandler(this);
			db.getAllPackages();
		}

		try {

			ActionBar bar = getActionBar();// creates and instantiate an action
											// bar object
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);// sets
																	// navigationmode
																	// to tabs

			// creates 2 tabs and sets the name
			for (int i = 1; i <= 2; i++) {
				Tab tab = bar.newTab();
				if (i == 1) {
					tab.setText("Track a package");
				} else {
					tab.setText("Tracked Packages");
				}
				// sets it to listen
				tab.setTabListener(this);
				// adds tab to the bar
				bar.addTab(tab);

			}
		} catch (Exception ex) {

		}
	}
}
