package edu.wmich.dso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Secretary extends Activity {
	private RadioGroup rGroup;
	private int selectedId = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secretary);
		rGroup = (RadioGroup) findViewById(R.id.secretaryradiog);
		loadSettings();
		Button back = (Button) findViewById(R.id.btnsecretaryback);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedId = rGroup.getCheckedRadioButtonId();
				if (selectedId != -1) {
					saveVote();
				}
				Intent k = new Intent(Secretary.this, President.class);
				startActivity(k);
				Secretary.this.finish();
			}
		});

		Button next = (Button) findViewById(R.id.btnsecretarynext);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedId = rGroup.getCheckedRadioButtonId();
				if (selectedId != -1) {
					saveVote();
					Intent k = new Intent(Secretary.this, Treasurer.class);
					startActivity(k);
					Secretary.this.finish();
				} else {
					negative();
				}
			}
		});

	}

	private void loadSettings() {

		if (MainActivity.currentVote.get(0).getSecretary() != "") {
			if (MainActivity.currentVote
					.get(0)
					.getSecretary()
					.equalsIgnoreCase(
							getResources().getString(R.string.secretary1))) {
				RadioButton btn = (RadioButton) rGroup.getChildAt(0);
				btn.toggle();
			} else {
				RadioButton btn = (RadioButton) rGroup.getChildAt(1);
				btn.toggle();
			}
		}
	}

	private void saveVote() {
		View radioButton = rGroup
				.findViewById(rGroup.getCheckedRadioButtonId());
		selectedId = rGroup.indexOfChild(radioButton);
		switch (selectedId) {
		case 0:
			MainActivity.currentVote.get(0).setSecretary(
					getResources().getString(R.string.secretary1));
			break;
		case 1:
			MainActivity.currentVote.get(0).setSecretary(
					getResources().getString(R.string.secretary2));
			break;
		}
	}

	private void negative() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("You must select an option");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Please select a person for the position of Secretary")
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
	public void onBackPressed() {

	}

}
