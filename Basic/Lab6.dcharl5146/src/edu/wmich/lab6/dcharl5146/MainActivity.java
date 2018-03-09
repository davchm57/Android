package edu.wmich.lab6.dcharl5146;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	final ActionBar actionBar = getActionBar();
	actionBar.setDisplayShowTitleEnabled(false);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // ...

	    View menuItemView = findViewById(R.id.menu); // SAME ID AS MENU ID
	    PopupMenu popupMenu = new PopupMenu(this, menuItemView); 
	    popupMenu.inflate(R.menu.counters_overflow);
	    // ...
	    popupMenu.show();
	    // ...
	    return true;
	}

}
