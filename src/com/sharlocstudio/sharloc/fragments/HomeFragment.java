package com.sharlocstudio.sharloc.fragments;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import com.sharlocstudio.sharloc.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

public class HomeFragment extends Fragment {
	public HomeFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		initCards();
	}
	
	public void initCards(){
		//card nomor 1
		//Card card = new Card(getActivity().getApplicationContext(),R.layout.card_friend);
		Card card = new Card(getActivity().getApplicationContext());
		CardHeader cardHeader = new CardHeader(getActivity().getApplicationContext());
		cardHeader.setTitle(getResources().getString(R.string.label_current_location));
		card.addCardHeader(cardHeader);
		card.setTitle("Jl. Pulo Macan IV No. 10, West Jakarta");
		CardView cardView = (CardView) getActivity().findViewById(R.id.card_current_location);
		cardView.setCard(card);
	}
}
