package dc.batterycalibrationpro;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
private String [] healthArray={"Unknown","Unknown","Good","OverHeat","Dead","Over Voltage","Unspecified Failue","COLD"};
private View mainLayout;
private TextView batteryHealth;
private TextView batteryStatus;
private TextView batteryLevel;
private TextView batteryTemperature;
private ProgressBar batteryLevelPb;
private Button calibrateButton;
private Button batteryDrain;
private String isCharging="";
private int level;
private int health;
private int temperature;
/** The view to show the ad. */
private AdView adView;

/* Your ad unit id. Replace with your actual ad unit id. */
private static final String AD_UNIT_ID = "ca-app-pub-9744805869727773/9989656241";
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
			public void onReceive(Context context, Intent i) {	
	try{
		if (i.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				level = i.getIntExtra("level", 0);
				batteryLevelPb.setProgress(level);//sets the battery level to the progress bar
			    batteryLevel.setText("Battery Level: "+Integer.toString(level)+"%");//sets battery information to the textivew
		}
		
		int plugged = i.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
        	isCharging="AC Charging";
        } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
        	isCharging="USB Charging";
        } else if (plugged == 0) {
            isCharging="Discharging";
        } else {
            isCharging="---";
        }
		
			    health = i.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
			    batteryHealth.setText("Battery Health: "+healthArray[health]);
			    
			    temperature=i.getIntExtra("temperature", 0) / 10;
			    batteryTemperature.setText("Temperature: "+temperature+" "+"\u00b0C");

			    batteryStatus.setText("Status: "+isCharging);
	}
	catch (Exception e)
	{
	}
			}
		 };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
		batteryLevelPb = (ProgressBar) findViewById(R.id.progressbar);
		batteryLevel=(TextView) findViewById(R.id.batteryLevelTv);
		batteryTemperature=(TextView) findViewById(R.id.batteryTemperature);
		batteryHealth=(TextView) findViewById(R.id.batteryHealthTv);
		batteryStatus=(TextView) findViewById(R.id.batteryStatus);
		calibrateButton=(Button) findViewById(R.id.calibrateBtn);
		calibrateButton.setOnClickListener(this.calibrate);
		batteryDrain=(Button) findViewById(R.id.batteryDrain);
		batteryDrain.setOnClickListener(this.batteryDrainPro);
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		loadAds();
		
	    registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		}
		catch (Exception ex)
		{
			
		}
	}
	
	@Override
	protected void onDestroy()
	  {
		try{
	    unregisterReceiver(this.mBatInfoReceiver);
		}
		catch (Exception ex)
		{
			
		}
	    super.onDestroy();
	  }

	@Override
	protected void onResume()
	{
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		try{
		unregisterReceiver(this.mBatInfoReceiver);
		}
		catch (Exception ex)
		{
			
		}
		super.onPause();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	    private View.OnClickListener calibrate = new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	        AlertDialog.Builder localBuilder = new AlertDialog.Builder(MainActivity.this);
	        if (MainActivity.this.level < 100)
	        {
	          localBuilder.setTitle("Warning!");
	          localBuilder.setMessage("Wait until battery level is 100%!");
	        }
	        if (MainActivity.this.level == 100)
	        {
	          localBuilder.setTitle("Success!");
	          localBuilder.setMessage("Battery is calibrated!\nNow let your phone completely discharge to 0%.");
	        }
	        AlertDialog localAlertDialog = localBuilder.create();
	        localAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
	        {
	          public void onDismiss(DialogInterface paramAnonymous2DialogInterface)
	          {
	            if (MainActivity.this.level == 100) {
	             
	            }
	          }
	        });
	        localAlertDialog.show();
	      }
	    };
	    
	    
	    private View.OnClickListener batteryDrainPro= new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=programator.batterydrainpro"));
	          MainActivity.this.startActivity(localIntent);
	      }
	    };
	    
	    
	    private void loadAds()
	    {
	  	// Create an ad.
	  	    adView = new AdView(this);
	  	    adView.setAdSize(AdSize.BANNER);
	  	    adView.setAdUnitId(AD_UNIT_ID);

	  	    // Add the AdView to the view hierarchy. The view will have no size
	  	    // until the ad is loaded.	   
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
	    
 
}
