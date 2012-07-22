package com.orleonsoft.android.barcamp;

/**
 *Archivo: HomeActivity.java
 *Autor:Yesid Lazaro lazaro.yesid@gmail.com / https://twitter.com/ingyesid
 *Fecha:10/07/2012
 */

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.JSONParser;
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
		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);

		adapter = new PagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new ListSalasFragment());
		adapter.addFragment(new TwitterFeedFragment());
<<<<<<< HEAD
		adapter.addFragment(new PhotosFragment());
		
=======
		adapter.addFragment(new PlainFragment());

>>>>>>> f1acce5f8ecbfd352c9c7a89337d0f80e3741ee0
		pager.setAdapter(adapter);
		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		titleIndicator.setViewPager(pager);

		SharedPreferences settings = getSharedPreferences("settings",
				MODE_PRIVATE);

		if (!settings.getBoolean("hayDatosDescargados", false)) {
			new descargarConferenciasTask().execute();
		}

	}

	private class descargarConferenciasTask extends
			AsyncTask<Void, Void, Boolean> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(HomeActivity.this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Descargando datos...");
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			JSONArray salas;
			JSONArray unconferences;
			try {
				salas = JSONParser
						.getJSONArrayFromURL(AppsConstants.URL_GET_SALAS);
				unconferences = JSONParser
						.getJSONArrayFromURL(AppsConstants.URL_GET_DESCONFERENCIAS);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			BDAdapter dbAdapter = new BDAdapter(HomeActivity.this);
			dbAdapter.openDataBase();
			if (salas != null && salas.length() > 0 && unconferences != null
					&& unconferences.length() > 0) {
				// insertar data de las salas en la tabla places
				ContentValues record;
				for (int i = 0; i < salas.length(); i++) {
					record = new ContentValues();
					try {
						record.put("Identifier", salas.getJSONObject(i)
								.getString("_id"));
						record.put("Name",
								salas.getJSONObject(i).getString("Name"));
						record.put("Description", salas.getJSONObject(i)
								.getString("Description"));
						record.put("Image",
								salas.getJSONObject(i).getString("Image"));
						dbAdapter
								.insert(AppsConstants.Database.NAME_TABLE_PLACE,
										record);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				// insertar data de unconferences
				for (int i = 0; i < unconferences.length(); i++) {
					record = new ContentValues();
					try {
						record.put("Identifier", unconferences.getJSONObject(i)
								.getString("Identifier"));
						record.put("Description", unconferences
								.getJSONObject(i).getString("Description"));
						record.put("Name", unconferences.getJSONObject(i)
								.getString("Name"));
						record.put("Keywords", unconferences.getJSONObject(i)
								.getString("Keywords"));
						record.put("place", unconferences.getJSONObject(i)
								.getInt("Place"));
						record.put("Schedule", unconferences.getJSONObject(i)
								.getString("Schedule"));
						record.put("ScheduleId", unconferences.getJSONObject(i)
								.getInt("ScheduleId"));
						record.put("Speakers", unconferences.getJSONObject(i)
								.getString("Speakers"));
						record.put("StartTime", unconferences.getJSONObject(i)
								.getString("StartTime").replace('/', '-'));
						record.put("EndTime", unconferences.getJSONObject(i)
								.getString("EndTime").replace('/', '-'));
						dbAdapter.insert(
								AppsConstants.Database.NAME_TABLE_UNCONFERENCE,
								record);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result) {
				SharedPreferences settings = getSharedPreferences("settings",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("hayDatosDescargados", true);
				editor.commit();
			} else {
				// si no hubo resultado, mostrar mensaje error
			}
		}

	}
}
