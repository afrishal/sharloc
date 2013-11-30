package com.sharlocstudio.sharloc;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.cards.FriendCard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendsFragment extends Fragment {
	
	public FriendsFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		setHasOptionsMenu(true); //kasih tau kalo fragmen ini punya options menu sendiri

		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		initCards();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.fragment_friend, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	public void initCards(){
		/*//card nomor 1
		Card card = new Card(getActivity().getApplicationContext(), R.layout.card_friend);
		//Card card = new Card(getActivity().getApplicationContext());
		CardHeader cardHeader = new CardHeader(getActivity().getApplicationContext());
		cardHeader.setTitle("Afrishal Priyandhana");
		card.addCardHeader(cardHeader);
		card.setTitle("At Depok, 21 mins ago");
		CardView cardView = (CardView) getActivity().findViewById(R.id.carddemo);
		cardView.setCard(card);*/
		
		/*//Tes friendcard
		FriendCard aCard = new FriendCard(getActivity().getApplicationContext());
		aCard.setCardHeader("Afrishal Priyandhana");
		aCard.setCardContent("At Depok, 21 mins ago");
		aCard.createCard();
		CardView card2View = (CardView) getActivity().findViewById(R.id.carddemo2);
		card2View.setCard(aCard);*/
		
		
		//card nomor 2
		/*Card card2 = new Card(getActivity().getApplicationContext());
		CardHeader card2Header = new CardHeader(getActivity().getApplicationContext());
		card2Header.setTitle("Yehezkiel Chrisby Gulo");
		card2.addCardHeader(card2Header);
		
		CardView card2View = (CardView) getActivity().findViewById(R.id.carddemo2);
		card2View.setCard(card2);*/
		
		ArrayList<Card> friendCards = new ArrayList<Card>();
		
		FriendCard friend1 = new FriendCard(getActivity().getApplicationContext());
		friend1.setCardHeader("Afrishal Priyandhana");
		friend1.setCardContent("At Depok, 21 mins ago");
		friend1.createCard();
		friendCards.add(friend1);
		
		FriendCard friend2 = new FriendCard(getActivity().getApplicationContext());
		friend2.setCardHeader("Harish Muhammad Nazief");
		friend2.setCardContent("At Bogor, just now");
		friend2.createCard();
		friendCards.add(friend2);
		
		FriendCard friend3 = new FriendCard(getActivity().getApplicationContext());
		friend3.setCardHeader("Nisrina Rahmah");
		friend3.setCardContent("At Cawang, 2 hours ago");
		friend3.createCard();
		friendCards.add(friend3);
		
		FriendCard friend4 = new FriendCard(getActivity().getApplicationContext());
		friend4.setCardHeader("Yehezkiel Chrisby Gulo");
		friend4.setCardContent("At Cawang, 48 mins ago");
		friend4.createCard();
		friendCards.add(friend4);
		
		CardArrayAdapter friendCardArrayAdapter = new CardArrayAdapter(getActivity(),friendCards);
		friendCardArrayAdapter.setInnerViewTypeCount(1);
		
		CardListView friendCardListView = (CardListView) getActivity().findViewById(R.id.friends_fragment_card_list_friends);
		if(friendCardListView != null){
			friendCardListView.setAdapter(friendCardArrayAdapter);
		}
		
	}
	
}
