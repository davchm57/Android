package edu.wmich.teambus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* Displaying Kalamazoo Metro Transit web site by using WebView class.
*************************************
*/

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity {
	// WebView displays web pages.
	private WebView webView;
	private String stopId; // String for bus stop id.
	private Intent intent ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview); // Initiate 'webview.xml' file.
        intent = getIntent(); 
        // Different bus schedule will be loaded depending on stop id.
        stopId=intent.getStringExtra("stopId"); 
        webView = (WebView) findViewById(R.id.webView);
        // Enable java script execution.
        webView.getSettings().setJavaScriptEnabled(true);
        // Load the web site.
        webView.loadUrl("http://kmetro.org:8081/Minimal/Departures/ForStop?stopId="+stopId);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    
    //this section of makes the actionbar click return to the previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

}