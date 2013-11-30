package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {

	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private Intent homeIntent;
	private Activity loginActivity;

	public LoginServerComm(Intent intent, Activity activity) {
		homeIntent = intent;
		loginActivity = activity;
	}
	
	

	@Override
	protected void onPreExecute() {
		EditText uEmail = (EditText) loginActivity.findViewById(R.id.login_email);
		EditText uPass = (EditText) loginActivity.findViewById(R.id.login_password);
		Button loginBtn = (Button) loginActivity.findViewById(R.id.login_login);
		Button regBtn = (Button) loginActivity.findViewById(R.id.login_register);
		ProgressBar progress = (ProgressBar) loginActivity.findViewById(R.id.login_progress_bar);
		uEmail.setEnabled(false);
		uPass.setEnabled(false);
		loginBtn.setVisibility(View.INVISIBLE);
		regBtn.setVisibility(View.INVISIBLE);
		progress.setVisibility(View.VISIBLE);
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
					loginActivity.startActivity(homeIntent);
					loginActivity.finish();
				} else {
					// handle if error
					Toast.makeText(loginActivity, result.getString("message"), Toast.LENGTH_SHORT).show();
					
				}
			} else {
				Log.e("login", "json error");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
