package com.orleonsoft.android.barcamp.database;

/**
 * Archivo: BDCreator.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

import com.orleonsoft.android.barcamp.AppsConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Esta Clase los metodos para crear la base de datos
 * 
 */

public class BDCreator extends SQLiteOpenHelper {
		
	public BDCreator(Context context) {
		super(context, AppsConstants.NAME_DATABASE, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		
		
		Log.d(AppsConstants.LOG_TAG, "BD CREATOR --> onCreate()");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
		

	}
}

