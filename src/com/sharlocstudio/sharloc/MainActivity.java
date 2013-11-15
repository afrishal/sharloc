package com.sharlocstudio.sharloc;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout sharlocDrawer; //
	private ListView drawerListView;
	private ActionBarDrawerToggle sharlocBarDrawerToggle; //
	
	private CharSequence barDrawerTitle;
	private CharSequence barTitle;
	private String[] drawerMenuArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		barTitle = barDrawerTitle = getTitle();
		drawerMenuArray = getResources().getStringArray(R.array.drawer_array);
		sharlocDrawer = (DrawerLayout) findViewById(R.id.drawer_layout); //
		drawerListView = (ListView) findViewById(R.id.navigation_drawer);//
		
		// set up the drawer's list view with items and click listener
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerMenuArray));
		//--drawerListView.setOnItemClickListener(new DrawerItemClickListener());
		
		sharlocBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				sharlocDrawer,
				R.drawable.ic_navigation_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				){
			
			public void onDrawerClosed(View view){
				getActionBar().setTitle(barTitle);
			}
			
			public void onDrawerOpened(View view){
				getActionBar().setTitle(barDrawerTitle);
			}
			
		};
		
		// Set the drawer toggle as the DrawerListener
        sharlocDrawer.setDrawerListener(sharlocBarDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
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
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	

}
