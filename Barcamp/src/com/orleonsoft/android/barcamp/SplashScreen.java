package com.orleonsoft.android.barcamp;

import android.app.Activity;
import android.os.Bundle;

/**
 *Archivo: SplashScreen.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */


/**
 * Splash de la aplicacion
 *
 */
public class SplashScreen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
	}

}
