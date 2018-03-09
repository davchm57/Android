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
* This java file contains string values that read data from the database.
* 
*  The "setter and getter" for the busstops
*  
*************************************
*/

public class BusStop {
	
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
	//this method can be called to get the name 
	public String getName()
	{
		return this.name;
	}
	
	//method to get the stopid 
	public int getStopId()
	{
		return this.stopId;
	}
	
	//method used to set the lat by passing a string
	public void setLat(String lat){
		
		this.lat = lat;
		
	}
	
	//method used to get the lay that was passed
	public String getLat(){
		
		return this.lat;
	}
	
	//method used to get the longi value
	public void setLongi(String longi){
		
		this.longi = longi;
	}
	
	//method used to get the longi value
	public String getLongi(){
		
		return this.longi;
	}

}