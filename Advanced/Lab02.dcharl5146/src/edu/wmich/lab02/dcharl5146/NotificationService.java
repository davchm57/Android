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


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;



public class NotificationService extends Service {

	 private static final String ACTION="android.intent.action.BATTERY_CHANGED";//adds this action to the broadcast receiver
	 private static final int FM_NOTIFICATION_ID = 0;//notification id
	 private BroadcastReceiver notificationReceiver;//this is used to monitor the battery level
	 //the service will start this broadcast receiver
	 private int rawlevel,scale,level;//used to get the battery level 
	 private String minimunBatteryLevel;//minimun battery level set by the user(for the notificaiton)
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        this.notificationReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

            	
            	//gets the battery level
        		rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                
                
                //saveSharedPreferences(context);
                saveSharedPreferences(context,minimunBatteryLevel);
                
                //checks if the current battery level is equal or lower than the one set by the user
                //it if its it will create a notification
                if (level<=Integer.parseInt(minimunBatteryLevel) )
        		{
                	//this will make sure the toggle button is set off since the service won't be running
                	saveSharedPreferences(false);
                	try
                	{
                	BatteryFragment.toggle.setChecked(false);
                	}
                	catch (Exception ex)
                	{
                		
                	}
                	context.stopService(new Intent(context,NotificationService.class));//stops the service
                	//since the notitification will be fired up
                	createNotification(context,intent);//calls createnotifiction method
                	
        		}

            }
        };
        //registers the receiver
        this.registerReceiver(this.notificationReceiver, theFilter);
	}
	
	
	//saves the minimun level of the battery so the bootbroadcast knows what was it
	//so if the bootbroadcast starts the service, the service just have to load this shared preference
	private void saveSharedPreferences(Context context,String minimunBatteryLevel)
	{
		  
		  SharedPreferences sharedPreferences = PreferenceManager
  				.getDefaultSharedPreferences(context);
  		Editor editor = sharedPreferences.edit();
  		editor.putString("minimunlevel", minimunBatteryLevel);
  		editor.commit();
		
	}
	
	//loads the minimun battery level either from the same service that saved it before the phone rebooted
	//or from the user itself
	private void loadSharedPreferences()
	{
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
     minimunBatteryLevel=sharedPreferences.getString("minimunlevel","50");
		
	}
	
	
	//tells the bootbroadcast the status of the service
		private void saveSharedPreferences(boolean status)
		{
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("serviceStatus", status);
			editor.commit();
		}
	

   private void createNotification(Context context, Intent intent)
   {
	
   	Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//default sound notificaiton
   	NotificationManager mNotificationManager  = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
   	Intent notificationIntent = new Intent(getApplicationContext(), NotificationIntent.class);
   	notificationIntent.putExtra("batlevel", "Current Battery level: "+String.valueOf(level)+"%");
   	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
   	
  	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
   	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
   	.setSmallIcon(R.drawable.fullbar)
   	.setContentTitle("Battery Notification")
   	.setStyle(new NotificationCompat.BigTextStyle()
   	.bigText("Current Battery Level:"+String.valueOf(level)))
   	.setLights(Color.BLUE, 3000, 3000)//led going to turn blue
   	.setContentText("Current Battery Level:"+String.valueOf(level)).setAutoCancel(true);
   	mBuilder.setSound(alarmSound);
   	mBuilder.setContentIntent(contentIntent);
   	mNotificationManager.notify(FM_NOTIFICATION_ID, mBuilder.build());
   
   }
   
   
	@Override
	public void onDestroy() {
		super.onDestroy();
		saveSharedPreferences(false);
		//unregister the receiver
		 this.unregisterReceiver(this.notificationReceiver);
	}
	
	
	

	  //starting the service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
	    //saves the service status as on
        saveSharedPreferences(true);
        
		//will get the minimunbatterylevel set by the user from shared preferences
		//using shared preferences because that way if the phone is restarted
		//it will still get it
		try{
			loadSharedPreferences();//will load shared preferences to the minimun battery level
			//is passed through this
		}
		catch (Exception ex)
		{
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
}
