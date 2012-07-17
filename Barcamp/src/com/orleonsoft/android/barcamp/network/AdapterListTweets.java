package com.orleonsoft.android.barcamp.network;

/**
 *Archivo: AdapterListCategorias.java
 *@autor:Yesid Lazaro
 *Fecha:2/03/2012
 */

import java.util.ArrayList;

import com.orleonsoft.android.barcamp.R;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Esta Clase define el adaptador de la lista de categorias
 * 
 */

public class AdapterListTweets extends ArrayAdapter<TweetMessage> {

	Context context;
	ArrayList<TweetMessage> tweets;

	public AdapterListTweets(Context context, int resource,
			ArrayList<TweetMessage> elements) {

		super(context, resource, elements);
		this.context = context;
		this.tweets = elements;
		resource = R.layout.tweet_row;

	}

	@Override
	public int getCount() {

		return tweets.size();
	}

	@Override
	public TweetMessage getItem(int position) {
		return tweets.get(position);
	}

	@Override
	public int getPosition(TweetMessage item) {
		return tweets.indexOf(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.tweet_row, null);

		TextView labMessage = (TextView) view
				.findViewById(R.id.lab_tweet_message);
		
		Linkify.addLinks(labMessage, Linkify.WEB_URLS);
		labMessage.setMovementMethod(LinkMovementMethod.getInstance());
		
		
		
		TextView labDateTime = (TextView) view
				.findViewById(R.id.lab_date_time);

		labMessage.setText(tweets.get(position).getMesagge());
		labDateTime.setText(tweets.get(position).getDate());
		

		return view;
	}

}
