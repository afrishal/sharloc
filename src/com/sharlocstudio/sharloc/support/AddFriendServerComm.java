package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AddFriendServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {

	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private Activity addActivity;

	public AddFriendServerComm(Activity activity) {
		addActivity = activity;
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
					Toast.makeText(addActivity, result.getString("message"),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(addActivity, MainActivity.class);
					addActivity.startActivity(intent);
					addActivity.finish();
				}
			} else {
				Toast.makeText(addActivity, "Failed to Connect Server",
						Toast.LENGTH_SHORT).show();
				Log.e("login", "json error");
			}
		} catch (Exception e) {

		}
	}
}
