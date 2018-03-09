package edu.wmich.lab04.dcharl5146;


/* 
 *************************************
 * Programmer: David Charles
 * Class ID: dchar5146
 * Lab 04
 * CIS 4700
 * Spring 2014
 * Due date: 4/21/14
 * Date completed: 4/19/14
 *************************************
This app has the following features:
Tab 1: Music
Shaking your phone will play an animation and music
Shake it again it will stop the animation and music
Tab 2: Selfie
Like the name says, you can take a selfie in this tab.
You can save a taken picture or discard it
Share it via gmail, Bluetooth, ETC
You can put effects on the picture clicking on the menu button right upper corner
You can also send set an option that will send a text to a number every time you save a picture to your phone saying how awesome the app is.
Tested on Samsung galaxy S4.

Tested on Galaxy S4
*************************************
 */

import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class Music extends Fragment implements AccelerometerListener{
	private AnimationDrawable anim;//animation that will play the android dancing
	private ImageView imgFrame;//imageview that will display the animation
	private boolean isOn=false;//to check if the animation is on or off
	private MediaPlayer myMediaPlayer;//mediaplayer to play music when the phone is shaked
	private TextView title;//title that says if the music is playing or not
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	    	myMediaPlayer=new MediaPlayer();//instantiates the media player object
	}

public void onShake(float force) {
		try
		{
			if (isOn==false)//if is false it means there is nothing playing
			{
				isOn=true;	//sets it to true			
				//starts the animation
				title.setText("Dancing");//sets the title
                try
                {
                	this.myMediaPlayer=MediaPlayer.create(getActivity(), R.raw.ring);//gets the file
                	
                	myMediaPlayer.setLooping(true);//set it to loop
                    this.myMediaPlayer.start();//starts playing
                    
                    anim.start();
                }
                catch(Exception e)
                {

                }
				
			}
			else
			{
				stopPlaying();//stops playing
				anim.stop();//stops the animation
				isOn=false;//sets playing to false
				title.setText("Shake it!");//changes the the title since is not playing
			}
			
		}
		catch (Exception ex)
		{
			
		}
			
	}

	@Override
    public void onResume() {
            super.onResume();
            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(getActivity().getBaseContext())) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.startListening(this);
            }
    }
	
	//stops playing
	private void stopPlaying()
	{
		if (isOn==true)
		{
		try
        {
            this.myMediaPlayer.stop();//stops
            this.myMediaPlayer=null;//sets it to null
        }
        catch(Exception e)
        {

        }
		}
	}
	
	@Override 
	public void onPause()
	{
		super.onPause();

		
	}
	@Override
    public void onStop() {
            super.onStop();
            stopPlaying();
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isListening()) {
            	//Stop Accelerometer Listening
    			AccelerometerManager.stopListening();
            }
           
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {
			//Stop Accelerometer Listening
			AccelerometerManager.stopListening();
        }
			
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.music, container, false);
		 title=(TextView) view.findViewById(R.id.title);//textview that displays the title
		 
		 
			imgFrame= (ImageView) view.findViewById(R.id.frameImage);//imageview that will show the animation
			imgFrame.setBackgroundResource(R.drawable.animation);//set the background
			anim=(AnimationDrawable) imgFrame.getBackground();//instantiates the animatation object

		return view;
	}

	
	
	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}


}
