package edu.wmich.dso;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Thank extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thank);

		DatabaseHandler myDbHelper = new DatabaseHandler(this); // instantiate
																// the dbhelper

		// checks if the database has been created
		try {
			myDbHelper.createDatabase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");
		}

		// open the database, use a try catch to avoid any possiblity of a crash
		try {

			myDbHelper.openDatabase();

		} catch (SQLException sqle) {

			throw sqle;
		}
		myDbHelper.addVote(MainActivity.currentVote.get(0));

		Button close = (Button) findViewById(R.id.btnclose);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(Thank.this, MainActivity.class);
				startActivity(k);
				Thank.this.finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		Thank.this.finish();
	}
}
