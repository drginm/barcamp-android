package com.orleonsoft.android.barcamp.database;

/**
 * Archivo: BDCreator.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

import com.orleonsoft.android.barcamp.AppsConstants;
import com.orleonsoft.android.barcamp.AppsConstants.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Esta Clase los metodos para crear la base de datos
 * 
 */

public class BDCreator extends SQLiteOpenHelper {
		
	public BDCreator(Context context) {
		super(context, AppsConstants.Database.NAME_DATABASE, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		
		// creamo la tabla places
				bd.execSQL("CREATE TABLE "+Database.NAME_TABLE_PLACE+" (" +
						Database._ID+" "+Database.PRIMARY_KEY+","+
						"Name"+" "+Database.TEXT_NOT_NULL+","+
						"Description"+" "+Database.TEXT+","+
						"Image"+" "+Database.TEXT+						
						")");
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
		

	}
}

