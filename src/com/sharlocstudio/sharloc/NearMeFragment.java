package com.sharlocstudio.sharloc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.model.Friends;
import com.sharlocstudio.sharloc.model.User;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearMeFragment extends Fragment {

	private MapView mMapView;
	private GoogleMap googleMap;
	private Bundle mBundle;
	private double myLat;
	private double myLong;
	private LatLng myPosition = null;
	SupportMapFragment sMapFragment;

	public NearMeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nearme, container,
				false);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		mMapView = (MapView) rootView.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(rootView);

		getMyLocation();

		ArrayList<User> friendList = getFriendList();

		if (friendList != null) {

			ArrayList<User> nearestFriend = getNearestFriend(friendList);

			if (nearestFriend != null) {
				addMarker(nearestFriend);
			}
		}
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;

	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (googleMap == null) {
			googleMap = ((MapView) inflatedView.findViewById(R.id.map))
					.getMap();
			if (googleMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

	}

	private void addMarker(ArrayList<User> nearestFriend) {
		for (int i = 0; i < nearestFriend.size(); i++) {
			double friendLat = Double.parseDouble(nearestFriend.get(i)
					.getLatitude());
			double friendLong = Double.parseDouble(nearestFriend.get(i)
					.getLongitude());

			String name = nearestFriend.get(i).getName();
			String time = getUpdateTime(nearestFriend.get(i));

			googleMap.addMarker(new MarkerOptions().position(
					new LatLng(friendLat, friendLong)).title(name + ", " + time));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	private void getMyLocation() {
		googleMap.setMyLocationEnabled(true);
		getActivity();
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);

		LatLng myLastPosition = new LatLng(location.getLatitude(),
				location.getLongitude());

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				myLastPosition, 16.0f));

		if (googleMap.getMyLocation() != null) {
			myLat = googleMap.getMyLocation().getLatitude();
			myLong = googleMap.getMyLocation().getLongitude();

			myPosition = new LatLng(myLat, myLong);
		}

	}

	private ArrayList<User> getFriendList() {
		InputStream is = null;
		ArrayList<User> friendList = null;

		try {
			is = new BufferedInputStream(new FileInputStream(new File(
					getActivity().getFilesDir() + "/" + Friends.FILE_NAME)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			friendList = Friends.getFriendList(is);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return friendList;
	}

	private ArrayList<User> getNearestFriend(ArrayList<User> friendList) {
		ArrayList<User> nearestFriend = new ArrayList<User>();

		for (int i = 0; i < friendList.size(); i++) {
			
			User friend = friendList.get(i);
			
			double friendLat = Double.parseDouble(friend.getLatitude());
			double friendLong = Double.parseDouble(friend.getLongitude());

			int distance = getDistance(myLat, myLong, friendLat, friendLong);
			
			if (distance <= 1500) {
				nearestFriend.add(friend);
			}

		}
		return nearestFriend;
	}

	private int getDistance(double lat1, double lat2, double long1,
			double long2) {

		Location locationA = new Location("point A");
		locationA.setLatitude(lat1 / 1e6);
		locationA.setLongitude(long1 / 1e6);

		Location locationB = new Location("point B");
		locationB.setLatitude(lat2 / 1e6);
		locationB.setLatitude(long2 / 1e6);

		return (int) Math.round(locationA.distanceTo(locationB));
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
		long diffWeeks = diff / (7 * 24 * 60 * 1000) % 4;
		long diffMonths = (diff / (7 * 24 * 60 * 1000)) / 30;
			
		if (diffMonths > 0) {
			return (diffMonths == 1)?(diffMonths + " Month ago"):(diffMonths + " Months ago");
		} else if (diffWeeks > 0) {
			return (diffWeeks == 1)?(diffWeeks + " Week ago"):(diffWeeks + " Week ago");
		} else if (diffDays > 0) {
			return (diffDays == 1)?(diffDays + " Day ago"):(diffDays + " Days ago");
		} else if (diffHours > 0) {
			return (diffHours == 1)?(diffHours + " Hour ago"):(diffHours + " Hours ago");
		} else if (diffMinutes > 0) {
			return (diffMinutes == 1)?(diffMinutes + " Minute ago"):(diffMinutes + " Minutes ago");
		} else if (diffSeconds > 0) {
			return (diffSeconds == 1)?(diffSeconds + " Second ago"):(diffSeconds + " Seconds ago");
		} else {
			return "Now";
		}
		
	}
}
