/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Exercise 4
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 10/09/13
* Date completed: 10/06/13
*************************************
In this program there is 1 radiogrup made by 3 radiobuttons.
When one of the radio buttons is selected and the select button is pressed
it will show what console you selected as your favorite and how much point currently has
for that we created counters xboxCounter, ps4Counter and wiiCounter which are incremented 
depending on which radiobutton is pressed
*************************************
*/
package edu.wmich.exercise4.dcharl1678;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	int xboxCounter=1;
	int ps4Counter=1;
	int wiiCounter=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Creates radio buttons
    	final RadioButton xboxRB = (RadioButton) findViewById(R.id.xbox);
    	final RadioButton ps4RB= (RadioButton) findViewById(R.id.ps4);
    	final RadioButton wiiuRB = (RadioButton) findViewById(R.id.wiiu);
    	//Creates textviews, this will show how many times each console has been selected
    	final TextView xboxC = (TextView) findViewById(R.id.xboxC);
    	final TextView ps4C = (TextView) findViewById(R.id.ps4C);
    	final TextView wiiC = (TextView) findViewById(R.id.wiiC);
    	//Creates the select button. 
	  	final Button selectButton= (Button)findViewById(R.id.selectBtn);
		selectButton.setOnClickListener(new OnClickListener(){
		//When pressed there will be 3 choices since we have a radiogrup with 3 radio buttons
		//every choice will trigger a different action, which is why we need if statements
	    public void onClick(View v) {	
	    	if (xboxRB.isChecked())
	    	{
	    		Toast.makeText(MainActivity.this,1 +" Point for the Xbox one", Toast.LENGTH_LONG).show();
	    		//The toast will show that 1 point have been added to the respective checked console
	    		//Because there is a 1 and then a string saying that it added 1 point
	    		xboxC.setText(String.valueOf(xboxCounter));//we convert it to string so we can set it to the textview
	    		xboxCounter++;//it keeps tracks of how many times
	    					//the ++ sign means it will add one every time this console has been selected as favorite 
	    	}
	    	if (ps4RB.isChecked())
	    	{
	    		Toast.makeText(MainActivity.this,1 +" Point for the Play Station 4", Toast.LENGTH_LONG).show();
	    		//The toast will show that 1 point have been added to the respective checked console
	    		//Because there is a 1 and then a string saying that it added 1 point
	    		ps4C.setText(String.valueOf(ps4Counter));//we convert it to string so we can set it to the textview
	    		ps4Counter++;//it keeps tracks of how many times
	    		//the ++ sign means it will add one every time this console has been selected as favorite 
	    	}
	    	if (wiiuRB.isChecked())
	    	{
	    		Toast.makeText(MainActivity.this,1 +" Point for the Wii U", Toast.LENGTH_LONG).show();
	    		//The toast will show that 1 point have been added to the respective checked console
	    		//Because there is a 1 and then a string saying that it added 1 point
	    		wiiC.setText(String.valueOf(wiiCounter));//we convert it to string so we can set it to the textview
	    		wiiCounter++;//it keeps tracks of how many times
	    		//the ++ sign means it will add one every time this console has been selected as favorite 
	    	}
	    }});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
