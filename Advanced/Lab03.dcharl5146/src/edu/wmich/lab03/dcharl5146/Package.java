package edu.wmich.lab03.dcharl5146;
/* 
 *************************************
 * Programmer: David Charles
 * Class ID: dchar5146
 * Lab 03
 * CIS 4700
 * Spring 2014
 * Due date: 3/28/14
 * Date completed: 3/25/14
 *************************************
 Package Tracker app
 Features:
 TAB 1-Lets you add a package:
	-You can add a package entering a tracking number and a name for the package. Also a carrier is needed, by default is UPS but this can be changed in the settings
 TAB 2-Displays added packages: 
  Displays current added packages labeling them with their tracking number and name.
  Pressing a package will show a dialog with a couple of options:
  	   -Show Complete information: Displays more information about the package such as (Location, carrier, status, estimated delivery and Owner (by default a package has no Owner)
       -Remove this package: Removes currently selected package 
       -Add to calendar: Add an estimated delivery date to the calendar
       -Set an owner using contacts book: You can pick someone from your contacts book and set it as owner of this package
       -Cancel 
-Options button: When pressed it will show a preference activity. In this preference activity you can make some changes such as:
	  -Change your profile: Here you can store information about yourself and it will be stored as long as the app is installed
	  -Carrier: You can change the default carrier which is UPS
      -Background Color: You can change the background color
      -Clear packages list: Will clear the packages list
      -Turn on Bluetooth: Turns on the Bluetooth
      
*The app will load the packages from the database everytime it starts up 
*everytime a package is added, removed or edited it will be refelected in the database
*and will save the preferences set on the preference activity 

Tested on and Galaxy S4
*************************************
 */
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Package {

	private String estimatedDelivery = "";//holds estimated delivery date
	private String location = "";//hols the location
	private String carrier = "";//holds the carrier
	private String status = "";//holds the status
	private String trackingNumber;//holds the tracking number
	private String name = "";//holds the values when parsin the xml
	private String packageName = "";//holds the name
	private String packageOwner = "";//holds the owner

	
	//set and gets method that will be used to set/retrieve specific information about a package
	
	public void setLocation(String location) {
		this.location = location;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public void setEstimated(String estimatedDelivery) {
		this.estimatedDelivery = estimatedDelivery;
	}

	public void setName(String packageName) {
		this.packageName = packageName;
	}

	public void setOwner(String packageOwner) {
		this.packageOwner = packageOwner;
	}

	public String getLocation() {
		return this.location;
	}

	public String getCarrier() {
		return this.carrier;
	}

	public String getStatus() {
		return this.status;
	}

	public String getTrackingNumber() {
		return this.trackingNumber;
	}

	public String getEstimated() {
		return this.estimatedDelivery;
	}

	public String getName() {
		return this.packageName;
	}

	public String getOwner() {
		return this.packageOwner;
	}

	//no parameters constructors needed for the database
	public Package() {

	}

	//constructor with parameters needed to add a package
	public Package(String trackingNumber, String packageName, String carrier,
			XmlPullParser myParser) throws XmlPullParserException, IOException {
		this.trackingNumber = trackingNumber;
		this.packageName = packageName;
		this.carrier = carrier;
		parseXML(myParser);//calls the method that parses the xml

	}

	//this method parses the xml
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		int eventType = parser.getEventType();
		boolean h = false;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				//if there is data 
				if (name.equalsIgnoreCase("product") == true) {
					h = true;//set h to true 
				} else if (h == true) {
					//gets the status, estimated delivery and location
					if (name.equalsIgnoreCase("status") == true) {
						this.status = parser.nextText();
					} else if (name.equalsIgnoreCase("estimated") == true) {
						this.estimatedDelivery = parser.nextText();
					} else if (name.equalsIgnoreCase("location") == true) {
						this.location = parser.nextText();
					}
				}
				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
			}
			eventType = parser.next();
		}

	}

}
