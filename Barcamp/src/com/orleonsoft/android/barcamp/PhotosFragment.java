package com.orleonsoft.android.barcamp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orleonsoft.android.barcamp.network.AdapterListPhotos;
import com.orleonsoft.android.barcamp.network.JSONParser;
import com.orleonsoft.android.barcamp.network.PhotoBarcamp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Archivo: PhotosFragment.java Autor:Yesid Lazaro lazaro.yesid@gmail.com /
 * https://twitter.com/ingyesid Fecha:21/07/2012
 */

public class PhotosFragment extends Fragment {

	private View viewRoot;
	private ListView listPhotos;
	private AdapterListPhotos adapterListPhotos;

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

			photos = new ArrayList<PhotoBarcamp>();
		}

		System.out.println(getListPhotos());
		Log.d(AppsConstants.LOG_TAG, "OnCreate Fragment photos ");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewRoot = inflater.inflate(R.layout.photos_feed_screen, null);

		// listPhotos=(ListView)viewRoot.findViewById(R.id.list_photo);

		// adapterListPhotos= new AdapterListPhotos(getActivity(),
		// android.R.layout.simple_list_item_1,photos);

		// listPhotos.setAdapter(adapterListPhotos);

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

				//JSONObject aux = arrayEntries.getJSONArray(i).getJSONArray(0)
					//	.getJSONObject(0);
				System.out.println(arrayEntries.getJSONArray(i).getJSONArray(0));
			//	System.out.println(aux.length());
				
			//	result.add(new PhotoBarcamp(aux.getString(AppsConstants.KEY_URL), aux.getString(AppsConstants.KEY_TITLE)));
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
