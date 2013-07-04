package com.orleonsoft.android.barcamp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.orleonsoft.android.barcampmed.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



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
	

	  public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_TIME_FORMAT);
	    return sdf.format(cal.getTime());

	  }
	  
	  /**
	   * Este metodo retorna un String con la fecha actual en formato YYYY-MM-DD
	   * @return String con fecha
	   */
	  public static String nowDate() {
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		    return sdf.format(cal.getTime());

		  }
	
	  
	 
	    public static void showAlert(Context context, String title, String text) {
	        Builder alertBuilder = new Builder(context);
	        alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
	        alertBuilder.setTitle(title);
	        alertBuilder.setMessage(text);
	        alertBuilder.create().show();
	    }
	 
		public static void share(Context ctx, String subject, String text) {

			 Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, text);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			ctx.startActivity(Intent.createChooser(intent,
					ctx.getString(R.string.tittle_share_app)));

		}
	    
	
	

}
