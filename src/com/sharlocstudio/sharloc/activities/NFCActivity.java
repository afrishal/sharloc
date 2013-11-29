package com.sharlocstudio.sharloc.activities;

import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.R.layout;
import com.sharlocstudio.sharloc.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NFCActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

}
