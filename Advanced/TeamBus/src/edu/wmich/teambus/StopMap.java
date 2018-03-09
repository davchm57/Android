package edu.wmich.teambus;

/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
This activity is loaded when the user clicks on a stop time.  When this occurs, the stopid value is passed to this 
activity using and intent and then ran through a query to get the stop based on the stop ID value.  

The map is set a zoom value that should allow the user to see their current location as well as the stop when it is loaded.
*************************************
*/

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

public class StopMap extends Activity {
	
	
	//establish class variables and objs
	double longi = -85.615219 ; //load a couple dummy numbers to avoid a null pointer (these are campus coordinates)
	double lat = 42.283178; 
	LatLng currentLatLong;
	GoogleMap busMap; 	
	List<NearestStopObj> nearestStopList;
	private Intent intent;
	protected DatabaseHandler db;
	LocationManager locationManager;
	String stopID;
	AlertDialog.Builder dialog;  //used for checking for locations services
	
	  
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_map);//set the xml layout!!! dont forget this....will cause many problems if you forget it
		
		
		//setup a try catch
		try	{
			
			
		db = new DatabaseHandler(this);//instantiate the database
		busMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapTwo)).getMap(); //link the map and xml resouce
		busMap.setMyLocationEnabled(true); //enable the my location button
		busMap.setTrafficEnabled(true); //turn on current traffic! why not?
		
		intent = getIntent(); //get intent containt the stopid
		stopID = intent.getStringExtra("stopId");//load the stopid into an int variable
		
		
		getLocation(); //run get location method
		getStopLocation(); //run stop location method
		

		getActionBar().setDisplayHomeAsUpEnabled(true); //enable the action bar to be a button
		
		
		
		
		currentLatLong = new LatLng(lat, longi); //get the lat and long from getlocation and create a coordinat
		
	
		
		CameraUpdate currentLoc = CameraUpdateFactory.newLatLngZoom(currentLatLong, 11);//create map postion with the coordinate
		
		busMap.moveCamera(currentLoc); //move the map to the postion created above
		
		}
		
		//catch an errors, good news...there were not many
		catch (Exception ex)
		{
			Log.v("map","error "+ex.getMessage()); 
		}
		
		
	}
	
	//method used to get the users current location
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
	private void getLocationServices()
	{
		//instantiate the location manager
		LocationManager locationManager;
		    
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
	    
	    //get the location using the provider established above
		Location now = locationManager.getLastKnownLocation(provider);
		
		//load the lat and long with the info that was retrieved
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
	
	
	//method for getting the location of the stop 
	public void getStopLocation(){
		
		
		//make sure the list is empty
		if (nearestStopList != null){
			
			nearestStopList.clear();//clear is needed
			
			}
		
			//convert the stopid string to an integer
			int stopidInt = Integer.valueOf(stopID);
			
			//load the list with a query from the db (it will onyl be one stop this time)
			nearestStopList =  db.getStopLocation(stopidInt);
			
			//start a loop that will load the stop on to the map (there will only be one stop)
			for (int i = 0; i < nearestStopList.size(); i++) {
				
				double lat = Double.valueOf(nearestStopList.get(i).getLat()); //get the lat 
				double longi = Double.valueOf(nearestStopList.get(i).getLongi());//get the long
				LatLng pos = new LatLng(lat, longi);//create a map coorindate
				
				busMap.addMarker(new MarkerOptions() //create a new coordinate
						.title(nearestStopList.get(i).getName())//get the name of the stop
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.stopicon))//get a customer icon
						.position(pos));//load the item
			}
			
			
	}
	
	//allows the user to click the action bar to go back to the previous screen
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        switch(item.getItemId()){
	            case android.R.id.home:
	                onBackPressed();
	        }
	        return true;
	    }
		
		
	

	
	

}
