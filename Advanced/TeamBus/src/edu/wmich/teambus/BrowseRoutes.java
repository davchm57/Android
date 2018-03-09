package edu.wmich.teambus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* This java file pulls a list view that shows the list of all bus stops for a specific bus.  
* For this prototype, only the information from route 3 is available.  All times, stops, and geotags
* had t be obtained manually.  However, we just learned they have a REST server, which may change things.
* At the very least, it will make getting the route data faster and easier. * 
* 
*************************************
*/

public class BrowseRoutes extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
	
	//create some variables 
	 String currentRoute ="";  
	 ArrayList<String> routes = new ArrayList<String>();  //used to store route info
	 String[] items ; //string will be set to the view
	 private ListView list;//will display the data
	 private SharedPreferences preferences;//shared preferences will be loaded
	 private Editor editor;//used to save shared preferences
	 private  ImageView image ;//contains the heart image
	 private TextView routeInfo;
	HashMap<String, String> favoriteBusRoutes=new HashMap<String, String>(); //used to save and retrieve fav routes
	public static ArrayList<BusRoute> busRoute = new ArrayList<BusRoute>();  //used to store info using the BusRoute java object

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_route_layout);
		list = (ListView)findViewById(R.id.browseListview);
	    
	    busRoute.clear(); //clear the busroute array to ensure it is empty before use
	    DatabaseHandler myDbHelper = new DatabaseHandler(this); //instantiate the dbhelper
	    routeInfo=(TextView) findViewById(R.id.routeInfo);
	    routeInfo.setText("Select a route");
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
    
	    
//	  //checks if the database has been created
      try {
    	  myDbHelper.createDatabase();
    	  
      } catch (IOException ioe) {
    	  
            throw new Error("Unable to create database");
      }
      
      //open the database, use a try catch to avoid any possiblity of a crash
      try {
    	  
    	  myDbHelper.openDatabase();
    	  
      }catch(SQLException sqle){
    	  

            throw sqle;
      }
//	    try {
//			myDbHelper.copyDataBase();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
      
	    loadFavoriteRoutes();//load the favorite routes
	    myDbHelper.getWmuRoutes();//gets all routes
		updateValues();//update the values with the routes
		setList();//binds the data
		myDbHelper.closeDataBase(); //dont forget to close your database!
		
		//if (savedInstanceState == null) {		}   -----> maybe used in final version
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; thi adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	  @Override
	    public void onClick(View v) {
	        switch(v.getId()) {
	        
	        //this code runs if the heart is clicked
	        case R.id.right:
	    	    image = (ImageView) findViewById(R.id.right);	    	    
	    	    
	            String routeId=""; // set value to stopID to zero
	            routeId= busRoute.get(Integer.parseInt(String.valueOf(v.getTag()))).getRouteId();  //store value of row clicked

	        	int calc=(Integer.parseInt(String.valueOf(v.getTag())))-list.getFirstVisiblePosition();
	            View right = list.getChildAt(calc).findViewById(R.id.right);
                image = (ImageView) right.findViewById(R.id.right); 	
	            
	            //checks the shared preferences about the favorite bus routes 
	            //if this (the currently clicked) bus routes id is in the shared preferences
	            //it will turn on the heart
	            //else it will turn it off and remove it from the shared preferences
	            
	            if (!favoriteBusRoutes.containsKey(routeId))  //this case runs if the route is not in favorites
	            {
	            	favoriteBusRoutes.put(routeId, "");  // add value
	            	image.setImageResource(R.drawable.hred);  //change the heart to red
	            	saveFavoriteRoutes(); //save the route
	            	Toast.makeText(getBaseContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
	            	
	            }
	            else { //the only other case is that it IS a already a favorite route, so we will remove it
	            
	            	 favoriteBusRoutes.remove(routeId);  // remove the route from the list
	            	 image.setImageResource(R.drawable.hblue); //change the heart back to the normal color
	            	 saveFavoriteRoutes(); //save the preferences
	            	 Toast.makeText(getBaseContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
	            }
	           break;
	        }
	    }
	    
	  
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        switch(item.getItemId()){
	            case android.R.id.home:
	                onBackPressed();
	        }
	        return true;
	    }
	  
	  //this section of code is ran when a list item is clicked
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	
	    	
	        //Toast.makeText(this, "Item Click "+position, Toast.LENGTH_SHORT).show();  <---used for testing
	        Intent myIntent = new Intent(BrowseRoutes.this, Stops.class);//create an intent to 
	        myIntent.putExtra("routeId",busRoute.get(position).getRouteId()); //add selected route to the intent
	        myIntent.putExtra("routeName",busRoute.get(position).getName()); //add selected route to the intent
	        BrowseRoutes.this.startActivity(myIntent); //run the intent
	    }
	    
	    
	    //this method updates the values of the array
		public void updateValues() {
			
			//will loop the routes array and get the name and route id
			//then add the string to the values list
			for (int counter = 0; counter < BrowseRoutes.busRoute.size(); counter++) {
				currentRoute = String.valueOf(BrowseRoutes.busRoute.get(counter)
						.getRouteId()+" "
						+ BrowseRoutes.busRoute.get(counter).getName());
						
				routes.add(currentRoute); //add the current route value to routes
			
				currentRoute = ""; //clear the value
			}
		}

		private static class ViewHolder {
		    public TextView text;
		}
		
		private void setList() {
			
			//passes the values from valuesList(a list) to values(an array)
			items = new String[routes.size()]; //creates a string array based on the size of routes
			items = routes.toArray(items); //add the values of routes to the items lists

			try {
				//call an array adapter and name it adapter in this context 
			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_view, R.id.text, items) {
		            
			    	//
			    	@Override
		            public View getView(int position, View convertView, ViewGroup parent) {
			    		
		                View row =  super.getView(position, convertView, parent);
		                
		                View right = row.findViewById(R.id.right);
		                right.setTag(position);

		                right.setOnClickListener(BrowseRoutes.this);
		                image = (ImageView) right.findViewById(R.id.right);
		                //sets the heart red meaning is in the favorite routes
		            	if (favoriteBusRoutes.containsKey(busRoute.get(position).getRouteId()))
						{
		            		   
		            		 image.setImageResource(R.drawable.hred);
						}
		            	else
		            	{
		            		 image.setImageResource(R.drawable.hblue);
		            	}
		                
		                return row;
		            }
		        };
		        list.setAdapter(adapter);
		        
		        
		        list.setOnItemClickListener(this);
		        
			} catch (Exception ex) {

			}
			
			routes.clear();//clear valuesList so it can be used again since it keeps temp values

		}
		
		//saves the favorite routes to the shared preferences
		//it looks at the hashmap that contains the information using bus route id as the key
		//to know which one is currently in the favorite
		private void saveFavoriteRoutes() {
			
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this); //load preferences
			
			editor = preferences.edit(); //load the editor
			
			String temp=""; //create string
			
			if(favoriteBusRoutes.size()>0)  //check to see if there are routes in favorites
			{
				Set<String> keys = favoriteBusRoutes.keySet(); // get key set from hash map
				for (String key : keys) { //add a avlue
				temp=temp+String.valueOf(key)+","; //assign the key to the value of temp (the bus route number)
				}                            //^this comma is important for loading the favorites
				
				editor.putString("favoriteBusRoutes", temp); //add the value of temp to the favoriteBusRoutes preference
				editor.commit(); //submit the changes
			}
			else
			{
				editor.putString("favoriteBusRoutes", ""); 
				editor.commit();
			}
			

		}
		
		//method that loads favorite routes
		private void loadFavoriteRoutes()
		{	
			
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);//load the preferences
			
			String temp = preferences.getString("favoriteBusRoutes", ""); // get values or existing entries to favoriteBusRoutes
																
			StringTokenizer st;  //create a stringtokenizer to separate the favorites that are store with a comma between them
			
			if (temp.length()>0) //check for favorites
			{
				st = new StringTokenizer(temp, ","); //create the tokenizer and load the string temps and assign the "." as the delimiter
				 
				
				while (st.hasMoreTokens()) { //this code runs while there are still more commas
					
					
					 favoriteBusRoutes.put((st.nextToken()), ""); //add next value to favoritebusroutes
					 
					  }
			}				
			
		}
		
		
		@Override
		public void onResume()
		{
			loadFavoriteRoutes();
			super.onResume();
		}
	    
	

}



