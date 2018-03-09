package edu.wmich.lab01.dcharl1546;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class ProximityIntentReceiver extends BroadcastReceiver {
	private String message;
	private static final int FM_NOTIFICATION_ID = 0;
	@Override
	public void onReceive(Context arg0, Intent intent) {
     		Bundle extras = intent.getExtras();
     		message=extras.getString("message");
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//default sound notificaiton
        Intent resultIntent = new Intent(arg0, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    arg0,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_CANCEL_CURRENT
		);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		        arg0).setSmallIcon(R.drawable.ic_launcher_icon)
		        .setContentTitle("WMU app")
		        .setContentText("You are near "+message)
		        //when clicked it will go there
		        .setContentIntent(resultPendingIntent)
		        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })//it will vibrate
		        .setLights(Color.BLUE, 3000, 3000)
		        .setSound(sound)//default sound 
		        .setAutoCancel(true);
		
		NotificationManager manager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(FM_NOTIFICATION_ID, mBuilder.build());
	}

}
