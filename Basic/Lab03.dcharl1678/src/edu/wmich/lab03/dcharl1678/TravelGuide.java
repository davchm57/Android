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

import android.net.Uri;
import android.os.Bundle;

import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TravelGuide extends ListActivity{

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Create an array that will serve as Data for the listview 
		String [] travel={"Dominican Republic","Mexico","Meet Punta Cana, Dominican Republic","Meet Cancun, Mexico","Meet their music","Look at what you are missing!"};
		//feeding the listview with the array, thats the function of setlistadapter
		setListAdapter(new ArrayAdapter<String>(this, R.layout.travelguide,R.id.travelTv,travel));
	}

	protected void onListItemClick(ListView l, View v, int position, long id){
		//we use switch as options 
		switch(position){
		case 0://if position is zero it will go to this website
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.godominicanrepublic.com/rd/?lang=en&idioma=1")));
			break;
		case 1://if position is one it will go to this website
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.visitmexico.com/")));
			break;
		case 2://if position is 2 it will go to this activity
			startActivity(new Intent(TravelGuide.this, Drpic.class));
			
			break;
		case 3://if position is 3 it will go to this activity
			startActivity(new Intent(TravelGuide.this, Mexicopic.class));
			break;
		case 4://if position is 4 it will go to this activity
			startActivity(new Intent(TravelGuide.this,Music.class));
			break;
		case 5://if position is 5 it will go to this activity
			startActivity(new Intent(TravelGuide.this, GalleryPictures.class));
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_guide, menu);
		return true;
	}

}
