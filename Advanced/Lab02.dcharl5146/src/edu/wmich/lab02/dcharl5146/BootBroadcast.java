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


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {        

    	
		if (loadSavedPreferencesServiceStatus(ctx)) {
        try{
        //it will check if the service was started when the phone got shut down
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
        {
        	//tells the app that the service was started by the bootbroadcast
            //saveSavedPreferencesToggleButtonStatus(ctx);
        	ctx.startService(new Intent(ctx, NotificationService.class));
        }
        }
        catch (Exception ex)
        {

        }
		}
    }
    
    //loads the service status because the phone is just booting
    //finds out if the service was on or off when the phone got turned off
    private boolean loadSavedPreferencesServiceStatus(Context ctx)
    { 
    	SharedPreferences sharedPreferences = PreferenceManager
	.getDefaultSharedPreferences(ctx);
    return sharedPreferences.getBoolean("serviceStatus", false);
    }
    
 
}