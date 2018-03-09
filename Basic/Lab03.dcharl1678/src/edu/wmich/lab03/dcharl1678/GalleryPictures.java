package edu.wmich.lab03.dcharl1678;

import android.app.Activity;

import android.os.Bundle;
/* 
*************************************
* Programmer: David Charles
* Class ID: dcharl1678
* Lab03
* CIS 2610: Business Mobile Programming
* Fall 2013
* Due date: 11/06/13
* Date completed: 11/04/13
*************************************
This app have a list view that lets you choose different things to do
2 Classes which function is to open a browser and to go a website
2 Classes with large images
1 Class which function is to play music 
1 Class with a gallery
*************************************
*/
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class GalleryPictures extends Activity {

	//array with the travel destinations name
	Integer [] Cities={R.drawable.d1,R.drawable.d2,R.drawable.m1,R.drawable.m2};
	String [] CitiesS={"Punta Cana, Dominican Republic", "Samana, Dominican Republic", "Cancun, Mexico", "Acapulco, Mexico"};
	//creates an array to show (in the toast) the name of the city when it's clicked 
	ImageView imageView1;//creates the imageview
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallerypictures);
		Gallery ga=(Gallery)findViewById(R.id.gallery1);//creates the gallery
		imageView1=(ImageView)findViewById(R.id.cities);//gets the imageview
		ga.setAdapter(new ImageAdapter(this));//sets the adapter
		ga.setOnItemClickListener( new OnItemClickListener() {
		
		//it will show a toast and the selected image
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
			return null;
		}
		
		public long getItemId(int arg0)
		{
			return 0;
		}
		
		public View getView(int arg0, View arg1, ViewGroup arg2)
		{
			ImageView pic=new ImageView(context);//creates an image view object
			pic.setImageResource(Cities[arg0]);//sets the view to the arg0 which could be any of the pictures
			pic.setScaleType(ImageView.ScaleType.FIT_XY);//sets an option to fit xy so the image
			//won't look weird on the screen
			pic.setLayoutParams(new Gallery.LayoutParams(200,175));//resizes the image to 200x175
			//so the gallery will look good, if we resized it to lets say 400*400 it might just look
			//bad
			return pic;
		}
	}

}
