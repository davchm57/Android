package edu.wmich.dso;

import java.io.IOException;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

public class MembersList extends Activity {
	private DatabaseHandler myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberslist);
		TextView members = (TextView) findViewById(R.id.tvmembers);
		myDbHelper = new DatabaseHandler(this); // instantiate the dbhelper
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

		myDbHelper.getMemberList();
		String temp = "";
		for (int i = 0; i < MainActivity.memberId.size(); i++) {
			temp = temp + "Member Name:" + MainActivity.memberName.get(i)
					+ "\n" + "Member Id: " + MainActivity.memberId.get(i)
					+ "\n\n";
		}
		members.setText(temp);
	}

}
