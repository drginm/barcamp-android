package com.barcampmed.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.barcampmed.PagerAdapter;
import com.barcampmed.R;
import com.barcampmed.db.BDAdapter;
import com.barcampmed.fragments.AboutFragment;
import com.barcampmed.fragments.ListFavoritesFragment;
import com.barcampmed.fragments.ListSalasFragment;
import com.barcampmed.util.AppConstants;
import com.barcampmed.util.Utils;
import com.barcampmed.ws.JSONParser;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private TitlePageIndicator titleIndicator;
	private ListSalasFragment mListSalasFragment;
	private SimpleDateFormat sourceDateFormat, targetDateFormat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		sourceDateFormat = new SimpleDateFormat(AppConstants.DATE_TIME_API_FORMAT);
		targetDateFormat = new SimpleDateFormat(AppConstants.DATE_TIME_SCHEDULE_FORMAT);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);

		mListSalasFragment = new ListSalasFragment();

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

		mPagerAdapter.addFragment(mListSalasFragment);
		mPagerAdapter.addFragment(new ListFavoritesFragment());
		mPagerAdapter.addFragment(new AboutFragment());

		mViewPager.setAdapter(mPagerAdapter);
		titleIndicator.setViewPager(mViewPager, 0);

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
				dbAdapter.delete(AppConstants.Database.NAME_TABLE_PLACE, null, null);
				dbAdapter.delete(AppConstants.Database.NAME_TABLE_UNCONFERENCE, null, null);

				int unconferenceCounter = 0;
				HashMap<String, Integer> unconferenceMap = new HashMap<String, Integer>();
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
								record = new ContentValues();
								try {
									JSONObject unconference = unconferences.getJSONObject(j);

									String unconferenceId = unconference.getString("id");

									if(unconferenceMap.containsKey(unconferenceId)){
										unconferenceCounter = unconferenceMap.get(unconferenceId) + 1;
										unconferenceMap.put(unconferenceId, unconferenceCounter);
										unconferenceId += "0" + unconferenceCounter;
									}
									else {
										unconferenceMap.put(unconferenceId, 1);
									}
									record.put("_id", unconferenceId);
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

									record.put("StartTime", schedule
											.getString("fechaInicio").replace('/', '-'));
									record.put("EndTime", schedule
											.getString("fechaFin").replace('/', '-'));
									try {
										Date fechaInicio = sourceDateFormat.parse(schedule.getString("fechaInicio"));
										Date fechaFin = sourceDateFormat.parse(schedule.getString("fechaFin"));
										record.put("Schedule", String.format("%s - %s",
												targetDateFormat.format(fechaInicio),
												targetDateFormat.format(fechaFin)));
									} catch (ParseException e) {
										e.printStackTrace();
									}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Refresh")
		.setIcon(R.drawable.ic_action_refresh)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add("Share")
		.setIcon(R.drawable.ic_action_share)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if("Refresh".equals(item.getTitle())){
			new DescargarDataTask().execute();
			return true;
		}
		else if("Share".equals(item.getTitle())){
			Utils.share(MainActivity.this, AppConstants.SHARE_SUBJECT,
					AppConstants.SHARE_MSJ + " " + AppConstants.LINK_PLAY_STORE);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
