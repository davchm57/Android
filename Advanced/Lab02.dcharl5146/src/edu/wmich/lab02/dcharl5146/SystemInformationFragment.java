package edu.wmich.lab02.dcharl5146;
/* 
*************************************
* Programmer: David Charles
* Class ID: dchar5146
* Lab 02
* CIS 4700
* Spring 2014
* Due date: 3/10/14
* Date completed: 3/09/14
*************************************
Battery checker app 
Features:
TAB 1-Displays battery level
TAB 2-Displays System information
-Notification Service: Let the user to set a minimum battery level, when the battery get to this level a notification will be fired up
-When the notification is clicked it will open up an activity that will show an image shaking (an animation). It will also display the battery level. The notification will make the phone vibrate or making a sound depending on the phone state and it will make the led of the phone go blue.
-This app will save the settings using sharedpreferences. When the phone is rebooted it will load those settings using a bootbroadcast. So if the notification service was on before the phone rebooted it will continue to be on after rebooting.
-Installs a widget that displays the battery level
	-When clicked it will open up the app
	-It will tell the user if the service is running or not (the app will communicate with the widget) 
This app is designed for large screens as well
Tested on Nexus 7 and Galaxy S4
The normal layout is the only one commented (since the other layouts are the same but with larger widgets/images)
*************************************
*/

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SystemInformationFragment extends Fragment {
	
	private String imei,networkOperator,phoneNumber;//this variables holds the imei, network operator and phone number
	//that will be displayed as part of the system information
	ListView lv;//listview object to set the system information

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		getInfo(data);
		
	}

	private void getInfo(Bundle data)
	{
		//gets inform it was sent from the other activity
				imei = data.getString("idx");
				networkOperator=data.getString("idx2");
				phoneNumber= data.getString("idx3");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.systeminformationfragment, container, false);

	        lv = (ListView) view.findViewById(R.id.lv1);  
	        //It will create an array using android.os information and information received from other activity
	        String[] values = new String[] {"Os Version Release: "+android.os.Build.VERSION.RELEASE, "Model number:" +android.os.Build.MODEL, 
                    "Manufacturer: "+android.os.Build.MANUFACTURER, "IMEI: "+imei,"Network Operator: " +networkOperator, "Phone Number: "+phoneNumber };
	        
	           //it will bind the information in the view
               ArrayAdapter<String> files = new ArrayAdapter<String>(getActivity(), 
                        android.R.layout.simple_list_item_1, 
                        values);

                lv.setAdapter(files);//sets the adapter so bind the information to the listview
		return view;
	}
}
