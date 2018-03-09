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
Tab1:Displays an image as talks about BookItCheap
Tab2:Lets the user choose a destination among 6 and a from and to Date
Tab3:Shows a Gridview Image Gallery with the destinations pictures
Tab4:Goes to a website that talks about these destinations 
Tab5:Lets the user entere a location and a destination to book a flight
*************************************
*/
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;



public class Tab4 extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab4);//sets the tab4
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://travel.usnews.com/Rankings/Worlds_Best_Vacations/")));
		//goes to a website
	
	}
}
