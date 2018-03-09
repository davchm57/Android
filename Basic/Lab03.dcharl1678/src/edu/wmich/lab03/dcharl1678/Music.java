/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab03
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/06/13
* Date completed: 11/04/13
*************************************
This app have a list view that lets you choose different things to do
2 Classes which function is to open a browser and to go a website
2 Classes with large images
1 Class which function is to play music 
1 Class with a gallery
*************************************
*/
package edu.wmich.lab03.dcharl1678;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Music extends Activity {

	Button btp1,btp2,bts1,bts2;//creates buttons for play and stop (for both songs)
	MediaPlayer mp1,mp2;//2 mediaplayers since we have 2 songs to play
	TextView state;//textview that will show the state, if musing is being played or not
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music);
		
		btp1=(Button) findViewById(R.id.p1);//reference it to the activity widget
		btp2=(Button) findViewById(R.id.p2);//reference it to the activity widget
		bts1=(Button) findViewById(R.id.s1);//reference it to the activity widget
		bts2=(Button) findViewById(R.id.s2);//reference it to the activity widget
		
		
		btp1.setOnClickListener(bp1);//set it on listener
		btp2.setOnClickListener(bp2);//set it on listener
		bts1.setOnClickListener(bs1);//set it on listener
		bts2.setOnClickListener(bs2);//set it on listener
		
		mp1=new MediaPlayer();//creates the mediaplayer object
		mp1=(MediaPlayer)MediaPlayer.create(this, R.raw.merengue);//the create method so the media player object
		//so it will know what to play
		mp2=new MediaPlayer();//creates the mediaplayer object
		mp2=(MediaPlayer)MediaPlayer.create(this, R.raw.mariachi);//the create method so the media player object
		//so it will know what to play
		
		state=(TextView) findViewById(R.id.state);//references the state textview widget
		
	
	}
	
	Button.OnClickListener bp1 =new Button.OnClickListener()
	{//if this button which is the play button for the first song is clicked it will 
		public void onClick(View arg0) {
			mp1.start();//play the music
			btp2.setEnabled(false);//disable the other play button
			state.setText("Playing Merengue");//update the current state
		}
		
	};
	Button.OnClickListener bp2 =new Button.OnClickListener()
	{//if this button which is the play button for the second song is clicked it will 
		public void onClick(View arg0) {
			mp2.start();//play the music
			btp1.setEnabled(false);//disable the other play button
			state.setText("Playing Mariachi");//update the current state
		}
		
	};
	Button.OnClickListener bs1 =new Button.OnClickListener()
	{
		//if the first pause button is pressed
		public void onClick(View arg0) {
			mp1.pause();//it will puase the song
			btp2.setEnabled(true);//enable the second play button since the first one is paused
			//this one will be able to get pressed so it can get played
			state.setText("---");//updates the state
			
		}
		
	};
	Button.OnClickListener bs2=new Button.OnClickListener()
	{
		public void onClick(View arg0) {
			mp2.pause();//pauses the second player
			btp1.setEnabled(true);//enables the first player to so it can get played since the second is paused
			state.setText("---");//updates the state
		}
		
	};
}
