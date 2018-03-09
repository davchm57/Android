package programator.batterydrainpro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
  private AlertDialog.Builder mAlertbox;
  private Editor editor;
  SurfaceHolder mHolder;
  Context mContext;
  Camera mCamera;
  Camera.Parameters mParameters;
  Activity mActivity;
  boolean hasCamera = false;
  String [] settings={"vibrator","led","bright","wifi","cpu","blue"};
  String [] healthArray={"Unknown","Unknown","Good","OverHeat","Dead","Over Voltage","Unspecified Failue","COLD"};
  public static Boolean []values={false,false,false,false,false,false};
  private final static String TAG = "PreviewSurface";
  private SharedPreferences sharedPreferences;
  private static boolean wasWifiOn=false;
  /** The view to show the ad. */
  private AdView adView;

  /* Your ad unit id. Replace with your actual ad unit id. */
  private static final String AD_UNIT_ID = "ca-app-pub-9744805869727773/5537607041";

  
  private static int[]  check= {
	    R.id.vibrations,
	    R.id.led, 
	    R.id.bright,
	    R.id.wifi,
	    R.id.cpu,
	    R.id.blue, 
	};
  
  
//uses a broadcastreceiver to get the current battery level and set it to the progress bar and textview
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	@Override
		public void onReceive(Context c, Intent i) {	
try{
	if (i.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
			int level = i.getIntExtra("level", 0);
		    ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);//getting the progress bar
		    pb.setProgress(level);//sets the battery level to the progress bar
		    TextView tv = (TextView)findViewById(R.id.batteryLevelTv);//gets the textivew
		    tv.setText("Battery Level: "+Integer.toString(level)+"%");//sets battery information to the textivew
	}
		    int health = i.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
		    batteryHealth=(TextView)findViewById(R.id.batteryHealth);
		    batteryHealth.setText("Battery Health: "+healthArray[health]);	
		    
		    if(values[5]==true && isDraining==true &&  BluetoothAdapter.getDefaultAdapter().isEnabled()==false )
		    {
		    	turnOnBluetooth();
		    }
}
catch (Exception e)
{
}
		}
	 };
  private double mBigNumber = Math.pow(10000000.0D, 10000000.0D);
  private CheckBox blueButton;
  private Button startButton;
  double mCounter = 0.0D;
  private CheckBox cpuButton;
  boolean isDraining;
  private WindowManager.LayoutParams mLP;
  private View mainLayout;
  private float originalBrightness;
  private CheckBox brightButton;
  private CheckBox vibratorButton;
  private boolean isWiFiOn;
  private CheckBox wifiButton;
  private CheckBox flashButton;
  private Button stopButton;
  private TextView batteryHealth;

  private void turnOnCpu()
  { 
    int i = Runtime.getRuntime().availableProcessors();
    this.mCounter = 0.0D;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return;
      }
      Log.d("TAG", "Starting Thread " + j);
      new Thread(new Runnable()
      {
        public void run()
        {
          for (;;)
          {
            if (MainActivity.this.mCounter >= MainActivity.this.mBigNumber) {
              return;
            }
            MainActivity.this.hash(Double.toString(MainActivity.this.mCounter));
          }
        }
      }).start();
    }
  }
  
  private void turnOnFlashLight()
  {
	  try
	  {
	  if (hasCamera) {
          if (mParameters == null) {
                  setParameters();
          }
          mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
          mCamera.setParameters(mParameters);
  } 	 
	  else {
          initCamera();
  }
	  }
	  catch (Exception ex)
	  {
	  }
  }
  
  private void setParameters() {
      mParameters = mCamera.getParameters();

  }
    
  
  private boolean loadSavedPreferences()
	{
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		 try{
	    	  for (int i=0;i<check.length;i++)
	    	  {
	    		  CheckBox temp= (CheckBox)findViewById(check[i]);
	    		  if (values[i]==true)
	    		  {
	    			  temp.setChecked(true);
	    		  }
	    	  }
	          }
	          catch (Exception ex)
	          {
	        	  
	          }		    
			    return sharedPreferences.getBoolean("isOn", false);
	}
  


//saves the service status preference
private void saveSharedPreferences(boolean status)
{
	 sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(this);
	this.editor = sharedPreferences.edit();
	editor.putBoolean("isOn",status);
	editor.commit();
}
  

  public void initCamera() {
      if (!hasCamera) {
                      try {          
                      mCamera = Camera.open();
                      hasCamera = true;
                      } catch (RuntimeException e) {
                              Log.e(TAG, "Could not open Camera"+ e);      
                      hasCamera = false;
                              return;
                      }
              try {
                 mCamera.setPreviewDisplay(mHolder);
              } catch (IOException exception) {
                              Log.e(TAG, "Could not set preview surface");
                  mCamera.release();
                  mCamera = null;
                  hasCamera = false;
                  // TODO: add more exception handling logic here
              }
      }
  }
  
  private void turnOffFlashLight()
  {
	  try
	  {
	  if (hasCamera) {
          mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
          mCamera.setParameters(mParameters);
	  }
	  }
	  catch (Exception ex)
	  {
	  }
  }
  
  private void releaseCamera() {
      if (mCamera != null) {
    	  try
    	  {
          mCamera.release(); // release the camera for other applications
    	  }
    	  catch (Exception ex)
    	  {  
    	  }
      }
  }

  private void hash(String paramString)
  {
	  MessageDigest localMessageDigest1;
    for (;;)
    {
      try
      {
        MessageDigest localMessageDigest2 = MessageDigest.getInstance("SHA-512");
        localMessageDigest1 = localMessageDigest2;
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        localMessageDigest1 = null;
        continue;
      }
      try
      {
        localMessageDigest1.update(paramString.getBytes("iso-8859-1"), 0, paramString.length());
        convertToHex(localMessageDigest1.digest());
        this.mCounter = (1.0D + this.mCounter);
        return;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    }
  }
  
  
  private static String convertToHex(byte[] paramArrayOfByte)
  {
	  StringBuilder localStringBuilder = new StringBuilder();
	  try
	  {
    int i = 0;
    if (i >= paramArrayOfByte.length) {
      return localStringBuilder.toString();
    }
    int j = 0xF & paramArrayOfByte[i] >>> 4;
    int m;
    label94:
    for (int k = 0;; k = m)
    {
      if ((j >= 0) && (j <= 9)) {
        localStringBuilder.append((char)(j + 48));
      }
      for (;;)
      {
        j = 0xF & paramArrayOfByte[i];
        m = k + 1;
        if (k < 1) {
          break label94;
        }
        i++;
        localStringBuilder.append((char)(97 + (j - 10)));
        break;
       
      }
    }
	  }
	  catch (Exception ex)
	  {
		  
	  }
	  
    return (localStringBuilder.toString());
  }
  

  
  private void disableCheckBoxes()
  {
    this.vibratorButton.setEnabled(values[0]);
    this.flashButton.setEnabled(values[1]);
    this.brightButton.setEnabled(values[2]);
    this.wifiButton.setEnabled(values[3]);
    this.cpuButton.setEnabled(values[4]);
    this.blueButton.setEnabled(values[5]);


  }
  
  private void enableCheckboxes()
  {
    this.vibratorButton.setEnabled(true);
    this.brightButton.setEnabled(true);
    this.blueButton.setEnabled(true);
    this.wifiButton.setEnabled(true);
    this.cpuButton.setEnabled(true);
    this.flashButton.setEnabled(true);
  }
  

  private boolean isWifiOn()
  {
    return ((WifiManager)getSystemService("wifi")).isWifiEnabled();
  }
  
 
  private void returnScreenToOriginal()
  {
    this.mLP.screenBrightness = this.originalBrightness;
    getWindow().setAttributes(this.mLP);
    this.mainLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
  }
  
  private void setScreento100()
  {
    this.mLP.screenBrightness = 1.0F;
    getWindow().setAttributes(this.mLP);
    this.mainLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
  }
  
  private void turnOffBluetooth()
  {
    try
    {
      BluetoothAdapter.getDefaultAdapter().disable();
      return;
    }
    catch (SecurityException localSecurityException) {}catch (NullPointerException localNullPointerException) {}
  }
  
  private void turnOffCpu()
  {
    this.mCounter = this.mBigNumber;
  }
  
  private void reset()
  {
    turnOffEverything();
  }
  
  private void turnOffEverything()
  {
    turnOffCpu();  
  }
  
  private void turnOffVibrator()
  {
    ((Vibrator)getSystemService("vibrator")).cancel();
  }
  
  private void turnOffWifi()
  {
    ((WifiManager)getSystemService("wifi")).setWifiEnabled(false);
  }
  
  private void turnOnBluetooth()
  {
    try
    {
      BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	  bluetoothAdapter.enable(); 
        
    }
    catch (NullPointerException localNullPointerException)
    {
      this.mAlertbox.setMessage("Bluetooth can't be used");
      this.mAlertbox.show();
    }
    catch (SecurityException localSecurityException)
    {
      this.mAlertbox.setMessage("Bluetooth can't be used");
      this.mAlertbox.show();
    }
  }
  
  
  private void turnOnVibrator()
  {
    this.mAlertbox.setMessage("Vibrating might hurt your phone");
    this.mAlertbox.show();
    ((Vibrator)getSystemService("vibrator")).vibrate(21600000L);
  }
  
  private void turnOnWifi()
  {
    ((WifiManager)getSystemService("wifi")).setWifiEnabled(true);
  }
  
  private void clearSharedPreferences()
  {
	  sharedPreferences = PreferenceManager
	 			.getDefaultSharedPreferences(this);
	 	editor = sharedPreferences.edit();
	 	editor.putBoolean("isOn",false);
	 	editor.commit();
  }
  
 
  private void loadAds()
  {
	// Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.	s   
	    RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT);
	    //adParams.addRule(RelativeLayout.ALIGN_TOP);
	    adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    adParams.addRule(RelativeLayout.ALIGN_LEFT);
	    ((RelativeLayout)this.mainLayout).addView(adView,adParams);
	    //layout.addView(adView, adsParams);
	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder()
	        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	  
  }
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); 
    

    
    try{
    if (savedInstanceState==null)
    {
    	clearSharedPreferences();
    }
    else
    {
    	 this.isDraining= loadSavedPreferences();
    	
    	 if((values[1]==true) && (this.isDraining==true))
    	    {
    	    	hasCamera=!hasCamera;
    	    }
    }
    wasWifiOn=isWifiOn();
    }
 catch(Exception ex)
 {
	 clearSharedPreferences();
 }
    try
    {

    this.isWiFiOn = isWifiOn();

    registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    this.mLP = getWindow().getAttributes();
    
    this.originalBrightness = this.mLP.screenBrightness;
    this.mAlertbox = new AlertDialog.Builder(this);
    this.mAlertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {}
    });
    setContentView(R.layout.activity_main);
    this.mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
    this.startButton = ((Button)findViewById(R.id.startBtn));
    this.stopButton = ((Button)findViewById(R.id.stopBtn));
    stopButton.setEnabled(false);
    this.brightButton = ((CheckBox)findViewById(R.id.bright));
    this.blueButton = ((CheckBox)findViewById(R.id.blue));
    this.vibratorButton = ((CheckBox)findViewById(R.id.vibrations));
    this.wifiButton = ((CheckBox)findViewById(R.id.wifi));
    this.cpuButton = ((CheckBox)findViewById(R.id.cpu));
    this.flashButton = ((CheckBox)findViewById(R.id.led));
    loadAds();
    
    if (isDraining==true)
    {
    	MainActivity.this.disableCheckBoxes();
    	checkSettings();
    }
    initCamera();
    if (hasCamera==false)
    {
    	this.flashButton.setEnabled(false);
    }
    
    }
    catch (Exception ex)
    {
    }

    this.vibratorButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
  	   @Override
  	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
  		   if(vibratorButton.isChecked())
  				   {
  			   			values[0]=true;
  				   }
  		   else
  		   {
  			   values[0]=false;
  		   }
  	   }
  });
    
    this.flashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

   	   @Override
   	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
   		 if(flashButton.isChecked()==true)
		   {
   			
	   			values[1]=true;
	   		
		   }
   		else
		   {
   			
			   values[1]=false;
		   }

   	   }
   });
    
    this.brightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

    	   @Override
    	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
    		   if(brightButton.isChecked())
				   {
			   			values[2]=true;
				   }
    		   else
      		   {
      			   values[2]=false;
      		   }

    	   }
    });
  
    this.wifiButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

 	   @Override
 	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
 		   
 		  if(wifiButton.isChecked())
			   {
		   			values[3]=true;
			   }
 		 else
		   {
			   values[3]=false;
		   }

 	   }
 });

    this.cpuButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

 	   @Override
 	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
 		   
 		  if(cpuButton.isChecked())
			   {
		   			values[4]=true;
			   }
 		 else
		   {
			   values[4]=false;
		   }

 	   }
 });

 
    this.blueButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

  	   @Override
  	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
  		 if(blueButton.isChecked())
		   {
	   			values[5]=true;
		   }
  		else
		   {
			   values[5]=false;
		   }

  	   }
  });

    this.startButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
    	  checkSettings();
          
        return;
      }
    });
    
    this.stopButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
    	  startButton.setEnabled(true);
          stopButton.setEnabled(false);
    	  MainActivity.this.isDraining = false;
          MainActivity.this.reset();
          MainActivity.this.turnOffVibrator();
          MainActivity.this.turnOffFlashLight();
          MainActivity.this.turnOffBluetooth();
          if(wasWifiOn==false)
          {
        	  turnOffWifi();
          }
          returnScreenToOriginal();
          enableCheckboxes();
      }
    });
    }
  
  private void checkSettings()
  { 
      MainActivity.this.isDraining = true;
      startButton.setEnabled(false);
      stopButton.setEnabled(true);
    if (values[0]) {
    MainActivity.this.turnOnVibrator();
  }
    
  if (values[1]) {
	  
	  MainActivity.this.turnOnFlashLight();
	  
  }
      if (values[2]==true)
      {
    	  MainActivity.this.setScreento100();
      }
      
      
    if (values[3]==true) {
	  MainActivity.this.isWiFiOn = MainActivity.this.isWifiOn();
	  if(MainActivity.this.isWiFiOn==true)
	  {
	  }
	  else
	  {
		  MainActivity.this.turnOnWifi();
	  }
    }
    
  if (values[4]) {
  MainActivity.this.turnOnCpu();
}
  
if (values[5]) {

	
MainActivity.this.turnOnBluetooth();
}
      saveSharedPreferences(true);
      MainActivity.this.disableCheckBoxes();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(this.mBatInfoReceiver);
    saveSharedPreferences(isDraining);
    this.isDraining = false;
    releaseCamera();
    turnOffBluetooth();
    if (wasWifiOn==true)
    {
    	((WifiManager)getSystemService("wifi")).setWifiEnabled(true);
    }
    else
    {
    	((WifiManager)getSystemService("wifi")).setWifiEnabled(false);
    }
    
    turnOffEverything();
    turnOffVibrator();
    returnScreenToOriginal();
    // Destroy the AdView.
    if (adView != null) {
      adView.destroy();
    }
  }
  @Override
  public void onPause()
  {
	  if (adView != null) {
	      adView.pause();
	    }
    if((values[1]==true) && (this.isDraining==true))
    {
    	turnOffFlashLight();
    	releaseCamera();
    }
    saveSharedPreferences(isDraining);
    super.onPause();
  }
  
  @Override
  public void onResume()
  {
    super.onResume();
    if (adView != null) {
        adView.resume();
      }
    this.mainLayout.invalidate();
    this.isDraining=loadSavedPreferences();
    if((values[1]==true) && (this.isDraining==true))//ff
    {
    	hasCamera=!hasCamera;
    	initCamera();
    	turnOnFlashLight();
    }
    this.isWiFiOn = isWifiOn();
    reset();
  }
   
  }



  

