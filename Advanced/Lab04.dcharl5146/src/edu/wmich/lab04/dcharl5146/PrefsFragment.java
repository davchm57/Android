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


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefsFragment extends PreferenceFragment {
	SharedPreferences preferences;//used to save preferences
	SharedPreferences.Editor editor;//used to save preferences


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		Preference myPref2 = findPreference("effects");
		//same concept uses a string that will be shown to the user 
		//the user will select one of these options
		//the selected number will be sent to saveCarrierPreferences
		myPref2.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				AlertDialog.Builder b = new Builder(getActivity());
				b.setTitle("Select a carrier");
				final String[] types = { "none", "sepia",
						"mono","negative"};
				b.setItems(types, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						saveEffects(types[which]);//using an array saves the selected pref
				
			}
		});

				b.show();

				return true;

			}
		});
	}
	
	//save selected effect in the shared preferences
	private void saveEffects(String effect) {
		this.preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		editor.putString("effect", effect);
		editor.commit();

	}
	
}



