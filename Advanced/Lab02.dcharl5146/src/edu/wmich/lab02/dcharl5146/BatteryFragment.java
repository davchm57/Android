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


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BatteryFragment extends Fragment {

	public static ToggleButton toggle;//static so it can be used in the dialog class. Used to start the notification service
	public String batteryInfo="";//contains the battery level

	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		getActivity().unregisterReceiver(mBatInfoReceiver);//unregister receiver 
		
	}
	
	//uses a broadcastreceiver to get the current battery level and set it to the progress bar and textview
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	@Override
		public void onReceive(Context c, Intent i) {	

			int level = i.getIntExtra("level", 0);
		    ProgressBar pb = (ProgressBar) getView().findViewById(R.id.progressbar);//getting the progress bar
		    pb.setProgress(level);//sets the battery level to the progress bar
		    TextView tv = (TextView )getView().findViewById(R.id.batteryLevelTv);//gets the textivew
		    tv.setText("Battery Level: "+Integer.toString(level)+"%");//sets battery information to the textivew
		    batteryInfo=Integer.toString(level);//string to store the battery level
		}
	 };
	  


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.batteryfragment, null);
		getActivity().registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		//using a drawable to make the progressbar look more like a battery
		Drawable draw= getResources().getDrawable(R.drawable.customprogressbar);
        // set the drawable as progress drawavle
		ProgressBar pb = (ProgressBar) v.findViewById(R.id.progressbar);
        pb.setProgressDrawable(draw);
   
		//setting toggle button on listener
			toggle = (ToggleButton) v.findViewById(R.id.alertBtn);
			
			//cheks if the service is on
			//so the toggle should be on
			if (MainActivity.serviceStatus==true)
			{
				toggle.setChecked(true);
			}
			
			toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked)
		        {
		        	//it will show the dialog
		        	new NotificationDialog().show(getFragmentManager(), "MyDialog");
		        } 
		        else 
		        {
		        	
		        try
		        {
		        	//this needs to be done so if the app is running and the notification
		        	//gets fired up, the notification will toggle off the button
		        	//so it cant stop the service twice else it would crash
		        	MainActivity.serviceStatus=loadSavedPreferencesServiceStatus();
		        	if(MainActivity.serviceStatus==true)
		        	{
		        	//it will stop the service
		        	getActivity().stopService(new Intent(getActivity(),NotificationService.class));
		       
		        	}
		        	 //save the service status as false so when the user reboots the phone 
		        	//the bootbroadcast will know it shouldnt start the service
		        	saveSharedPreferences(MainActivity.serviceStatus);//saving so we know the service is not running at the same time
		        	//the widget will get this information
		        	MainActivity.serviceStatus=false;//service status toggling it off will turn off the service
		        	//saveServiceStartedPreferences();//if is turned off it will set this to false even if the serviec
		        	//was not started by the bootbroadcast so when the phone reboots the boot broadcast won't start the service
		        	
		        }
		        catch (Exception ex)
		        {
		        		
		        }
		     
		        }
		    }
		});
			
		return v;
		
	}
	
	//saves notification service status in sharedpreferences
	private void saveSharedPreferences(boolean status)
	{
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("serviceStatus", status);
		editor.commit();
	}
	
	private boolean loadSavedPreferencesServiceStatus()
    { 
    	SharedPreferences sharedPreferences = PreferenceManager
	.getDefaultSharedPreferences(getActivity());
    return sharedPreferences.getBoolean("serviceStatus", false);
    }

	
	
}
