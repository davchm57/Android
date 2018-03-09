package edu.wmich.dso;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin extends Activity  {
	private DatabaseHandler myDbHelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);

		Button result = (Button) findViewById(R.id.btnresult);

		myDbHelper = new DatabaseHandler(this);

		result.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(Admin.this, Result.class);
				startActivity(k);

			}
		});

		Button membersList = (Button) findViewById(R.id.btnmemberslist);
		membersList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(Admin.this, MembersList.class);
				startActivity(k);
			}
		});

		Button adminBack = (Button) findViewById(R.id.btnadminback);
		adminBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(Admin.this, MainActivity.class);
				startActivity(k);
				Admin.this.finish();
			}
		});

		Button reset = (Button) findViewById(R.id.btnreset);
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDbHelper.resetVotes();
				negative("Votes", "Votes cleared sucessfully");
			}
		});

		Button delete = (Button) findViewById(R.id.btnmemberdelete);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				spin("Votes", "Votes cleared sucessfully");
			}
		});

		Button addMember = (Button) findViewById(R.id.btnmembers);
		addMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(Admin.this);

				View promptView = layoutInflater.inflate(R.layout.prompts2,
						null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Admin.this);

				// set prompts.xml to be the layout file of the alertdialog
				// builder
				alertDialogBuilder.setView(promptView);

				final EditText memberId = (EditText) promptView
						.findViewById(R.id.entermemberid);

				final EditText memberName = (EditText) promptView
						.findViewById(R.id.entermembername);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Add Member",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// get user input and set it to result

										try {

											String inputMemberId = memberId
													.getText().toString();
											String inputMemberName = memberName
													.getText().toString();

											if (inputMemberId.length() == 9
													&& inputMemberName.length() > 3) {

												addMember(inputMemberId,
														inputMemberName);

												positive();
											} else {
												negative("Invalid Member",
														"Make sure you are entering a WIN Number and a valid name");
											}
										} catch (Exception ex) {

										}
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alertD = alertDialogBuilder.create();
				alertD.show();
			}
		});

	}


	private void openDb() {
		// instantiate the dbhelper
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

	}

	private void addMember(String memberId, String memberName) {
		openDb();
		myDbHelper.addMember(memberId, memberName);
	}

	private void positive() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Sucessfully authenticated");
		alertDialogBuilder
				.setMessage("Member Added Sucessfully")
				.setCancelable(false)
				.setPositiveButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
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

	private void spin(String message, String message2) {
		openDb();
		myDbHelper.getMemberList();
		String[] bar = MainActivity.memberName
				.toArray(new String[MainActivity.memberName.size()]);
		AlertDialog.Builder b = new Builder(Admin.this);

		b.setTitle("Select a member to delete");
		b.setItems(bar, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				myDbHelper.deleteMember(Integer.parseInt(MainActivity.memberId
						.get(which)));
				Toast.makeText(getApplicationContext(),
						MainActivity.memberName.get(which) + " deleted",
						Toast.LENGTH_LONG).show();
			}
		});

		b.show();

	}


}
