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
* This java file is and object that is used as a "getter and setter"
* for the array list in the BrowseRoutes activity. 
*************************************
*/

public class BusRoute {
	
	//declare some class variable 
	private String name="";
	private String routeId;
	private String time;
	
	//this method can be called to set the current name in a Arraylist
	public void setName(String name)
	{
		this.name=name; //set value of name to the string passed when the method is called
	}
	
	//method for setting the route id
	public void setRouteId(String routeId)
	{
		this.routeId=routeId;  //set value of route id to the value passed into the method
	}
	
	//method for setting the time 
	public void setTime(String time)
	{
		this.time=time;    //set value of time to the value passed into the method
	}
	
	//this method is called to get the current name
	public String getName()
	{
		return this.name;  //return the name 
	}
	
	//this method is called to get the routeID
	public String getRouteId()
	{
		return this.routeId; //return the routeID
	}
	
	////this method is called to get the time
	public String getTime() 
	{
		return this.time;  //return route time
	}

}

