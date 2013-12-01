package com.sharlocstudio.sharloc.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sharlocstudio.sharloc.MainActivity;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.support.AddFriendServerComm;
import com.sharlocstudio.sharloc.support.CameraPreview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Button;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import android.widget.TextView;
import android.graphics.ImageFormat;

/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class QRCodeActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;

	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_code);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_viewfinder);
		preview.addView(mPreview);

		scanText = (TextView) findViewById(R.id.scanText);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mPreview.getHolder().removeCallback(mPreview);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mCamera == null) {
			mCamera = getCameraInstance();

			/* Instance barcode scanner */
			scanner = new ImageScanner();
			scanner.setConfig(0, Config.X_DENSITY, 3);
			scanner.setConfig(0, Config.Y_DENSITY, 3);

			mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_viewfinder);
			preview.addView(mPreview);
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing && mCamera != null)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					// scanText.setText("barcode result " + sym.getData());
					barcodeScanned = true;
					addRelation(sym);
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
	
	private String getUserEmail() {
		// get email from shared prefs
		SharedPreferences sharedPref = getSharedPreferences("userData", 0);
		String email = sharedPref.getString("email", "");
		return email;
	}
	
	@SuppressWarnings("unchecked")
	private void addRelation(Symbol sym) {
		
		String user1 = getUserEmail();
		String user2 = sym.getData();
		AddFriendServerComm addFriend = new AddFriendServerComm(this);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "add_relation"));
		params.add(new BasicNameValuePair("user1", user1));
		params.add(new BasicNameValuePair("user2", user2));
		addFriend.execute(params);
	}
}