package com.barcampmed.db;

/**
 * Archivo: BDAdapter.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:10/07/2012
 */

import android.content.ContentValues;
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
	
	/**
	 * Borra los datos de una tabla
	 * 
	 * @param table
	 *            Nombre de la tabla
	 * @param where
	 *            Atributos de la sentencia where
	 * 
	 *            example where: _id = ? AND column1 = ? AND column2 = ?...OR
	 * 
	 * @param whereArgs
	 *            Valores a remplazar por los (?) en la sentencia where
	 * 
	 *            example:new String[] {"value1", "value2", ...}
	 * 
	 * 
	 * @return El numero de filas afectadas si una whereClause es pasada, 0 de
	 *         otra forma. Para remover todas las filas y obtener un conteo de
	 *         las filas borradas pase 1 en el parametro whereClause.
	 * 
	 */
	public long delete(String table, String where, String whereArgs[]) {
		return dataBase.delete(table, where, whereArgs);
	}
	
	/**
	 * Inserta una fila en una tabla
	 * 
	 * @param table
	 *            Nombre de la tabla en la cual insertar los datos
	 * @param values
	 *            ContentValues con los datos a insertar en la tabla
	 * 
	 * @return El id de la nueva fila insertada o -1 si algun error ocurre.
	 */
	public long insert(String table, ContentValues values) {
		return dataBase.insert(table, null, values);
	}

	

	


}
