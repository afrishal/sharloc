package com.sharlocstudio.sharloc.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sharlocstudio.sharloc.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterGCMServerComm extends AsyncTask<String, Void, String> {
	private Activity mainActivity;
	private GoogleCloudMessaging gcm;
	private Context context;
	private String SENDER_ID = "1039035400710";
	private String regId;
	public static final String TAG = "GCM";
	public static final String PROPERTY_REG_ID = "reg_id";
	public static final String PROPERTY_APP_VERSION = "1.0";
	
	public RegisterGCMServerComm(Activity activity, GoogleCloudMessaging gcm, Context context) {
		mainActivity = activity;
		this.gcm = gcm;
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regId = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regId;

			// You should send the registration ID to your server over HTTP,
			// so it can use GCM/HTTP or CCS to send messages to your app.
			// The request to your server should be authenticated if your
			// app
			// is using accounts.
			sendRegistrationIdToBackend(arg0[0], regId);

			// For this demo: we don't need to send it because the device
			// will send upstream messages to a server that echo back the
			// message using the 'from' address in the message.

			// Persist the regID - no need to register again.
			storeRegistrationId(context, regId);
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			// If there is an error, don't just keep trying to register.
			// Require the user to click a button again, or perform
			// exponential back-off.
		}
		return msg;
	}
	
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	
	private int getAppVersion(Context context2) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	private void sendRegistrationIdToBackend(String email, String regId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "registerGCMID"));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("gcmID", regId));
		String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
		JSONParser jsonParser = new JSONParser();
		jsonParser.getJSONFromUrl(serverURL, params);
	}
	
	private SharedPreferences getGCMPreferences(Context context2) {
		return mainActivity.getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
}
