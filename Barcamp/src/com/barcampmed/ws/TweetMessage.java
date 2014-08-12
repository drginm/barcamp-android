package com.barcampmed.ws;
/**
 *Archivo: TweetMessage.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:13/07/2012
 */

public class TweetMessage {
	
	private String mesagge;
	private String date;
	
	public TweetMessage(String mesagge, String date) {
		
		this.mesagge = mesagge;
		this.date = date;
	}

	public String getMesagge() {
		return mesagge;
	}

	public void setMesagge(String mesagge) {
		this.mesagge = mesagge;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "TweetMessage [mesagge=" + mesagge + ", date=" + date + "]";
	}
	
	
	
	
	

}
