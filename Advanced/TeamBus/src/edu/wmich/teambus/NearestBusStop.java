package edu.wmich.teambus;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* This is the java page for the map that displays the locations of all of the bus stops.  There are 3 routes, two of which have been
* split into two, which totals five.  On this page user can select a route and view all of the stop on google maps
*************************************
*/

public class NearestBusStop extends Activity {
	
	
	
	//setup some class variables and objects
	double longi = -85.615219 ;  //set up a lat/long for texting to avoid null pointers
	double lat = 42.283178; 
	LatLng currentLatLong; //used fr map location
	GoogleMap busMap; 
	List<NearestStopObj> nearestStopList;
	protected DatabaseHandler db;
	PolygonOptions line3, line11a, line11b, line16a, line16b;  // for the lines
	LocationManager locationManager;
	Button btnRouteThree, btnRoute11a, btnRoute11b, btnRoute16a, btnRoute16b;  //buttons
	AlertDialog.Builder dialog;  //used for checking for locations services
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearest_route);//load xml
		
		//link buttons in xml and java
		btnRouteThree = (Button)findViewById(R.id.btnRoute3);
		btnRoute11a = (Button)findViewById(R.id.btnRoute11a);
		btnRoute11b = (Button)findViewById(R.id.btnRoute11b);
		btnRoute16a = (Button)findViewById(R.id.btnRoute16a);
		btnRoute16b = (Button)findViewById(R.id.btnRoute16b);
		
		
		//instantiate the db
		db = new DatabaseHandler(this);
		try {
	    	  db.createDatabase();
	    	  
	      } catch (IOException ioe) {
	    	  
	            throw new Error("Unable to create database");
	      }
	      
	      //open the database, use a try catch to avoid any possiblity of a crash
	      try {
	    	  
	    	  db.openDatabase();
	    	  
	      }catch(SQLException sqle){
	    	  

	            throw sqle;
	      }
		
	      
	    getLocation(); // get users current position
		
		currentLatLong = new LatLng(lat, longi);  //load it into a map coordinate
		
		getActionBar().setDisplayHomeAsUpEnabled(true);  //set actionbar as a back button
		
		
		
		//setup lines to "trace" the routes, there is one for each route
		line3 = new PolygonOptions();
		line11a = new PolygonOptions();
		line11b = new PolygonOptions();
		line16a = new PolygonOptions();
		line16b = new PolygonOptions();
		busMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		busMap.setMyLocationEnabled(true);
		busMap.setTrafficEnabled(true);
		
		
		
		
		//create a map location and zoom level using the current lat and long
		CameraUpdate currentLoc = CameraUpdateFactory.newLatLngZoom(currentLatLong, 12.5f); 
		
		busMap.moveCamera(currentLoc);  //zoom to the position created above
		
		
		//click command for route three
		btnRouteThree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if (nearestStopList != null){//check to see if the list has values
					nearestStopList.clear();//if so, clear them
				}
				busMap.clear(); //clear the map to get rid of any routes currently showing
				
				loadRoute3(); //run the route three method to get the bus routes (another method adds them to the line during this process)
				//line.color(Color.BLUE);
				//busMap.addPolyline(line);
				busMap.addPolygon(line3); //display line 3
			
				
				
			}
		});
		
		
		//click event for route 11a, its the exacte same as route three
		btnRoute11a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//check and clear list, if needed
				if (nearestStopList != null){
					nearestStopList.clear();
				}
				busMap.clear(); //clear the mad
				
				loadRoute11a(); //run method to load stops and pass them to line (nestd method)
				busMap.addPolygon(line11a);//display the line
				
				
			}
		});
		
		//click event for route 11b
		btnRoute11b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//check and clear list, if needed
				if (nearestStopList != null){ 
					nearestStopList.clear();
				}
				busMap.clear();//clear the map
				
				loadRoute11b(); //grab the line 11b routes, and load them into the line
				busMap.addPolygon(line11b); //add the line to the map
				
			}
		});
		
		//click event for route 16a, again the same stuff
		btnRoute16a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//check and clear list as needed
				if (nearestStopList != null){
					nearestStopList.clear();
				}
				busMap.clear();//clear the map
				
				loadRoute16a();//load 16a routes
				busMap.addPolygon(line16a); //display the line
				
			}
		});
		
		
		//click event for 16b
		btnRoute16b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				//check and clear list
				if (nearestStopList != null){
					nearestStopList.clear();
				}
				busMap.clear();//clear the map
				
				loadRoute16b();//load stops and add to map
				busMap.addPolygon(line16b);//add line to the map
				
			}
		});
		
	}
	
	
	
	//this is the method that grabs all the stops from the database and adds them to the line.
	public void loadRoute3 () {
		
		//check and clear list if needed
		if (nearestStopList != null){ 
			nearestStopList.clear();
		}
		
		//run the query from the database and enter them into the list
		nearestStopList =  db.getRouteThreeStops();
				
		
		//start a loop to grab all results of the query 
		for (int i = 0; i < nearestStopList.size(); i++) {
			
					//pull values added to list obj from the query
					double lat = Double.valueOf(nearestStopList.get(i).getLat()); //get the lat
					double longi = Double.valueOf(nearestStopList.get(i).getLongi());//get the long
					LatLng pos = new LatLng(lat, longi); //add them to a latlong (that mans "point of stop"...not anything else :D
					
					busMap.addMarker(new MarkerOptions()  //create a marker
							.title(nearestStopList.get(i).getName()) //add the name from the query
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon)) //use an icon from the drawable folder
							.position(pos));//load the POS created above
					
					 addLine3(pos);//add the point to the line
					 
					 
			
				}
				
		
	}
	
	
	//this method is the exact same as the one above, so I will not force you to read it again.  get location
	public void loadRoute11a () {
		
			
			if (nearestStopList != null){
				
			nearestStopList.clear();
			
			}
			
			nearestStopList =  db.getRoute11aStops();
					
			for (int i = 0; i < nearestStopList.size(); i++) {
						
						double lat = Double.valueOf(nearestStopList.get(i).getLat());
						double longi = Double.valueOf(nearestStopList.get(i).getLongi());
						LatLng pos = new LatLng(lat, longi);
						
						busMap.addMarker(new MarkerOptions()
								.title(nearestStopList.get(i).getName())
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon))
								.position(pos));
						
						 addLine11a(pos);
						 
						 
				
					}
					
			
		}
	
	public void loadRoute11b () {
		
		
		if (nearestStopList != null){
			
		nearestStopList.clear();
		
		}
		
		nearestStopList =  db.getRoute11bStops();
				
		for (int i = 0; i < nearestStopList.size(); i++) {
					
					double lat = Double.valueOf(nearestStopList.get(i).getLat());
					double longi = Double.valueOf(nearestStopList.get(i).getLongi());
					LatLng pos = new LatLng(lat, longi);
					
					busMap.addMarker(new MarkerOptions()
							.title(nearestStopList.get(i).getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon))
							.position(pos));
					
					 addLine11b(pos);
					 
					 
			
				}
				
		
	}
	
	public void loadRoute16a () {
		
		
		if (nearestStopList != null){
			
		nearestStopList.clear();
		
		}
		
		nearestStopList =  db.getRoute16aStops();
				
		for (int i = 0; i < nearestStopList.size(); i++) {
					
					double lat = Double.valueOf(nearestStopList.get(i).getLat());
					double longi = Double.valueOf(nearestStopList.get(i).getLongi());
					LatLng pos = new LatLng(lat, longi);
					
					busMap.addMarker(new MarkerOptions()
							.title(nearestStopList.get(i).getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon))
							.position(pos));
					
					 addLine16a(pos);
					 
					 
			
				}
				
		
	}
		
	public void loadRoute16b () {
		
		
		if (nearestStopList != null){
			
		nearestStopList.clear();
		
		}
		
		nearestStopList =  db.getRoute16bStops();
				
		for (int i = 0; i < nearestStopList.size(); i++) {
					
					double lat = Double.valueOf(nearestStopList.get(i).getLat());
					double longi = Double.valueOf(nearestStopList.get(i).getLongi());
					LatLng pos = new LatLng(lat, longi);
					
					busMap.addMarker(new MarkerOptions()
							.title(nearestStopList.get(i).getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon))
							.position(pos));
					
					 addLine16b(pos);
					 
					 
			
				}
				
		
	}
		
	//this method looks to see if location services is active either prompts the usr to turn them on or gets the current location
	public void getLocation (){
		
		 
		 //setup a couple booleans
		 boolean network_enabled = true;
		 boolean gps_enabled = true;
		 
		 	//instantiate the lm
		 	locationManager = null;
		 	
		 	//look to see if its null, and runn code accordingly
		    if(locationManager == null)
		    	
		        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);//get location services
		    
		    try{
		    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //try getting the gps provider
		    
		    
		    }catch(Exception ex){} //catch any exception
		    
		    
		    try{
		    	
		    network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//try getting network based location
		    
		    }catch(Exception ex){}  //catch any exception
		    
		    
		    //if there are no lcoation services, run this code
		   if(!gps_enabled && !network_enabled){
		        dialog = new AlertDialog.Builder(this); //create an alert dialog in this context
		        dialog.setMessage("Location Services is not Enabled!"); //set the content of the dialog
		        
		        //creat the positive button and start a click listener
		        dialog.setPositiveButton("Enable Location Services", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
		                // TODO Auto-generated method stub
		            	
		            	//create and intent to launch location service settings
		                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
		                
		                //launch the intent for a result 
		                startActivityForResult(myIntent, 0);    
		            }
		        });
		        
		        //add a negative button and setup the click listener
		        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
		                // TODO Auto-generated method stub
		            //not code added, it just return to the activity
		            }
		        });
		        dialog.show();  //display the dialog box, this 1 line of code is key to displaying the dialog!

		    }
		   
		   //if location servivces is active run the getLocationServices method
		   else
		   
		   {
			   getLocationServices();
	  
		   }
	       
		
		
	}
	
	
	
	//this short method is used above to see if location service was turned on using the start activity for result
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == 0 && resultCode == RESULT_OK) 
		{
		getLocationServices(); //if it has been turned in, run get location services method
		}
     }

	
	//this method is used to get the GPS location
	private void getLocationServices()
	{
		  //create a string that will access location service
	    String svcName = Context.LOCATION_SERVICE;
	    
	    //use location manager to get system services
	    locationManager = (LocationManager)getSystemService(svcName);

	    //create criteria for the GPS info that will ben used
	    Criteria criteria = new Criteria();
	    
	    //accuracy will be fine
	    criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
	    //low power requirement
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	    
	    //these settings are not needed and will not be used
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setSpeedRequired(false);
	    
	    //allow the provider to incur a monetary cost
	    criteria.setCostAllowed(true);
	    
	    //create a string that holds the criteria
	    String provider = locationManager.getBestProvider(criteria, true);
	    
	    //get the last location based on the criteria applied to provider
	  	Location now = locationManager.getLastKnownLocation(provider);
	
		try
		{
		
	    lat = now.getLatitude();//get lat from the now location manager 
	    longi = now.getLongitude();//get long from the now location manager 
		
		
		}
		
		//catch an errors, if necessary it will launch the location manager settings
		catch (Exception ex)
		{
			Log.v("ex","exs"+ex.getMessage());
			 Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
             startActivity(myIntent);
		}
	    
	}
	
	
	
	//this method loads the coordinates to the line during the loadRoute3 method
	public void addLine3(LatLng latlng){
		
		line3.add(latlng);//load the coordinates from the iteration
		line3.strokeColor(Color.rgb(109,182,219));//set the color of the line
		//line.fillColor(Color.BLUE);
		
		
	}
	
	
	//this method loads the coordinates to the line during the loadRoute11a method
	public void addLine11a(LatLng latlng){
		
		
		line11a.add(latlng);
		line11a.strokeColor(Color.rgb(109,182,219));
		//line.fillColor(Color.BLUE);
		
		
	}
	
	//this method loads the coordinates to the line during the loadRoute11b method
	public void addLine11b(LatLng latlng){
			
			
			line11b.add(latlng);
			line11b.strokeColor(Color.rgb(109,182,219));
			//line.fillColor(Color.BLUE);
			
			
		}
	
	//this method loads the coordinates to the line during the loadRoute16a method
	public void addLine16a(LatLng latlng){
		
		
		line16a.add(latlng);
		line16a.strokeColor(Color.rgb(109,182,219));
		//line.fillColor(Color.BLUE);
		
		
	}
	
	//this method loads the coordinates to the line during the loadRoute16b method
	public void addLine16b(LatLng latlng){
			
		line16b.add(latlng);
		line16b.strokeColor(Color.rgb(109,182,219));
		//line.fillColor(Color.BLUE);
					}
	
	
	//this bit of make the actionbar button to to the previous page
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        switch(item.getItemId()){
	            case android.R.id.home:
	                onBackPressed();
	        }
	        return true;
	    }
	  
	

	

}
