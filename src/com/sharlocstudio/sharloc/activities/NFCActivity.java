package com.sharlocstudio.sharloc.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sharlocstudio.sharloc.support.AddFriendServerComm;
import com.sharlocstudio.sharloc.support.NfcUtils;
import com.sharlocstudio.sharloc.FriendsFragment;
import com.sharlocstudio.sharloc.MainActivity;
import com.sharlocstudio.sharloc.R;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.Toast;

public class NFCActivity extends Activity implements CreateNdefMessageCallback,
		OnNdefPushCompleteCallback {

	private NfcAdapter mNfcAdapter;
	private static final String MIME_TYPE = "application/com.sharlocstudio.sharloc";
	private static final int MESSAGE_SENT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);

		// Check availability of NFC
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "This Device not support NFC",
					Toast.LENGTH_LONG).show();
		}
		// register callback to set NDEF message
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		// register callback to listen for message-sent succes
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	/** penerima pesan **/
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SENT:
				Toast.makeText(getApplicationContext(), "Request sent!",
						Toast.LENGTH_LONG).show();
				finish();
				break;
			}
		}
	};

	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		// TODO Auto-generated method stub
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {
		// TODO Auto-generated method stub
		String text = getUserEmail();
		NdefMessage msg = new NdefMessage(
				new NdefRecord[] {
						NfcUtils.createRecord(MIME_TYPE, text.getBytes()),
						NdefRecord
								.createApplicationRecord("com.sharlocstudio.sharloc") });
		return msg;
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		// check
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	// parse the NDEF message
	void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// sent one message over beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		String payload = new String(msg.getRecords()[0].getPayload());
		addRelation(payload);
		// Toast.makeText(getApplicationContext(),
		// "Message received over beam : " + payload, Toast.LENGTH_LONG).show();
	}

	private String getUserEmail() {
		// get email from shared prefs
		SharedPreferences sharedPref = getSharedPreferences("userData", 0);
		String email = sharedPref.getString("email", "");
		return email;
	}

	@SuppressWarnings("unchecked")
	private void addRelation(String user) {
		String user1 = getUserEmail();
		String user2 = user;
		AddFriendServerComm addFriend = new AddFriendServerComm(this);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "add_relation"));
		params.add(new BasicNameValuePair("user1", user1));
		params.add(new BasicNameValuePair("user2", user2));
		addFriend.execute(params);
	}

}
