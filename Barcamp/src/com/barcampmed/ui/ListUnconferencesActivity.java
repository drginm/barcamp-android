package com.barcampmed.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.barcampmed.R;
import com.barcampmed.db.BDAdapter;
import com.barcampmed.util.AppConstants;
import com.barcampmed.util.Utils;
import com.barcampmed.ws.Unconference;

public class ListUnconferencesActivity extends SherlockListActivity {

	private LayoutInflater mInflater;
	private ArrayList<Unconference> mListUnconferences;
	private HashMap<Long, Long> mFavoritosMap;
	private TextView mLabNamePlace;
	private UnconferencesEfficientAdapter mListAdapter;
	private long mIdPlace;

	public ListUnconferencesActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unconference_screen);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mLabNamePlace = (TextView) findViewById(R.id.lab_name_place);

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFavoritosMap = new HashMap<Long, Long>();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mIdPlace = (extras.getLong(AppConstants.KEY_ID) != 0) ? extras
					.getLong(AppConstants.KEY_ID) : -1;
			mLabNamePlace.setText(extras.getString("name_place"));
		}
		new ConsultarUnconferencesTask().execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
		new ConsultarFavoritosTask().execute();
	}

	// view holder
	static class ViewHolder {
		TextView nameUnconference;
		TextView speakers;
		ImageView image;
	}

	// adapter list
	private class UnconferencesEfficientAdapter extends BaseAdapter implements
			OnItemClickListener {

		Bitmap unFavorite = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_unfavorite);
		Bitmap favorite = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_favorite);

		public UnconferencesEfficientAdapter() {
		}

		@Override
		public int getCount() {
			return mListUnconferences.size();
		}

		@Override
		public Object getItem(int position) {
			return mListUnconferences.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mListUnconferences.get(position).getIdentifier();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;
			final int pos = position;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.unconference_row, null);
				holder = new ViewHolder();
				holder.nameUnconference = (TextView) convertView
						.findViewById(R.id.lab_name_unconference);
				holder.speakers = (TextView) convertView
						.findViewById(R.id.lab_speakers);
				holder.image = (ImageView) convertView
						.findViewById(R.id.img_favorite);
				holder.image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						if ((Boolean) v.getTag()) {
							ListUnconferencesActivity.this
									.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											long idUnconference = getItemId(pos);
											if (borrarFavorito(idUnconference)) {
												v.setTag(false);
												((ImageView) v)
														.setImageBitmap(unFavorite);
												mFavoritosMap
														.remove(idUnconference);
											}
										}
									});

						} else {
							ListUnconferencesActivity.this
									.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											v.setTag(true);
											long idUnconference = getItemId(pos);
											long idFavorito = insertarFavorito(idUnconference);
											if (idFavorito != -1) {
												((ImageView) v)
														.setImageBitmap(favorite);
												mFavoritosMap.put(
														idUnconference,
														idFavorito);
											}
										}
									});
						}
					}
				});

				// actualiza el estado del boton favorito de cada row
				final boolean esFavorito = mFavoritosMap
						.containsKey(getItemId(pos));
				holder.image.setImageBitmap(esFavorito ? favorite : unFavorite);
				holder.image.setTag(esFavorito);

				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			holder.nameUnconference.setText(mListUnconferences.get(position)
					.getName());
			holder.speakers.setText(mListUnconferences.get(position)
					.getSpeakers());
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent();
			intent.setClass(ListUnconferencesActivity.this,
					UnconferenceDetailActivity.class);
			Unconference u = (Unconference) getItem(position);
			intent.putExtra("_id", u.getIdentifier());
			intent.putExtra("Name", u.getName());
			intent.putExtra("Description", u.getDescription());
			intent.putExtra("Place", u.getPlace());
			intent.putExtra("Keywords", u.getKeywords());
			intent.putExtra("Speakers", u.getSpeakers());
			intent.putExtra("StarTime", u.getStartTime());
			intent.putExtra("EndTiem", u.getEndTime());
			intent.putExtra("ScheduleId", u.getScheduleId());
			intent.putExtra("Schedule", u.getSchedule());
			intent.putExtra("esFavorito", mFavoritosMap.containsKey(id));
			startActivity(intent);
		}
	}

	private class ConsultarUnconferencesTask extends
			AsyncTask<Void, Void, Void> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(ListUnconferencesActivity.this);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Cargando datos...");
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
			dbAdapter.openDataBase();
			Cursor cursor = dbAdapter.consultar(
					AppConstants.Database.NAME_TABLE_UNCONFERENCE, null,
					"Place = ?", new String[] { mIdPlace + "" },
					"StartTime ASC");
			mListUnconferences = new ArrayList<Unconference>();
			if (cursor != null) {
				Cursor favoritas = dbAdapter.consultar(
						AppConstants.Database.NAME_TABLE_FAVORITE, null, null,
						new String[] {}, null);
				if (favoritas != null) {
					if (favoritas.moveToFirst()) {
						do {
							mFavoritosMap.put(favoritas.getLong(1),
									favoritas.getLong(0));
						} while (favoritas.moveToNext());
					}
					favoritas.close();
				}
				if (cursor.moveToFirst()) {
					do {
						Unconference u = new Unconference();
						u.setIdentifier(cursor.getLong(0));
						u.setName(cursor.getString(1));
						u.setDescription(cursor.getString(2));
						u.setPlace(cursor.getLong(3));
						u.setKeywords(cursor.getString(4));
						u.setSpeakers(cursor.getString(5));
						u.setStartTime(cursor.getString(6));
						u.setEndTime(cursor.getString(7));
						u.setScheduleId(cursor.getLong(8));
						u.setSchedule(cursor.getString(9));
						mListUnconferences.add(u);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
			dbAdapter.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			mListAdapter = new UnconferencesEfficientAdapter();
			setListAdapter(mListAdapter);
			getListView().setOnItemClickListener(mListAdapter);
		};

	}

	// consultar los favoritos
	private class ConsultarFavoritosTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
			dbAdapter.openDataBase();
			Cursor cursor = dbAdapter.consultar(
					AppConstants.Database.NAME_TABLE_FAVORITE, null, null,
					new String[] {}, null);
			boolean hayCambios = false;
			if (cursor != null && cursor.getCount() > 0) {
				// verifica si la lista de favoritos en la db ha cambiado con
				// respecto a
				// la lista de favoritos de la vista
				if (cursor.getCount() != mFavoritosMap.size()) {
					hayCambios = true;
					// limpia el mapa de favoritos
					mFavoritosMap.clear();
					if (cursor.moveToFirst()) {
						do {
							mFavoritosMap.put(cursor.getLong(1),
									cursor.getLong(0));
						} while (cursor.moveToNext());
					}

				}
				cursor.close();
			}
			dbAdapter.close();
			return hayCambios;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				if (mListAdapter != null) {
					mListAdapter.notifyDataSetChanged();
				}
			}
		}

	}

	public long insertarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
		dbAdapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("id_unconference", idUnconference);
		long result = dbAdapter.insert(
				AppConstants.Database.NAME_TABLE_FAVORITE, values);
		dbAdapter.close();
		return result;
	}

	public boolean borrarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
		dbAdapter.openDataBase();
		long result = dbAdapter.delete(
				AppConstants.Database.NAME_TABLE_FAVORITE,
				"id_unconference = ?", new String[] { "" + idUnconference });
		dbAdapter.close();
		return result != 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Share")
		.setIcon(R.drawable.ic_action_share)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this,MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		       startActivity(intent);
			return true;
		}

		if("Share".equals(item.getTitle())){
			Utils.share(ListUnconferencesActivity.this, AppConstants.SHARE_SUBJECT, AppConstants.SHARE_MSJ+" "+AppConstants.LINK_PLAY_STORE);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
