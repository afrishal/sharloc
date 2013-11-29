package com.sharlocstudio.sharloc.fragments;

import com.sharlocstudio.sharloc.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
	public ProfileFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		setHasOptionsMenu(true); //kasih tau kalo fragmen ini punya options menu sendiri
		
		
		return rootView;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.fragment_profile, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
}
