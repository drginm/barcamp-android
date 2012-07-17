package com.orleonsoft.android.barcamp;


/**
 * Archivo: AppsConstants.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

public interface AppsConstants {

	int SPLASH_DELAY = 1500;

	String LOG_TAG = "com.orleonsoft.android.barcamp";

	

	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	String DATE_FORMAT = "yyyy-MM-dd";

	String URL_GET_SALAS = "http://barcampmedellin.org/barcampmed2012/Services/BarCamp.svc/GetPlaces";
	String URL_GET_DESCONFERENCIAS = "http://barcampmedellin.org/barcampmed2012/Services/BarCamp.svc/GetUnconferences";
	
		
	String TWITTER_FEED_BARCAMP="http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=20&q=https://twitter.com/statuses/user_timeline/36675597.rss";
	
	public interface Database{
		
		String NAME_DATABASE = "barcamp.db";
		
		
		String NAME_TABLE_PLACE="place";
		
		String _ID="_id integer";
		
		String PRIMARY_KEY = "PRIMARY KEY";
		String INTEGER_NOT_NULL = "INTEGER NOT NULL";
		String INTEGER1_NOT_NULL = "INTEGER(1) NOT NULL";
	    String DATE_NOT_NULL = " DATE NOT NULL";
	    String TIMESTAMP_NOT_NULL = " TIMESTAMP NOT NULL";
		String TEXT_NOT_NULL = "TEXT NOT NULL";
		String TEXT = "TEXT";
		
	}

}
