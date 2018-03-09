/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Exercise 7
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/11/13
* Date completed: 11/11/13
*************************************
This app lets the user choose a date for a reservation using datepicker
uses table layout to organize the content
*************************************
*/
package edu.wmich.exercise7.dcharl1678;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends Activity {
private int currentYear;//holds current year
private int currentMonth;//holds current month
private int currentDay;//holds current day
static final int DATE_DIALOG_ID=0;//used for the switch
private Button btDate;//triggers actions to save the selected date
private TextView reservation;//output textview
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btDate=(Button) findViewById(R.id.btnDate);//referencing
		reservation=(TextView) findViewById(R.id.txtReservation);//referencing
		btDate.setOnClickListener(new View.OnClickListener() {//making it it listen for an event
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				showDialog(DATE_DIALOG_ID);//on click it will call the showDialog method
				
			}
		});
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
			reservation.setText("Your reservation is set for " + (month+1)+"-"+dayOfMonth+("-")+year);
			//it will get the month and year and show it in a textview along with other text
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
