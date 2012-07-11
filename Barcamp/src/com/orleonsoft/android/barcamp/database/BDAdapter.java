package com.orleonsoft.android.barcamp.database;

/**
 * Archivo: BDAdapter.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Esta Clase define metodos para abrir y cerrar conexiones con la base de datos
 * 
 */
public class BDAdapter {

	SQLiteDatabase dataBase; // base de datos
	Context context; // contexto
	BDCreator dbCreator; // helper que crea la base de datos

	public BDAdapter(Context ctx) {
		context = ctx;
		dbCreator = new BDCreator(context);
	}

	/**
	 * Abre la conexion con la base de datos
	 * 
	 * @return BDAdapter
	 */
	public BDAdapter openDataBase() {
		dataBase = dbCreator.getWritableDatabase();
		return this;
	}

	/**
	 * Cierra la conecion con la base de datos
	 */
	public void close() {
		dbCreator.close();
	}

	/**
	 * Consulta los datos de una tabla ordenados por la clausula orderBy
	 * 
	 * @param table
	 *            Nombre de la tabla
	 * @param columns
	 *            Nombres de las columnas que se quieren consultar,pasar null
	 *            para consultar todas las columnas
	 * 
	 *            example: new String[] {"_id", "column1", "column2"}
	 * @param selection
	 *            la setencia where del query
	 * 
	 *            example _id = ? AND column1 = ? AND column2 = ?...OR
	 * @param selectionArgs
	 *            los valores de la sentencia where del query, estos seran
	 *            remplazados por los (?) en el mismo orden en que aparecen
	 * 
	 *            example:new String[] {"value1", "value2", ...}
	 * @param orderBy
	 *            Sentencia orderBy
	 * 
	 *            example: "nombre1_columna" [ASC, DESC], "nombre2_columna"
	 *            [ASC, DESC]
	 * 
	 * @return Un cursor que apunta a los resultados
	 */
	public Cursor consultar(String table, String[] columns, String selection,
			String[] selectionArgs, String orderBy) {
		return dataBase.query(table, columns, selection, selectionArgs, null,
				null, orderBy);
	}

	


}
