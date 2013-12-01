package com.sharlocstudio.sharloc;

import java.io.File;
import java.util.ArrayList;

import com.sharlocstudio.sharloc.model.Friends;
import com.sharlocstudio.sharloc.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
					logout();
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
	
	public void logout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure?").setTitle("Logout");
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				File userFile = new File(getFilesDir() + "/" + User.FILE_NAME);
				File friendsFile = new File(getFilesDir() + "/"
						+ Friends.FILE_NAME);
				userFile.delete();
				friendsFile.delete();
				Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
				startActivity(intent);
				MainActivity.mainActivity.finish();
				finish();
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
	}

}
