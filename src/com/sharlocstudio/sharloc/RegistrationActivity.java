package com.sharlocstudio.sharloc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.sharlocstudio.sharloc.support.RegisterServerComm;
import com.sharlocstudio.sharloc.support.SHAHasher;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class RegistrationActivity extends Activity {
	
	EditText uName;
	EditText uEmail;
	EditText uPass;
	EditText uPassCnf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		uName = (EditText) findViewById(R.id.registration_name);
		uEmail = (EditText) findViewById(R.id.registration_email);
		uPass = (EditText) findViewById(R.id.registration_password);
		uPassCnf = (EditText) findViewById(R.id.registration_reenter_password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	public void register(View view) {
		SHAHasher hasher = new SHAHasher();
		String name = uName.getText().toString();
		String email = uEmail.getText().toString();
		String password = hasher.hash512(uPass.getText().toString());
		String passConf = hasher.hash512(uPassCnf.getText().toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "register"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("confirm", passConf));
		Intent intent = new Intent(this, MainActivity.class);
		RegisterServerComm regComm = new RegisterServerComm(intent, this);
		regComm.execute(params);
		
	}

}
