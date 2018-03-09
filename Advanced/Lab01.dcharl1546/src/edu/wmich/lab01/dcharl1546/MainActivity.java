package edu.wmich.lab01.dcharl1546;
/* 
*************************************
* Programmer: David Charles
* Class ID: dchar5146
* Lab 01
* CIS 4700
* Spring 2014
* Due date: 2/12/14
* Date completed: 2/11/14
*************************************
Simple wmu app
Features:
Marks on a map:
Current location with address and coordinates
WMU Location
Proximity Alert:
This app lets you set a proximity alert to the following places:
Schenaider hall
Waldo library
The recreation center
The Bernhard center
If you click the main icon it will show a picture of wmu using a fragment
Tested on a Samsung Galaxy S4
*************************************
*/
import java.util.Stack;
import edu.wmich.lab01.dcharl1546.MainFragment.OnFragment1ClickedListener;
import edu.wmich.lab01.dcharl1546.WmuFragment.OnFragment2ClickedListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnFragment1ClickedListener, OnFragment2ClickedListener{

	public Stack<String> mFragmentStack;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//creates a stack of type string to add and pop fragment tags 
		//since I use 2 fragments 
		//this is important because when the fragment is added to the Fragmenttransaction
		//it needs a tag because it uses findfragmentbytag
		
		mFragmentStack = new Stack<String>();

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = new MainFragment();
		String tag = fragment.toString();
		mFragmentStack.add(tag);
		transaction.add(R.id.fragment_swap, fragment,tag);
		transaction.addToBackStack(tag);
		transaction.commit();
		
		//when one of these image buttons is clicked it will go to their respective activity
		ImageButton myLoc= (ImageButton)findViewById(R.id.myLocationBtn);
		myLoc.setOnClickListener(new View.OnClickListener() 
	      {
	               public void onClick(View v) 
	               {
	            	   Intent intent = new Intent(MainActivity.this, MyLocation.class);
	            	   startActivity(intent);
	            	   
	               }
	  });
		 
		    
	        
		ImageButton proximityAlert= (ImageButton)findViewById(R.id.proximityAlertBtn);
		proximityAlert.setOnClickListener(new View.OnClickListener() 
	      {
	               public void onClick(View v) 
	               {
	            	   Intent intent = new Intent(MainActivity.this, ListFragment.class);
	            	   startActivity(intent);
	            	   
	               }
	  });
	        
		
	}

	//method will be fired up when fragment number 1 is clicked
	@Override
	public void onFragment1Clicked() {
		//creates a new fragment 
		Fragment newFragment = new WmuFragment();
		Bundle args = new Bundle();
		String tag = newFragment.toString();
		newFragment.setArguments(args);

		//creates a transaction
		//calls the method begintransaction
		//set the transaction animations 
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fragment_animation_fade_in, R.anim.fragment_animation_fade_out);
		
		//takes out the stack (peek) the current fragment
		Fragment currentFragment = getFragmentManager().findFragmentByTag(mFragmentStack.peek());
		transaction.hide(currentFragment);

		//adds the new fragment
		transaction.add(R.id.fragment_swap, newFragment,newFragment.toString());
		transaction.addToBackStack(tag);
		mFragmentStack.add(tag);
		
		//this is needed if this is not written there it will not do anything
		transaction.commit();
	}
	
	//method will be fired up when fragment number 2 is clicked
	@Override
	public void onFragment2Clicked() {
		Fragment newFragment = new MainFragment();
		Bundle args = new Bundle();
		String tag = newFragment.toString();
		newFragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fragment_animation_fade_in, R.anim.fragment_animation_fade_out);

		Fragment currentFragment = getFragmentManager().findFragmentByTag(mFragmentStack.peek());
		transaction.hide(currentFragment);

		transaction.add(R.id.fragment_swap, newFragment,newFragment.toString());
		transaction.addToBackStack(tag);
		mFragmentStack.add(tag);

		transaction.commit();
	}

	//override this method because if not it would remove the fragment
	//instead of finishing and it would leave a white space in the screen
	//it would actually clal finish() the second time back was pressed
	@Override
	public void onBackPressed(){
		finish();
		
	}

}
