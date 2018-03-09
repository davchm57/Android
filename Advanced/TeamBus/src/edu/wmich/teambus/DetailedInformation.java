package edu.wmich.teambus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* This activity is ran when the user selects a bus stop.  In short, it receives the stop id from an intent, 
* then grabs the time from the database, determines the system time and then displays the next time a bus will
* be at the stop.  For instance, if the bus runs on the 15th minute every hour and its 3:00, it will display "3:15"
* However at 3:15, the output will be 4:15.   

* 
*************************************
*/


public class DetailedInformation extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
		 String currentBusRoute="";
		 ArrayList<String> routes = new ArrayList<String>();
		 String[] items ;
		 private ListView list;
		 private int stopId=0;
		 private String currentHour="";//current device hour
		 private String currentMinute="";//current device minute
		 private String busTempMinute="";//temporal minute (a bus can have up to 2 diffenet times every hour)
		 private String current24HourTime="";//current device hour in 24 hours format
		 private String currentBusTime="";//current bus time
		 private  ImageView image;
		 private TextView routeInfo;
		 private String stopName="";
		 private SimpleDateFormat sdf ;//used to get the hour and minute
		 final Context context = this;

	public static ArrayList<BusRoute> detailedInformation = new ArrayList<BusRoute>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.browse_route_layout);//load xml layout
				 
		list = (ListView)findViewById(R.id.browseListview); //instantiate list and link to listview in xml
		
	    
	    stopId = getIntent().getIntExtra("stopId", 0);//gets the selected stop ID
	    stopName = getIntent().getStringExtra("stopName");//gets the selected stop Name
		DatabaseHandler db = new DatabaseHandler(this);//creates db object
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		db.getStopsDetailedInformation(stopId);//calls this method to get the detailed information
		
		routeInfo=(TextView) findViewById(R.id.routeInfo); //instantiate and link textview
	    routeInfo.setText("Routes and times for:\n"+stopName); //load the text values
	    routeInfo.setTextSize(20);  //set the size of the text
	    
		updateValues();//calls this method to update the values in the array that is going to bet
		//set to the listview. The values are retreived from an static arraylist with objects that will have information
		//from our databate since the getStopsDetailedInformation adds object to this arraylist
		
		
		setList();//binds the information to the adapter
		
	     
		db.closeDataBase();  //close db
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; thi adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	
	   @Override
	    public void onClick(View arg0) {
		   
		   
		 //create and intent to start webactiity class 
		 Intent intent = new Intent(context, WebActivity.class);
		 
		 //pass the stopid value to that class
		 intent.putExtra("stopId",String.valueOf(stopId));
		 
		 
		 //run the intent
         startActivity(intent);
         
         
		  
	    }
	    
	   
	   //create a click event for the row
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	
	    	//create an intent that will load the stopMap, which will take the user to a map showing the  location of the stop
	    	Intent intent = new Intent(context, StopMap.class);
			
	    	//send the stopId value, which will be used get the geo location of the stop
	    	intent.putExtra("stopId",String.valueOf(stopId));
	    	
	    	//start the activity
	    	startActivity(intent);
	    	
	    	Log.v("v","click"); //used for debugging
	    }
	    
	    
	    //here gets the current hour and min
	    //to determine the bus departing time
	    private void getTime()
	    {
    		sdf = new SimpleDateFormat("hh"); //create a date format
    		
    		currentHour = sdf.format(new Date());//current hour
    		
    		sdf = new SimpleDateFormat("mm");//create a minute date format
    		
    		currentMinute= sdf.format(new Date());//current minute and apply format
    		
	    	for (int i=0;i<detailedInformation.size();i++)
	    	{
	    		busTempMinute=detailedInformation.get(i).getTime();
	    		StringTokenizer st;
		 		sdf = new SimpleDateFormat("HH");
	    		current24HourTime = sdf.format(new Date());//gets the 24 hours time just for comparison
	      		//checks if the current time is between 7am and 5pm
	    		//which is in the time range where this app gives accurate information
	    		
	    	
		    	
	    		/*
	    		 * This section of code evaluates the current minute of the hour, looks at the stop minute value
	    		 * and determine if the bus will pass within the next hour, or it has already passed.  For instance, if its
	    		 * 1:15 and the database entry is 01, the app will output 2:01 for the next time the bus passes
	    		 */    		
	    		if((Integer.parseInt(current24HourTime)<16 && (Integer.parseInt(current24HourTime)>6)))
		    	{
		    		
					if (busTempMinute.length()>0)
						
					{
							st = new StringTokenizer(busTempMinute, ",");
							 while (st.hasMoreTokens()) {
								 
								 currentBusTime=st.nextToken();//minute of the departing bus
								//if the minute of the departing time is greater than the current minute
								 //adds one to the hour

								 if(Integer.parseInt(currentBusTime)<Integer.parseInt(currentMinute))
										 {
									 		 if(currentHour.equalsIgnoreCase("12"))
									 		{
									 			 currentHour="1";
									 			 detailedInformation.get(i).setTime(currentHour+":"+currentBusTime+" ");
									 			
									 		}
									 		else
									 		{
									 			//adds one to the current hour to get the next bus coming
									 			detailedInformation.get(i).setTime((String.valueOf(Integer.parseInt(currentHour)+1))+":"+currentBusTime+" ");
									 			
									 		}
										 }
							
								   else
								   {
											 detailedInformation.get(i).setTime(currentHour+":"+currentBusTime+" ");
											
								   }
							 } 
							 
					    	}
				        }
		    	
				    	else
				    	{
				    		detailedInformation.get(i).setTime("This app provides information between 7am and 4:59pm. For a complete information press the button in the right");
				    	
				    	}
	  
				}
				
	    	}
	    
	    //back
	    
		  @Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		 
		        switch(item.getItemId()){
		            case android.R.id.home:
		                onBackPressed();
		        }
		        return true;
		    }

		public void updateValues() {
			  
			//get the times
			//these are stored in a bi-dimentional array
			for (int upper = 0; upper < detailedInformation.size(); upper++)
			{
				String timesTemp="";
			    for (int lower = 0; lower < MainActivity.times[upper].length; lower++)
			    {
			    	if(MainActivity.times[upper][lower] != null && ((MainActivity.times[upper][lower].length())>0))
			    	{

			    		timesTemp=timesTemp+(MainActivity.times[upper][lower].trim()+",");
			    		  
			    	}
			    }
			    
			    if(timesTemp.length()>0)//if this is true it means there is time to be set
		    	{
			    	 detailedInformation.get(upper).setTime(timesTemp);
		    	}
			   
			}
			
			getTime();
			
			//will loop the package array and get the name and tracking number
			//then add the string to the values list
			//the listfragment displays name and tracking number
			currentBusRoute="";
			for (int counter = 0; counter < detailedInformation.size(); counter++) {
				currentBusRoute = currentBusRoute
						+ detailedInformation.get(counter).getRouteId()+" "+(detailedInformation.get(counter).getName())
						+ "\n"+detailedInformation.get(counter).getTime();
				routes.add(currentBusRoute);
				currentBusRoute = "";//clears it since it contains temp values
			}


		}

		private void setList() {

			//At the moment we only give the next bus time (instead of giving 2 or 3 next bus times) so we need to remove the duplicates times since
			//The app is only calculating one time but some buses such as 16 and 11 have 2 times in 1 hour so that creates duplicates with the current algorithm
			ArrayList<String> removeDuplicates = new ArrayList<String>();
			HashSet<String> lookup = new HashSet<String>();
			for (String item: routes) {
			    if (lookup.add(item)) {
			        // Set.add returns false if item is already in the set
			        removeDuplicates.add(item);
			    }
			}
			routes= removeDuplicates;
			
			//passes the values from valuesList(a list) to values(an array)
			items = new String[routes.size()];
			items = routes.toArray(items);
		
			try {
					
				//create and array to load the list into
			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.detailedinformation , R.id.text, items) {
		            @Override
		            public View getView(int position, View convertView, ViewGroup parent) {
		                View row =  super.getView(position, convertView, parent);
		                
		           
		                //gets the imageview which will be in the right
		                View right = row.findViewById(R.id.right);
		                right.setTag(position);//sets the tag to identify this row
		                
		                right.setOnClickListener(DetailedInformation.this);
		                
		            	image = (ImageView) right.findViewById(R.id.right); //instiate an image view to th right resources
	            		image.setImageResource(R.drawable.info);//set and drawable resource to it
	            		

	            		
		                return row;
		            }
		        };
		        
		        
			    
		        list.setAdapter(adapter); //bind the items to the list
		        list.setOnItemClickListener(this); //set the list click adapter into this context
		       
			 
			//look for exceptions    
			} catch (Exception ex) {

			}
			routes.clear();//clear valuesList so it can be used again since it keeps temp values
		}

}


