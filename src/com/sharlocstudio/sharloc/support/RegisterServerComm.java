package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RegisterServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {

	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private Intent homeIntent;
	private Activity registerActivity;

	public RegisterServerComm(Intent intent, Activity activity) {
		homeIntent = intent;
		registerActivity = activity;
	}

	@Override
	protected void onPreExecute() {
		
	}

	@Override
	protected JSONObject doInBackground(List<NameValuePair>... params) {
		Log.i("onBackground", params[0].toString());
		JSONParser jsonParser = new JSONParser();
		return jsonParser.getJSONFromUrl(serverURL, params[0]);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		Log.i("JSON", result.toString());
		try {
			if (result.getString("success") != null) {
				String res = result.getString("success");
				if (Integer.parseInt(res) == 1) {
					String message = result.getString("message");
					JSONObject user = result.getJSONObject("user");
					String name = user.getString("name");
					String email = user.getString("email");
					String longitude = user.getString("longitude");
					String latitude = user.getString("latitude");
					String lastUpdate = user.getString("last_update");
					homeIntent.putExtra("name", name);
					homeIntent.putExtra("email", email);
					homeIntent.putExtra("longitude", longitude);
					homeIntent.putExtra("latitude", latitude);
					homeIntent.putExtra("lastUpdate", lastUpdate);
					registerActivity.startActivity(homeIntent);
					registerActivity.finish();
					LoginActivity.loginActivity.finish();
				} else {
					// handle if error
					Toast.makeText(registerActivity,
							result.getString("message"), Toast.LENGTH_SHORT)
							.show();

				}
			} else {
				Log.e("login", "json error");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
