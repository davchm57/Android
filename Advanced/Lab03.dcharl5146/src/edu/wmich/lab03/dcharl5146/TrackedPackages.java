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
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TrackedPackages extends ListFragment {


	private SharedPreferences preferences;//shared preferenes to load the background color
	int mStackLevel = 0;//stack level of fragments
	public static final int DIALOG_FRAGMENT = 1;//case in switch
	private int position;//currently selected position in the listfragment
	private int year = 0;//year of estimated delivery date
	private int month = 0;//month of estimated delivery date
	private int day = 0;//day of estimated delivery date
	private Intent intent;//intent used to go to another activity
	private String[] dateArray;//contains the estimated delivery date
	private GregorianCalendar packageDate;//calendar to add the estimated delivery date
	String[] values;//used to set the view
	String currentPackage = "";//used to hold data that will be displayed in the view
	ArrayList<String> valuesList = new ArrayList<String>();//used to set the hold values that will be displayed in the view
	int counter;//used to count
	private DatabaseHandler db;//used to read/write to the db

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mStackLevel = savedInstanceState.getInt("level");//gets the level of the stack

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);//saves the level of the stack
	}

	public void updateValues() {
		//will loop the package array and get the name and tracking number
		//then add the string to the values list
		//the listfragment displays name and tracking number
		for (counter = 0; counter < MainActivity.packagesArray.size(); counter++) {
			currentPackage = "Package Name: "
					+ MainActivity.packagesArray.get(counter).getName()
					+ "\nTracking Number:"
					+ MainActivity.packagesArray.get(counter)
							.getTrackingNumber();
			valuesList.add(currentPackage);
			currentPackage = "";//clears it since it contains temp values
		}

	}

	public void removePackage() {
		db.deletePackage(MainActivity.packagesArray.get(position));// deleting
																	// from
																	// database
		MainActivity.packagesArray.remove(position);// deleting from arraylist
		valuesList.clear();// clearing the list
		updateValues();// updating the list with the packages objects arraylist
		setList(); // updating the array in the listview
	}

	private void setList() {

		//psases the values from valuesList(a list) to values(an array)
		values = new String[valuesList.size()];
		values = valuesList.toArray(values);
		try {

			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, values));
		} catch (Exception ex) {

		}
		valuesList.clear();//clear valuesList so it can be used again since it keeps temp values

	}

	private void setItToCalendar(String date, String name, String status) {

		//the date is read separated by -
		//splits it so it can be set to the gregorian calendar
		dateArray = date.split("-");

		year = Integer.parseInt(dateArray[0]);
		month = Integer.parseInt(dateArray[1]);
		day = Integer.parseInt(dateArray[2]);
		// -1 so it will be the right date since starts from 0
		packageDate = new GregorianCalendar(year, month - 1, day);

		
		//it checks if the package was delivered
		if (!status.equalsIgnoreCase("delivered")) {
			//if it wasnt delivered it will start up the calendar
			//but it will prepopulate some things 
			Intent calIntent = new Intent(Intent.ACTION_INSERT);//is inserting a contact
			calIntent.setType("vnd.android.cursor.item/event");
			calIntent.putExtra(Events.TITLE, "Package Delivery");//setting the title
			calIntent.putExtra(Events.DESCRIPTION, name);//description will be the package's name

			calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);//all day
			//being and end time
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
					packageDate.getTimeInMillis());
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
					packageDate.getTimeInMillis());
			//starts the intent
			startActivity(calIntent);
		} else {
			message();//else calls message() which will tell the user that the packages was already delivered 
		}
	}

	//tells the user that the packages was already delivered 
	private void message() {
		//creates a dialog
		AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
		ad.setCancelable(false);//can be canceled unless clicked
		ad.setTitle("Can't add it to calendar");//sets the title
		ad.setMessage("This packages was already delivered");//sets the message
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {//makes a button "ok" when clicked it will dismiss the dialog
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		ad.show();//shows the dialog
	}



	
	private void loadBackgroundPreferences() {
		
		//gets the view to set the color
		ListView l = getListView();
		
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());//instanciate the preference object
		
		int color = preferences.getInt("background", 4); // it means a value has
															// not been set

		
		    //goes trough the switch to find out which color will be set
			//for some reason I couldn't find the way to use system colors like in the other cases
			//I had to use the color from the resources folder
			switch (color) {
			case 0:
				l.setBackgroundColor(getResources().getColor(R.color.blue));
				;
				break;
			case 1:
				l.setBackgroundColor(getResources().getColor(R.color.green));
				;
				break;

			case 2:
				l.setBackgroundColor(getResources().getColor(R.color.yellow));
				;
				break;
			case 3:
				l.setBackgroundColor(getResources().getColor(R.color.white));
				;
				break;
			case 4:
				l.setBackgroundColor(getResources().getColor(R.color.beige));
				;
				break;
			}

		
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
	    super.onViewCreated(view, savedInstanceState);
	    loadBackgroundPreferences();//when the view has been created it loads the background
	    //this can't be done on create view. 
	   
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		updateValues();//updates the values using the packages array
		setList();//sets the list to the view using the updated values
		db = new DatabaseHandler(getActivity());//instantiaces the database object

		return super.onCreateView(inflater, container, savedInstanceState);

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		this.position = position;//saves the selected position
		showDialog(1);//calls the showdialog method

	}

	@Override
	public void onResume() {
		loadBackgroundPreferences();//loads the background preferences this has to be done on resume again
		//because when a preference activity starts the fragment goes on pause and if the background color is changed
		//and onresume is not handle correctly  it won't load the color until the fragment gets refreshed
		updateValues();//updates the values using the packages ist
		setList();//sets the updated values
		super.onResume();
	}

	void showDialog(int type) {

		mStackLevel++;//adds one to the level of the stack
		
		
		FragmentTransaction ft = getActivity().getFragmentManager()
				.beginTransaction();
		
		Fragment prev = getActivity().getFragmentManager().findFragmentByTag(
				"dialog");//finds fragment by tag 
		
		if (prev != null) {
			ft.remove(prev);//if there is a fragment with that tag it removes it
		}
		
		ft.addToBackStack(null);//adding to backstack so the fragment can be removed later

		switch (type) {

		case DIALOG_FRAGMENT:

			//creates a new instance of the dialog fragment and shows it 
			DialogFragment dialogFrag = MyDialogFragment.newInstance(0);
			dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT);
			dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");

			break;
		}
	}
	
	
	//this method is called from the dialog
	//this has to be done since there are variables that are used in this fragment that need to be used
	//when an option in the dialog is selected 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {

		//if is 1 removes a package
		case 1:
			removePackage();
			break;

		case 2:
			//calls the method setittocalendar and sends the position, name and status as argument
			setItToCalendar(MainActivity.packagesArray.get(this.position)
					.getEstimated(),
					MainActivity.packagesArray.get(this.position).getName(),
					MainActivity.packagesArray.get(this.position).getStatus());
			break;
		case 3:
			//starts an intent and sends the position as argument
			intent = new Intent(getActivity(), Contacts.class);
			intent.putExtra("position", this.position);
			getActivity().startActivity(intent);

			break;
		case 4:
			//starts an intent and sends the position as argument
			intent = new Intent(getActivity(), PackageInformation.class);
			intent.putExtra("selectedPosition", this.position);
			getActivity().startActivity(intent);

			break;
		}
	}
}
