package edu.wmich.dso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VoteMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.votemenu);

		Button button = (Button) findViewById(R.id.btnstartvote);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent k = new Intent(VoteMenu.this, MembershipDirector.class);
				startActivity(k);
				VoteMenu.this.finish();
			}
		});
	}

	@Override
	public void onBackPressed() {

	}
}
