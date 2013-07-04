package com.orleonsoft.android.barcamp.fragments;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orleonsoft.android.barcamp.adapters.AdapterListTweets;
import com.orleonsoft.android.barcamp.util.AppConstants;
import com.orleonsoft.android.barcamp.util.Utils;
import com.orleonsoft.android.barcamp.ws.JSONParser;
import com.orleonsoft.android.barcamp.ws.TweetMessage;
import com.orleonsoft.android.barcampmed.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Archivo: TwitterFeedActivity.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:12/07/2012 
 */

public class TwitterFeedFragment extends Fragment {

	private View viewRoot;
	private ListView listTimeLime;
	private TextView labTwitterAccount;

	private AdapterListTweets adapterListTweets;

	ArrayList<TweetMessage> timeLine;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if (!Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
			Toast.makeText(getActivity().getBaseContext(),
					"Error cargando Tuits, no hay conexion a internet",
					Toast.LENGTH_SHORT).show();

			timeLine = new ArrayList<TweetMessage>();

		} else {
			if (timeLine == null) {
				timeLine = getTimeLine();
				
			}
		}

		Log.d(AppConstants.LOG_TAG, "OnCreate Fragment");
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.twitter_feed_screen, null);

		listTimeLime = (ListView) viewRoot.findViewById(R.id.list_time_line);
		labTwitterAccount = (TextView) viewRoot
				.findViewById(R.id.lab_twitter_account);

		labTwitterAccount.setText(AppConstants.TWITTER_ACCOUNT);

		adapterListTweets = new AdapterListTweets(getActivity()
				.getBaseContext(), android.R.layout.simple_list_item_1,
				timeLine);

		listTimeLime.setAdapter(adapterListTweets);

		return viewRoot;
	}

	public ArrayList<TweetMessage> getTimeLine() {
		ArrayList<TweetMessage> result = new ArrayList<TweetMessage>();
		JSONObject object = null;
		try {
			object = JSONParser.getJSONObjectFromURL(AppConstants.RSS_TO_JSON_SERVICE_URL
					+ AppConstants.TWITTER_FEED_BARCAMP);

			JSONArray arrayTweets = object
					.getJSONObject(AppConstants.KEY_RESPONSE)
					.getJSONObject(AppConstants.KEY_FEED)
					.getJSONArray(AppConstants.KEY_ENTRIES);

			for (int i = 0; i < arrayTweets.length(); i++) {
				JSONObject auxObject = arrayTweets.getJSONObject(i);
				result.add(new TweetMessage(auxObject
						.getString(AppConstants.KEY_CONTENT), auxObject
						.getString(AppConstants.KEY_PUBLISH_DATE)));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

}
