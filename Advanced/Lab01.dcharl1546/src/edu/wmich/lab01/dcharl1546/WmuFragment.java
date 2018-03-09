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

*************************************
*/

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WmuFragment extends Fragment {
	private OnFragment2ClickedListener mOnFragment2ClickedListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.wmufragment, container, false);
		//sets the view to listen
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnFragment2ClickedListener.onFragment2Clicked();
				
			}
		});
		return view;
	}
	//this is called when a fragment gets attached to the activity
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnFragment2ClickedListener = (OnFragment2ClickedListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	//this interface is implemented by the main class
	//this is used to communicate with the activity
	public interface OnFragment2ClickedListener{
		public void onFragment2Clicked();
	}


}
