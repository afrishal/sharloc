package com.sharlocstudio.sharloc.cards;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sharlocstudio.sharloc.FriendsFragment;
import com.sharlocstudio.sharloc.LocateOnMapActivity;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.support.RemoveFriendServerComm;
import com.sharlocstudio.sharloc.support.RequestLocationServerComm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

public class FriendCard extends Card{
	
	private Bitmap bitmap;
	private Context context; //pengganti getActivity().getApplicationContext()
	protected String mTitleHeader; //jadi nama teman
	protected String mTitleMain; //jadi data lokasi akhir
	protected String resourceUrlThumbnail;
	protected String friendEmail;
	protected String friendLatitude;
	protected String friendLongitude;
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
	
//	public void setCardThumbnailResUrl(String resourceUrlThumbnail) {
//		this.resourceUrlThumbnail = resourceUrlThumbnail;
//	}
	
	public void setCardImageBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setFragment(FriendsFragment fragment) {
		friendsFragment = fragment;
	}
	
	public void setFriendEmail(String email) {
		friendEmail = email;
	}
	
	public void setFriendLatitude (String latitude) {
		friendLatitude = latitude;
	}
	
	public void setFriendLongitude (String longitude) {
		friendLongitude = longitude;
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
		
		//Harusnya di bawah ini bisa nge-add gambar
		/*CardThumbnail cardThumbnail = new CardThumbnail(context);
		cardThumbnail.setUrlResource(resourceUrlThumbnail);
        addCardThumbnail(cardThumbnail);*/
		
		header.setPopupMenu(R.menu.menu_card_friend, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                //Toast.makeText(getActivity().getApplicationContext(), "Click on card menu" + mTitleHeader +" item=" +  item.getTitle(), Toast.LENGTH_SHORT).show();
            	switch (item.getItemId()){
            	case R.id.menu_friend_show_location:
            		//pass object friend
            		Intent intent = new Intent(context, LocateOnMapActivity.class);
            		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //jangan hapus baris ini, dipake karna mau panggil kelas activity dari kelas non-activity
            		intent.putExtra("userName", mTitleHeader);
            		intent.putExtra("latitude", friendLatitude);
            		intent.putExtra("longitude", friendLongitude);
            		context.startActivity(intent);
            		
            		break;
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
		//setTitle(mTitleMain);
		
		//Card cannot be swiped
		setSwipeable(false);
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		TextView textView = (TextView) view.findViewById(R.id.card_main_inner_simple_title);
		textView.setText(mTitleMain);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.maps_bitmap);
		imageView.setImageResource(R.drawable.logo_developer); //GAMBAR DUMMY, HAPUS AJA NANTI
		imageView.setImageBitmap(bitmap); //GANTI BITMAP DI SINI (set dulu objek bitmap-nya pake method setCardImageBitmap(Bitmap bitmap) di atas)
		
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
