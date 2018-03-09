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
* This java file reads users' favorite bus Stops.
*************************************
*/

public class FavoriteStops extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
	String currentFavoriteStop="";
	ArrayList<String> stops = new ArrayList<String>();
	 String[] items ;
	 private ListView list;
	 private SharedPreferences preferences;
	 private Editor editor;
	 private  ImageView image ;
	HashMap<Integer, String> favoriteBusStops=new HashMap<Integer, String>();
	public static ArrayList<BusStop> favoriteStops = new ArrayList<BusStop>();
	private  DatabaseHandler myDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ListView(this);
		setContentView(list);
		//list = (ListView)findViewById(R.id.listViewFavStops);
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    //everytime the user open this activity
	    //it will readd the favorite stops from the
	    //shared preferences so the array favoriteStops will
	    //contain temporal values and it will be cleared
	    updateView();//updates the view (set the adapter to the listview) this method will be called when stop  is deleted from the favorites as well
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
	    	 
	            int stopId=0;
	            stopId=favoriteStops.get(Integer.parseInt(String.valueOf(v.getTag()))).getStopId();
	            int calc=(Integer.parseInt(String.valueOf(v.getTag())))-list.getFirstVisiblePosition();
	            View right = list.getChildAt(calc).findViewById(R.id.right);
                image = (ImageView) right.findViewById(R.id.right);
                FavoriteStops.favoriteStops.clear();
	            //if the heart is clicked it will remove it from the favorite stops hashmap
	            //and will save the data from the hashmap to the shared preferences
	            favoriteBusStops.remove(stopId);
	            //image.setImageResource(R.drawable.sblue);
	            saveFavoriteStops();
	            
	            updateView();

	            break;
	        default:
	            break;
	        }
	    }
	    
	    @Override
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	       	//it will go to detailedinformation
	    	//sends the stop id clicked 
	        Intent myIntent = new Intent(FavoriteStops.this, DetailedInformation.class);
	        myIntent.putExtra("stopId",favoriteStops.get(position).getStopId());
	        myIntent.putExtra("stopName",favoriteStops.get(position).getName());
	        FavoriteStops.this.startActivity(myIntent);
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
				    
				    favoriteStops.clear();
				    myDbHelper = new DatabaseHandler(this);
				    loadFavoriteStops();//loads the favorite routes from the shared preferences
					updateValues();//update values
					setList();//updates the view
			 }
		  
		public void updateValues() {
			stops.clear();
//			will loop the favorite stops array and get the stop id and name
//			then add the string to the values list
			for (int counter = 0; counter < favoriteStops.size(); counter++) {
//				
				currentFavoriteStop = favoriteStops.get(counter).getName();
				stops.add(currentFavoriteStop);
			
				currentFavoriteStop = "";//clears it since it contains temp values
			}
			
			

		}


		private void setList() {

			//passes the values from valuesList(a list) to values(an array)
			items = new String[stops.size()];
			items = stops.toArray(items);
			
			try {

			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_view, R.id.text, items) {
		            @Override
		            public View getView(int position, View convertView, ViewGroup parent) {
		                View row =  super.getView(position, convertView, parent);
		                
		                //gets the imageview which will be in the right
		                View right = row.findViewById(R.id.right);
		                right.setTag(position);//sets the tag to identify this row
		                
		                right.setOnClickListener(FavoriteStops.this);
		                
		                //if the stop id is in the hashmap
		                //it will change the image (with a red heart)
		                //so the user can see is a favorite (blue heart is not)
		                //the favorite list will only show red hearts
		                
		            	if (favoriteBusStops.containsKey(favoriteStops.get(position).getStopId()))
						{
		            		image = (ImageView) right.findViewById(R.id.right);
		            		image.setImageResource(R.drawable.sred);
						}
		            	
		                return row;
		            }
		        };
		        list.setAdapter(adapter);
		        list.setOnItemClickListener(this);
			} catch (Exception ex) {

			}
			
			stops.clear();//clear valuesList so it can be used again since it keeps temp values

		}
		
		//saves the favorite stops id 
		//taking these from the hashmap 
		//saves it to a string separating each one by comma
		private void saveFavoriteStops() {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			editor = preferences.edit();
			String temp="";
			Set<Integer> keys = favoriteBusStops.keySet();
			for (Integer key : keys) {
			temp=temp+String.valueOf(key)+",";
			}
			editor.putString("favoriteBusStops", temp);
			editor.commit();

		}
		
		private void loadFavoriteStops()
		{	
			favoriteBusStops.clear();
			//loads the favorite stops from the shared preferences
			//these are stops id separated by comma
			preferences = PreferenceManager
					.getDefaultSharedPreferences(this);//instanciate the preference object
			
			String temp = preferences.getString("favoriteBusStops", ""); // it means a value has
																// not been set
			StringTokenizer st;
			
			if (temp.length()>0)
			{
				st = new StringTokenizer(temp, ",");
				 while (st.hasMoreTokens()) {
					 favoriteBusStops.put(Integer.parseInt(st.nextToken()), "");
					  }
				 //using the shared preferences the key of each bus stop is retreived
				 //using a has map we set the keys
					Set<Integer> keys = favoriteBusStops.keySet();
					for (Integer key : keys) {
				myDbHelper.getFavoriteStops(key);
					}
			}

		}
		
		@Override
		public void onResume()
		{
			loadFavoriteStops();
			super.onResume();
		}

}



