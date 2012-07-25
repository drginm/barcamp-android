package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
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

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.Unconference;

public class ListUnconferencesActivity extends ListActivity {

	private LayoutInflater mInflater;
	private ArrayList<Unconference> mListUnconferences;
	private HashMap<Long, Long> mFavoritosMap;

	private UnconferencesEfficientAdapter mListAdapter;
	private long mIdPlace;

	public ListUnconferencesActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unconference_screen);

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFavoritosMap = new HashMap<Long, Long>();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mIdPlace = (extras.getLong(AppsConstants.KEY_ID) != 0) ? extras
					.getLong(AppsConstants.KEY_ID) : -1;
		}
		new ConsultarUnconferencesTask().execute();
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

		Bitmap unStar = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_unstar);
		Bitmap star = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_star);

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
											v.setTag(false);
											((ImageView) v)
													.setImageBitmap(unStar);
											long idUnconference = getItemId(pos);
											if (borrarFavorito(idUnconference)) {
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
											((ImageView) v)
													.setImageBitmap(star);
											long idUnconference = getItemId(pos);
											long idFavorito = insertarFavorito(idUnconference);
											if (idFavorito != -1) {
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
				holder.image.setImageBitmap(esFavorito ? star : unStar);
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
					AppsConstants.Database.NAME_TABLE_UNCONFERENCE, null,
					"Place = ?", new String[] { mIdPlace + "" },
					"StartTime ASC");
			mListUnconferences = new ArrayList<Unconference>();
			if (cursor != null) {
				Cursor favoritas = dbAdapter.consultar(
						AppsConstants.Database.NAME_TABLE_FAVORITE, null, null,
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
		}

	}

	public long insertarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
		dbAdapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("id_unconference", idUnconference);
		long result = dbAdapter.insert(
				AppsConstants.Database.NAME_TABLE_FAVORITE, values);
		dbAdapter.close();
		return result;
	}

	public boolean borrarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(ListUnconferencesActivity.this);
		dbAdapter.openDataBase();
		long result = dbAdapter.delete(
				AppsConstants.Database.NAME_TABLE_FAVORITE,
				"id_unconference = ?", new String[] { "" + idUnconference });
		dbAdapter.close();
		return result != 0;
	}
}
