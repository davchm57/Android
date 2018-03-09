package edu.wmich.teambus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* This java file reads users' favorite bus routes.
*************************************
*/

public class FavoriteRoutes extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
	String currentFavoriteRoute="";
	ArrayList<String> routes = new ArrayList<String>();
	 String[] items ;
	 private ListView list;
	 private SharedPreferences preferences;
	 private Editor editor;
	 private  ImageView image ;
	HashMap<String, String> favoriteBusRoutes=new HashMap<String, String>();
	public static ArrayList<BusRoute> favoriteRoutes = new ArrayList<BusRoute>();
	private  DatabaseHandler myDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ListView(this);
	    setContentView(list);
	    //everytime the user open this activity
	    //it will readd the favorite routes from the
	    //shared preferences so the array favoriteRoutes will
	    //contain temporal values and it will be cleared
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    updateView();//updates the view (set the adapter to the listview) this method will be called when a route is deleted from the favorites as well
	    
		
	}
	

	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        switch(item.getItemId()){
	            case android.R.id.home:
	            	onBackPressed();
	        }
	        return true;
	    }
	
	 private void updateView()
	 {
		    
		    favoriteRoutes.clear();
		    myDbHelper = new DatabaseHandler(this);
		    loadFavoriteRoutes();//loads the favorite routes from the shared preferences
			updateValues();//update values
			setList();//updates the view
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
	        case R.id.right:

	            String routeId="";
	            routeId=favoriteRoutes.get(Integer.parseInt(String.valueOf(v.getTag()))).getRouteId();
	            
	        	int calc=(Integer.parseInt(String.valueOf(v.getTag())))-list.getFirstVisiblePosition();
	            View right = list.getChildAt(calc).findViewById(R.id.right);
                image = (ImageView) right.findViewById(R.id.right);
	            
//	            if (!favoriteBusRoutes.containsKey(routeId))
//	            {
//	            	favoriteBusRoutes.put(routeId, "");
//	            	image.setImageResource(R.drawable.hred);
//	            	saveFavoriteRoutes();
//	            }
//	            else
//	            {
	            	 favoriteBusRoutes.remove(routeId);
	            	 //image.setImageResource(R.drawable.hblue);
	            	 saveFavoriteRoutes();
	         //   }
	            
	            	 updateView();
	            
	            break;
	        default:
	            break;
	        }
	    }
	    
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

	        Intent myIntent = new Intent(FavoriteRoutes.this, Stops.class);//create an intent to 
	        myIntent.putExtra("routeId",favoriteRoutes.get(position).getRouteId()); //add selected route to the intent
	        myIntent.putExtra("routeName",favoriteRoutes.get(position).getName()); //add selected route to the intent
	        FavoriteRoutes.this.startActivity(myIntent); //run the intent

	    }
	    
	    

		public void updateValues() {
//			will loop the favorite routes array and get the route id and name
//			then add the string to the values list
			routes.clear();		
			Log.v("routes",String.valueOf(favoriteRoutes.size()));
			
			for (int counter = 0; counter < favoriteRoutes.size(); counter++) {
				
				currentFavoriteRoute = String.valueOf(favoriteRoutes.get(counter)
						.getRouteId()+" "
						+ favoriteRoutes.get(counter).getName());
				routes.add(currentFavoriteRoute);
			
				currentFavoriteRoute = "";//clears it since it contains temp values
			}
			
			
			
		}


		private void setList() {

			//passes the values from valuesList(a list) to values(an array)
			
			items = new String[routes.size()];
			items = routes.toArray(items);

			try {

			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_view, R.id.text, items) {
		            @Override
		            public View getView(int position, View convertView, ViewGroup parent) {
		                View row =  super.getView(position, convertView, parent);
		                
		                //gets the imageview which will be in the right
		                View right = row.findViewById(R.id.right);
		                right.setTag(position);//sets the tag to identify this row
		                
		                right.setOnClickListener(FavoriteRoutes.this);
		                
		                //if the route id is in the hashmap
		                //it will change the image (with a red heart)
		                //so the user can see is a favorite (blue heart is not)
		                //the favorite list will only show red hearts
		                
		                
		            	if (favoriteBusRoutes.containsKey(favoriteRoutes.get(position).getRouteId()))
						{
		            		image = (ImageView) right.findViewById(R.id.right);
		            		image.setImageResource(R.drawable.hred);
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
		
		//saves the favorite routes id 
		//taking these from the hashmap 
		//saves it to a string separating each one by comma
		private void saveFavoriteRoutes() {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			editor = preferences.edit();
			String temp="";
			Set<String> keys = favoriteBusRoutes.keySet();
			for (String key : keys) {
			temp=temp+String.valueOf(key)+",";
			}
			editor.putString("favoriteBusRoutes", temp);
			editor.commit();

		}
		
		private void loadFavoriteRoutes()
		{	
			 favoriteBusRoutes.clear();
			//loads the favorite routes frmo the shared preferences
			//these are routes id separated by comma
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);//instanciate the preference object
			
			String temp = preferences.getString("favoriteBusRoutes", ""); // it means a value has
																// not been set
			StringTokenizer st;
			
			if (temp.length()>0)
			{
				st = new StringTokenizer(temp, ",");
				 while (st.hasMoreTokens()) {
					 favoriteBusRoutes.put((st.nextToken()), "");
					  }
				 //using the shared preferences the key of each bus route is retreived
				 //using a has map we set the keys
					Set<String> keys = favoriteBusRoutes.keySet();
					for (String key : keys) {
				myDbHelper.getFavoriteRoutes(key);
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



