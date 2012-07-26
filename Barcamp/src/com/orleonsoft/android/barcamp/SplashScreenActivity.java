package com.orleonsoft.android.barcamp;

/**
 *Archivo: SplashScreen.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.orleonsoft.android.barcampmed.R;

/**
 * Splash de la aplicacion
 * 
 */
public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent intent = new Intent(SplashScreenActivity.this,
						com.orleonsoft.android.barcamp.HomeActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			};
		}, AppsConstants.SPLASH_DELAY);
	}

}
