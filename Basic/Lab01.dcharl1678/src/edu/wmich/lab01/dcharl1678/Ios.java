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
*Create a button "backBtn" that will bring us back to the main activity.
*
*************************************
*/
package edu.wmich.lab01.dcharl1678;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ios extends Activity {
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ios);
	        Button backBtn =(Button)findViewById(R.id.iosBackButton);//Creating a button object, referencing it to iosBackButton 
	        backBtn.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        startActivity(new Intent(Ios.this, MainActivity.class));//When backBtn is clicked, this will be executed
	        	}
	        	});

	    }

}
