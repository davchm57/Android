package edu.wmich.dso;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends Activity {

	private TextView membershipDirector;
	private TextView president;
	private TextView secretary;
	private TextView treasurer;
	private TextView vicePresident;
	private TextView vocal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		MainActivity.currentResult.clear();

		membershipDirector = (TextView) findViewById(R.id.resultmembershipdirectortext);
		president = (TextView) findViewById(R.id.resultpresidenttext);
		secretary = (TextView) findViewById(R.id.resultsecretarytext);
		treasurer = (TextView) findViewById(R.id.resulttreasurertext);
		vicePresident = (TextView) findViewById(R.id.resultvicepresidenttext);
		vocal = (TextView) findViewById(R.id.resultvocaltext);

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

		myDbHelper.getResult();

		for (int i = 0; i < MainActivity.currentResult.size(); i++) {
			if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("MembershipDirector")) {
				// membershipDirector.setText(membershipDirector.getText()+"\n");
				membershipDirector.setText(membershipDirector.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			}

			else if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("President")) {
				// president.setText(president.getText()+"\n");
				president.setText(president.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			}

			else if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("Secretary")) {
				// secretary.setText(secretary.getText()+"\n");
				secretary.setText(secretary.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			}

			else if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("Treasurer")) {
				// treasurer.setText(treasurer.getText()+"\n");
				treasurer.setText(treasurer.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			} else if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("VicePresident")) {
				// vicePresident.setText(vicePresident.getText()+"\n");
				vicePresident.setText(vicePresident.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			} else if (MainActivity.currentResult.get(i).getPosition()
					.equalsIgnoreCase("Vocal")) {
				// vocal.setText(vocal.getText()+"\n");
				vocal.setText(vocal.getText()
						+ MainActivity.currentResult.get(i).toString() + "\n");
			}

		}
		Button resultback = (Button) findViewById(R.id.btnresultback);
		resultback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(Result.this, Admin.class);
				startActivity(k);
				Result.this.finish();
			}
		});

	}

}
