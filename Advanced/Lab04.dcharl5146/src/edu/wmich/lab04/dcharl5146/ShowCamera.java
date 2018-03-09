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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

   private SurfaceHolder mHolder;//surfaceholder
   private Camera mCamera;//camera object
   private Context c;//context
   private SharedPreferences preferences;//shared preferneces used to get the effects
   public ShowCamera(Context context,Camera camera) {
      super(context);
      
      mCamera = camera;//instantiates the object of the camera coming from the selfie activity
      mHolder = getHolder();//surface holder
      c=context;//context
      mHolder.addCallback(this);//adds the surface holder to call back
   }



public void surfaceCreated(SurfaceHolder holder) {

    // The Surface has been created, now tell the camera where to draw the preview.
    try {
        mCamera.setPreviewDisplay(holder);
        mCamera.startPreview();
    } catch (Exception e) {
    	
    }
}

public void surfaceDestroyed(SurfaceHolder holder) {
	try
	{
	mCamera.release();
	}
	catch (Exception ex)
	{
		
	}
    // empty. Take care of releasing the Camera preview in your activity.
}

public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    // If your preview can change or rotate, take care of those events here.
    // Make sure to stop the preview before resizing or reformatting it.

    if (mHolder.getSurface() == null){
      // preview surface does not exist
      return;
    }


    try
    {
   
    
    //gets the screen size
    Display display = ((WindowManager)
    		c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;
    
    
    Camera.Parameters parameters = mCamera.getParameters();//camera parameters
    Camera.Size previewSize= getBestPreviewSize(width, height);//gets the best supported screen size
    parameters.setPreviewSize(previewSize.width, previewSize.height);
    //set focus mode to continuous
    parameters.setFocusMode("continuous-picture");
    

    	//sets the orientation to 90 since this app only works in portrait mode atm      
        mCamera.setDisplayOrientation(90);
    	parameters.setColorEffect(loadPreferences());//sets the color effect from the shared preferences
    	mCamera.setParameters(parameters);//set the parameters 
    	
    
    }
    	    catch (Exception ex)
    	    {
    	    }
    //previews the camera 
    previewCamera();  
   
}


//gets the best preview size of the current device
private Camera.Size getBestPreviewSize(int width, int height)
{
  Camera.Size result=null;    
  Camera.Parameters p = mCamera.getParameters();
  for (Camera.Size size : p.getSupportedPreviewSizes()) {
      if (size.width<=width && size.height<=height) {
          if (result==null) {
              result=size;
          } else {
              int resultArea=result.width*result.height;
              int newArea=size.width*size.height;

              if (newArea>resultArea) {
                  result=size;
              }
          }
      }
  }
  return result;
}
//it will make the camera show in the screen
public void previewCamera()
{        
    try 
    {           
        mCamera.setPreviewDisplay(mHolder);          
        mCamera.startPreview();
    }
    catch(Exception e)
    {
  
    }
}

//loads the effect from the preference activity
private String loadPreferences()
{
	preferences = PreferenceManager
			.getDefaultSharedPreferences(c);
	 return preferences.getString("effect","none");
}

// Restart camera preview between shots
public void restartPreview(){ 
	mCamera.startPreview();
} 
}