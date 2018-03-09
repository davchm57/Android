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

import java.io.InputStream;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class TrackPackage extends Fragment {

	private Package packageObject;//package object 
	private Button trackPackageBtn;//track a package btn will actually start the process of validating input and adding a package
	private EditText trackPackageEd;//tracking number is written here
	private EditText packageNameEd;//package name is written here
	private Spinner carrierSpinner;//carrier is selected
	private SharedPreferences preferences;//shared preferences to load the background color
	private RelativeLayout layout;//layout to change background color
	private DatabaseHandler db;//db to load the tracked packages
	private Random randomGenerator;//random generator used for the files
	//there are 12 files named 1-12 and the selected file will depend on the random generator
	//it changes everytime you add a package
	private int randomInt;//holds the random number

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onResume() {
		loadBackgroundPreferences();//loads the saved background color
		loadCarrierPreferences();//load the saved carrier
		super.onResume();
	}

	//method to add a package takes the tracking number that the use entered
	//package name and carrier (default ups but it can be changed in the settings)
	private void addPackage(String trackingNumber, String packageName,
			String carrier) {
		XmlPullParserFactory pullParserFactory;


		try {
			// There are 12 xmls files "packages" to feed of
			// it will choose one everytime add a package is called
			randomGenerator = new Random();
			randomInt = randomGenerator.nextInt(12) + 1;

			//here it will just get the xml and send this unparsed data to the object 
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();
			// +1 because random generator starts in 0
			InputStream in_s = getActivity().getAssets().open(
					String.valueOf(randomInt) + ".xml");
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_s, null);
			
			packageObject = new Package(trackingNumber, packageName, carrier,
					parser);//instantiate the object sending this info 
					//it will parse the xml and get other info about the package from it

			//adds the package object to the database and displays a toast
			db.addPackge(packageObject);
			Toast.makeText(getActivity(), "Package Added", 
					   Toast.LENGTH_LONG).show();
			//clear the packages array in order to take the packages from the database
			//that way we make sure there are no repeated tracking number which is the key of packages in the database
			MainActivity.packagesArray.clear();
			db.getAllPackages();//gets all packages will put any added package to the db in the packages array
			

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	//loads the background color
	private void loadBackgroundPreferences() {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		int color = preferences.getInt("background", 4); // if it is 4 it means a value has not been set

			switch (color) {
			case 0:
				layout.setBackgroundColor(Color.BLUE);
				;
				break;
			case 1:
				layout.setBackgroundColor(Color.GREEN);
				;
				break;

			case 2:
				layout.setBackgroundColor(Color.YELLOW);
				;
				break;
			case 3:
				layout.setBackgroundColor(Color.WHITE);
				;
				break;
			case 4:
				layout.setBackgroundColor(getResources().getColor(R.color.beige));
				;
				break;
			}
		
	}

	//loads the saved carrier
	private void loadCarrierPreferences() {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		int carrier = preferences.getInt("carrier", 5); // if is 5 means a value has
														// not been set

		if (carrier != 5) {
			//preferences are saved using index and looking a the order of the carriers
			//this concept is used. The order doesn't change so just an index is needed
			carrierSpinner.setSelection(carrier);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.trackpackage, null);//get the view
		layout = (RelativeLayout) v.findViewById(R.id.carrierTv);//gets the layout

		db = new DatabaseHandler(getActivity());//instantiate the db object

		//button needed to add a package
		trackPackageBtn = (Button) v.findViewById(R.id.trackPackageBtn);
		//edit texts so the user can enter information about the package
		trackPackageEd = (EditText) v.findViewById(R.id.packageTr);
		packageNameEd = (EditText) v.findViewById(R.id.packageName);
		//prepopulated with carriers so the user will choose one of these
		carrierSpinner = (Spinner) v.findViewById(R.id.spinner1);
		loadCarrierPreferences();//loads the carrier preferences
		loadBackgroundPreferences();//loads the background color preferences
		trackPackageBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//validates the input if the editexts are not empty then
					if (trackPackageEd.getText().toString().length() > 0
							&& packageNameEd.getText().toString().length() > 0
							&& String.valueOf(carrierSpinner.getSelectedItem())
									.length() > 0) {
						//adds a package and sends the tracking number, name and selected carrier
						addPackage(trackPackageEd.getText().toString(),
								packageNameEd.getText().toString(), String
										.valueOf(carrierSpinner
												.getSelectedItem()));
					} else {
						//empty edit text and notifies the user 
						Toast.makeText(
								getActivity(),
								"Make sure you entered a tracking number, name and carrier",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(
							getActivity(),
							"Make sure you entered a tracking number, name and carrier",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		return v;

	}

}
