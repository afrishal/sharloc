package com.sharlocstudio.sharloc.fragments;

import com.sharlocstudio.sharloc.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendsFragment extends Fragment {
	public FriendsFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		
		
		return rootView;
	}
}
