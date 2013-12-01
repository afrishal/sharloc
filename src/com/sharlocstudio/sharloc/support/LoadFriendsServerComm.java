package com.sharlocstudio.sharloc.support;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sharlocstudio.sharloc.FriendsFragment;
import com.sharlocstudio.sharloc.model.Friends;
import com.sharlocstudio.sharloc.model.User;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoadFriendsServerComm extends
		AsyncTask<List<NameValuePair>, Void, JSONObject> {

	private String serverURL = "http://frishproject.bl.ee/sharlocserver/service.php";
	private Activity homeActivity;
	private FriendsFragment friendsFragment;

	public LoadFriendsServerComm(FriendsFragment fragment, Activity activity) {
		homeActivity = activity;
		friendsFragment = fragment;
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
						JSONArray friends = result.getJSONArray("friends");
						ArrayList<User> friendList = new ArrayList<User>();
						Log.i("friendslength", friends.length() + "");
						for (int i = 0; i < friends.length(); i++) {
							JSONObject friend = friends.getJSONObject(i);
							Log.i("friendloop", friend.toString());
							String name = friend.getString("name");
							String email = friend.getString("email");
							String longitude = friend.getString("longitude");
							String latitude = friend.getString("latitude");
							String lastUpdate = friend.getString("lastUpdate");
							User user = new User(email, name, latitude,
									longitude, Timestamp.valueOf(lastUpdate));
							friendList.add(user);
							Log.i("friendloop", "friend added");
						}
						Log.i("friends", friendList.toString());
						Friends friendManager = new Friends(friendList);
						friendManager.saveFriends(homeActivity.openFileOutput(
								Friends.FILE_NAME, Context.MODE_PRIVATE));
						
						InputStream is = new BufferedInputStream(new FileInputStream(new File(homeActivity.getFilesDir() + "/" + Friends.FILE_NAME)));
						friendList = Friends.getFriendList(is);
						friendsFragment.initCards(friendList);
					} else {
						// handle if error
						Toast.makeText(homeActivity,
								result.getString("message"), Toast.LENGTH_SHORT)
								.show();
					}
				}
			} else {
				Toast.makeText(homeActivity, "Failed to Connect Server",
						Toast.LENGTH_SHORT).show();
				Log.e("loadFriend", "json error");
			}
		} catch (Exception e) {
			cancel(true);
		}
	}

}
