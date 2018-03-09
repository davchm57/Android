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


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationIntent extends Activity {

	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.notificationintent);
	        try
	        {
	        //gets the battery level so the activity can show some information about he notification
	        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("batlevel")) {
	            String batteryLevel= getIntent().getExtras().getString("batlevel");
	            TextView title=(TextView) findViewById(R.id.notificationBatteryText);
	            title.setText(batteryLevel);
	    }
	        //creates a button to dismiss the activity
	        Button dismissBtn=(Button) findViewById(R.id.dismissBtn);
	        
	        dismissBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	finish();//if dismiss button is clicked it will call finish
                }
            });
	        //get the image view and makes it shake using an animation
	        ImageView img = (ImageView) findViewById(R.id.batteryImage);
	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.anim);
	        shake.reset();
	        shake.setFillAfter(true);
	        img.startAnimation(shake);
	        BatteryFragment.toggle.setChecked(false);//turns the toggle button off(in the tab1)
	        }
	        catch (Exception ex)
	        {
	        	
	        }
}
}