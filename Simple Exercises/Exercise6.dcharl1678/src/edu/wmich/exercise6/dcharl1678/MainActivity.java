/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Exercise 6
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 10/28/13
* Date completed: 10/28/13
*************************************
This App displays a gallery with different great cities images. When you click on one image it shows
a toast saying which city did you click and it shows a bigger picture of the image below 
*************************************
*/
package edu.wmich.exercise6.dcharl1678;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	//array with the city nanmes
	Integer [] Cities={R.drawable.ny,R.drawable.la,R.drawable.chicago,R.drawable.toronto,R.drawable.montreal};
	String [] CitiesS={"New York", "Los Angeles", "Chicago", "Toronto", "Montreal"};
	ImageView imageView1;//creates the imageview
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Gallery ga=(Gallery)findViewById(R.id.gallery1);//creates the gallery
		imageView1=(ImageView)findViewById(R.id.cities);//gets the imageview
		ga.setAdapter(new ImageAdapter(this));//sets the adapter
		ga.setOnItemClickListener( new OnItemClickListener() {
		
		//here it will print a toast and show the selected image
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Toast.makeText(getBaseContext(),  "This is a picture of "+(CitiesS[arg2]), Toast.LENGTH_SHORT).show();
			imageView1.setImageResource(Cities[arg2]);
		}
			
		});
	}
	
	public class ImageAdapter extends BaseAdapter{
		private Context context;
		//constructor
		public ImageAdapter(Context c)
		{
			context=c;//defines the context 
		}
		
		public int getCount(){ 
			return Cities.length;//return the lenght of the cities array
		}
		
		public Object getItem(int arg0)
		{
			return null;//not used
		}
		
		public long getItemId(int arg0)
		{
			return 0;//not used
		}
		
		public View getView(int arg0, View arg1, ViewGroup arg2)
		{
			//Resizes and show the currently selected image
			ImageView pic=new ImageView(context);
			pic.setImageResource(Cities[arg0]);
			pic.setScaleType(ImageView.ScaleType.FIT_XY);
			pic.setLayoutParams(new Gallery.LayoutParams(200,175));
			return pic;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
