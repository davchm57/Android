package edu.wmich.teambus;



import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/04/14
* Date completed: 04/04/14
*************************************
* rewrites the pressing of the parent view
*************************************
*/

public class NoParentPressImageView extends ImageView {
    
	
	
    public NoParentPressImageView(Context context) {
        this(context, null);
    }
    
    public NoParentPressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void setPressed(boolean pressed) {
        // If the parent is pressed, do not set to pressed.
        if (pressed && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }
}
