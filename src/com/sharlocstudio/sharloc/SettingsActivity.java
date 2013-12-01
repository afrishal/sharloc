package com.sharlocstudio.sharloc;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsActivity extends Activity {
	
	ListView menuListView;
	String[] menuList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		menuListView = (ListView) findViewById(R.id.settings_list);
		menuList = getResources().getStringArray(R.array.settings_menu_array);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, menuList);
		
		menuListView.setAdapter(adapter);
		
		menuListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if(position == 0){
					//Buka About
					Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
					startActivity(intent);
				} else if (position == 1){
					//Sign Out
					
				}
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
