package com.sharlocstudio.sharloc;

import java.util.ArrayList;
import java.util.HashMap;

import com.sharlocstudio.sharloc.activities.NFCActivity;
import com.sharlocstudio.sharloc.activities.QRCodeActivity;
import com.sharlocstudio.sharloc.fragments.*;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private DrawerLayout sharlocDrawerLayout; //
	private ListView drawerListView;
	private ActionBarDrawerToggle sharlocBarDrawerToggle; //
	
	private CharSequence barDrawerTitle;
	private CharSequence barTitle;
	private String[] drawerMenuArray;
	private Intent intent;

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
        	
        	if(menu.findItem(R.id.action_edit_profile) != null){
        		menu.findItem(R.id.action_edit_profile).setVisible(false);
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
        switch(item.getItemId()) {
        case R.id.action_settings:
        	Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
        	intent = new Intent(MainActivity.this, RegistrationActivity.class);
    		startActivity(intent);
        	break;
        case R.id.action_add_friend:
        	//Toast.makeText(this, "Add Friend", Toast.LENGTH_LONG).show();
        	createAddFriendDialog();
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void createAddFriendDialog(){
    	//Setting up the list
    	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    	HashMap<String, String> map;
    	
    	map = new HashMap<String, String>();
    	map.put("method_label", getResources().getString(R.string.contact_add_method_qr_code));
    	map.put("method_icon", String.valueOf(R.drawable.ic_qr_code));
    	listItem.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("method_label", getResources().getString(R.string.contact_add_method_nfc));
    	map.put("method_icon", String.valueOf(R.drawable.ic_nfc));
    	listItem.add(map);
    	
    	SimpleAdapter meths = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.dialog_add_friend_method,
    			new String[]{"method_icon", "method_label"}, new int[]{R.id.method_icon, R.id.method_label});
    	
    	
    	//Setting up dialog box
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	
    	builder.setTitle("Choose method").setAdapter(meths, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	//Intent intent;
                // The 'which' argument contains the index position
                // of the selected item
            	switch (which) {
            	case 0:
            		//Toast.makeText(MainActivity.this, "QR Code!", Toast.LENGTH_LONG).show();
            		// flow add temen pake qr code masukin di sini
            		intent = new Intent(MainActivity.this, QRCodeActivity.class);
            		startActivity(intent);
            		break;
            	case 1:
            		//Toast.makeText(MainActivity.this, "NFC!", Toast.LENGTH_LONG).show();
            		// flow add temen pake nfc masukin di sini
            		intent = new Intent(MainActivity.this, NFCActivity.class);
            		startActivity(intent);
            		break;
            		
            	}
            }
    	});
    	
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
    	
    	AlertDialog dialog = builder.create();
    	
    	dialog.show();
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
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
