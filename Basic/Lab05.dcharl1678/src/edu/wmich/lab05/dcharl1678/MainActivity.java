package edu.wmich.lab05.dcharl1678;
/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab05
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 12/11/13
* Date completed: 12/8/13
*************************************
This app have 3 tabs and it shows different animations
Tab1 Frame by Frame
Tab2 Tween
Tab3 Alpha
*************************************
*/
import android.os.Bundle;
import android.view.Menu;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")//another deprecated
public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost=getTabHost();//creating the TabHost object
		//any tab created needs to be added to this object
		//First Tab
		TabSpec frameTab=tabHost.newTabSpec("Frame");
		frameTab.setIndicator("Frame");
		Intent frameIntent=new Intent(this, Tab1.class);//it will happen when the tab is requested by the user
		//it will go to tab1 
		frameTab.setContent(frameIntent);
		
		//Second Tab
		TabSpec tweenTab=tabHost.newTabSpec("Tween");
		tweenTab.setIndicator("Tween");
		Intent tweenIntent=new Intent(this, Tab2.class);
		tweenTab.setContent(tweenIntent);
		
		//Third Tab
		TabSpec alphaTab=tabHost.newTabSpec("Alpha");
		alphaTab.setIndicator("Alpha");
		Intent alphaIntent=new Intent(this, Tab3.class);
		alphaTab.setContent(alphaIntent);
		
		//adding tabs 
		tabHost.addTab(frameTab);
		tabHost.addTab(tweenTab);
		tabHost.addTab(alphaTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
