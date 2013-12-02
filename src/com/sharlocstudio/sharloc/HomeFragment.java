package com.sharlocstudio.sharloc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.support.BroadcastLocationServerComm;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnClickListener {

	private MapView mMapView;
	private GoogleMap googleMap;
	private Bundle mBundle;
	private double myLat;
	private double myLong;
	private LatLng myPosition;
	SupportMapFragment sMapFragment;
	List<Address> list = new ArrayList<Address>();
	private Card card;
	private CardView cardView;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		mMapView = (MapView) rootView.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(rootView);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setZoomGesturesEnabled(false);
		googleMap.getUiSettings().setAllGesturesEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);

		getMyLocation();

		Button button = (Button) rootView
				.findViewById(R.id.home_broadcast_location);
		button.setOnClickListener((OnClickListener) this);

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

		if (location != null) {

			LatLng myLastPosition = new LatLng(location.getLatitude(),
					location.getLongitude());

			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					myLastPosition, 16.0f));
		}

		if (googleMap.getMyLocation() != null) {
			myLat = googleMap.getMyLocation().getLatitude();
			myLong = googleMap.getMyLocation().getLongitude();

			myPosition = new LatLng(myLat, myLong);

			updateMap(myPosition);
		}

	}

	public void updateMap(LatLng myPosition) {
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition,
				16.0f));

		card.setTitle(getAddress(googleMap.getMyLocation()));
		cardView = (CardView) getActivity().findViewById(
				R.id.card_current_location);
		cardView.refreshCard(card);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initCard();

	}

	public void initCard() {
		// card nomor 1
		// Card card = new
		// Card(getActivity().getApplicationContext(),R.layout.card_friend);
		card = new Card(getActivity().getApplicationContext());
		CardHeader cardHeader = new CardHeader(getActivity()
				.getApplicationContext());
		cardHeader.setTitle(getResources().getString(
				R.string.label_current_location));
		card.addCardHeader(cardHeader);
		card.setTitle("Press Broadcast Location to share your location");
		cardView = (CardView) getActivity().findViewById(
				R.id.card_current_location);
		cardView.setCard(card);
	}

	private String getAddress(Location myPosition) {

		if (googleMap.getMyLocation() != null) {

			myPosition = googleMap.getMyLocation();
		} else {
			myPosition.setLatitude(myLat);
			myPosition.setLongitude(myLong);
		}
		try {
			Geocoder geo = new Geocoder(getActivity().getApplicationContext());
			list = geo.getFromLocation(myPosition.getLatitude(),
					myPosition.getLongitude(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.i("CURRENT_LOC", "location: " + myPosition.getLatitude() + ", "
				+ myPosition.getLongitude());

		Log.i("VARIABLES", "" + myLat + ", " + myLong);

		Log.i("ADDRESS_SIZE", "list.size = " + list.size());

		if (list.size() != 0) {

			return list.get(0).getAddressLine(0) + ", "
					+ list.get(0).getLocality() + ", "
					+ list.get(0).getCountryName();

		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Coordinates set", Toast.LENGTH_SHORT).show();
			return "Unable to get current address";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_broadcast_location:
			getMyLocation();
			BroadcastLocationServerComm broadcast = new BroadcastLocationServerComm(
					getActivity());
			// get email from shared prefs
			SharedPreferences sharedPref = getActivity().getSharedPreferences(
					"userData", 0);
			String email = sharedPref.getString("email", "");

			// broadcast
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "broadcast"));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("longitude", String
					.valueOf(myLong)));
			params.add(new BasicNameValuePair("latitude", String.valueOf(myLat)));
			broadcast.execute(params);

			break;
		}
	}
}
