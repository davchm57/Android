package edu.wmich.lab4.dcharl1678;
/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab04
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/26/13
* Date completed: 11/25/13
*************************************
BookItCheap is a travel agency 
We guarantee the best prices!
Book your trips safe and easy with this app
It has 6 classes
MainActivity
Tab1:Displays an image and talks about BookItCheap
Tab2:Lets the user choose a destination among 6 and a from and to Date
Tab3:Shows a Gridview Image Gallery with the destinations pictures
Tab4:Goes to a website that talks about these destinations 
Tab5:Lets the user entere a location and a destination to book a flight
*************************************
*/
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Tab3 extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab3);
		final String [] destinationsArray={"New York","Paris","Barcelona","London","San Francisco","Sydney"};//holds the destionations name
		//when a picture is clicked it will show this name
		GridView g=(GridView) findViewById(R.id.destinations);//creates a gridview
		//connects the girdview with the pictures and sets a listener
		//when a picture is pressed it will show a toast with a customized message
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemClickListener( new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Toast.makeText(getBaseContext(),  "This is a picture of "+(destinationsArray[arg2]), Toast.LENGTH_SHORT).show();
		}	
		});
}

//references the pictures in the drawable folder
public class ImageAdapter extends BaseAdapter{
	private Context context;
	private Integer[] Destinations={R.drawable.ny,R.drawable.paris,R.drawable.barcelona,R.drawable.london,R.drawable.sf,R.drawable.sydney};
	public ImageAdapter(Context c)
	{
		context=c;
	}
	
	
	public int getCount()
	{
		return Destinations.length;//return how many items are in the array Destinations
	}
	
	public Object getItem(int arg0)
	{
		return null;
	}
	
	public long getItemId(int arg0)
	{
		return 0;
	}
	
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		ImageView pic=new ImageView(context);//creates an imageview object and sends the context as parameter
		pic.setLayoutParams(new GridView.LayoutParams(190, 190));//resizes the pic
		pic.setPadding(5, 5, 5, 5);//sets the padding 
		pic.setImageResource(Destinations[arg0]);
		return pic;//returns the image view object
	}
	}
	}

