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


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;



public class BatteryWidget extends AppWidgetProvider {
    /** Called when the activity is first created. */
	
	public String batteryLevel = "init";//hold the current battery level
	private int widgetImageFrame=R.drawable.fullbar;//hodls the current widget image displayed
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		context.getApplicationContext().registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		

		//creates a remote view object and intantiates it with the widget layout
	    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	    

		//fires up the app(Main activity) when the widget is clicked
		//for this a button is used in the widget layout
	    Intent configIntent = new Intent(context, MainActivity.class);
	    PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
	    remoteViews.setOnClickPendingIntent(R.id.widgetButton, configPendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		
		
	    updateView(context);
	}

	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		//gets the battery level and it shows in the widget
		int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = -1;
        
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;//assigns the battery level as an int
            batteryLevel = Integer.toString(level);//assings to the battery level as an string 
          //depeding on the battery level, the widget will show an image
            if (level >= 75) {
            	widgetImageFrame = R.drawable.fullbar;
            }
            if (level >= 50) {
            	widgetImageFrame = R.drawable.threebar;
            }
            else if (level >35 ) {
            	widgetImageFrame = R.drawable.halfbar;
            }
            else if (level >= 15) {
            	widgetImageFrame = R.drawable.twobar;
            }
            else
            {
            	widgetImageFrame = R.drawable.lowbar;
            }
        }
        else
        {
        	batteryLevel = "loading";
        }        
        updateView(context);
        super.onReceive(context, intent);
	}
	//when the phone reboots
	@Override
	public void onEnabled(Context context)
	{
		context.getApplicationContext().registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    updateView(context);
	}
	
	//gets the service status so it can be displayed in the widget
	  private boolean loadSavedPreferencesServiceStatus(Context ctx)
	    { 
	    	SharedPreferences sharedPreferences = PreferenceManager
		.getDefaultSharedPreferences(ctx);
	    return sharedPreferences.getBoolean("serviceStatus", false);
	    }
	    
	
	public void updateView(Context context) {
		RemoteViews thisViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);
		String status="";//holds the service status 
    	//finds out if the service is running our not
        boolean serviceStatus =loadSavedPreferencesServiceStatus(context);//calls the method to check if the service is on
        //if is on or off it will display it on the textview
        if (serviceStatus==true)
        {
        	status="Service:on";
        }
        else
        {
        	status="Service:off";
        }
		//sets the service status and batterly level
		thisViews.setTextViewText(R.id.widget_text, status+"\n"+batteryLevel+"%");
		thisViews.setImageViewResource(R.id.widget, widgetImageFrame);//sets the image to the widget view
		//updates the widget
		ComponentName thisWidget = new ComponentName(context, BatteryWidget.class);
		AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, thisViews);
	}
}