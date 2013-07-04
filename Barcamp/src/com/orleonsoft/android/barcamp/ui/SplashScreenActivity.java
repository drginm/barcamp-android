package com.orleonsoft.android.barcamp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.orleonsoft.android.barcamp.util.AppConstants;
import com.orleonsoft.android.barcampmed.R;

public class SplashScreenActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent intent = new Intent(SplashScreenActivity.this,
						MainActivity.class);

				startActivity(intent);
				finish();
			};
		}, AppConstants.SPLASH_DELAY);
	}

}
