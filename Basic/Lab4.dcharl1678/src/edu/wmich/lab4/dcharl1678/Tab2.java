/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab04
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/26/13
* Date completed: 11/25/13
*************************************
BookItCheap is a travel agency 
We guarantee the best prices!
Book your trips safe and easy with this app
It has 6 classes
MainActivity
Tab1:Displays an image and talks about BookItCheap
Tab2:Lets the user choose a destination among 6 and a from and to Date
Tab3:Shows a Gridview Image Gallery with the destinations pictures
Tab4:Goes to a website that talks about these destinations 
Tab5:Lets the user entere a location and a destination to book a flight
*************************************
*/
package edu.wmich.lab4.dcharl1678;

import java.util.Calendar;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Tab2 extends Activity {
private int currentYear;//holds current year
private int currentMonth;//holds current month
private int currentDay;//holds current day
private final int DATE_DIALOG_FROM=0;//used for the switch
private final int DATE_DIALOG_TO=1;//used for the switch
private boolean fromB=false;//checks if from date have been selected
private boolean toB=false;//checks if to date have been selected
private String fromDate="";//holds checking in date as a string
private String toDate="";//holds checking out date as a string
private Button btDateFrom;//triggers actions to get the from date
private Button btDateTo;//triggers the action to get the to date
private String bookDestination="";//holds the destination selected in the radiogroup
private String bookResult="";//holds the final string that is outputed
private int [] checkFrom=new int[3];//keeps track of checking in date
private int[] checkTo=new int[3];//keeps track of checking out date
int from_year, from_month, from_day,to_year, to_month, to_day; //initialize them to current date in onStart()/onCreate()
//DatePickerDialog.OnDateSetListener from_dateListener,to_dateListener;
private TextView bookeddestination;//output textview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2);
		btDateFrom=(Button) findViewById(R.id.btnDateFrom);//referencing
		btDateTo=(Button) findViewById(R.id.btnDateTo);
		bookeddestination=(TextView) findViewById(R.id.txtReservation);//referencing
		//Referencing the radio buttons, each one represents a destination
		//the user can select only one option of these 6 destinations
		//paris, barcelona, london, new york san francisco or sydney
		final RadioButton parisRb = (RadioButton) findViewById(R.id.paris);
		final RadioButton barcelonaRb = (RadioButton) findViewById(R.id.barcelona);
		final RadioButton londonRb = (RadioButton) findViewById(R.id.london);
		final RadioButton newYorkRb = (RadioButton) findViewById(R.id.newYork);
		final RadioButton sanFranciscoRb = (RadioButton) findViewById(R.id.sanFrancisco);
		final RadioButton sydneyRb = (RadioButton) findViewById(R.id.sydney);
		final Button resultButton=(Button) findViewById(R.id.book);//will trigger the action to show
		//the booking results
		resultButton.setEnabled(false);//disable the result button so the user won't press it without
		//first selecting the dates from and to
		btDateFrom.setOnClickListener(new View.OnClickListener() {//making it it listen for an event
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				fromB=true;
				showDialog(DATE_DIALOG_FROM);//on click it will call the showDialog method
				if (toB==true)//check if toB which is a boolean is true, if is true it means the user selected a to Date
				{//and that means the result button must be enable since the user is currently selecting a from date 
					resultButton.setEnabled(true);//enables the result button 
					//
				}
			}
		});
		//it uses the same logic explained above
		btDateTo.setOnClickListener(new View.OnClickListener() {//making it it listen for an event
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				toB=true;
				showDialog(DATE_DIALOG_TO);//on click it will call the showDialog method
				if (fromB==true)
				{
					resultButton.setEnabled(true);
				}
			}
		});
		
		resultButton.setOnClickListener(new OnClickListener(){
			//checks if both dates have been selected
			//The method checkForDate() checks if the date is valid or not (explained below)
		    public void onClick(View v) {	
		    	if (fromB==true && toB==true && checkForDate()==true){
		    	//checks every radiobutton and assign the selected destination to the string bookDestination
			    	if (parisRb.isChecked())
			    	{
			    		bookDestination="Paris";
			    	}
			    	else if (barcelonaRb.isChecked())
			    	{
			    		bookDestination="Barcelona";
			    	}
			    	else if (londonRb.isChecked())
			    	{
			    		bookDestination="London";
			    	}
			    	else if (newYorkRb.isChecked())
			    	{
			    		bookDestination="New York";
			    	}
			    	else if(sanFranciscoRb.isChecked())
			    	{
			    		bookDestination="San Francisco";
			    	}
			    	else if (sydneyRb.isChecked())
			    	{
			    		bookDestination="Sydney";
			    	}
			    	bookResult="Good News, your trip to "+bookDestination+" is booked from " + fromDate +" to "+toDate;//formatting the final result
			    	bookeddestination.setText(bookResult);//sets the final booking result
		    	}
			    	else//shows a toast since the user entered an invalid checking in or checking out date
			    	{
			    		Toast toast = Toast.makeText(Tab2.this, "Make sure you chose a valid check in and check out date",Toast.LENGTH_LONG);
			    		toast.show();
			    		bookeddestination.setText("---");
			    	}
		    }});

		final Calendar c=Calendar.getInstance();//creates a calendar to get the current date
		currentYear=c.get(Calendar.YEAR);//gets current year
		currentMonth=c.get(Calendar.MONTH);//gets current month
		currentDay=c.get(Calendar.DAY_OF_MONTH);//get current day
	}
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DATE_DIALOG_FROM://from date
				//creates a new datepickerdialog object with the current date information
				return new DatePickerDialog(this, from_date, currentYear,
						currentMonth,currentDay);
			case DATE_DIALOG_TO://to date
				//creates a new datepickerdialog object with the current date information
				return new DatePickerDialog(this, to_date, currentYear,
						currentMonth,currentDay);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener from_date=
			new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int month,
				int dayOfMonth) {
			//This will happen when the user select a date
			fromDate=(month+1)+"-"+dayOfMonth+("-")+year;
			//these arrays hold the selected date
			//to make sure the user did not entered a invalid date
			//for example checking in oct 25 2014 and checking out oct 25 2013
			checkFrom[0]=month+1;
			checkFrom[1]=dayOfMonth;
			checkFrom[2]=year;
			//gets month, year and day
			
		}
	};

	//uses the same logic explained above
	private DatePickerDialog.OnDateSetListener to_date=
			new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int month,
				int dayOfMonth) {
			//This will happen when the user select a date
			checkTo[0]=month+1;
			checkTo[1]=dayOfMonth;
			checkTo[2]=year;
			toDate=(month+1)+"-"+dayOfMonth+("-")+year;
		}
	};
	
	//this method checks for the date entered
	//it will check if the year is valid
	//then the month
	//then the day 
	//if it finds out is invalid it will return false
	//else true
	private boolean checkForDate()
	{

		if (checkTo[2]>checkFrom[2])
		{
			return true;
		}
		else if (checkTo[2]<checkFrom[2])
		{
			return false;
		}
		else if(checkTo[0]>checkFrom[0])
		{
			return true;
		}
		else if(checkTo[0]<checkFrom[0])
		{
			return false;
		}
		else if(checkTo[1]>checkFrom[1])
		{
			return true;
		}
		else if(checkTo[1]<checkFrom[1])
		{
			return false;
		}

		return false;
	}
}

