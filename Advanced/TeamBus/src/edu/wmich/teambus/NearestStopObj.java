package edu.wmich.teambus;


/*
*************************************
* Programmer: TeamBus
* Final project
* CIS 4700: Mobile Commerce Development
* Spring 2014
* Due date: 04/21/14
* Date completed: 04/21/14
*************************************
* this is java object used as a setter//getter for the queries that will return the geo coordinates.
* Meaning it will be found in the nearestBusStop and StopMap Java classes
* 
* 
* 
*************************************
*/

public class NearestStopObj {

	
	//declare some private class variables
		private String name="";
		private int stopId;
		private String lat;
		private String longi;

		//this method can be called to set the current name in a Arraylist
		public void setName(String name)
		{
			this.name=name;   //set value of name to the string passed when the method is called
		}
		
		//this method can be called to set the current stop id
		public void setStopId(int stopId)
		{
			this.stopId=stopId;
		}
		
		//can bec called to get the name that was set into the object
		public String getName()
		{
			return this.name;
		}
		
		//can be called to get the stopid loaded into the obj
		public int getStopId()
		{
			return this.stopId;
		}
		
		//can be called to set the lat by passing a string value
		public void setLat(String lat){
			
			this.lat = lat;
			
		}
		
		//can be called to get the lat that was set
		public String getLat(){
			
			return this.lat;
		}
		//can be called to set the longi by passing a a string value
		public void setLongi(String longi){
			
			this.longi = longi;
		}
		
		//can be called to get the longi value
		public String getLongi(){
			
			return this.longi;
		}
	
	
}
