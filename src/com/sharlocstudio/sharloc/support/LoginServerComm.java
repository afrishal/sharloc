package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.MainActivity;
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
	private Activity loginActivity;
	private EditText uEmail;
	private EditText uPass;
	private Button loginBtn;
	private Button regBtn;
	private ProgressBar progress;

	public LoginServerComm(Activity activity) {
		loginActivity = activity;
	}

	@Override
	protected void onPreExecute() {
		uEmail = (EditText) loginActivity.findViewById(R.id.login_email);
		uPass = (EditText) loginActivity.findViewById(R.id.login_password);
		loginBtn = (Button) loginActivity.findViewById(R.id.login_login);
		regBtn = (Button) loginActivity.findViewById(R.id.login_register);
		progress = (ProgressBar) loginActivity
				.findViewById(R.id.login_progress_bar);
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
		try {
			if (result != null) {
				Log.i("JSON", result.toString());
				if (result.getString("success") != null) {
					String res = result.getString("success");
					if (Integer.parseInt(res) == 1) {
						JSONObject user = result.getJSONObject("user");
						String name = user.getString("name");
						String email = user.getString("email");
						String longitude = user.getString("longitude");
						String latitude = user.getString("latitude");
						String address = user.getString("address");
						String lastUpdate = user.getString("last_update");
						
						Intent homeIntent = new Intent(loginActivity, MainActivity.class);
						homeIntent.putExtra("name", name);
						homeIntent.putExtra("email", email);
						homeIntent.putExtra("longitude", longitude);
						homeIntent.putExtra("latitude", latitude);
						homeIntent.putExtra("address", address);
						homeIntent.putExtra("lastUpdate", lastUpdate);
						loginActivity.startActivity(homeIntent);
						loginActivity.finish();
					} else {
						// handle if error
						uEmail.setEnabled(true);
						uPass.setEnabled(true);
						loginBtn.setVisibility(View.VISIBLE);
						regBtn.setVisibility(View.VISIBLE);
						progress.setVisibility(View.GONE);
						Toast.makeText(loginActivity,
								result.getString("message"), Toast.LENGTH_SHORT)
								.show();

					}
				}
			} else {
				Toast.makeText(loginActivity, "Failed to Connect Server", Toast.LENGTH_SHORT).show();
				Log.e("login", "json error");
			}
		} catch (Exception e) {
			cancel(true);
		}
	}

}
