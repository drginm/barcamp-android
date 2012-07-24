package com.orleonsoft.android.barcamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog.Builder;
import android.content.Context;
/**
 *Archivo: Utils.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  Clase donde estan metodos de utilidad varios
 *  
 */
public class Utils {
	
	
	/**
	 * Este metodo sirve para saber si hay conexion a internet
	 * 
	 * @param context
	 * @return true si hay conexion / false si no hay conexion
	 */
	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	

	/**
	   * Este metodo retorna un String con la fecha actual en formato YYYY-MM-DD 
	   * @return String con fecha
	   */
	  public static String nowDateTime() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(AppsConstants.DATE_TIME_FORMAT);
	    return sdf.format(cal.getTime());

	  }
	  
	  /**
	   * Este metodo retorna un String con la fecha actual en formato YYYY-MM-DD
	   * @return String con fecha
	   */
	  public static String nowDate() {
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(AppsConstants.DATE_FORMAT);
		    return sdf.format(cal.getTime());

		  }
	
	  
	  /**
	     * Display a simple alert dialog with the given text and title.
	     *
	     * @param context
	     *          Android context in which the dialog should be displayed
	     * @param title
	     *          Alert dialog title
	     * @param text
	     *          Alert dialog message
	     */
	    public static void showAlert(Context context, String title, String text) {
	        Builder alertBuilder = new Builder(context);
	        alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
	        alertBuilder.setTitle(title);
	        alertBuilder.setMessage(text);
	        alertBuilder.create().show();
	    }
	    
	
	

}
