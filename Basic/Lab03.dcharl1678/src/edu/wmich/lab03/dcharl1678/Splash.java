/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab03
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/06/13
* Date completed: 11/04/13
*************************************
This app have a list view that lets you choose different things to do
2 Classes which function is to open a browser and to go a website
2 Classes with large images
1 Class which function is to play music 
1 Class with a gallery
*************************************
*/
package edu.wmich.lab03.dcharl1678;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//creates a timer task so after a amount of time something will happen
		//in this case the splash screen will be displayed for 5 seconds, after that it will go
		//to the main activity
		TimerTask task = new TimerTask()
		{
			
			public void run()
			{
				finish();//letting the os know that this activity won't be active
				startActivity(new Intent(Splash.this, TravelGuide.class));//goes to the main activity
			}
			
		};
		Timer opening= new Timer();//creates a timer to time the amount of time this activity(the splash screen)
		//will be on the screen
		opening.schedule(task, 5000);//in this case is 5000
			}

	}


