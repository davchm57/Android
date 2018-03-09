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


public class MainFragment extends Fragment {

	private OnFragment1ClickedListener mOnFragment1ClickedListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.mainfragment, container, false);
		
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnFragment1ClickedListener.onFragment1Clicked();
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnFragment1ClickedListener = (OnFragment1ClickedListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	public interface OnFragment1ClickedListener{
		public void onFragment1Clicked();
	}

}
