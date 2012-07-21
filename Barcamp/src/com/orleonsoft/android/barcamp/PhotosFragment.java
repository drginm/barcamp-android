package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import com.orleonsoft.android.barcamp.network.TweetMessage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Archivo: TwitterFeedActivity.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:12/07/2012
 */

public class PhotosFragment extends Fragment {

	private View viewRoot;
	
	
	ArrayList<TweetMessage> timeLine;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
			

		} else {
			
		}
		
		Log.d(AppsConstants.LOG_TAG, "OnCreate Fragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.twitter_feed_screen, container); 
		
		return viewRoot; 
	}


}
