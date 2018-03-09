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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
public class ListFragment extends FragmentActivity {
 //shows the listfragment 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.listfragment);

}
}