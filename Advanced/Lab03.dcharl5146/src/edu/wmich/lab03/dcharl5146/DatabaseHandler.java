package edu.wmich.lab03.dcharl5146;
/* 
 *************************************
 * Programmer: David Charles
 * Class ID: dchar5146
 * Lab 03
 * CIS 4700
 * Spring 2014
 * Due date: 3/28/14
 * Date completed: 3/25/14
 *************************************
 Package Tracker app
 Features:
 TAB 1-Lets you add a package:
	-You can add a package entering a tracking number and a name for the package. Also a carrier is needed, by default is UPS but this can be changed in the settings
 TAB 2-Displays added packages: 
  Displays current added packages labeling them with their tracking number and name.
  Pressing a package will show a dialog with a couple of options:
  	   -Show Complete information: Displays more information about the package such as (Location, carrier, status, estimated delivery and Owner (by default a package has no Owner)
       -Remove this package: Removes currently selected package 
       -Add to calendar: Add an estimated delivery date to the calendar
       -Set an owner using contacts book: You can pick someone from your contacts book and set it as owner of this package
       -Cancel 
-Options button: When pressed it will show a preference activity. In this preference activity you can make some changes such as:
	  -Change your profile: Here you can store information about yourself and it will be stored as long as the app is installed
	  -Carrier: You can change the default carrier which is UPS
      -Background Color: You can change the background color
      -Clear packages list: Will clear the packages list
      -Turn on Bluetooth: Turns on the Bluetooth
      
*The app will load the packages from the database everytime it starts up 
*everytime a package is added, removed or edited it will be refelected in the database
*and will save the preferences set on the preference activity 

Tested on and Galaxy S4
*************************************
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "packagesDb";

	// Packages table name
	private static final String TABLE_PACKAGES = "packages";

	// Packages Table Columns names
	private static final String KEY_TRACKINGNUMBER = "tracking_number";
	private static final String KEY_CARRIER = "carrier";
	private static final String KEY_STATUS = "status";

	private static final String KEY_LOCATION = "location";
	private static final String KEY_ESTIMATED = "estimated";

	private static final String KEY_NAME = "name";
	private static final String KEY_OWNER = "owner";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	//tracking number is used as the primary key
	//there is also carrier, status, location, estimated delivery time, name and owner
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PACKAGES_TABLE = "CREATE TABLE " + TABLE_PACKAGES + "("
				+ KEY_TRACKINGNUMBER + " TEXT PRIMARY KEY NOT NULL,"
				+ KEY_CARRIER + " TEXT," + KEY_STATUS + " TEXT," + KEY_LOCATION
				+ " TEXT," + KEY_ESTIMATED + " TEXT," + KEY_NAME + " TEXT,"
				+ KEY_OWNER + " TEXT" + ")";

		db.execSQL(CREATE_PACKAGES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new packages 
	long addPackge(Package packages) {

		SQLiteDatabase db = this.getWritableDatabase();//opens the database for writing
		
		ContentValues values = new ContentValues();//creates an empty set of values
		
		//add the values to the Contentvalues object
		
		values.put(KEY_TRACKINGNUMBER, packages.getTrackingNumber());//gets he tracking number using a get method
		values.put(KEY_CARRIER, packages.getCarrier());//gets the carrier
		values.put(KEY_STATUS, packages.getStatus());//gets the status

		values.put(KEY_LOCATION, packages.getLocation());//gets the location
		values.put(KEY_ESTIMATED, packages.getEstimated());//gets the estiamted delivery time

		values.put(KEY_NAME, packages.getName());//gets the name
		values.put(KEY_OWNER, packages.getOwner());//gets the owner
		long result = 0;

		// Inserting Row
		result = (db.insert(TABLE_PACKAGES, null, values));
		db.close(); // Closing database connection
		return result;
	}

	// Getting All Packages
	public List<Package> getAllPackages() {
		List<Package> packageList = new ArrayList<Package>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PACKAGES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Package packages = new Package();
				packages.setTrackingNumber(((cursor.getString(0))));
				packages.setCarrier(cursor.getString(1));
				packages.setStatus(cursor.getString(2));
				packages.setLocation(cursor.getString(3));
				packages.setEstimated(cursor.getString(4));
				packages.setName(cursor.getString(5));
				packages.setOwner(cursor.getString(6));
				// Adding package to list
				MainActivity.packagesArray.add(packages);
			} while (cursor.moveToNext());
		}

		// return package list
		return packageList;
	}

	// Updating single package takes the package object
	//to be updated
	public int updatePackage(Package packages) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_TRACKINGNUMBER, packages.getTrackingNumber());
		values.put(KEY_CARRIER, packages.getCarrier());
		values.put(KEY_STATUS, packages.getStatus());

		values.put(KEY_LOCATION, packages.getLocation());
		values.put(KEY_ESTIMATED, packages.getEstimated());

		values.put(KEY_NAME, packages.getName());
		values.put(KEY_OWNER, packages.getOwner());

		// updating row usnig trackingnumber as the key
		return db.update(TABLE_PACKAGES, values, KEY_TRACKINGNUMBER + " = ?",
				new String[] { (packages.getTrackingNumber()) });
	}

	// Deleting single package
	public void deletePackage(Package packages) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PACKAGES, KEY_TRACKINGNUMBER + " = ?",
				new String[] { String.valueOf(packages.getTrackingNumber()) });
		db.close();
	}

	//deletes every single package from the db
	public void clear() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PACKAGES);
		db.close();
	}

}
