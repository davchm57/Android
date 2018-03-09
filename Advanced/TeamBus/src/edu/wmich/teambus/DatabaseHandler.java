package edu.wmich.teambus;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/* 
 *************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
 *************************************
* SQLiteOpenHelper class manages database creation and version management.
* 
* it contains all the methods that are called to access the database throughout the entire application.
* 
* 
* 
*************************************
 */

@SuppressLint("SdCardPath")
public class DatabaseHandler extends SQLiteOpenHelper {

	
	//Static variables 
	private static final int DATABASE_VERSION = 2;  //set the version of the database
	private static final int DATABASE_VERSION_OLD = 2;  //set old version of the database
	private SQLiteDatabase myDataBase;
	private static final String DATABASE_NAME = "busdb"; //set the name
	private static final String KEY_STOPID = "route_ID"; //set column name as key
	private static final String KEY_NAME = "route_Name"; //set route name as key
	public final static String DATABASE_PATH ="/data/data/edu.wmich.teambus/databases/";  //set path to db
	
	public static final String KEY_STOP_LAT = "lat";
	public static final String KEY_STOP_LONGI = "longi";
	public static final String KEY_TABLE_STOPS = "STOPS";
	public static final String KEY_STOP_NAME = "stop_Name";	
	public static final String KEY_ROUTE_11A = "11a";
	public static final int KEY_ROUTE_3 = 3;
	public static final String KEY_STOP_ID_ID = "stop_ID";
	public static final String KEY_TABLE_ROUTE_STOP = "ROUTE_STOP";
	
	/*
	 * 
	 * Note:  The variables above are declared, but we mostly used raw queries after spending to much time 
	 * locating missing spaces between variables and strings.  Plus, with the use of SQLiteStudio, we were able
	 * to contsruct the query and simply copy and paste
	 * 
	 */
	
	
	 
	private Context context;
	public DatabaseHandler(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	//Check database already exist or not
    public boolean checkDataBase()
    {
          boolean checkDB = false; //create true/false and set it false
          
          //look for db
          try
          {
                String myPath = DATABASE_PATH + DATABASE_NAME; //combine the path an db name
                File dbfile = new File(myPath); //create path to file
                checkDB = dbfile.exists(); //see if it exists
          }
          
          //catch exceptions
          catch(SQLiteException e)
          {
        	  return checkDB;
          }
          return checkDB;
    }
	
    //creates the database
	  public void createDatabase() throws IOException
      {
            boolean dbExist = checkDataBase(); //create true/false and assign it to false
            
            //if database base does not exists
            if(!dbExist)
            {
            	this.getReadableDatabase(); //create database
            	
                try {
                	
                    copyDataBase(); //run copy database method (copies from assets folder)
                    
                   //catch errors 
                } catch (IOException mIOException) {
                	
                    mIOException.printStackTrace();
                    throw new Error("Error copying database");
                  
                 //close db   
                } finally {
                    this.close();
                }
            }
           //to upgrade the database
      	  //it will compare the old version string with the new version string
      	  //if these are different then it will copy the file again 
          //if there is a better procedure for this please let us know
          //so basically this would be our own method of onUpGrade
      }
	  
	  //public method used for opening the database
	  public void openDatabase() throws SQLException
      {
            String myPath = DATABASE_PATH + DATABASE_NAME; //set the path
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);  //open the db
      }

	  
	  //public method for closing the database
      public synchronized void closeDataBase()throws SQLException
      {
    	  	
            if(myDataBase != null)  //look for open db
            	
                  myDataBase.close(); //if open then close it!
            
            super.close();
      }
	
	//copy the database from the assets folder
	public void copyDataBase() throws IOException
    {

          String outFileName = DATABASE_PATH + DATABASE_NAME;  //set the path
          OutputStream myOutput = new FileOutputStream(outFileName);  //create output stream
          InputStream myInput = this.context.getAssets().open(DATABASE_NAME); //create input stream, (don't ever cross the streams)
          byte[] buffer = new byte[1024]; //set a reasonable buffer size
          int length; //create int to read buffer
          while ((length = myInput.read(buffer)) > 0) //while data is coming in assign it to int
          {
                myOutput.write(buffer, 0, length);
          }
          myInput.close(); //once completed close the inputstream
          myOutput.flush(); //flush or clear the output streatm
          myOutput.close(); //close it 
    }
	
	
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + "ROUTE");
		// Create tables again
		onCreate(db);
	}
	
	
	
	//This method is used by the BrowseRoutes activity to get a list of all bus routes
	public List<Package> getWmuRoutes() {
		
		List<Package> packageList = new ArrayList<Package>();  //create list to hold routes
		
		// Select All Query
		String selectQuery = "SELECT  * FROM " + "ROUTE";  //create query to return all from the route table
		SQLiteDatabase db = this.getWritableDatabase(); //get the database
		Cursor cursor = db.rawQuery(selectQuery, null); //run the query

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			
			do {
				BusRoute busroute = new BusRoute(); //createa  new busroute obj
				busroute.setRouteId((((cursor.getString(0))))); //load the route id
				busroute.setName(((cursor.getString(1)))); //load the name
				
				BrowseRoutes.busRoute.add(busroute);  //add the whole thing to the list in browseroutes
			} while (cursor.moveToNext());
		}

		// return package list
		return packageList;
	}
	

	//using the shared preferences gets the favorites from the database
	//the shared preferences will countain a routeID
	public void getFavoriteRoutes(String routeId) {
		// Select All Query
		String selectQuery = "SELECT  route_Name FROM ROUTE WHERE route_ID ='" + routeId+"'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				BusRoute busroute = new BusRoute();
				busroute.setName(((cursor.getString(0))));
				busroute.setRouteId(routeId);
				// Adding package to list
				FavoriteRoutes.favoriteRoutes.add(busroute);
			} while (cursor.moveToNext());
		}
	}
	
	//using the shared preferences gets the favorites from the database
		//the shared preferences will countain a stopId
		public void getFavoriteStops(int stopId) {
			// Select All Query
			
			String selectQuery = "SELECT  stop_Name FROM STOPS WHERE stop_ID ="+stopId;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					BusStop busStop= new BusStop();
					busStop.setName(((cursor.getString(0))));
					busStop.setStopId((stopId));
					// Adding package to list
					FavoriteStops.favoriteStops.add(busStop);
				} while (cursor.moveToNext());
			}
		}
	
	
		//getting the number of stops associated with this bus route, this is used in stops java file
		public void getStops(String routeId) {
			
			
			ArrayList<Integer>tempArray=new ArrayList<Integer>(); //create a temporary array to hold the stop information
			
			//create query that gets the stops from the route id and order them ascending 0 to 60 so they are in order
			String selectQuery = "SELECT stop_ID FROM ROUTE_STOP WHERE route_ID ='"+routeId+"' ORDER BY stop_time asc";
			
			SQLiteDatabase db = this.getWritableDatabase(); //get the db
			Cursor cursor = db.rawQuery(selectQuery, null); //run the query
			Stops.busStops.clear(); //clear the list in the busStops object
			
			// loop through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					tempArray.add(cursor.getInt(0));//add the 
					
				} while (cursor.moveToNext());
			}
			
			
			//gets all the stops name from the stops ID
			for (int i=0;i<tempArray.size();i++)
			{
				selectQuery = "SELECT stop_Name FROM STOPS WHERE stop_ID="+tempArray.get(i); //get th stop name from each row
				cursor = db.rawQuery(selectQuery, null);//run the query
				
				if (cursor.moveToFirst()) {
					
					do {
						
						BusStop busStop= new BusStop();//create a new bustop object
						busStop.setName(((cursor.getString(0))));//load the name of the route
						busStop.setStopId(tempArray.get(i)); //lead the stopid to the 
						Stops.busStops.add(busStop); //add the stop to the stops.busStops list
					} while (cursor.moveToNext()); //move to the next item
				}
				
			}

		}

		//gets the detailed information of a stop
		//this include the routes that go by and the times that those route goes by
		 public void getStopsDetailedInformation(int stopId)
		 {
			 
			 
				String selectQuery = "SELECT  route_ID FROM ROUTE_STOP WHERE stop_ID =" + stopId;//create query to get all routes based on a stopid
				SQLiteDatabase db = this.getWritableDatabase();//open the db
				Cursor cursor = db.rawQuery(selectQuery, null);//run the query
				ArrayList<String>tempArray=new ArrayList<String>();//create a temp array
				DetailedInformation.detailedInformation.clear(); //clear the list in detailed information
				
				// loop through all rows and adding to temporary list
				if (cursor.moveToFirst()) {
					do {
						
						tempArray.add(cursor.getString(0));
						
						// Adding package to list
					} while (cursor.moveToNext());
				}
				
				getTimeByRouteId(stopId,tempArray);//load these items into a method called getTimeByRouteID
				
				//getting the routes name whitin this route id
				for (int i=0;i<tempArray.size();i++)
				{
					selectQuery = "SELECT route_Name FROM ROUTE WHERE route_ID='"+tempArray.get(i)+"'";
					cursor = db.rawQuery(selectQuery, null);
					
					if (cursor.moveToFirst()) {
						do {
							
							//create busRoute obj and load values
							BusRoute busRoute= new BusRoute();
							busRoute.setName(((cursor.getString(0))));
							busRoute.setRouteId(tempArray.get(i));
							// Adding bus route to list
							DetailedInformation.detailedInformation.add(busRoute);
						} while (cursor.moveToNext());
					}
				}

		 }
		
		
		 //gets time by route id 
		 //it will get the minutes and put it in an array named times declared in the main activity
		 //with the minutes the departing time can be calculated using the current hour
	public void getTimeByRouteId(Integer stopId, ArrayList<String>routesIds)
	{
		MainActivity.times=new String[routesIds.size()][2];//every route will go around a stop
		
		//at most twice an hour this is why the 2 in the array is the size for the column
		for(int i=0;i<routesIds.size();i++)//goes by every route id that go by this stop
		{
			String selectQuery = "SELECT stop_time FROM ROUTE_STOP WHERE stop_ID="+stopId+ " AND route_ID='"+routesIds.get(i)+"'";
			int inner=0;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					MainActivity.times[i][inner++]=cursor.getString(0);//puts it in the bi dimensional array
				} while (cursor.moveToNext());
			}
		}
		
		
	}
	
	
	
	/*
	 * I will do a quick block comment up here for the rest of this page.  getRouteThreeStops is commented fully,
	 * However the following four methods are the exact same.  Just copy, paste, and change the rout number.  After that
	 * just use the navigator off to the right to skip down to getStopLocation.   
	 * 
	 */
	
	
	//this method is used to load all of the stop of route three on the nearest routes activity
	public List<NearestStopObj> getRouteThreeStops (){
			
			List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();  //instantiate the object (avoid nlp)
			
			
			SQLiteDatabase db = this.getWritableDatabase();  //open the db
			
			//create the cursor and run the query, I created the query in SQLiteStudio and copy and pasted here...
			Cursor cursor = db.rawQuery("SELECT * FROM STOPS  WHERE stop_ID IN (SELECT stop_ID FROM ROUTE_STOP WHERE route_ID =3)", null);
			
			//start the curosor
			if (cursor.moveToFirst()) {
		    	
		    	do {  //if moving.toFirst do this stuff
		    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
		    	
		    	nearestRouteObj.setStopId(cursor.getInt(0));//set the stop id from the iteration
		    	
		    	nearestRouteObj.setLat(cursor.getString(1)); //set the lat value during the same iteration
		    	
		    	nearestRouteObj.setLongi(cursor.getString(2)); //set the long value during the iteration
		    	
		    	nearestRouteObj.setName(cursor.getString(3)); //set the name of the set stop during the iteration
		    	
		    	nearestStopList.add(nearestRouteObj); //add the object to the list (basically add everything as one entity
		    	
		    	//while cursor is moving to next, do nothing
		    	} while (cursor.moveToNext());
		    	}
			
			return nearestStopList;  //return the list
		
			
		}
	
	
	//code for getting route 11a
	public List<NearestStopObj> getRoute11aStops (){
		
		List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM STOPS  WHERE stop_ID IN (SELECT stop_ID FROM ROUTE_STOP WHERE route_ID =\"11a\")", null);
		
		if (cursor.moveToFirst()) {
	    	
	    	do {  //if moving.toFirst do this stuff
	    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
	    	
	    	nearestRouteObj.setStopId(cursor.getInt(0));
	    	nearestRouteObj.setLat(cursor.getString(1));
	    	nearestRouteObj.setLongi(cursor.getString(2));	
	    	nearestRouteObj.setName(cursor.getString(3));
	    	nearestStopList.add(nearestRouteObj); 
	    	
	    	//while cursor is moving to next, do nothing
	    	} while (cursor.moveToNext());
	    	}
		
		return nearestStopList;
	
		
	}
	
	//code for getting route 11b
	public List<NearestStopObj> getRoute11bStops (){
			
			List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			Cursor cursor = db.rawQuery("SELECT * FROM STOPS  WHERE stop_ID IN (SELECT stop_ID FROM ROUTE_STOP WHERE route_ID =\"11b\")", null);
			
			if (cursor.moveToFirst()) {
		    	
		    	do {  //if moving.toFirst do this stuff
		    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
		    	
		    	nearestRouteObj.setStopId(cursor.getInt(0)); 
		    	nearestRouteObj.setLat(cursor.getString(1));
		    	nearestRouteObj.setLongi(cursor.getString(2));
		    	nearestRouteObj.setName(cursor.getString(3));
		    	nearestStopList.add(nearestRouteObj);
		    	
		    	//while cursor is moving to next, do nothing
		    	} while (cursor.moveToNext());
		    	}
			
			return nearestStopList;
		
			
		}

	
	//code for getting route 16a
	public List<NearestStopObj> getRoute16aStops (){
		
		List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM STOPS  WHERE stop_ID IN (SELECT stop_ID FROM ROUTE_STOP WHERE route_ID =\"16a\")", null);
		
		if (cursor.moveToFirst()) {
	    	
	    	do {  //if moving.toFirst do this stuff
	    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
	    	
	    	nearestRouteObj.setStopId(cursor.getInt(0));
	    	nearestRouteObj.setLat(cursor.getString(1));
	    	nearestRouteObj.setLongi(cursor.getString(2));
	    	nearestRouteObj.setName(cursor.getString(3));
	    	nearestStopList.add(nearestRouteObj); //add the object to the list (basically add everything as one entity
	    	
	    	//while cursor is moving to next, do nothing
	    	} while (cursor.moveToNext());
	    	}
		
		return nearestStopList;
	
		
	}
	
	//code for getting route 16b
	public List<NearestStopObj> getRoute16bStops (){
		
		List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();
		

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM STOPS  WHERE stop_ID IN (SELECT stop_ID FROM ROUTE_STOP WHERE route_ID =\"16b\")", null);
		
		if (cursor.moveToFirst()) {
	    	
	    	do {  //if moving.toFirst do this stuff
	    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
	    	
	    	nearestRouteObj.setStopId(cursor.getInt(0));
	    	nearestRouteObj.setLat(cursor.getString(1));
	    	nearestRouteObj.setLongi(cursor.getString(2));
	    	nearestRouteObj.setName(cursor.getString(3));	
	    	nearestStopList.add(nearestRouteObj); //add the object to the list (basically add everything as one entity
	    	
	    	//while cursor is moving to next, do nothing
	    	} while (cursor.moveToNext());
	    	}
		
		return nearestStopList;

		
	}

	
	/*
	 * This is the last method, congratulations, you have made it through 580 lines of database code
	 * This used in the StopMap file to load the information of the stop that was selected
	 * 
	 */
	public List<NearestStopObj> getStopLocation (int stopid){ 
		
		//instantiate the list
		List<NearestStopObj> nearestStopList = new ArrayList<NearestStopObj>();
		
		//get the db
		SQLiteDatabase db = this.getWritableDatabase();
		
		//run the query to locate the stopid that was passed into this method 
		Cursor cursor = db.rawQuery("SELECT * FROM STOPS WHERE stop_id=" + stopid, null);
	
		//similar code to previous methods, start the cursor and load the setter and getter
		if (cursor.moveToFirst()) {
	    	
	    	do {  //if moving.toFirst do this stuff
	    	NearestStopObj nearestRouteObj= new NearestStopObj();  //create instance of object
	    	
	    	nearestRouteObj.setStopId(cursor.getInt(0));
	    	nearestRouteObj.setLat(cursor.getString(1));	    	
	    	nearestRouteObj.setLongi(cursor.getString(2));	    	
	    	nearestRouteObj.setName(cursor.getString(3));	    	
	    	nearestStopList.add(nearestRouteObj); //
	    	} while (cursor.moveToNext());
	    	}
		
		return nearestStopList;  //return the list
	
		
	}


}
