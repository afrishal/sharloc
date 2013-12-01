package com.sharlocstudio.sharloc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sharlocstudio.sharloc.model.User;
import com.sharlocstudio.sharloc.support.LoginServerComm;
import com.sharlocstudio.sharloc.support.SHAHasher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	private ImageView obj1;
	private ImageView obj2;
	private TranslateAnimation obj1Slide;
	private TranslateAnimation obj2Slide;
	private int obj1FromX, obj1ToX;
	private final int[] location = {200, 0};
	private EditText uEmail;
	private EditText uPass;
	private Button loginBtn;
	private Button regBtn;
	
	public static LoginActivity loginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginActivity = this;
		
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		
		if (User.isLoggedIn(getFilesDir() + "/" + User.FILE_NAME)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		setContentView(R.layout.activity_login);
		uEmail = (EditText) findViewById(R.id.login_email);
		uPass = (EditText) findViewById(R.id.login_password);
		loginBtn = (Button) findViewById(R.id.login_login);
		regBtn = (Button) findViewById(R.id.login_register);
		
		this.getWindowManager().getDefaultDisplay();
		this.loadView();
		this.doAnimation();
		
		new IntentLauncher().execute();

	}
	
	private void loadView()
	{

		this.loadObj1();
		this.loadObj2();

	}

	private void loadObj2()
	{
		this.obj2 = (ImageView) this.findViewById(R.id.sharloc_logo);
		this.obj2.getLocationOnScreen(this.location);
	}

	private void loadObj1()
	{
		this.obj1 = (ImageView) this.findViewById(R.id.sharloc_name);
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
			uEmail.setVisibility(View.VISIBLE);
			uPass.setVisibility(View.VISIBLE);
			loginBtn.setVisibility(View.VISIBLE);
			regBtn.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void login(View view) {
		SHAHasher hasher = new SHAHasher();
		String email = uEmail.getText().toString();
		String password = hasher.hash512(uPass.getText().toString());
		Log.e("password",uPass.getText().toString() + ":" + password);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "login"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		LoginServerComm loginComm = new LoginServerComm(this);
		loginComm.execute(params);
	}
	
	public void register(View view) {
		Intent intent = new Intent(this, RegistrationActivity.class);
		startActivity(intent);
	}

}
