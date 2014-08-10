package com.orleonsoft.android.barcamp.ui;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.orleonsoft.android.barcamp.PagerAdapter;
import com.orleonsoft.android.barcamp.db.BDAdapter;
import com.orleonsoft.android.barcamp.fragments.ListFavoritesFragment;
import com.orleonsoft.android.barcamp.fragments.ListSalasFragment;
import com.orleonsoft.android.barcamp.fragments.PhotosFragment;
import com.orleonsoft.android.barcamp.fragments.TwitterFeedFragment;
import com.orleonsoft.android.barcamp.util.AppConstants;
import com.orleonsoft.android.barcamp.util.Utils;
import com.orleonsoft.android.barcamp.ws.JSONParser;
import com.orleonsoft.android.barcampmed.R;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener {

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private TitlePageIndicator titleIndicator;
	private ListSalasFragment mListSalasFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);

		mListSalasFragment = new ListSalasFragment();

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

		mPagerAdapter.addFragment(new ListFavoritesFragment());
		mPagerAdapter.addFragment(mListSalasFragment);
		//mPagerAdapter.addFragment(new TwitterFeedFragment());
		//mPagerAdapter.addFragment(new PhotosFragment());

		mViewPager.setAdapter(mPagerAdapter);
		titleIndicator.setViewPager(mViewPager, 1);

		SharedPreferences settings = getSharedPreferences("settings",
				MODE_PRIVATE);

		if (!settings.getBoolean("hayDatosDescargados", false)) {
			new DescargarDataTask().execute();
		}
	}

	private class DescargarDataTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MainActivity.this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Descargando datos...");
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			JSONArray salas;
			try {
				salas = JSONParser
						.getJSONArrayFromURL(AppConstants.URL_GET_PROGRAMACION);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			BDAdapter dbAdapter = new BDAdapter(MainActivity.this);
			dbAdapter.openDataBase();
			if (salas != null && salas.length() > 0) {
				// insertar data de las salas en la tabla places
				for (int i = 0; i < salas.length(); i++) {
					ContentValues record = new ContentValues();
					try {
						JSONObject sala = salas.getJSONObject(i);
						record.put("_id",
								sala.getString("id"));
						record.put("Name",
								sala.getString("nombre"));
						record.put("Description", sala
								.getString("descripcion"));
						record.put("Image",
								sala.getString("imagen"));
						dbAdapter.insert(
								AppConstants.Database.NAME_TABLE_PLACE, record);
						JSONArray unconferences = sala.getJSONArray("Conferencias");
						if(unconferences != null
								&& unconferences.length() > 0){
							// insertar data de unconferences
							for (int j = 0; j < unconferences.length(); j++) {
								/*ContentValues*/ record = new ContentValues();
								try {
									JSONObject unconference = unconferences.getJSONObject(j);
									record.put("_id", unconference
											.getString("id"));
									record.put("Description", unconference.getString("resumen"));
									record.put("Name", unconference
											.getString("nombre"));
									record.put("Keywords", unconference
											.getString("palabrasClave"));
									record.put("place", unconference
											.getInt("idEspacio"));
									JSONObject schedule = unconference.getJSONObject("horario");
									record.put("ScheduleId", schedule
											.getInt("id"));
									record.put("Speakers", unconference
											.getString("expositores"));
									//referencia
									//twitter
									//"horario":{"id":12,"fechaInicio":"2013-07-27T09:00:00","fechaFin":"2013-07-27T09:30:00"}
									
									record.put("StartTime", schedule
											.getString("fechaInicio").replace('/', '-'));
									record.put("EndTime", schedule
											.getString("fechaFin").replace('/', '-'));
									record.put("Schedule", "09:40am - 10:10am"/*unconferences.getJSONObject(j)
											.getString("Schedule")*/);
									dbAdapter.insert(
											AppConstants.Database.NAME_TABLE_UNCONFERENCE,
											record);
								} catch (JSONException e) {
									e.printStackTrace();
								}
	
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			dbAdapter.close();
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
				mListSalasFragment.cargarDatosSalas();
			} else {
				// si no hubo resultado, mostrar mensaje error
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_action_about:
			startActivity(new Intent(MainActivity.this, AcercaDeActivity.class));
			break;

		case R.id.but_action_share:
			Utils.share(MainActivity.this, AppConstants.SHARE_SUBJECT,
					AppConstants.SHARE_MSJ + " " + AppConstants.LINK_PLAY_STORE);
			break;

		case R.id.but_action_refresh:
			Toast.makeText(MainActivity.this, "Pulso refresh",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}
