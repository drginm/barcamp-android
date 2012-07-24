package com.orleonsoft.android.barcamp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UnconferenceDetailsActivity extends Activity {
	
	private TextView labNombre;
	private TextView labDescription;
	private TextView labKeywods;
	private TextView labSpeakes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_unconference_screen);
	}

}
