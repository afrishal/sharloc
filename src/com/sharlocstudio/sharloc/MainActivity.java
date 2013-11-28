package com.sharlocstudio.sharloc;

import com.sharlocstudio.sharloc.fragments.*;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private DrawerLayout sharlocDrawerLayout; //
	private ListView drawerListView;
	private ActionBarDrawerToggle sharlocBarDrawerToggle; //
	
	private CharSequence barDrawerTitle;
	private CharSequence barTitle;
	private String[] drawerMenuArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//atur nama action bar pertama kali jadi langsung "home"
		getActionBar().setTitle(getResources().getString(R.string.app_open_bar_title));
		
		//Ambil resources dari folder res
		drawerMenuArray = getResources().getStringArray(R.array.drawer_array);
		sharlocDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); //
		drawerListView = (ListView) findViewById(R.id.navigation_drawer);//
		
		//Prepare title for action bar
		barTitle = barDrawerTitle = getTitle();
		
		// set up the drawer's list view with items and click listener
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerMenuArray));
		drawerListView.setOnItemClickListener(new DrawerItemClickListener());
		
		sharlocBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				sharlocDrawerLayout,
				R.drawable.ic_navigation_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				){
			
			public void onDrawerClosed(View view){
				getActionBar().setTitle(barTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
			
			public void onDrawerOpened(View view){
				getActionBar().setTitle(barDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
			
		};
		
		// Set the drawer toggle as the DrawerListener
        sharlocDrawerLayout.setDrawerListener(sharlocBarDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        if (savedInstanceState == null){
        	selectItem(0);
        }
		
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = sharlocDrawerLayout.isDrawerOpen(drawerListView);        
        if (drawerOpen) {
        	//karna si action_add_friend itu punya si friend fragment, dan di halaman pertama aplikasi
        	//belum pernah dimunculin, si menu.findItem(R.id.action_add_friend akan return null, makanya
        	//di bawah ini, cek apakah dia null atau enggak, kalau enggak, berarti kita lagi di halaman
        	//friend fragment, tapi kalau iya, berarti di halaman lainnya
        	if(menu.findItem(R.id.action_add_friend) != null){
        		menu.findItem(R.id.action_add_friend).setVisible(false);
        	}
		}
		return super.onPrepareOptionsMenu(menu);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//apalah ini
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        sharlocBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sharlocBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (sharlocBarDrawerToggle.onOptionsItemSelected(item)) {
        	return true;
        }
        
        // Handle action buttons
        
        return super.onOptionsItemSelected(item);
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			// TODO Auto-generated method 	stub
			selectItem(position);			
		}
    	
    }
    
    //*****************************************************
    //switch fragment mana yang mau ditaro di content_frame
    //*****************************************************
    private void selectItem(int position){
    	//Create a new fragment and specify the page to be show based on position
    	Fragment homeFragment = new HomeFragment();
    	Fragment profileFragment = new ProfileFragment();
    	Fragment nearMeFragment = new NearMeFragment();
    	Fragment friendsFragment = new FriendsFragment();
    	
    	FragmentManager fragmentManager = getFragmentManager();
    	
    	switch(position){
    	case 0:
    		//commit2 fragmentManager-nya
    		fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    		drawerListView.setItemChecked(position, true);
    		setTitle(drawerMenuArray[position]);
    		sharlocDrawerLayout.closeDrawer(drawerListView);
    		break;
    	case 1:
    		fragmentManager.beginTransaction().replace(R.id.content_frame, nearMeFragment).commit();
    		drawerListView.setItemChecked(position, true);
    		setTitle(drawerMenuArray[position]);
    		sharlocDrawerLayout.closeDrawer(drawerListView);
    		break;
    	case 2:
    		fragmentManager.beginTransaction().replace(R.id.content_frame, friendsFragment).commit();
    		drawerListView.setItemChecked(position, true);
    		setTitle(drawerMenuArray[position]);
    		sharlocDrawerLayout.closeDrawer(drawerListView);
    		break;
    	case 3:
    		fragmentManager.beginTransaction().replace(R.id.content_frame, profileFragment).commit();
    		drawerListView.setItemChecked(position, true);
    		setTitle(drawerMenuArray[position]);
    		sharlocDrawerLayout.closeDrawer(drawerListView);
    		break;
    	}
    	
    }
    
    @Override
    public void setTitle(CharSequence title){
    	barTitle = title;
    	getActionBar().setTitle(barTitle);
    }
    
    
    
    
    

}
