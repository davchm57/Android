package edu.wmich.lab01.dcharl1546;
import java.util.List;
import java.util.Locale;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
/* 
*************************************
* Programmer: David Charles
* Class ID: dchar5146
* Lab 01
* CIS 4700
* Spring 2014
* Due date: 2/12/14
* Date completed: 2/11/14
*************************************
Simple wmu app
Features:
Marks on a map:
Current location with address and coordinates
WMU Location
Proximity Alert:
This app lets you set a proximity alert to the following places:
Schenaider hall
Waldo library
The recreation center
The Bernhard center
If you click the main icon it will show a picture of wmu using a fragment

*************************************
*/

public class MyLocation extends FragmentActivity implements LocationListener, OnNavigationListener, OnInfoWindowClickListener {	
	
	
	private static final String STATE_NAV="nav";
	private static final int[] MAP_TYPES= { GoogleMap.MAP_TYPE_NORMAL,
	GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_SATELLITE,
	GoogleMap.MAP_TYPE_TERRAIN };
	
	
GoogleMap googleMap;
MapView m;
MapController mc;double l1;double l2;
String provider;
LocationManager locationManager;

final double wmuLat=42.283373;
final double wmuLng=-85.615212;
private Criteria criteria;
private SupportMapFragment fm;
private Location location;
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mylocation);
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the locatioin provider -> use
    // default
    criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    location = locationManager.getLastKnownLocation(provider);
	fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    googleMap = fm.getMap();
    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      onLocationChanged(location);
    } else {
      System.out.println("Location not avilable");
    }
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  @Override
  public void onLocationChanged(Location location) {
	  googleMap.clear();//clear any marker
	    //adding a marker on the map
	  addMarker(googleMap,wmuLat ,wmuLng ,
	            "Western Michigan University",
	            "1903 W Michigan Ave\nKalamazoo\nMichigan");
	  
      double lat = (double) (location.getLatitude());
      double lng = (double) (location.getLongitude());
      String address;
      LatLng latLng = new LatLng(lat, lng);

      //once the method getAddress is called
      //the address is retreived using the split method
      //and it goes to an array
      address=getAddress(lat,lng);
      String [] addressArray=address.split(",");
      String result=addressArray[0]+"\n"+addressArray[1]+"\n"+addressArray[2]+"\n"+"Latitude: "+String.valueOf(lat)+"\nLongitude: "+String.valueOf(lng);

      
      addMarker(googleMap, lat, lng,
              "My Location",result);

      //adds a line between wmu and current location
      googleMap.addPolyline(new PolylineOptions()
      .add(new LatLng(wmuLat, wmuLng), new LatLng(lat, lng))
      .width(5)
      .color(Color.RED)
      .geodesic(true));

      //setting our customized popupadapter to the window
    googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
    googleMap.setOnInfoWindowClickListener(this);//starts listening 
    
      //gives google the option to get our location
      googleMap.setMyLocationEnabled(true);
      
     //zoom to our location
     CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
     googleMap.animateCamera(cameraUpdate);
     locationManager.removeUpdates(this);
     
  }

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}

//This methods get the address using latitude and longitude then return it
//as a string
public String getAddress(double latitude,double longitude)
{
	String result="";
	try
	{
	Geocoder geocoder;
	List<Address> addresses;
	geocoder = new Geocoder(this, Locale.getDefault());
	addresses = geocoder.getFromLocation(latitude, longitude, 1);

	String address = addresses.get(0).getAddressLine(0);
	String city = addresses.get(0).getAddressLine(1);
	String country = addresses.get(0).getAddressLine(2);
	result=address+","+city+","+country;
	return result;
	}
	catch (Exception e)
	{
		result="Cannot get address";
		return result;
	}
}

//adds the marker to the map
//uses the lat and long to know where will put the marker
//title and snippet so the address long and lat can be displayed
//showinfowindows() will make it display at the beginning without even
//having to press the marker
private void addMarker(GoogleMap googleMap, double lati, double longi,
        String title, String snippet) {
googleMap.addMarker(new MarkerOptions().position(new LatLng(lati, longi))
                    .title((title))
                    .snippet((snippet))).showInfoWindow();

}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}

@Override
public void onInfoWindowClick(Marker marker) {
	// TODO Auto-generated method stub
	
}

@Override
public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	  googleMap.setMapType(MAP_TYPES[itemPosition]);

	    return(true);
}
@Override
public void onSaveInstanceState(Bundle savedInstanceState) {
  super.onSaveInstanceState(savedInstanceState);
  
  savedInstanceState.putInt(STATE_NAV,
                            getActionBar().getSelectedNavigationIndex());
}


}
