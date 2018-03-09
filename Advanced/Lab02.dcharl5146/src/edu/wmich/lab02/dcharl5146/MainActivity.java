package edu.wmich.lab02.dcharl5146;
/* 
*************************************
* Programmer: David Charles
* Class ID: dchar5146
* Lab 02
* CIS 4700
* Spring 2014
* Due date: 3/10/14
* Date completed: 3/09/14
*************************************
Battery checker app 
Features:
TAB 1-Displays battery level
TAB 2-Displays System information
-Notification Service: Let the user to set a minimum battery level, when the battery get to this level a notification will be fired up
-When the notification is clicked it will open up an activity that will show an image shaking (an animation). It will also display the battery level. The notification will make the phone vibrate or making a sound depending on the phone state and it will make the led of the phone go blue.
-This app will save the settings using sharedpreferences. When the phone is rebooted it will load those settings using a bootbroadcast. So if the notification service was on before the phone rebooted it will continue to be on after rebooting.
-Installs a widget that displays the battery level
	-When clicked it will open up the app
	-It will tell the user if the service is running or not (the app will communicate with the widget) 
This app is designed for large screens as well
Tested on Nexus 7 and Galaxy S4
The normal layout is the only one commented (since the other layouts are the same but with larger widgets/images)
*************************************
*/


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;



public class MainActivity extends Activity implements TabListener{

	
	List<Fragment> fragList = new ArrayList<Fragment>();//list of fragments
	TelephonyManager tManager;//telephonymanager object to get network information
	public String batteryLevel = "init";//holds the battery level
	public static boolean serviceStatus=false;//holds the current service status

	//menu it shows it in a dialog 
	//it will show some information about the app
	//it has a button close or it could be dismissed by clicking back since setcancellable is not changed
	public void buildDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setCancelable(true);
        builder.setTitle("About");
        builder.setMessage("Battery Checker is an app that monitors battery levels and allows the user to set a notification when the battery level gets to a specific percentage.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
	}
	  

	@Override
	public void onDestroy()
	{
		saveSharedPreferences();
  		super.onDestroy();
	}
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
		 MenuItem item = menu.add ("About");//creates an item and adds it to the menu
		 item.setOnMenuItemClickListener (new OnMenuItemClickListener(){
			    @Override
			    public boolean onMenuItemClick (MenuItem item){
			    	buildDialog();//it will show a dialog
			      return true;
			    }
			  });
			return true;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}

		//on tab selected
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			//it will get the tab selected position
			Fragment f = null;
			if (fragList.size() > tab.getPosition())
					fragList.get(tab.getPosition());
			
			if (f == null) {
				//if is 0 it means the first tab is selected
				if(tab.getPosition()==0)
				{
					BatteryFragment tf = null;
					
			   //intantiates tf 
				tf = new BatteryFragment();
				Bundle data = new Bundle();
				tf.setArguments(data);//sets the data
				//adds tf to the fragment list
				fragList.add(tf);
				//replace fragment
				ft.replace(android.R.id.content, tf);
				}
				//same thing happens down here but
				//here information network information is sent to the fragment
				else
				{
					SystemInformationFragment tf = null;
					
					tf = new SystemInformationFragment();
					Bundle data = new Bundle();
					data.putString("idx",  tManager.getDeviceId());
					data.putString("idx2",  tManager.getNetworkOperatorName());
					data.putString("idx3",  tManager.getLine1Number());
					tf.setArguments(data);
					fragList.add(tf);
					ft.replace(android.R.id.content, tf);
				}
			}
		}

		//removes the current fragment
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
	        //saveServiceStartedPreferencesAtStart();
	        
	        serviceStatus=loadServiceStatus();//gets the servicestatus
	        
//	        //sets servicestatus to false meaning that if it started by a bootbroadcast the 
//	        //bootbroadcast will have to change it to true again so the app will know the service wasn't started 
//	        //right now but it was started before the phone rebooted

	        ActionBar bar = getActionBar();//creates and instantiate an action bar object
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//sets navigationmode  to tabs
			tManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);//instantiate the tmanager object to get network information
			//creates 2 tabs and sets the name
			for (int i=1; i <= 2; i++) {
				Tab tab = bar.newTab();
				if(i==1)
				{
					tab.setText("Battery Status");
				}
				else
				{
					tab.setText("System Information");
				}
				//sets it to listen
				tab.setTabListener(this);
				//adds tab to the bar
				bar.addTab(tab);
				
			}
			saveSharedPreferences();
	    }
	    
	 
		//loads the service started status 
	 	//this way the app finds out of the bootbroadcast started the service before the app was opened
		private boolean loadServiceStatus()
		{
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
				    return sharedPreferences.getBoolean("serviceStatus", false);
		}

	 //saves the service status preference
	 private void saveSharedPreferences()
	 {
		 SharedPreferences sharedPreferences = PreferenceManager
 				.getDefaultSharedPreferences(this);
 		Editor editor = sharedPreferences.edit();
 		editor.putBoolean("serviceStatus", serviceStatus);
 		editor.commit();
		 
	 }
	 
	 
}
