package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orleonsoft.android.barcamp.network.AdapterListTweets;
import com.orleonsoft.android.barcamp.network.JSONParser;
import com.orleonsoft.android.barcamp.network.TweetMessage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *Archivo: TwitterFeedActivity.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:12/07/2012
 */

public class TwitterFeedFragment extends Fragment {
	
	private View viewRoot;
	private ListView listTimeLime;
    private TextView labTwitterAccount;
	
	AdapterListTweets adapterListTweets;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.twitter_feed_screen, null, false); 
		
		listTimeLime=(ListView)viewRoot.findViewById(R.id.list_time_line);
		labTwitterAccount=(TextView)viewRoot.findViewById(R.id.lab_twitter_account);
		labTwitterAccount.setText(AppsConstants.TWITTER_ACCOUNT);
		
		if(!Utils.isNetworkAvailable(getActivity().getApplicationContext())){
			Toast.makeText(getActivity().getApplicationContext(), "Error cargando Tuits, no hay conexion a internet", Toast.LENGTH_SHORT).show();
						
		}
		else{
			adapterListTweets= new AdapterListTweets(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, getTimeLine());
			listTimeLime.setAdapter(adapterListTweets);
		}
		
		
		return viewRoot; 
	}
	
	public ArrayList<TweetMessage> getTimeLine(){
		ArrayList<TweetMessage> result=new ArrayList<TweetMessage>();
		 JSONObject object;
		try {
			object = JSONParser.getTweets(AppsConstants.RSS_TO_JSON_SERVICE_URL+AppsConstants.TWITTER_FEED_BARCAMP);
						
			JSONArray arrayTweets= object.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
			
			for (int i = 0; i < arrayTweets.length(); i++) {
				JSONObject auxObject=arrayTweets.getJSONObject(i);
				result.add(new TweetMessage(auxObject.getString("content"), auxObject.getString("publishedDate")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}

}
