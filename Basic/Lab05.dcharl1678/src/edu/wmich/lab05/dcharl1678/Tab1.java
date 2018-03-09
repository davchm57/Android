/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab05
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 12/11/13
* Date completed: 12/8/13
*************************************
This app have 3 tabs and it shows different animations
Tab1 Frame by Frame
Tab2 Tween
Tab3 Alpha
*************************************
*/
package edu.wmich.lab05.dcharl1678;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Tab1 extends Activity {
	AnimationDrawable anim;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1);//sets the contentview to tab1
	
		ImageView imgFrame= (ImageView) findViewById(R.id.frameImage);//creates an imageview
		imgFrame.setBackgroundResource(R.drawable.animation);//sets the background of the imageview to the xml animation
		anim=(AnimationDrawable) imgFrame.getBackground();
		//creates the start and top 
		
		Button btstart=(Button) findViewById(R.id.btnStart);
		Button btstop=(Button) findViewById(R.id.btnStop);
		btstart.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				anim.start();//starts the animation
			}
			
		});
		btstop.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				anim.stop();//stops the animation
			}
	
		});
		}
}
