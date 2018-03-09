/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Exercise 8
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/18/13
* Date completed: 11/18/13
*************************************
This app shows uses tabs to show the different ways of transportation when traveling
Each tab displays an Image and title using a Textview 
*************************************
*/
package edu.wmich.exercise8.dcharl1678;

import ed.wmich.exercise8.dcharl1678.R;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")//another deprecated
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost=getTabHost();//creating the TabHost object
		//any tab created needs to be added to this object
		//First Tab
		TabSpec planespec=tabHost.newTabSpec("Plane");
		planespec.setIndicator("Plane", getResources().getDrawable(R.drawable.planeicon));//setting the icon for the tab
		Intent PlaneIntent=new Intent(this, Tab1.class);//it will happen when the tab is requested by the user
		//it will go to tab1 which will display the textview with the image and title, in this case of a Plane
		planespec.setContent(PlaneIntent);
		
		//Second Tab
		TabSpec busspec=tabHost.newTabSpec("Bus");
		busspec.setIndicator("Bus", getResources().getDrawable(R.drawable.busicon));
		Intent BusIntent=new Intent(this, Tab2.class);
		busspec.setContent(BusIntent);
		
		//Third Tab
		TabSpec trainspec=tabHost.newTabSpec("Train");
		trainspec.setIndicator("Train", getResources().getDrawable(R.drawable.trainicon));
		Intent TrainIntent=new Intent(this, Tab3.class);
		trainspec.setContent(TrainIntent);
		
		//Fourth Tab
		TabSpec ferryspec=tabHost.newTabSpec("Ferry");
		ferryspec.setIndicator("Ferry", getResources().getDrawable(R.drawable.ferryicon));
		Intent FerryIntent=new Intent(this, Tab4.class);
		ferryspec.setContent(FerryIntent);
		
		//adding it to the tabhost, if these statements are forgotten then the tabs won't be displayed
		tabHost.addTab(planespec);
		tabHost.addTab(busspec);
		tabHost.addTab(trainspec);
		tabHost.addTab(ferryspec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
