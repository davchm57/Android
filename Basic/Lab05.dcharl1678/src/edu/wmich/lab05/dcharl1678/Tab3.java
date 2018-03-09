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

In this class the animation will fade
*************************************
*/
package edu.wmich.lab05.dcharl1678;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class Tab3 extends Activity {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab3);//sets the contentview to tab3
		
		final ImageView animFade= (ImageView) findViewById(R.id.alpha);//creates the imagesview
		final Button b1=(Button) findViewById(R.id.alphastart);//button to start the animation
	  	final Animation fade1 =AnimationUtils.loadAnimation(this,  R.anim.alpha);//creates the animation object
		b1.setOnClickListener(new OnClickListener(){
	        public void onClick(View v) {
	        	 animFade.startAnimation(fade1);//starts the animation

	        }
		});
	}
}