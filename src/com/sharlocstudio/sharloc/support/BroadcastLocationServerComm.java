package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class BroadcastLocationServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {
	
	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private Activity homeActivity;
	
	public BroadcastLocationServerComm(Activity activity) {
		homeActivity = activity;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
				}
				Toast.makeText(homeActivity, result.getString("message"), Toast.LENGTH_SHORT).show();
			} else {
				Log.e("login", "json error");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	

	
}
