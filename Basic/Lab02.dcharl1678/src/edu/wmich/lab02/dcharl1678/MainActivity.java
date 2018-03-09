/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab02
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 10/16/13
* Date completed: 10/13/13
*************************************
*This app will let the user choose among 4 items (smart phones). 
*Each smartphone have a price
*User will have to input a quantity
*The output will be the price of the smart phone selected by the quantity
*This will be outputed in a textview 
*************************************
*/
package edu.wmich.lab02.dcharl1678;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	final double s4Price=4.00;//Price of the first smart phone, the samsumg galaxy s4
	final double g2Price=3.52;//Price of the second smart phone, the lg g2
	final double htcPrice=3.10;//Price of the third smart phone, the htc one
	final double n4Price=2.00;//Price of the fourth smart phone, the nexus 4
	String input;//contains the quantity of cellphones as a string taken from the edittext "inputText"
	int quantity=0;//contain the quantity of cellphones as an integer so we can do math operations with it
	String output="";//contains the final output
	double productsTotal=0.00;//cotains the total of the calculations (quantity of items by the price of the item)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//1 button, 4 radio buttons, 1 textview and 1 editexts objects are created in the following part of code
		final Button b1=(Button) findViewById(R.id.buyB);
		final RadioButton cs4 = (RadioButton) findViewById(R.id.c1);
    	final RadioButton cg2= (RadioButton) findViewById(R.id.c2);
    	final RadioButton chtc= (RadioButton) findViewById(R.id.c3);
      	final RadioButton cn4= (RadioButton) findViewById(R.id.c4);
      	final TextView outputText = (TextView) findViewById(R.id.output);
      	final EditText inputText = (EditText) findViewById(R.id.inputText);
		b1.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	              	DecimalFormat fm=new DecimalFormat("$#.##");//creating a decimalformat object so it will look like money and limiting it to 2 decimal place
	            	outputText.setText("");//sets the textview that contains the output to nothing
	            	input=inputText.getText().toString();
	            	if (input.equals(""))//checking if there is actual input so the program won't crash
	            	//when trying to convert the input to integer
	            	{
	            		Toast.makeText(MainActivity.this,"Enter the number of products you want to buy", Toast.LENGTH_LONG).show();
	            	}
	            	else
	            	{	
	            		quantity=Integer.parseInt(input);//parsing the input to integer so it can be displayed in the final output text 
	            		productsTotal=Double.parseDouble(input);//parsing the input to double so it can be displayed since a price can have decimal points
	            		//double variable type is needed
	            		if (quantity<3)//checking if quantity is less than 3, since the specs say that the quantity have to be 3 or greater
		            		{
	            			Toast.makeText(MainActivity.this,"You need to buy at least 3 items", Toast.LENGTH_LONG).show();
		            		}
	            		else if(quantity>24)//checking if the quantity is more than 24 since the specs say that the quantity can't be greater than 24
		            		{
	            			Toast.makeText(MainActivity.this,"You can't buy more than 24 items", Toast.LENGTH_LONG).show();
		            		}
	            		else
	            		{
	            			//in this will there are if statements to find out which radio button in the radio group
	            			//is checked and take a different action depending on which is checked
	            			//there will be a concatenation to get the final text that is going to be set
	            			//in the textview "outputText". 
	            			output="You just bought ";
			            	if (cs4.isChecked())//checks if this option in the radio groupd is seleted
			    	    	{
			            		output=output+quantity +" Samsumg Galaxy s4"+" for the cost of "+String.valueOf(fm.format(productsTotal*s4Price))+"!";
			            		outputText.setText((output));	
			    	    	}
			            	else if (cg2.isChecked())//checks if this option in the radio groupd is seleted
			    	    	{
			            		output=output+quantity+" LG G2"+" for the cost of "+String.valueOf(fm.format(productsTotal*g2Price))+"!";
			            		outputText.setText((output));	
			    	    	}
			            	else if (chtc.isChecked())//checks if this option in the radio groupd is seleted
			    	    	{
			            		output=output+quantity+" HTC One"+" for the cost of "+String.valueOf(fm.format(productsTotal*htcPrice))+"!";
			            		outputText.setText((output));	
			    	    	}
			            	else if (cn4.isChecked())//checks if this option in the radio groupd is seleted
			    	    	{
			            		output=output+quantity+" Nexus 4"+" for the cost of "+String.valueOf(fm.format(productsTotal*n4Price))+"!";
			            		outputText.setText((output));	 
			    	    	}
	            		}
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
