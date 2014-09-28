package com.barcampmed.util;

/**
 * Archivo: AppsConstants.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

public interface AppConstants {

	int SPLASH_DELAY = 1500;

	String LOG_TAG = "com.orleonsoft.android.barcamp";

	String SHARE_MSJ= "Descarga ya la aplicación de @Barcamp_Med y disfruta del contenido de este evento ,";
	String SHARE_SUBJECT = "Barcamp Med 2014";
	String TWITTER_ACCOUNT = "@Barcamp_Med";
	
	String LINK_PLAY_STORE = "https://play.google.com/store/apps/details?id=com.barcampmed";

	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	String DATE_FORMAT = "yyyy-MM-dd";
	String DATE_TIME_API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	String DATE_TIME_SCHEDULE_FORMAT = "hh:mma";

	String URL_GET_PROGRAMACION="http://barcampnet.azurewebsites.net/api/programacion";

	String RSS_TO_JSON_SERVICE_URL = "http://ajax.googleapis.com/ajax/services/feed/load?v=2.0&num=20&q=";

	String KEY_ENTRIES = "entries";
	String KEY_MEDIA_GROUPS = "mediaGroups";
	String KEY_TITLE = "description";
	String KEY_URL = "url";
	String KEY_CONTENTS = "contents";
	String KEY_CONTENT = "content";
	String KEY_RESPONSE = "responseData";
	String KEY_FEED = "feed";
	String KEY_PUBLISH_DATE = "publishedDate";
	String KEY_DESCRIPTION = "description";

	String KEY_ID = "_id";

	public interface Database {

		String NAME_DATABASE = "barcamp.db";

		String NAME_TABLE_PLACE = "place";
		String NAME_TABLE_UNCONFERENCE = "unconference";
		String NAME_TABLE_FAVORITE = "favorite";
		String _ID = "_id integer";

		String PRIMARY_KEY = "PRIMARY KEY";
		String INTEGER_NOT_NULL = "INTEGER NOT NULL";
		String INTEGER1_NOT_NULL = "INTEGER(1) NOT NULL";
		String DATE_NOT_NULL = " DATE NOT NULL";
		String TIMESTAMP_NOT_NULL = " TIMESTAMP NOT NULL";
		String TEXT_NOT_NULL = "TEXT NOT NULL";
		String TEXT = "TEXT";

	}

}
