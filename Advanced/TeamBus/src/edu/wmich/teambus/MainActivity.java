package edu.wmich.teambus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* This java file instantiate a 'activity_main.xml' file which is a main screen for our application.
*************************************
*/

public class MainActivity extends Activity{
	
		
		//declare some class variablese
		private Button btnBrowse, btnFavoriteRoutes, btnFavoriteStops, btnNearMe ;
		
		public static String [][] times;
		//public static ArrayList<String> times=new ArrayList<String>();

		Intent intent;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    setContentView(R.layout.activity_main); //load xml
	    
	    //link java and xml
	    btnBrowse =(Button) findViewById(R.id.btnRoutes); 
	    btnFavoriteRoutes =(Button) findViewById(R.id.btnFavRoutes); 
	    btnFavoriteStops = (Button)findViewById(R.id.btnFavStops);  
	    btnNearMe = (Button)findViewById(R.id.btnNearMe);
	    
	    
	    //set onclick listener for the browse button
	    btnBrowse.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	
	        	//create intent to run the browser activity
	            intent = new Intent(MainActivity.this, BrowseRoutes.class);
	           
	            //run the activity
	            MainActivity.this.startActivity(intent);
	        }
	    });
	    
	    //set onclick listener for the favorite routes button
	    btnFavoriteRoutes.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	
	        	//create intent to load the favorites class
	            intent = new Intent(MainActivity.this, FavoriteRoutes.class);
	            
	            //run the intent
	            MainActivity.this.startActivity(intent);
	        }
	    });
	    
	    //set click for favorite stops button
	    btnFavoriteStops.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//create intent to load the favorites class
	            intent = new Intent(MainActivity.this, FavoriteStops.class);
	            
	            //run the intent
	            MainActivity.this.startActivity(intent);
				
			}
		});
	    
	    //set click for near me button
	    btnNearMe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent startMap = new Intent(MainActivity.this, NearestBusStop.class);
				startActivity(startMap);
				
				//fire a toast bc this is under construction
				//Toast.makeText(getBaseContext(), "This feature is under construction....", Toast.LENGTH_SHORT).show();
				
			}
		});
	    
	  
}
}

