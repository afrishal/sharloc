package com.sharlocstudio.sharloc;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class LocateOnMapActivity extends Activity {

	private GoogleMap googleMap;
	private String userName;
	private double latitude;
	private double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_on_map);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userName = extras.getString("userName");
			latitude = Double.parseDouble(extras.getString("latitude"));
			longitude = Double.parseDouble(extras.getString("longitude"));
		}

		try {
			// Loading map
			initilizeMap();
			addMarker(userName, latitude, longitude);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locate_on_map, menu);
		return true;
	}

	private void addMarker(String userName, double latitude, double longitude) {
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(latitude, longitude)).title(userName));
	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

}
