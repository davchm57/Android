package edu.wmich.lab4.dcharl1678;
/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab04
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/26/13
* Date completed: 11/25/13
*************************************
BookItCheap is a travel agency 
We guarantee the best prices!
Book your trips safe and easy with this app
It has 6 classes
MainActivity
Tab1:Displays an image and talks about BookItCheap
Tab2:Lets the user choose a destination among 6 and a from and to Date
Tab3:Shows a Gridview Image Gallery with the destinations pictures
Tab4:Goes to a website that talks about these destinations 
Tab5:Lets the user entere a location and a destination to book a flight
*************************************
*/
import android.os.Bundle;
import android.view.Menu;
import android.app.TabActivity;
import android.content.Intent;
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
		TabSpec bookItCheap=tabHost.newTabSpec("BookItCheap");
		bookItCheap.setIndicator("BookItCheap", getResources().getDrawable(R.drawable.mainicon));//setting the icon for the tab
		Intent BookItCheapIntent=new Intent(this, Tab1.class);//it will happen when the tab is requested by the user
		//it will go to tab1 
		bookItCheap.setContent(BookItCheapIntent);
		
		//Second Tab
		TabSpec destinations=tabHost.newTabSpec("Destinations");
		destinations.setIndicator("Destinations", getResources().getDrawable(R.drawable.travelicon));
		Intent DestinationsIntent=new Intent(this, Tab3.class);
		destinations.setContent(DestinationsIntent);
		
		//Third Tab
		TabSpec bookDestinations=tabHost.newTabSpec("Booking");
		bookDestinations.setIndicator("Booking", getResources().getDrawable(R.drawable.cityicon));
		Intent BookDestinationIntent=new Intent(this, Tab2.class);
		bookDestinations.setContent(BookDestinationIntent);
		
		//Fourth Tab
		TabSpec website=tabHost.newTabSpec("Website");
		website.setIndicator("Website", getResources().getDrawable(R.drawable.websiteicon));
		Intent websiteIntent=new Intent(this, Tab4.class);
		website.setContent(websiteIntent);
		

		//Fifth Tab
		TabSpec bookFlight=tabHost.newTabSpec("Flights");
		bookFlight.setIndicator("Flights", getResources().getDrawable(R.drawable.planeicon));
		Intent BookFlightIntent=new Intent(this, Tab5.class);
		bookFlight.setContent(BookFlightIntent);
		
		tabHost.addTab(bookItCheap);
		tabHost.addTab(destinations);
		tabHost.addTab(bookDestinations);
		tabHost.addTab(website);
		tabHost.addTab(bookFlight);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
