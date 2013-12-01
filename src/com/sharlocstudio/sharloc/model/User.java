package com.sharlocstudio.sharloc.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;



public class User implements Comparable<User> {
	
	private String email;
	private String name;
	private String latitude;
	private String longitude;
	private Timestamp lastUpdate;
	public static final String FILE_NAME = "user.xml";
	
	public User() {
		
	}
	
	public User(String email, String name, String latitude, String longitude, Timestamp lastUpdate) {
		this.email = email;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.lastUpdate = lastUpdate;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	
	public void saveProfile(FileOutputStream fos) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer xml = Xml.newSerializer();
		xml.setOutput(fos, "UTF-8");
	    xml.startDocument(null, Boolean.valueOf(true));
	    xml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	    
	    xml.startTag(null, "user");
	    
	    xml.startTag(null, "email");
	    xml.text(email);
	    xml.endTag(null, "email");
	    
	    xml.startTag(null, "name");
	    xml.text(name);
	    xml.endTag(null, "name");
	    
	    xml.startTag(null, "latitude");
	    xml.text(latitude);
	    xml.endTag(null, "latitude");
	    
	    xml.startTag(null, "longitude");
	    xml.text(longitude);
	    xml.endTag(null, "longitude");
	    
	    xml.startTag(null, "lastUpdate");
	    xml.text(lastUpdate.toString());
	    xml.endTag(null, "lastUpdate");
	    
	    xml.endTag(null, "user");
	    
	    xml.endDocument();
	    xml.flush();
	    fos.close();
	}
	
	public static User getProfile(InputStream is) throws XmlPullParserException, IOException {
		String text = null;
		User user = null;
		
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
				if (tagname.equalsIgnoreCase("email")) {
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
		return user;
	}
	
	public static boolean isLoggedIn(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(User another) {
		return this.name.compareToIgnoreCase(another.getName());
	}
	
	

}
