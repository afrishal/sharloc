package com.sharlocstudio.sharloc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.cards.FriendCard;
import com.sharlocstudio.sharloc.model.Friends;
import com.sharlocstudio.sharloc.model.User;
import com.sharlocstudio.sharloc.support.LoadFriendsServerComm;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FriendsFragment extends Fragment {

	private List<Address> list;

	public static FriendsFragment friendsFragment;

	public FriendsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);
		setHasOptionsMenu(true); // kasih tau kalo fragmen ini punya options
									// menu sendiri

		friendsFragment = this;
		new LoadFriendList().execute();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_friend, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void initCards(ArrayList<User> friendList) {
		// public void initCards(){
		/*
		 * //card nomor 1 Card card = new
		 * Card(getActivity().getApplicationContext(), R.layout.card_friend);
		 * //Card card = new Card(getActivity().getApplicationContext());
		 * CardHeader cardHeader = new
		 * CardHeader(getActivity().getApplicationContext());
		 * cardHeader.setTitle("Afrishal Priyandhana");
		 * card.addCardHeader(cardHeader);
		 * card.setTitle("At Depok, 21 mins ago"); CardView cardView =
		 * (CardView) getActivity().findViewById(R.id.carddemo);
		 * cardView.setCard(card);
		 */

		/*
		 * //Tes friendcard FriendCard aCard = new
		 * FriendCard(getActivity().getApplicationContext());
		 * aCard.setCardHeader("Afrishal Priyandhana");
		 * aCard.setCardContent("At Depok, 21 mins ago"); aCard.createCard();
		 * CardView card2View = (CardView)
		 * getActivity().findViewById(R.id.carddemo2); card2View.setCard(aCard);
		 */

		// card nomor 2
		/*
		 * Card card2 = new Card(getActivity().getApplicationContext());
		 * CardHeader card2Header = new
		 * CardHeader(getActivity().getApplicationContext());
		 * card2Header.setTitle("Yehezkiel Chrisby Gulo");
		 * card2.addCardHeader(card2Header);
		 * 
		 * CardView card2View = (CardView)
		 * getActivity().findViewById(R.id.carddemo2); card2View.setCard(card2);
		 */

		ArrayList<Card> friendCards = new ArrayList<Card>();

		for (User friend : friendList) {
			FriendCard friendCard = new FriendCard(getActivity()
					.getApplicationContext());
			friendCard.setCardHeader(friend.getName());
			friendCard.setFragment(this);
			friendCard.setFriendEmail(friend.getEmail());
			friendCard.setCardContent(getCity(friend.getLatitude(),
					friend.getLongitude())
					+ ", " + getUpdateTime(friend));
			friendCard.setCardImageBitmap(getMapImage(friend)); // SHOW
			friendCard.setFriendLatitude(friend.getLatitude());
			friendCard.setFriendLongitude(friend.getLongitude());
			friendCard.createCard();
			friendCards.add(friendCard);
		}

		CardArrayAdapter friendCardArrayAdapter = new CardArrayAdapter(
				getActivity(), friendCards);
		friendCardArrayAdapter.setInnerViewTypeCount(1);

		CardListView friendCardListView = (CardListView) getActivity()
				.findViewById(R.id.friends_fragment_card_list_friends);
		if (friendCardListView != null) {
			friendCardListView.setAdapter(friendCardArrayAdapter);
		}

	}

	private Bitmap getMapImage(User friend) {
		String latitude = friend.getLatitude();
		String longitude = friend.getLongitude();

		String url = "http://maps.google.com/maps/api/staticmap?center="
				+ latitude + "," + longitude
				+ "&zoom=18&size=800x500&markers=color:blue%7C" + latitude
				+ "," + longitude + "&zoom=18&sensor=false";

		Bitmap map = getBitmapFromURL(url);

		return map;
	}

	public static Bitmap getBitmapFromURL(String src) {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getCity(String lat, String longi) {

		double latitude = Double.parseDouble(lat);
		double longitude = Double.parseDouble(longi);

		try {
			Geocoder geo = new Geocoder(getActivity().getApplicationContext());
			list = geo.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("LOCATION", list.get(0).getLocality());

		if (list.size() != 0) {

			return list.get(0).getLocality();
		}

		return "";

	}

	@SuppressWarnings("unchecked")
	private void loadFriend() {
		// load friend from server
		LoadFriendsServerComm loadFriendSC = new LoadFriendsServerComm(this,
				getActivity());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "load_friend"));
		params.add(new BasicNameValuePair("email", getUserEmail()));
		loadFriendSC.execute(params);
	}

	public String getUserEmail() {
		// get email from shared prefs
		SharedPreferences sharedPref = getActivity().getSharedPreferences(
				"userData", 0);
		String email = sharedPref.getString("email", "");
		return email;
	}

	public void refreshFriendList() {
		loadFriend();
	}

	private String getUpdateTime(User user) {
		Timestamp time = user.getLastUpdate();
		Timestamp now = new Timestamp(new Date().getTime());
		long diff = now.getTime() - time.getTime();
		Log.i("friend time", time.toString());
		Log.i("user time", now.toString());

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000) % 7;
		long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000) % 4;
		long diffMonths = (diff / (7 * 24 * 60 * 1000)) / 30;

		if (diffMonths > 0) {
			return (diffMonths == 1) ? (diffMonths + " Month ago")
					: (diffMonths + " Months ago");
		} else if (diffWeeks > 0) {
			return (diffWeeks == 1) ? (diffWeeks + " Week ago")
					: (diffWeeks + " Week ago");
		} else if (diffDays > 0) {
			return (diffDays == 1) ? (diffDays + " Day ago")
					: (diffDays + " Days ago");
		} else if (diffHours > 0) {
			return (diffHours == 1) ? (diffHours + " Hour ago")
					: (diffHours + " Hours ago");
		} else if (diffMinutes > 0) {
			return (diffMinutes == 1) ? (diffMinutes + " Minute ago")
					: (diffMinutes + " Minutes ago");
		} else if (diffSeconds > 0) {
			return (diffSeconds == 1) ? (diffSeconds + " Second ago")
					: (diffSeconds + " Seconds ago");
		} else {
			return "Now";
		}

	}

	private class LoadFriendList extends AsyncTask<Void, Void, ArrayList<User>> {

		@Override
		protected ArrayList<User> doInBackground(Void... arg0) {
			ArrayList<User> friendList = null;
			InputStream is;
			try {
				is = new BufferedInputStream(new FileInputStream(new File(
						getActivity().getFilesDir() + "/" + Friends.FILE_NAME)));
				friendList = Friends.getFriendList(is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return friendList;
		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			if (result != null) {
				initCards(result);
			}
			super.onPostExecute(result);
		}
		
		
	}

}
