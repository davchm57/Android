/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab01: Android vs iOS
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 9/25/13
* Date completed: 9/24/13
*************************************
*Creates 2 image buttons "iosBtn" and "androidBtn"
*iosBtn will take us to the iOS activity when clicked
*androidBtn will take us to the android antivity when clicked
*************************************
*/
package edu.wmich.lab01.dcharl1678;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageButton iosBtn = (ImageButton)findViewById(R.id.appleLogoButton);//Creating a button object, referencing it to appleLogoButton

		  	iosBtn.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {//when iosBtn (referencing to appleLogoButton) is clicked, this will be executed
	            	 startActivity(new Intent(MainActivity.this, Ios.class));
	            }});
		  	
		  	
		  	ImageButton androidBtn = (ImageButton)findViewById(R.id.androidLogoButton);//Creating a button object, referencing it to androidLogoButton
			
			  androidBtn.setOnClickListener(new OnClickListener(){
		            public void onClick(View v) {//when androidBtn (referencing to androidLogoButton) is clicked, this will be executed
		            	 startActivity(new Intent(MainActivity.this, Android.class));

		            }});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
