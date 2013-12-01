package com.sharlocstudio.sharloc.cards;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sharlocstudio.sharloc.FriendsFragment;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.support.RemoveFriendServerComm;
import com.sharlocstudio.sharloc.support.RequestLocationServerComm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

public class FriendCard extends Card{
	private Context context; //pengganti getActivity().getApplicationContext()
	protected String mTitleHeader; //jadi nama teman
	protected String mTitleMain; //jadi data lokasi akhir
	protected String friendEmail;
	protected FriendsFragment friendsFragment;
	
	public FriendCard(Context context) {
		super(context, R.layout.card_friend);
		// TODO Auto-generated constructor stub
		mTitleHeader = "Header Title";
		mTitleMain = "Card content.";
		this.context = context;
		//init();
	}
	
	public void setCardHeader(String text) {
		this.mTitleHeader = text;
	}
	
	public void setCardContent(String text) {
		this.mTitleMain = text;
	}
	
	public void setFragment(FriendsFragment fragment) {
		friendsFragment = fragment;
	}
	
	public void setFriendEmail(String email) {
		friendEmail = email;
	}
	
	@Override
	public int getType(){
		return 1;
	}
	
	public void createCard() {
		//Creater header
		CardHeader header = new CardHeader(context);
		
		//Set header title
		header.setTitle(mTitleHeader);
		
		//Add the header
		addCardHeader(header);
		
		header.setPopupMenu(R.menu.menu_card_friend, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                //Toast.makeText(getActivity().getApplicationContext(), "Click on card menu" + mTitleHeader +" item=" +  item.getTitle(), Toast.LENGTH_SHORT).show();
            	switch (item.getItemId()){
            	case R.id.menu_friend_request_location:
            		requestLocation();
            		break;
            	case R.id.menu_friend_delete:
            		AlertDialog.Builder builder = new AlertDialog.Builder(friendsFragment.getActivity());
            		builder.setMessage("Remove "+ mTitleHeader + "?")
            	       .setTitle("Remove Friend");
            		// Add the buttons
            		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int id) {
            				removeFriend();
            			}
            		});
            		builder.setNegativeButton("Cancel",
            				new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog, int id) {
            						// User cancelled the dialog
            					}
            				});
            		AlertDialog dialog = builder.create();
            		dialog.show();
            		break;
            	}
            	
            }
        });
		
		//Set card title (content)
		setTitle(mTitleMain);
		
		//Card cannot be swiped
		setSwipeable(false);
	}
	
	@SuppressWarnings("unchecked")
	private void requestLocation() {
		RequestLocationServerComm reqLocation = new RequestLocationServerComm(friendsFragment);
		String email = friendsFragment.getUserEmail();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "request"));
		params.add(new BasicNameValuePair("requester", email));
		params.add(new BasicNameValuePair("target", friendEmail));
		reqLocation.execute(params);
	}
	
	@SuppressWarnings("unchecked")
	private void removeFriend() {
		RemoveFriendServerComm remFriend = new RemoveFriendServerComm(friendsFragment);
		String email = friendsFragment.getUserEmail();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "delete_relation"));
		params.add(new BasicNameValuePair("user1", email));
		params.add(new BasicNameValuePair("user2", friendEmail));
		remFriend.execute(params);
	}

}
