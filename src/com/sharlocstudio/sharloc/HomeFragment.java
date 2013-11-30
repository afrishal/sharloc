package com.sharlocstudio.sharloc;

import java.io.IOException;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.R.id;
import com.sharlocstudio.sharloc.R.layout;
import com.sharlocstudio.sharloc.R.string;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnClickListener {

	private MapView mMapView;
	private GoogleMap googleMap;
	private Bundle mBundle;
	private double myLat = 0.0;
	private double myLong = 0.0;
	private LatLng myPosition = null;
	SupportMapFragment sMapFragment;
	List<Address> list;
	private boolean gpsActive;
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

		// FragmentManager fragmentManager = getFragmentManager();
		// fragmentManager.beginTransaction().replace(R.id.home_map_frame,
		// homeFragment).commit();
		// GoogleMap googleMap = ((MapFragment)
		// getFragmentManager().findFragmentById(R.id.home_map_frame)).getMap();

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
		card.setTitle("Waiting for current location..");
		cardView = (CardView) getActivity().findViewById(
				R.id.card_current_location);
		cardView.setCard(card);
	}

	private String getAddress() {
		getMyLocation();

		try {
			Geocoder geo = new Geocoder(getActivity().getApplicationContext());
			list = geo.getFromLocation(myLat, myLong, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list.size() != 0) {

			return list.get(0).getAddressLine(0) + ", "
					+ list.get(0).getLocality() + ", "
					+ list.get(0).getCountryName();
		}
		return "Unable to get current location";

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_broadcast_location:
			getMyLocation();
			Toast.makeText(getActivity().getApplicationContext(),
					"Your coordinates are: " + myLat + ", " + myLong,
					Toast.LENGTH_SHORT).show();
			

			card.setTitle(getAddress());
			cardView = (CardView) getActivity().findViewById(
					R.id.card_current_location);
			cardView.refreshCard(card);

			break;
		}
	}
}
