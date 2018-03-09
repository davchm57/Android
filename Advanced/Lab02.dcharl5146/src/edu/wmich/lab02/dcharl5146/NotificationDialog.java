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

import android.app.AlertDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NotificationDialog extends DialogFragment {

	
	
	//invalid input
	 private void invalidInput()
     {
     	
 		Toast.makeText(getActivity(),"Enter a number between 1 and 99 as the minimun battery level\nWhen the battey gets to this level it will fire up a notification", Toast.LENGTH_LONG).show();
 		new NotificationDialog().show(getFragmentManager(), "MyDialog");
     	
     }
	
	//minimun level of the battery set by the user
	private void saveSharedPreferences(String minimunBatteryLevel)
	{
		  
		  SharedPreferences sharedPreferences = PreferenceManager
  				.getDefaultSharedPreferences(getActivity());
  		Editor editor = sharedPreferences.edit();
  		editor.putString("minimunlevel", minimunBatteryLevel);
  		editor.commit();
		
	}
	
	
	  @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		  
		 LayoutInflater li = LayoutInflater.from(getActivity());//layout inflater object 
		 View promptsView = li.inflate(R.layout.dialog_fragment, null);//it will inflate or load the layout containing the 
		 //dialog fragment into promptsview
			
			//alert dailog to ask the user for the minimun battery level
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());//instantiates the alertdialog object

			alertDialogBuilder.setView(promptsView);//sets the view
			final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);//edit text that will be used for the dialog
	        alertDialogBuilder.setTitle("Notification");//sets the title
	        alertDialogBuilder.setMessage("Enter minimun battery level");//message...
	        
	        //positive button
	        alertDialogBuilder.setPositiveButton("Set Notification",new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	try{
	            		
	            	//validates the user input because the battery level needs to be
	            	//between 0 and 99
	            	String input=(userInput.getText().toString());
	            	
	            	if ((Integer.parseInt(input)>0) && (Integer.parseInt(input)<100))
	            	{
	            		//if is valid it will start the service and send the minimun battery level to the service class
	            		Intent mServiceIntent = new Intent(getActivity(), NotificationService.class);
		                //mServiceIntent.putExtra("Level",  userInput.getText().toString());
	            		saveSharedPreferences(userInput.getText().toString());
	            		
	    
		                
		                MainActivity.serviceStatus=true;
		                
		                getActivity().startService(mServiceIntent);
			                
	            	}
	            	else
	            	{
	            		//invalid input it wasnt between the range
	            		invalidInput();
	            	}
	            	}
	            	catch (Exception ex)
	            	{
	            		//invalid input it was not even a number
	            		invalidInput();
	            	}
	            	

	            }
	        });
	        
	       
	        
	        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	BatteryFragment.toggle.toggle();//if it cancels then it should set the toggle button off
	                dialog.dismiss();
	            }
	        });

	        setCancelable(false);//so the user wont be able to close the dialog
	        return alertDialogBuilder.create(); //returns the dialog
	        
	    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
}