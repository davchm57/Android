package edu.wmich.dso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubmitVote extends Activity {
	private TextView membershipDirector;
	private TextView president;
	private TextView secretary;
	private TextView treasurer;
	private TextView vicePresident;
	private TextView vocal;
	private Button submit;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitvote);

		submit = (Button) findViewById(R.id.btnsubmit);
		back = (Button) findViewById(R.id.btnback);

		membershipDirector = (TextView) findViewById(R.id.tvmembershipdirector);
		president = (TextView) findViewById(R.id.tvpresident);
		secretary = (TextView) findViewById(R.id.tvsecretary);
		treasurer = (TextView) findViewById(R.id.tvtreasurer);
		vicePresident = (TextView) findViewById(R.id.tvvicepresident);
		vocal = (TextView) findViewById(R.id.tvvocal);

		membershipDirector.setText("Membership Director: "
				+ MainActivity.currentVote.get(0).getMembershipDirector());
		president.setText("President: "
				+ MainActivity.currentVote.get(0).getPresident());
		secretary.setText("Secretary: "
				+ MainActivity.currentVote.get(0).getSecretary());
		treasurer.setText("Treasurer: "
				+ MainActivity.currentVote.get(0).getTreasurer());
		vicePresident.setText("VicePresident: "
				+ MainActivity.currentVote.get(0).getVicePresident());
		vocal.setText("Vocal: " + MainActivity.currentVote.get(0).getVocal());

		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				question();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(SubmitVote.this, Vocal.class);
				startActivity(k);
				SubmitVote.this.finish();
			}
		});

	}

	private void question() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder
				.setTitle("Are you sure you want to submit your vote?");

		// set dialog message
		alertDialogBuilder
				.setMessage("Click yes to submit!")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								positive();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void positive() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Vote");

		// set dialog message
		alertDialogBuilder
				.setMessage("Vote Sucessfully submitted")
				.setCancelable(false)
				.setPositiveButton("Dismiss",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent k = new Intent(SubmitVote.this,
										Thank.class);
								startActivity(k);
								SubmitVote.this.finish();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	public void onBackPressed() {

	}
}
