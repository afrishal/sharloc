package com.sharlocstudio.sharloc.cards;

import com.sharlocstudio.sharloc.R;

import android.content.Context;
import android.view.MenuItem;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

public class FriendCard extends Card{
	private Context context; //pengganti getActivity().getApplicationContext()
	protected String mTitleHeader; //jadi nama teman
	protected String mTitleMain; //jadi data lokasi akhir
	
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
            }
        });
		
		//Set card title (content)
		setTitle(mTitleMain);
		
		//Card cannot be swiped
		setSwipeable(false);
	}

}
