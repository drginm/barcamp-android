package com.orleonsoft.android.barcamp.db;

/**
 * Archivo: BDCreator.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

import com.orleonsoft.android.barcamp.util.AppConstants;
import com.orleonsoft.android.barcamp.util.AppConstants.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta Clase los metodos para crear la base de datos
 * 
 */

public class BDCreator extends SQLiteOpenHelper {

	public BDCreator(Context context) {
		super(context, AppConstants.Database.NAME_DATABASE, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase bd) {

		// creamo la tabla places
		bd.execSQL("CREATE TABLE " + Database.NAME_TABLE_PLACE + " ("
				+ Database._ID + " " + Database.PRIMARY_KEY + "," + "Name"
				+ " " + Database.TEXT_NOT_NULL + "," + "Description" + " "
				+ Database.TEXT + "," + "Image" + " " + Database.TEXT + ")");

		// creamo la tabla unconferences
		bd.execSQL("CREATE TABLE " + Database.NAME_TABLE_UNCONFERENCE + " ("
				+ Database._ID + " " + Database.PRIMARY_KEY + "," + "Name"
				+ " " + Database.TEXT_NOT_NULL + "," + "Description" + " "
				+ Database.TEXT_NOT_NULL + "," + "Place" + " "
				+ Database.INTEGER_NOT_NULL + "," + "Keywords" + " "
				+ Database.TEXT_NOT_NULL + "," + "Speakers" + " "
				+ Database.TEXT_NOT_NULL + "," + "StartTime" + " "
				+ Database.TIMESTAMP_NOT_NULL + "," + "EndTime" + " "
				+ Database.TIMESTAMP_NOT_NULL + "," + "ScheduleId" + " "
				+ Database.INTEGER_NOT_NULL + "," + "Schedule" + " "
				+ Database.TEXT_NOT_NULL + ")");

		// creamos la tabla favorites
		bd.execSQL("CREATE TABLE " + Database.NAME_TABLE_FAVORITE + " ("
				+ Database._ID + " " + Database.PRIMARY_KEY + " AUTOINCREMENT, "
				+ "id_unconference" + " " + Database.INTEGER_NOT_NULL + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {

	}
}
