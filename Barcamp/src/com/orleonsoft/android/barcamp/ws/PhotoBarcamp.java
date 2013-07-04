package com.orleonsoft.android.barcamp.ws;
/**
 *Archivo: PhotoBarcamp.java
 *Autor:Yesid Lazaro
 *Fceha:21/07/2012
 */

public class PhotoBarcamp {
	
	private String PhotoURL;
	private String PhotoTitle;
	
	public PhotoBarcamp(String photoURL, String photoTitle) {
		PhotoURL = photoURL;
		PhotoTitle = photoTitle;
		
		
	}
	public String getPhotoURL() {
		return PhotoURL;
	}
	public void setPhotoURL(String photoURL) {
		PhotoURL = photoURL;
	}
	public String getPhotoTitle() {
		return PhotoTitle;
	}
	public void setPhotoTitle(String photoTitle) {
		PhotoTitle = photoTitle;
	}
	@Override
	public String toString() {
		return "PhotoBarcamp [PhotoURL=" + PhotoURL + ", PhotoTitle="
				+ PhotoTitle + "]";
	}
	
	
	

}
