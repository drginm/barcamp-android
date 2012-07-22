package com.orleonsoft.android.barcamp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orleonsoft.android.barcamp.network.JSONParser;
import com.orleonsoft.android.barcamp.network.PhotoBarcamp;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Archivo: PhotosFragment.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:21/07/2012
 */

public class PhotosFragment extends Fragment {

	private View viewRoot;
	private ListView listPhotos;
	private AdapterListPhotos adapterListPhotos;
	LayoutInflater mInflater;
	ArrayList<PhotoBarcamp> photos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Utils.isNetworkAvailable(getActivity().getApplicationContext())) {

			Toast.makeText(getActivity().getBaseContext(),
					"Error descando imagenes, no hay conexion a internet",
					Toast.LENGTH_SHORT).show();

			photos = new ArrayList<PhotoBarcamp>();
		} else {

			photos = getListPhotos();
		}

		Log.d(AppsConstants.LOG_TAG, "OnCreate Fragment photos ");

		adapterListPhotos = new AdapterListPhotos();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		viewRoot = inflater.inflate(R.layout.photos_feed_screen, null);

		listPhotos = (ListView) viewRoot.findViewById(R.id.list_photo);

		listPhotos.setAdapter(adapterListPhotos);

		return viewRoot;
	}

	public ArrayList<PhotoBarcamp> getListPhotos() {

		ArrayList<PhotoBarcamp> result = new ArrayList<PhotoBarcamp>();
		JSONObject object;
		try {
			object = JSONParser
					.getJSONObjectFromURL(AppsConstants.RSS_TO_JSON_SERVICE_URL
							+ AppsConstants.PICASA_ALBUM_URL);

			JSONArray arrayEntries = object
					.getJSONObject(AppsConstants.KEY_RESPONSE)
					.getJSONObject(AppsConstants.KEY_FEED)
					.getJSONArray(AppsConstants.KEY_ENTRIES);

			for (int i = 0; i < arrayEntries.length(); i++) {

				JSONObject contents = arrayEntries.getJSONObject(i)
						.getJSONArray(AppsConstants.KEY_MEDIA_GROUPS).getJSONObject(0)
						.getJSONArray(AppsConstants.KEY_CONTENTS).getJSONObject(0);
				result.add(new PhotoBarcamp(contents.getString(AppsConstants.KEY_URL), contents
						.getString(AppsConstants.KEY_DESCRIPTION)));
				System.out.println(contents);
			}

			System.out.println(result);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.reverse(result);

		return result;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mBitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mBitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mBitmap;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	static class ViewHolder {
		TextView text;
		ImageView icon;
	}

	class AdapterListPhotos extends BaseAdapter {

		@Override
		public int getCount() {

			return photos.size();
		}

		@Override
		public PhotoBarcamp getItem(int position) {
			return photos.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.photo_row, parent,
						false);
				holder = new ViewHolder();

				holder.text = (TextView) convertView
						.findViewById(R.id.lab_title_photo);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.img_photo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			new DownloadImageTask(holder.icon).execute(photos.get(position)
					.getPhotoURL());
			holder.text.setText(photos.get(position).getPhotoTitle());

			Log.d(AppsConstants.LOG_TAG, "on create de adapter");
			return convertView;

		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
