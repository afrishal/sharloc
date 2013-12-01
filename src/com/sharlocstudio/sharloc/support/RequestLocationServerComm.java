package com.sharlocstudio.sharloc.support;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.FriendsFragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RequestLocationServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {
	
	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private FriendsFragment friendsFragment;
	
	public RequestLocationServerComm(FriendsFragment fragment) {
		friendsFragment = fragment;
	}

	@Override
	protected JSONObject doInBackground(List<NameValuePair>... params) {
		Log.i("onBackground", params[0].toString());
		JSONParser jsonParser = new JSONParser();
		return jsonParser.getJSONFromUrl(serverURL, params[0]);
	}
	
	protected void onPostExecute(JSONObject result) {
		try {
			if (result != null) {
				Log.i("JSON", result.toString());
				if (result.getString("success").equals(String.valueOf(1))) {
					Toast.makeText(friendsFragment.getActivity(), "Location Requested",
							Toast.LENGTH_SHORT).show();
					
				}
			} else {
				Toast.makeText(friendsFragment.getActivity(), "Failed to Connect Server",
						Toast.LENGTH_SHORT).show();
				Log.e("login", "json error");
			}
		} catch (Exception e) {

		}
	}

}
