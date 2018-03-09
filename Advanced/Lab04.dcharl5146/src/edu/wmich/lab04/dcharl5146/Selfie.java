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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Selfie extends Fragment {
	
	  private Camera cameraObject;//object of the camera
	   private ShowCamera showCamera;//surfaceview object
	
	   
	   private Button discard;//discard button to discard a picture
	   private Button save;//save button to save a picture
	   private FrameLayout preview ;//it will show the preview of the camera
	   private  Bitmap bitmap;//bitmap for the picture taken
	   private Button effect;//button for the effect
	   private Button share;//button to share the pic when taken
	   private Button camera;//button to take the picture
	   private SharedPreferences preferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	 public static Camera isCameraAvailiable(){
		boolean front=true;
		 //checks for the front camera
		 //if there is not facing camera it will try to open the front one
		 Camera object=null;
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	            Camera.CameraInfo info = new Camera.CameraInfo();
	            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
	                Camera.getCameraInfo(i, info);
	                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//tries to open the camera id that belongs to the front camera
	                    try {
	                        // Gets to here OK
	                        object = Camera.open(i);
	                    } catch (Exception e) {
	                    	object.release();
	                    	front=false;
	                    	
	                    }
	                    }
	               
	                }
	            
	             if (front==false)//if front is false it means there is no front camera so it will try to open the back camera
                {
	            	 try
	            	 {
	            	 object=null;
                	 object= Camera.open(CameraInfo.CAMERA_FACING_BACK);
	            	 }
	            	 catch (Exception ex)
	            	 {
	            		
	            	 }
                }
	             
	            }
	      return object; //returns this object
	   }

	 
	 //it will be called when the picture gets taken
	  private PictureCallback capturedIt = new PictureCallback() {

	      @Override
	      public void onPictureTaken(byte[] data, Camera camera) {

	      bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
	      if(bitmap==null){
	         Toast.makeText(getActivity().getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
	      }
	      else
	      {
	         Toast.makeText(getActivity().getApplicationContext(), "Picture Taken", Toast.LENGTH_SHORT).show();  
	         
	         discard.setVisibility(View.VISIBLE);//it will show discard button
	         save.setVisibility(View.VISIBLE);//it will show save button
	         share.setVisibility(View.VISIBLE);//it will show the share button

	      }
	      try{

	      }
	      catch (Exception ex)
	      {
	    	  
	      }
	   }
	};
	

	//override on pause
	@Override
	  public void onPause()
	  {
		try
		{
			if (cameraObject!=null)
			{
		cameraObject.setPreviewCallback(null);
		showCamera.getHolder().removeCallback(showCamera);
		cameraObject.release();
			}
		}
		catch (Exception ex)
		{
			
		}
		super.onPause();
	  }
	
	
	//override on destroy
	@Override 
	public void onDestroy()
	{
		try
		{
			if (cameraObject!=null)
			{
		cameraObject.setPreviewCallback(null);
		showCamera.getHolder().removeCallback(showCamera);
		cameraObject.release();
			}
		}
		catch (Exception ex)
		{
			
		}
		
	super.onDestroy();	
	}

	//override on resume
	@Override
	  public void onResume()
	  {
		super.onResume();
		try
		{
		
			  cameraObject=isCameraAvailiable();
		      showCamera = new ShowCamera(getActivity(), cameraObject);//surfaceview object that will initiate the camera
		      preview = (FrameLayout) getView().findViewById(R.id.camera_preview);//layout well the camera preview will be shown
		      preview.addView(showCamera);//adds to the layout the surfaceview object
		}
		catch (Exception ex)
		{
			
		}
	  }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.selfie, container, false);
		 
		  //creates the object of the camera
		  cameraObject = isCameraAvailiable();
	      showCamera = new ShowCamera(getActivity(), cameraObject);//surfaceview object that will initiate the camera
	      preview = (FrameLayout) view.findViewById(R.id.camera_preview);//layout well the camera preview will be shown
	      preview.addView(showCamera);//adds to the layout the surfaceview object
	      
	      discard=(Button) view.findViewById(R.id.btndiscard);//discard button
	      discard.setVisibility(View.GONE);//hides this button, it will be shown when a pic is taken
	      
	      
	      save=(Button) view.findViewById(R.id.btnsave);//save button
	      save.setVisibility(View.GONE);//hides this button, it will be shown when a pic is taken
	      
	      share = (Button) view.findViewById(R.id.btnShare);//button to share the picture
	      share.setVisibility(View.GONE);//hides the button
	      
	      camera=(Button) view.findViewById(R.id.btnCamera);//instantiate the button camera which is the one to take a picture
	      effect=(Button) view.findViewById(R.id.btnEffect);//instantates the button effect which is the one to put an effect on the picture
	      
	      camera.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	    
	    	    	snapIt(v);//snap it will call the method to take a picture
	    	    }
	    	});
	      
	      effect.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	    
	    	    	//initates the preferences 
	    			Intent intent = new Intent();
	    			intent.setClass(getActivity(), SetPreferenceActivity.class);
	    			startActivityForResult(intent, 0);
	    	    
	    	    }
	    	});
	      
	
	        share.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	try
	            	{
	            	//it will start an intent to share the image as a JPG
	            	bitmap=ImageProcessing.getCameraPhotoOrientation(getActivity(),bitmap);//fix the rotation
	            	String pathofBmp = Images.Media.insertImage(getActivity().getContentResolver(), bitmap,"Selfie", null);//path of the image
			        Uri bmpUri = Uri.parse(pathofBmp);//make it uri
			        //image will be send to an intent that will show the share options in the device
				    Intent shareIntent=new Intent(Intent.ACTION_SEND);
				    shareIntent.setAction(Intent.ACTION_SEND);
				    shareIntent.putExtra(Intent.EXTRA_STREAM,  bmpUri );
				    shareIntent.setType("image/jpeg");
				    startActivityForResult(Intent.createChooser(shareIntent, "Share via"),1);
				 
	            	}
	            	catch (Exception ex)
	            	{
	            		
	            	}
	                
	            }
	        });
	      
	      discard.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	  restartPreview();//restart the camera preview
	    	  hideButtons();//hide the buttons
	    	    }
	    	});
	      
	      save.setOnClickListener(new View.OnClickListener() {
	    	    @Override
	    	    public void onClick(View v) {
	    	    	savePic();
	    	    	Toast.makeText(getActivity().getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();//tells the user the picture was taken
	    	    	if (loadPreferences().length()>0)
	    	    	{
	    	    		try
	    	    		{
	    	    		SmsManager sms = SmsManager.getDefault();
	    	    		sms.sendTextMessage(loadPreferences(), null,"I just took a selfie with David's awesome app" , null, null); 
	    	    		Toast.makeText(getActivity().getApplicationContext(), "SMS Sent", Toast.LENGTH_SHORT).show();
	    	    		}
	    	    		catch (Exception ex)
	    	    		{
	    	    			Toast.makeText(getActivity().getApplicationContext(), "SMS can't be sent", Toast.LENGTH_SHORT).show();
	    	    		}
	    	    	}
	    	  restartPreview();//restart the camera rpeview
	    	  hideButtons();//hide the buttons
	    	    }
	    	});
	   
		return view;
	}

	//it will be executed when it returns from the preference intent
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    	restartPreview();//restarts the preview of the camera
	    	hideButtons();
	}
	
	 public void snapIt(View view){
		
			 cameraObject.takePicture(null, null, capturedIt);//takes the picture
	
	   }

	   private void savePic()
	   {
		   savePhoto(bitmap);//sends the bitmap of the picture taken to the save pic method
	   }
	   
	   
	   //hides share, save and discard when it comes back from share intent
	   private void hideButtons()
	   {
		   bitmap=null;//It was crashing because I kept the bitmapso when It took another picture it would run out of memory so setting this to null fixed that
		   share.setVisibility(View.GONE);//hides the button once the preview is set again	
	    	  save.setVisibility(View.GONE);//hides the button once the preview is set again
	    	  discard.setVisibility(View.GONE);//hides the button once the preview is set again
		   
	   }
	   
	
	   //saves the photo when taken and the button of save is clicked
	   private String savePhoto(Bitmap bmp)
	   {

	     File path = Environment.getExternalStoragePublicDirectory(
	        Environment.DIRECTORY_PICTURES //place where the pic will be saved
	     ); 
	     String date =   dateToString(new Date(),"yyyy-MM-dd-hh-mm-ss");//date is needed for the name, so it will be unique
	     File imageFileName = new File(path, date + ".jpg"); //path where the iamged will be stored
         bmp=ImageProcessing.getCameraPhotoOrientation(getActivity(),bmp);
	     
	     try
	     {
	    	//converts the bitmap to JPEG
	       FileOutputStream out = null;
	       out = new FileOutputStream(imageFileName);
	       bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
	       out.flush();
	       out.close();
	       scanPhoto(imageFileName.toString());
	       out = null;
	     } catch (Exception e)
	     {
	       e.printStackTrace();
	     }
	     return imageFileName.toString();
	   }
	   
	 
	   
	   
	   private void scanPhoto(String imageFileName)
	   {
		 //this is where the image is actually saved 
	     Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	     File f = new File(imageFileName);//creates a new file 
	     Uri contentUri = Uri.fromFile(f);
	     mediaScanIntent.setData(contentUri);
	     getActivity().sendBroadcast(mediaScanIntent); //broadcast to the media scanner intent in order to save the picture
	   }
	   
	   //returns the current date as a string for the name of the picture
	   public String dateToString(Date date, String format) {
	        SimpleDateFormat df = new SimpleDateFormat(format);
	        return df.format(date);
	    }
	   
	   private String loadPreferences()
	   {
	   	preferences = PreferenceManager
	   			.getDefaultSharedPreferences(getActivity());
	   	 return preferences.getString("sms","");
	   }

	   //restarts the preview of the camera
	   private void restartPreview()
	   {
		   try{
				preview.removeAllViews();//removes anything on the view
		      cameraObject = isCameraAvailiable();//this is the camera object 
		      showCamera = new ShowCamera(getActivity(), cameraObject);//instances the surface object of the class showCamera that will call
		      //all the methods to initialize the camera object 
		      preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
  		    preview.addView(showCamera);//adds the surface to the view
		   }
		   catch (Exception ex)
		   {
			   
		   }
	   }
	 
	   
	   };


	   

