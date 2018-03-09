package edu.wmich.dso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/* 
 *************************************
 * Programmer: TeamBus
 * Project Prototype
 * CIS 4700: Mobile Commerce Development
 * Spring 2014
 * Due date: 04/04/14
 * Date completed: 04/04/14
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

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2; // set the version of the
													// database
	
	private SQLiteDatabase myDataBase;
	private static final String DATABASE_NAME = "dsodb"; // set the name


	private String membershipDirector = "";
	private String president = "";
	private String secretary = "";
	private String vicePresident = "";
	private String treasurer = "";
	private String vocal = "";

	int membershipDirectorVote = 0;
	int presidentVote = 0;
	int secretaryVote = 0;
	int vicePresidentVote = 0;
	int treasurerVote = 0;
	int vocalVote = 0;
	// private static final String KEY_MEMID = ""; //set route name as key

	public final static String DATABASE_PATH = "/data/data/edu.wmich.dso/databases/"; // set
																						// path
																						// to
																						// db

	private Context context;

	public DatabaseHandler(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	// Check database already exist or not
	private boolean checkDataBase() {
		boolean checkDB = false; // create true/false and set it false

		// look for db
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME; // combine the path
															// an db name
			File dbfile = new File(myPath); // create path to file
			checkDB = dbfile.exists(); // see if it exists
		}

		// catch exceptions
		catch (SQLiteException e) {
			return checkDB;
		}
		return checkDB;
	}

	// creates the database
	public void createDatabase() throws IOException {
		boolean dbExist = checkDataBase(); // create true/false and assign it to
											// false

		// if database base does not exists
		if (!dbExist) {
			this.getReadableDatabase(); // create database

			try {

				copyDataBase(); // run copy database method (copies from assets
								// folder)

				// catch errors
			} catch (IOException mIOException) {

				mIOException.printStackTrace();
				throw new Error("Error copying database");

				// close db
			} finally {
				this.close();
			}
		}
		// to upgrade the database
		// it will compare the old version string with the new version string
		// if these are different then it will copy the file again
		// if there is a better procedure for this please let us know
		// so basically this would be our own method of onUpGrade
	}

	// public method used for opening the database
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME; // set the path
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE); // open the db
	}

	// public method for closing the database
	public synchronized void closeDataBase() throws SQLException {

		if (myDataBase != null) // look for open db

			myDataBase.close(); // if open then close it!

		super.close();
	}

	// copy the database from the assets folder
	public void copyDataBase() throws IOException {

		String outFileName = DATABASE_PATH + DATABASE_NAME; // set the path
		OutputStream myOutput = new FileOutputStream(outFileName); // create
																	// output
																	// stream
		InputStream myInput = this.context.getAssets().open(DATABASE_NAME); // create
																			// input
																			// stream,
																			// (don't
																			// ever
																			// cross
																			// the
																			// streams)
		byte[] buffer = new byte[1024]; // set a reasonable buffer size
		int length; // create int to read buffer
		while ((length = myInput.read(buffer)) > 0) // while data is coming in
													// assign it to int
		{
			myOutput.write(buffer, 0, length);
		}
		myInput.close(); // once completed close the inputstream
		myOutput.flush(); // flush or clear the output streatm
		myOutput.close(); // close it
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + "ROUTE");
		// Create tables again
		onCreate(db);
	}

	// //!!!! I need to find talk to David to determine what this is for, there
	// will not be a need to add a route!!!
	// void addContact(BusRoute busroute) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put(KEY_NAME, busroute.getName()); //
	// values.put(KEY_STOPID, busroute.getRouteId()); // Contact Phone
	//
	// // Inserting Row
	// db.insert("ROUTE", null, values);
	// db.close(); // Closing database connection
	// }

	public void getMemberList() {

		MainActivity.memberId.clear();
		MainActivity.memberName.clear();
		String selectQuery = "SELECT MEMID, MEMBER_NAME FROM MEMBER";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MainActivity.memberId.add(cursor.getString(0));
				MainActivity.memberName.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		db.close();
	}

	public void getCurrentMember(int memberId) {

		String selectQuery = "SELECT MEMBER_NAME FROM MEMBER WHERE MEMID="
				+ memberId;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MainActivity.currentUserName = cursor.getString(0);
				MainActivity.currentUserId = memberId;
			} while (cursor.moveToNext());
		}

	}

	public void deleteMember(int memberId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("MEMBER", "MEMID=" + memberId, null);
		db.close();
	}

	// Adding new packages
	public void addMember(String memberId, String memberName) {

		SQLiteDatabase db = this.getWritableDatabase();// opens the database for
														// writing

		ContentValues values = new ContentValues();// creates an empty set of
													// values

		try {
			values.put("MEMID", memberId);
			values.put("MEMBER_NAME", memberName);
			db.insert("MEMBER", null, values);

			db.close(); // Closing database connection
		}

		catch (Exception ex) {

		}

	}

	public void resetVotes() {
		SQLiteDatabase db = this.getWritableDatabase();// opens the database for
		// writing

		ContentValues values = new ContentValues();// creates
		values.put("CANDIDATE_VOTE", 0);// gets the
		// carrier
		db.update("CANDIDATE", values, null, null);

		db.close(); // Closing database connectiond

	}

	// Adding new packages
	long addVote(Vote vote) {

		SQLiteDatabase db = this.getWritableDatabase();// opens the database for
														// writing

		ContentValues values = new ContentValues();// creates an empty set of
													// values
		long result = 0;

		membershipDirector = vote.getMembershipDirector();
		president = vote.getPresident();
		secretary = vote.getSecretary();
		vicePresident = vote.getVicePresident();
		treasurer = vote.getTreasurer();
		vocal = vote.getVocal();

		getCurrentVotes();

		values.put("CANDIDATE_VOTE", membershipDirectorVote + 1);// gets the
																	// carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='"
				+ membershipDirector + "'", null));

		values.put("CANDIDATE_VOTE", presidentVote + 1);// gets the carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='" + president
				+ "'", null));

		values.put("CANDIDATE_VOTE", secretaryVote + 1);// gets the carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='" + secretary
				+ "'", null));

		values.put("CANDIDATE_VOTE", vicePresidentVote + 1);// gets the carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='"
				+ vicePresident + "'", null));

		values.put("CANDIDATE_VOTE", treasurerVote + 1);// gets the carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='" + treasurer
				+ "'", null));

		values.put("CANDIDATE_VOTE", vocalVote + 1);// gets the carrier
		result = (db.update("CANDIDATE", values, "CANDIDATE_NAME='" + vocal
				+ "'", null));

		db.close(); // Closing database connectiond
		return result;
	}

	public void getResult() {
		String selectQuery = "SELECT  CANDIDATE_NAME, CANDIDATE_VOTE, POSITIONID  FROM CANDIDATE";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ResultObject temp = new ResultObject();
				temp.setName(((cursor.getString(0))));
				temp.setVote(((cursor.getString(1))));
				temp.setPositionId(((cursor.getString(2))));
				MainActivity.currentResult.add(temp);
			} while (cursor.moveToNext());
		}
		for (int i = 0; i < MainActivity.currentResult.size(); i++) {
			MainActivity.currentResult.get(i).setPosition(
					getPosition(MainActivity.currentResult.get(i)
							.getPositionId()));
		}
		db.close(); // Closing database connectiond

	}

	public void deteleMember() {

	}

	private String getPosition(String positionId) {
		String selectQuery = "SELECT POSITION_NAME FROM POSITION WHERE POSITIONID="
				+ positionId;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		String positionResult = "";
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				positionResult = (((cursor.getString(0))));
			} while (cursor.moveToNext());
		}

		return positionResult;
	}

	// //This method is used by the BrowseRoutes activity to get a list of all
	// bus routes
	// public List<Package> getWmuRoutes() {
	//
	// List<Package> packageList = new ArrayList<Package>(); //create list to
	// hold routes
	//
	// // Select All Query
	// String selectQuery = "SELECT  * FROM " + "ROUTE";
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// BusRoute busroute = new BusRoute();
	// busroute.setRouteId(Integer.parseInt(((cursor.getString(0)))));
	// busroute.setName(((cursor.getString(1))));
	// // Adding package to list
	// BrowseRoutes.busRoute.add(busroute);
	// } while (cursor.moveToNext());
	// }
	//
	// // return package list
	// return packageList;
	// }
	//
	//
	// //using the shared preferences gets the favorites from the database
	// //the shared preferences will countain a routeID
	public void getCurrentVotes() {

		String selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ membershipDirector + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				membershipDirectorVote = Integer.parseInt((((cursor
						.getString(0)))));
			} while (cursor.moveToNext());
		}

		selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ president + "'";
		if (cursor.moveToFirst()) {
			do {
				presidentVote = Integer.parseInt((((cursor.getString(0)))));
			} while (cursor.moveToNext());
		}

		selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ secretary + "'";
		if (cursor.moveToFirst()) {
			do {
				secretaryVote = Integer.parseInt((((cursor.getString(0)))));
			} while (cursor.moveToNext());
		}

		selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ treasurer + "'";
		if (cursor.moveToFirst()) {
			do {
				treasurerVote = Integer.parseInt((((cursor.getString(0)))));
			} while (cursor.moveToNext());
		}

		selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ vicePresident + "'";
		if (cursor.moveToFirst()) {
			do {
				vicePresidentVote = Integer.parseInt((((cursor.getString(0)))));
			} while (cursor.moveToNext());
		}

		selectQuery = "SELECT CANDIDATE_VOTE FROM CANDIDATE WHERE CANDIDATE_NAME='"
				+ vocal + "'";
		if (cursor.moveToFirst()) {
			do {
				vocalVote = Integer.parseInt((((cursor.getString(0)))));
			} while (cursor.moveToNext());
		}

	}
	//
	// //getting the number of stops associated with this bus route
	// public void getStops(int routeId) {
	// ArrayList<Integer>tempArray=new ArrayList<Integer>();
	// // query
	// String selectQuery =
	// "SELECT stop_ID FROM ROUTE_STOP WHERE route_ID ="+routeId;
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	// Stops.busStops.clear();
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// tempArray.add(cursor.getInt(0));
	// } while (cursor.moveToNext());
	// }
	//
	// //all the stops id are in temparray
	// //gets all the stops name frmo these stops ID
	// for (int i=0;i<tempArray.size();i++)
	// {
	// selectQuery =
	// "SELECT stop_Name FROM STOPS WHERE stop_ID="+tempArray.get(i);
	// cursor = db.rawQuery(selectQuery, null);
	//
	// if (cursor.moveToFirst()) {
	// do {
	// BusStop busStop= new BusStop();
	// busStop.setName(((cursor.getString(0))));
	// busStop.setStopId(tempArray.get(i));
	// Stops.busStops.add(busStop);
	// } while (cursor.moveToNext());
	// }
	//
	// }
	//
	// }
	//
	// //gets the detailed information of a stop
	// //this include the routes that go by and the times that those route goes
	// by
	// public void getStopsDetailedInformation(int stopId)
	// {
	// // Select All Query
	// String selectQuery = "SELECT  route_ID FROM ROUTE_STOP WHERE stop_ID =" +
	// stopId;
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	// ArrayList<Integer>tempArray=new ArrayList<Integer>();
	// DetailedInformation.detailedInformation.clear();
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// tempArray.add(cursor.getInt(0));
	// // Adding package to list
	// } while (cursor.moveToNext());
	// }
	//
	// getTimeByRouteId(stopId,tempArray);
	//
	// //getting the routes name whitin this route id
	// for (int i=0;i<tempArray.size();i++)
	// {
	// selectQuery =
	// "SELECT route_Name FROM ROUTE WHERE route_ID="+tempArray.get(i);
	// cursor = db.rawQuery(selectQuery, null);
	//
	// if (cursor.moveToFirst()) {
	// do {
	// BusRoute busRoute= new BusRoute();
	// busRoute.setName(((cursor.getString(0))));
	// // Adding bus route to list
	// DetailedInformation.detailedInformation.add(busRoute);
	// } while (cursor.moveToNext());
	// }
	// }
	//
	// }
	//
	//
	// //gets time by route id
	// //it will get the minutes and put it in an array named times declared in
	// the main activity
	// //with the minutes the departing time can be calculated using the current
	// hour
	// public void getTimeByRouteId(Integer stopId, ArrayList<Integer>routesIds)
	// {
	// MainActivity.times=new String[routesIds.size()][2];//every route will go
	// around a stop
	// //at most twice an hour this is why the 2 in the array is the size for
	// the column
	// for(int i=0;i<routesIds.size();i++)//goes by every route id that go by
	// this stop
	// {
	// String selectQuery =
	// "SELECT stop_time FROM ROUTE_STOP WHERE stop_ID="+stopId+
	// " AND route_ID="+routesIds.get(i);
	// int inner=0;
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	// if (cursor.moveToFirst()) {
	// do {
	// MainActivity.times[i][inner++]=cursor.getString(0);//puts it in the bi
	// dimensional array
	// } while (cursor.moveToNext());
	// }
	// }
	//
	//
	// }

}
