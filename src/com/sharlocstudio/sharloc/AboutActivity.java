package com.sharlocstudio.sharloc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AboutActivity extends Activity {
	
	private ImageView obj1;
	private ImageView obj2;
	private TranslateAnimation obj1Slide;
	private TranslateAnimation obj2Slide;
	private int obj1FromX, obj1ToX;
	private final int[] location = {200, 0};
	private ImageView tagline;
	private ImageView developer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		tagline = (ImageView) findViewById(R.id.sharloc_about_tagline);
		developer = (ImageView) findViewById(R.id.sharloc_about_developer);
		
		this.getWindowManager().getDefaultDisplay();
		this.loadView();
		this.doAnimation();
		
		new IntentLauncher().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	
	private void loadView()
	{

		this.loadObj1();
		this.loadObj2();

	}

	private void loadObj2()
	{
		this.obj2 = (ImageView) this.findViewById(R.id.sharloc_about_logo);
		this.obj2.getLocationOnScreen(this.location);
	}

	private void loadObj1()
	{
		this.obj1 = (ImageView) this.findViewById(R.id.sharloc_about_name);
		this.obj1.getLocationOnScreen(this.location);
	}

	
	private void doAnimation()
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		this.obj1Animation(this.obj1FromX, this.obj1ToX, height, this.location[1]);
		this.obj2Animation(0, 0, (-height+100), this.location[1]);
	}	
	
	private void obj2Animation(int animateFromX, int animateToX, int animateFromY, int animateToY)
	{
		this.obj2Slide = new TranslateAnimation(animateFromX, animateToX, animateFromY, animateToY);
		this.obj2Slide.setDuration(1200);
		this.obj2Slide.setFillEnabled(true);
		this.obj2.setAnimation(this.obj2Slide);
	}

	private void obj1Animation(int animateFromX, int animateToX, int animateFromY, int animateToY)
	{
		this.obj1Slide = new TranslateAnimation(animateFromX, animateToX, animateFromY, animateToY);
		this.obj1Slide.setDuration(1200);
		this.obj1Slide.setFillEnabled(true);
		this.obj1.setAnimation(this.obj1Slide);
	}
	
	private class IntentLauncher extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void arg) {
			tagline.setVisibility(View.VISIBLE);
			developer.setVisibility(View.VISIBLE);
		}
		
	}

	

}
