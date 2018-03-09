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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyDialogFragment extends DialogFragment implements OnClickListener {

	//buttons that will be needed 
	//bshow show the complete information about the package
	//bdelete deletes a package
	//bestimated adds the packge estimated delivery time to the calendar
	//bcontacts sets the package an owner using the contact book
	//bcancel dismisses the dialog 
	public Button bshow, bdelete, bestimated, bcontacts, bcancel;

	public static MyDialogFragment newInstance(int num) {

		MyDialogFragment dialogFragment = new MyDialogFragment();
		return dialogFragment;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater li = LayoutInflater.from(getActivity());// layout
																// inflater
																// object
		View promptsView = li.inflate(R.layout.dialog_fragment, null);// inflates the fragment using a layout

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());// instantiates the alertdialog object

		alertDialogBuilder.setView(promptsView);// sets the view
		alertDialogBuilder.setTitle("About this package");// sets the title

		//buttons to show complete date, delete, set it to the calendar, set an owner using contacts and dimiss
		bshow = (Button) promptsView.findViewById(R.id.bshow);
		bdelete = (Button) promptsView.findViewById(R.id.bdelete);
		bestimated = (Button) promptsView.findViewById(R.id.bestimated);
		bcontacts = (Button) promptsView.findViewById(R.id.bcontacts);
		bcancel = (Button) promptsView.findViewById(R.id.bcancel);

		bshow.setOnClickListener(new OnClickListener() {

			//it will get the target fragment which will be the fragmentlist and send a code
			//this code will be the index of the picked option
			//so in the list fragment the right methods can be called
			@Override
			public void onClick(View view) {
				dismiss();
				getTargetFragment().onActivityResult(getTargetRequestCode(), 4,
						getActivity().getIntent());
			}

		});

		bcontacts.setOnClickListener(new OnClickListener() {

			//it will get the target fragment which will be the fragmentlist and send a code
			//this code will be the index of the picked option
			//so in the list fragment the right methods can be called
			@Override
			public void onClick(View view) {
				dismiss();

				getTargetFragment().onActivityResult(getTargetRequestCode(), 3,
						getActivity().getIntent());

			}

		});

		bdelete.setOnClickListener(new OnClickListener() {

			//it will get the target fragment which will be the fragmentlist and send a code
			//this code will be the index of the picked option
			//so in the list fragment the right methods can be called
			@Override
			public void onClick(View view) {
				dismiss();
				getTargetFragment().onActivityResult(getTargetRequestCode(), 1,
						getActivity().getIntent());
			}

		});

		bestimated.setOnClickListener(new OnClickListener() {

			//it will get the target fragment which will be the fragmentlist and send a code
			//this code will be the index of the picked option
			//so in the list fragment the right methods can be called
			@Override
			public void onClick(View view) {
				dismiss();
				getTargetFragment().onActivityResult(getTargetRequestCode(), 2,
						getActivity().getIntent());
			}

		});

		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				dismiss();//dismisses the dialog

			}

		});

		return alertDialogBuilder.create();//creates the dialog
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}