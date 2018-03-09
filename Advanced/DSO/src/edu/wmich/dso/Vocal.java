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

public class Vocal extends Activity {
	private RadioGroup rGroup;
	private int selectedId = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vocal);
		rGroup = (RadioGroup) findViewById(R.id.vocalradiog);
		loadSettings();
		Button back = (Button) findViewById(R.id.btnvocalback);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedId = rGroup.getCheckedRadioButtonId();
				if (selectedId != -1) {
					saveVote();
				}
				Intent k = new Intent(Vocal.this, VicePresident.class);
				startActivity(k);
				Vocal.this.finish();
			}
		});

		Button next = (Button) findViewById(R.id.btnvocalnext);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				selectedId = rGroup.getCheckedRadioButtonId();
				if (selectedId != -1) {
					saveVote();
					Intent k = new Intent(Vocal.this, SubmitVote.class);
					startActivity(k);
					Vocal.this.finish();
				} else {
					negative();
				}
			}
		});
	}

	private void loadSettings() {

		if (MainActivity.currentVote.get(0).getVocal() != "") {
			if (MainActivity.currentVote
					.get(0)
					.getVocal()
					.equalsIgnoreCase(getResources().getString(R.string.vocal1))) {
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
			MainActivity.currentVote.get(0).setVocal(
					getResources().getString(R.string.vocal1));
			break;
		case 1:
			MainActivity.currentVote.get(0).setVocal(
					getResources().getString(R.string.vocal2));
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
						"Please select a person for the position of Vocal")
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
