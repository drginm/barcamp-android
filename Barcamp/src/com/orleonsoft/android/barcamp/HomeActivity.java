package com.orleonsoft.android.barcamp;

/**
 *Archivo: HomeActivity.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TitlePageIndicator;

public class HomeActivity extends FragmentActivity {

	/** Called when the activity is first created. */
	ViewPager pager;
	PagerAdapter adapter;
	TitlePageIndicator titleIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new PagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new PlainFragment());
		adapter.addFragment(new ListSalasFragment());
		adapter.addFragment(new TwitterFeedFragment());
		pager.setAdapter(adapter);

		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		titleIndicator.setViewPager(pager);

	}

	
}
