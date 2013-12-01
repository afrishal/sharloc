package com.sharlocstudio.sharloc.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class Friends {

	private ArrayList<User> friendList;
	public static final String FILE_NAME = "friends.xml";
	
	public Friends(ArrayList<User> friendList) {
		this.friendList = friendList;
	}
	
	/*
	 * use friendManager.saveFriends(openFileOutput(Friends.FILE_NAME, Context.MODE_APPEND)); when calling
	 */
	public void saveFriends(FileOutputStream fos) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer xml = Xml.newSerializer();
		xml.setOutput(fos, "UTF-8");
	    xml.startDocument(null, Boolean.valueOf(true));
	    xml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	    
	    xml.startTag(null, "friends");
	    
	    for (User user : friendList) {
			xml.startTag(null, "user");
			
			xml.startTag(null, "email");
		    xml.text(user.getEmail());
		    xml.endTag(null, "email");
		    
		    xml.startTag(null, "name");
		    xml.text(user.getName());
		    xml.endTag(null, "name");
		    
		    xml.startTag(null, "latitude");
		    xml.text(user.getLatitude());
		    xml.endTag(null, "latitude");
		    
		    xml.startTag(null, "longitude");
		    xml.text(user.getLongitude());
		    xml.endTag(null, "longitude");
		    
		    xml.startTag(null, "lastUpdate");
		    xml.text(user.getLastUpdate().toString());
		    xml.endTag(null, "lastUpdate");
		    
		    xml.endTag(null, "user");
		}
	    
	    xml.endTag(null, "friends");
	    
	    
	    xml.endDocument();
	    xml.flush();
	    fos.close();
	}
	
	/*
	 * InputStream is = new BufferedInputStream(new FileInputStream(new File(homeActivity.getFilesDir() + "/" + Friends.FILE_NAME)));
	 * ArrayList<User> friendList = Friends.getFriendList(is);
	 */
	
	public static ArrayList<User> getFriendList(InputStream is) throws XmlPullParserException, IOException {
		User user = null;
		String text = null;
		ArrayList<User> friendList = new ArrayList<User>();
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(is, null);
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tagname = parser.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagname.equalsIgnoreCase("user")) {
					// create a new instance of employee
					user = new User();
				}
				break;
			case XmlPullParser.TEXT:
				text = parser.getText();
				break;

			case XmlPullParser.END_TAG:
				if (tagname.equalsIgnoreCase("user")) {
					friendList.add(user);
				} else if (tagname.equalsIgnoreCase("email")) {
					user.setEmail(text);
				} else if (tagname.equalsIgnoreCase("name")) {
					user.setName(text);
				} else if (tagname.equalsIgnoreCase("latitude")) {
					user.setLatitude(text);
				} else if (tagname.equalsIgnoreCase("longitude")) {
					user.setLongitude(text);
				} else if (tagname.equalsIgnoreCase("lastUpdate")) {
					user.setLastUpdate(Timestamp.valueOf(text));
				}
				break;

			default:
				break;
			}
			eventType = parser.next();
		}
		Collections.sort(friendList);
		return friendList;
	}
}
