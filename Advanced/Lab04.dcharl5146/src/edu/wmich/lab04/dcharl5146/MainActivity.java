package edu.wmich.lab04.dcharl5146;


/* 
 *************************************
 * Programmer: David Charles
 * Class ID: dchar5146
 * Lab 04
 * CIS 4700
 * Spring 2014
 * Due date: 4/21/14
 * Date completed: 4/19/14
 *************************************
This app has the following features:
Tab 1: Music
Shaking your phone will play an animation and music
Shake it again it will stop the animation and music
Tab 2: Selfie
Like the name says, you can take a selfie in this tab.
You can save a taken picture or discard it
Share it via gmail, Bluetooth, ETC
You can put effects on the picture clicking on the menu button right upper corner
You can also send set an option that will send a text to a number every time you save a picture to your phone saying how awesome the app is.
Tested on Samsung galaxy S4.

Tested on Galaxy S4
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements TabListener {
	public List<Fragment> fragList = new ArrayList<Fragment>();// list of														// fragments
	private SharedPreferences preferences;//used to save shared pref
	private Editor editor;//used to save shared pref
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {



		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate menu resource file.
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
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
				Music tf = null;
				// intantiates tf
				tf = new Music();
				// adds tf to the fragment list
				fragList.add(tf);
				// replace fragment
				ft.replace(android.R.id.content, tf);
			}
			// same thing happens down here but
			// here information network information is sent to the fragment
			else {
				Selfie tf = null;
				tf = new Selfie();
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


		if (savedInstanceState == null) {
			//deletes the shared preferences if is the first time running
			this.preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			editor = preferences.edit();
			editor.putString("effect", "none");
			editor.commit();

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
					tab.setText("Music");
				} else {
					tab.setText("Selfie");
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
