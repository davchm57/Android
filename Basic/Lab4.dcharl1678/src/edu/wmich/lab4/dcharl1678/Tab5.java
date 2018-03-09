package edu.wmich.lab4.dcharl1678;
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
import java.util.Calendar;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Tab5 extends Activity {
private int currentYear;//holds current year
private int currentMonth;//holds current month
private int currentDay;//holds current day
static final int DATE_DIALOG_ID=0;//used for the switch
private Button btDate;//triggers actions to save the selected date
private String bookResult="";//holds final output
private String bookDate="";//holds curret selected date as a string
private TextView bookeddestination;//output textview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab5);
		btDate=(Button) findViewById(R.id.btnFlightDate);//referencing the button that triggers
		//the action to select a date
		bookeddestination=(TextView) findViewById(R.id.resultstv);//referencing the textview
		//with the final results of booking
		final Button resultButton=(Button) findViewById(R.id.btnbookFlight);//triggers the action
		//to output the final booking results in the textview
		resultButton.setEnabled(false);//disabled to make sure
		//the user won't press it without choosing a date first
		final EditText FlightFrom=(EditText) findViewById(R.id.fromet);//from edit text
		final EditText FlightTo=(EditText) findViewById(R.id.toet);//to edittext
		btDate.setOnClickListener(new View.OnClickListener() {//making it it listen for an event
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_DIALOG_ID);//on click it will call the showDialog method
				resultButton.setEnabled(true);//it gets enabled since the user is selected a date
			}
		});
		

		resultButton.setOnClickListener(new OnClickListener(){
			//Makes sure that the user entered a from and a to 
			//if the user has not entered one of those, it will show a toast
			//else it will show the results in a textview
		    public void onClick(View v) {
		    	if (FlightFrom.getText().toString().length()<1 || FlightTo.getText().toString().length()<1 ){
		    		Toast toast = Toast.makeText(Tab5.this, "Make sure you From and To text fields are not blank!",Toast.LENGTH_LONG);
		    		toast.show();
		    	}
		    	else
		    	{
			    	bookResult="Good News, your Flight from "+ FlightFrom.getText()+" to "+ FlightTo.getText()+" is booked for " + bookDate;
			    	bookeddestination.setText(bookResult);
		    	}
		    }});
		    
	
    	
		final Calendar c=Calendar.getInstance();
		currentYear=c.get(Calendar.YEAR);//gets current year
		currentMonth=c.get(Calendar.MONTH);//gets current month
		currentDay=c.get(Calendar.DAY_OF_MONTH);//get current day
	}

	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DATE_DIALOG_ID:
				//creates a new datepickerdialog object with the current date information
				return new DatePickerDialog(this, reservationDate, currentYear,
						currentMonth,currentDay);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener reservationDate=
			new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int month,
				int dayOfMonth) {
			//This will happen when the user select a date
			bookDate=(month+1)+"-"+dayOfMonth+("-")+year;
			
			//it will get the month and year and show it in a textview along with other text
			
		}
	};


}
