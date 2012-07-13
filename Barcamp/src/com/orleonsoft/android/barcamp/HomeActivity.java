package com.orleonsoft.android.barcamp;

/**
 *Archivo: HomeActivity.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */


import org.json.JSONException;
import org.json.JSONObject;

import com.orleonsoft.android.barcamp.network.JSONParser;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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
		adapter.addFragment(new PlainFragment() );
		adapter.addFragment(new PlainFragment() );
		adapter.addFragment(new TwitterFeedFragment());
		pager.setAdapter(adapter);
		

		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		titleIndicator.setViewPager(pager);
		
		try {
			 JSONObject object =JSONParser.getTweets("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=20&q=https://twitter.com/statuses/user_timeline/36675597.rss");
			 System.out.println(object.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries"));
			 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
