package edu.wmich.dso;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static ArrayList<Vote> currentVote = new ArrayList<Vote>();
	public static ArrayList<ResultObject> currentResult = new ArrayList<ResultObject>();
	public static ArrayList<String> memberName = new ArrayList<String>();
	public static ArrayList<String> memberId = new ArrayList<String>();
	public static String currentUserName = "";
	public static int currentUserId = 0;

	private EditText inputed;
	private String userInput;

	private String password = "dso2014";
	private String inputPassword = "";
	private DatabaseHandler myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inputed = (EditText) findViewById(R.id.edlogin);
		MainActivity.currentVote.clear();
		memberName.clear();
		memberId.clear();

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

		// try
		// {myDbHelper.copyDataBase();
		// }
		// catch (Exception ex )
		// {
		//
		// }

		Button close = (Button) findViewById(R.id.btnclose);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});

		Button about = (Button) findViewById(R.id.btnabout);
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				negative("Programmers",
						"David Charles\nDouglas Wiencek\nJennifer O'Dea\nJohn Smith\n");
			}
		});

		Button button = (Button) findViewById(R.id.btnlogin);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					userInput = inputed.getText().toString();

					// Log.v("test", userInput);
					// Log.v("test2", String.valueOf(vote.getMemberWinNumber())
					// );
					myDbHelper.getCurrentMember(Integer.parseInt(userInput));

					if ((currentUserId == (Integer.parseInt(userInput)))) {
						Vote vote = new Vote(Integer.parseInt(userInput));
						vote.setCurrentMember(currentUserName);
						currentVote.add(vote);
						positive();
					} else {

						negative("Invalid WIN Number",
								"Please enter a valid WIN Number");
					}
				} catch (Exception ex) {

				}
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

		View promptView = layoutInflater.inflate(R.layout.prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);

		// set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder.setView(promptView);

		final EditText input = (EditText) promptView
				.findViewById(R.id.userInput);

		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Login",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								inputPassword = input.getText().toString();
								if (inputPassword.equals(password)) {
									myDbHelper.close();
									Intent k = new Intent(MainActivity.this,
											Admin.class);
									startActivity(k);
									MainActivity.this.finish();
								} else {
									negative("Invalid password",
											"Enter a valid password");
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alertD = alertDialogBuilder.create();

		alertD.show();

		return true;
	}

	private void positive() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Sucessfully authenticated");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Welcome " + this.currentVote.get(0).getCurrentMember()
								+ "!")
				.setCancelable(false)
				.setPositiveButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								Intent k = new Intent(MainActivity.this,
										MembershipDirector.class);
								startActivity(k);
								MainActivity.this.finish();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void negative(String message, String message2) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(message);

		// set dialog message
		alertDialogBuilder
				.setMessage(message2)
				.setCancelable(false)
				.setPositiveButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// inflates the item that is in menu.main
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
