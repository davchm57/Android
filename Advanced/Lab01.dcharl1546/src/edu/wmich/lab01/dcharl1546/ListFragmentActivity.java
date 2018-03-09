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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.v4.app.ListFragment;

public class ListFragmentActivity extends ListFragment {
	//string with places
    String[] places = {
            "Schneider Hall",
            "Waldo Library",
            "The Recreation Center",
            "The Bernhard Center",            
    };

    // Array of integers points to images stored in /res/drawable/
    int[] images = new int[]{
        R.drawable.schneider,
        R.drawable.waldo,
        R.drawable.reccenter,
        R.drawable.ben
 
    };
 
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		
		// Each row store a place and a picture
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();        
        
        for(int i=0;i<4;i++){
        	HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", places[i]);
            hm.put("image", Integer.toString(images[i]) );            
            aList.add(hm);        
        }
        
        // Keys used in Hashmap
        String[] from = { "image","txt" };
        
        // Ids of views in listview_layout
        int[] to = { R.id.image,R.id.txt};        
        
        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);       
		
		setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);		
	}

    public void onListItemClick(ListView parent, View v, int position, long id) {
    	//using an array to and using the position of the selected item
        String selectedPlace = places[position];
            Intent intent = new Intent(getActivity(), ProxAlertActivity.class);
            intent.putExtra("place", selectedPlace);
            startActivity(intent);
    }
    }


