package com.sharlocstudio.sharloc.fragments;

import com.sharlocstudio.sharloc.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearMeFragment extends Fragment {
	public NearMeFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nearme, container, false);
		
		
		return rootView;
	}
}
