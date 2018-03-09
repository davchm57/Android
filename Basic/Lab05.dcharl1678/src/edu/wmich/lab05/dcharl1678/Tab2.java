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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Tab2 extends Activity {

	public void onCreate(Bundle savedInstanceState)
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.tab2);//sets the contentview to tab2
			final ImageView imgRotate= (ImageView) findViewById(R.id.tweenImage);//creates the imageview
			final Button b1=(Button) findViewById(R.id.tweenstart);//button that starts the animation
		  	final Animation rotate1 =AnimationUtils.loadAnimation(this,  R.anim.tween);//xml tween
			b1.setOnClickListener(new OnClickListener(){
		        public void onClick(View v) {
		        	//starts the animation
		        	//the image will rotate
		        	 imgRotate.startAnimation(rotate1);
		        }
			});
		}
	}

