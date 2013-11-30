package com.sharlocstudio.sharloc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.sharlocstudio.sharloc.R;
import com.sharlocstudio.sharloc.model.User;
import com.sharlocstudio.sharloc.support.Contents;
import com.sharlocstudio.sharloc.support.QRCodeEncoder;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	public ProfileFragment() {
		//Empty constructor  required for fragment subclasses
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		setHasOptionsMenu(true); //kasih tau kalo fragmen ini punya options menu sendiri
		
		// Get user profile
		User user = null;
		try {
			user = getUserProfile();
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		TextView uName = (TextView) rootView.findViewById(R.id.profile_name);
		TextView uEmail = (TextView) rootView.findViewById(R.id.profile_email);
		uName.setText(user.getName());
		uEmail.setText(user.getEmail());
		String qrInputText = user.getEmail();
		
				  
		//Find screen size
		WindowManager manager = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		int height = point.y;
		int smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 3/4;
		
		//Encode with a QR Code image
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText, 
		null, 
		Contents.Type.TEXT,  
		BarcodeFormat.QR_CODE.toString(), 
		smallerDimension);
		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		    ImageView myImage = (ImageView) rootView.findViewById(R.id.profile_qr_code_image);
		    myImage.setImageBitmap(bitmap);
		 
		} catch (WriterException e) {
		    e.printStackTrace();
		}
		
		return rootView;
	}
	
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.fragment_profile, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	public User getUserProfile() throws XmlPullParserException, IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(new File(
					getActivity().getFilesDir() + "/user.xml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return User.getProfile(is);
	}
	
}
