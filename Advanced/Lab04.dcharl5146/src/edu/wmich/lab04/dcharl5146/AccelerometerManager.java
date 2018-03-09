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

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

public class AccelerometerManager {
 
	private static Context aContext=null;
	
	
    /** Accuracy configuration */
	private static  float SHAKE_THRESHOLD_GRAVITY = 2.7F;//level of shake
    private static int SHAKE_SLOP_TIME_MS = 1000;//interval between shake
    private static int SHAKE_COUNT_RESET_TIME_MS = 3000;//shake count reset time
 
    private static long mShakeTimestamp;//shake time stamp
    private static int mShakeCount;//shake count
    
    private static Sensor sensor;//used to get availables sensors
    private static SensorManager sensorManager;//access the device sensors
    
    // you could use an OrientationListener array instead
    // if you plans to use more than one listener
    private static AccelerometerListener listener;
 
    /** indicates whether or not Accelerometer Sensor is supported */
    private static Boolean supported;
    /** indicates whether or not Accelerometer Sensor is running */
    private static boolean running = false;
 
    /**
     * Returns true if the manager is listening to orientation changes
     */
    public static boolean isListening() {
        return running;
    }
 
    /**
     * Unregisters listeners
     */
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {}
    }
 
    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    public static boolean isSupported(Context context) {
    	aContext = context;
        if (supported == null) {
            if (aContext != null) {
            	
            	
                sensorManager = (SensorManager) aContext.
                        getSystemService(Context.SENSOR_SERVICE);
                
                // Get all sensors in device
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER);
                
                supported = Boolean.valueOf((sensors.size() > 0));
                
                
                
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }
 
    /**
     * Configure the listener for shaking
     * @param threshold
     *             minimum acceleration variation for considering shaking
     * @param interval
     *             minimum interval between to shake events
     */
    public static void configure(int threshold, int interval) {
        AccelerometerManager.SHAKE_THRESHOLD_GRAVITY = threshold;
        AccelerometerManager.SHAKE_SLOP_TIME_MS= interval;
    }
 
    /**
     * Registers a listener and start listening
     * @param accelerometerListener
     *             callback for accelerometer events
     */
    public static void startListening( AccelerometerListener accelerometerListener ) 
    {
    	
        sensorManager = (SensorManager) aContext.
                getSystemService(Context.SENSOR_SERVICE);
        
        // Take all sensors in device
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);
        
        if (sensors.size() > 0) {
        	
            sensor = sensors.get(0);
            
            // Register Accelerometer Listener
            running = sensorManager.registerListener(
                    sensorEventListener, sensor, 
                    SensorManager.SENSOR_DELAY_GAME);
            
            listener = accelerometerListener;
        }
        
		
    }
 
    /**
     * Configures threshold and interval
     * And registers a listener and start listening
     * @param accelerometerListener
     *             callback for accelerometer events
     * @param threshold
     *             minimum acceleration variation for considering shaking
     * @param interval
     *             minimum interval between to shake events
     */
    public static void startListening(
            AccelerometerListener accelerometerListener, 
            int threshold, int interval) {
        configure(threshold, interval);
        startListening(accelerometerListener);
    }
 
    /**
     * The listener that listen to events from the accelerometer listener
     */
    private static SensorEventListener sensorEventListener = 
        new SensorEventListener() {
    
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
 
        
        public void onSensorChanged(SensorEvent se) {
        	
        	// Gravity components of x, y, and z acceleration
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];

            
            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now ) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now ) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                listener.onShake(mShakeCount);
            }
            
            
            listener.onAccelerationChanged(x, y, z);
        }
 
        
        
    };
 
}

