package edu.wmich.teambus;

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
* Due date: 04/04/14
* Date completed: 04/04/14
*************************************
* When users touch a name of bus stop in the list,
  then this file pulls the name of bus stop and its id from the database
  and display it in a different screen.
*************************************
*/

public class Stops extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
	
	
	String currentStop="";
	 ArrayList<String> stops = new ArrayList<String>();//contains the stops name and id
	 String[] items ;//items to bet set in the view
	 private String routeId="";//route id from the browseroute class
	 private ListView list;//listview that contains the items
	 private SharedPreferences preferences;//shared preferences will be loaded
	 private Editor editor;//used to save shared preferences
	 public static ArrayList<BusStop> busStops = new ArrayList<BusStop>();
	 private ImageView image;
	 private TextView routeInfo;
	 private String routeName="";
	 HashMap<Integer, String> favoriteBusStops=new HashMap<Integer, String>(); //used to save and retrieve fav routes
	 //contains the buststop objects
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.browse_route_layout); //load xml file 
		list = (ListView)findViewById(R.id.browseListview); //attach list to listview in xml
	    routeInfo=(TextView) findViewById(R.id.routeInfo); //link textview and xml
	    routeId = getIntent().getStringExtra("routeId");//gets the routeid 
	    routeName=getIntent().getStringExtra("routeName");//gets the rote name
	    routeInfo.setText(routeId+" "+routeName+"\n"+"Select a stop");
		DatabaseHandler db = new DatabaseHandler(this);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    
	      //open the database, use a try catch to avoid any possiblity of a crash
	      try {
	    	  db.openDatabase();
	      }catch(SQLException sqle){
	            throw sqle;
	      }
		db.getStops(routeId);//gets the stops within this route 
		//calling the method getStops in DatabaseHandler
		updateValues();//updates the values
		setList();//set these updated values
		
		db.closeDataBase(); //close the database :)
		loadFavoriteStops();  //run the load stop method
		
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
	            //Toast.makeText(this, "Right Accessory "+v.getTag(), Toast.LENGTH_SHORT).show(); // test to ensure button click registers
	            
	        	
	    	    image = (ImageView) findViewById(R.id.right);//link image to imageview on row xml
	    	    
	            int stopId=0; // set value to stopID to zero
	            stopId= busStops.get((Integer.parseInt(String.valueOf(v.getTag())))).getStopId();  //store value of row clicked
	            try
	            {
	            	//subtract the position in the adapter from the first visible position.
	            	//since I was having problems because when the user scrolled the position changed and it would crash
	            	int calc=(Integer.parseInt(String.valueOf(v.getTag())))-list.getFirstVisiblePosition();
		            View right = list.getChildAt(calc).findViewById(R.id.right);
	                image = (ImageView) right.findViewById(R.id.right);
	            
	    
	            //checks the shared preferences about the favorite bus routes 
	            //if this (the currently clicked) bus routes id is in the shared preferences
	            //it will turn on the heart
	            //else it will turn it off and remove it from the shared preferences	            
	            if (!favoriteBusStops.containsKey(stopId))  //this case runs if the route is not in favorites
	            {
	            	
	            	favoriteBusStops.put(stopId, "");  // add value
	            	image.setImageResource(R.drawable.sred);  //change the heart to red
	            	saveFavoriteStops(); //save the route
	            	Toast.makeText(getBaseContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
	            	
	            }
	            else { //the only other case is that it IS a already a favorite route, so we will remove it
	            
	            	 favoriteBusStops.remove(stopId);  // remove the route from the list
	            	 image.setImageResource(R.drawable.sblue); //change the heart back to the normal color
	            	 saveFavoriteStops(); //save the preferences
	            	 Toast.makeText(getBaseContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
	            }
	            }
	            
	            catch (Exception ex)
	            {
	            	
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
	  	
	  
	  	//this code is ran when a list item is clicked
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	
	    	
	        Intent myIntent = new Intent(Stops.this, DetailedInformation.class);  //create intent to load detailedinformation class
	        myIntent.putExtra("stopId",busStops.get(position).getStopId()); //load the stop id
	        myIntent.putExtra("stopName",busStops.get(position).getName()); //load the stop name
	        Stops.this.startActivity(myIntent); //run the intent
	    }
	    
	    

		public void updateValues() {
			//updates the values to  bet set in added to the adapter
			for (int counter = 0; counter < busStops.size(); counter++) {
				currentStop = ""
						+ busStops.get(counter).getName();
				stops.add(currentStop);
				currentStop = "";//clears it since it contains temp values
			}

		}


		private void setList() {

			//passes the values from valuesList(a list) to values(an array)
			items = new String[stops.size()];
			items = stops.toArray(items);
			
			try {
				//create an array adapter and load the items array
			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_view, R.id.text, items) {
			    	
			    	//create a row view
		            @Override
		            public View getView(int position, View convertView, ViewGroup parent) {
		                View row =  super.getView(position, convertView, parent);
		                
		                //favorite stops not implemented yet
		                View right = row.findViewById(R.id.right);  //creata a view in this row using right xml resources
		                right.setTag(position); //set right to "position 
		                right.setOnClickListener(Stops.this); //create on on click in this context
		                image = (ImageView) right.findViewById(R.id.right); //set an image view for the right resource
		                
		                //look to see if the stop is currently a favorit
		                if (favoriteBusStops.containsKey(busStops.get(position).getStopId()))
						{
		            		  //if so, set the image to the red heart
		                	 image.setImageResource(R.drawable.sred);
						}
		                
		                //this code is ran if it is not in th favorites list
		                else
		                {
		                	  image.setImageResource(R.drawable.sblue); //set the to the blue image
		                }
		                
		                
		                return row;  //return the view
		            }
		        };
		        list.setAdapter(adapter);  //set the list adapter
		        list.setOnItemClickListener(this); //get the on item click event in this context
			} catch (Exception ex) {

			}
			
			stops.clear();//clear valuesList so it can be used again since it keeps temp values

		}
		
		
		
		//saves the favorite routes to the shared preferences
		//it looks at the hashmap that contains the information using bus route id as the key
		//to know which one is currently in the favorite
		private void saveFavoriteStops() {
			
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this); //load preferences
			
			editor = preferences.edit(); //load the editor
			
			String temp=""; //create string
			
			if(favoriteBusStops.size()>0)  //check to see if there are routes in favorites
			{
				Set<Integer> keys = favoriteBusStops.keySet(); // get key set from hash map
				for (Integer key : keys) { //add a avlue
				temp=temp+String.valueOf(key)+","; //assign the key to the value of temp (the bus route number)
				}                            //^this comma is important for loading the favorites
				
				editor.putString("favoriteBusStops", temp); //add the value of temp to the favoriteBusStops preference
				editor.commit(); //submit the changes
			}
			
			//if there are no favorites, the stop is added to the first location
			else
			{
				editor.putString("favoriteBusStops", ""); 
				editor.commit();
			}
			

		}
		
		//method that loads favorite routes
		private void loadFavoriteStops()
		{	
			
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);//load the preferences
			
			String temp = preferences.getString("favoriteBusStops", ""); // get values or existing entries to favoriteBusStops
																
			StringTokenizer st;  //create a stringtokenizer to separate the favorites that are store with a comma between them
			
			if (temp.length()>0) //check for favorites
			{
				st = new StringTokenizer(temp, ","); //create the tokenizer and load the string temps and assign the "." as the delimiter
				 
				
				while (st.hasMoreTokens()) { //this code runs while there are still more commas
					
					
					 favoriteBusStops.put(Integer.parseInt(st.nextToken()), ""); //add next value to favoritebusstops
					 
					  }
			}				
			
		}
		
		
		@Override
		public void onResume()
		{
			loadFavoriteStops(); //load favorite stops when the onresume method is called
			super.onResume();
		}
	    
	
	    
	

}


