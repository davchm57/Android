/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Exercise 5
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 10/21/13
* Date completed: 10/20/13
*************************************
This app use a list view that lets you choose between 4 websites, facebook's, twitter's, tumblr's and pinteret's
So when you click on one of the options it will take you to its respective website
*************************************
*/
package edu.wmich.exercise5.dcharl1678;

import android.net.Uri;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] data ={"Facebook", "Twitter","Tumblr", "Pinterest"};//creates the array with the websites name
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, R.id.website, data));//creates the adapter for the lsitview 
		//we need this statement to display data from the array in the list
		
		
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id){
		//we use switch as options 
		switch(position){
		case 0:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.Facebook.com")));//takes you to facebook's website
			break;
		case 1:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.Twitter.com")));//takes you to twitters's website
			break;
		case 2:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.Tumblr.com")));//takes you to tumblr's website
			break;
		case 3:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.Pinterest.com")));//takes you to pinterest's website
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
