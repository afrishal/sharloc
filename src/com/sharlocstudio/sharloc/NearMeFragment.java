package com.sharlocstudio.sharloc;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.R.id;
import com.sharlocstudio.sharloc.R.layout;

import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearMeFragment extends Fragment {

	private MapView mMapView;
	private GoogleMap googleMap;
	private Bundle mBundle;
	private double myLat = 0.0;
	private double myLong = 0.0;
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
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(-6.55939485, 106.77322609)).title(
				"Afrishal Priyandhana"));
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
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(getActivity().LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			myLat = location.getLatitude();
			myLong = location.getLongitude();

			myPosition = new LatLng(myLat, myLong);

			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					myPosition, 16.0f));
		}
	}

	private float getDistance(double lat1, double lat2, double long1,
			double long2) {
		
		Location locationA = new Location("point A");
		locationA.setLatitude(lat1 / 1e6);
		locationA.setLongitude(long1 / 1e6);

		Location locationB = new Location("point B");
		locationB.setLatitude(lat2 / 1e6);
		locationB.setLatitude(long2 / 1e6);

		return locationA.distanceTo(locationB);
	}
}
