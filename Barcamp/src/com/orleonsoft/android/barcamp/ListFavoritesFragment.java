package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.Unconference;
import com.orleonsoft.android.barcampmed.R;

public class ListFavoritesFragment extends Fragment {

	private LayoutInflater mInflater;
	private FavoritesEfficientAdapter mListAdapter;
	private ArrayList<Unconference> mListUnconference;
	private ListView mListViewFavoritos;

	public ListFavoritesFragment() {
		mListUnconference = new ArrayList<Unconference>();
		mListAdapter = new FavoritesEfficientAdapter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater
				.inflate(R.layout.favorites_panel, container, false);
		mListViewFavoritos = (ListView) view.findViewById(android.R.id.list);
		mListViewFavoritos.setAdapter(mListAdapter);
		mListViewFavoritos.setOnItemClickListener(mListAdapter);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		new ConsultarFavoritosTask().execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}

	// view holder
	static class ViewHolder {
		TextView nameUnconference;
		TextView place;
		TextView hour;
	}

	// adapter list
	private class FavoritesEfficientAdapter extends BaseAdapter implements
			OnItemClickListener {

		@Override
		public int getCount() {
			return mListUnconference.size();
		}

		@Override
		public Object getItem(int position) {
			return mListUnconference.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mListUnconference.get(position).getIdentifier();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.favorite_row, null);
				holder = new ViewHolder();
				holder.nameUnconference = (TextView) convertView
						.findViewById(R.id.lab_name_unconference);
				holder.place = (TextView) convertView
						.findViewById(R.id.lab_place);
				holder.hour = (TextView) convertView
						.findViewById(R.id.lab_hour);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			holder.nameUnconference.setText(mListUnconference.get(position)
					.getName());
			holder.place
					.setText(mListUnconference.get(position).getNamePlace());
			
			holder.hour
			.setText(mListUnconference.get(position).getSchedule());
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), UnconferenceDetailActivity.class);
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
			intent.putExtra("esFavorito", true);
			startActivity(intent);
		}
	}

	// consultar los favoritos
	private class ConsultarFavoritosTask extends
			AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			BDAdapter dbAdapter = new BDAdapter(getActivity());
			dbAdapter.openDataBase();
			Cursor cursor = dbAdapter.consultar(
					AppsConstants.Database.NAME_TABLE_FAVORITE, null, null,
					new String[] {}, null);
			ArrayList<String> favoritos = new ArrayList<String>();
			if (cursor != null && cursor.getCount() > 0) {
				// verifica si la lista de favoritos en la db ha cambiado con
				// respecto a
				// la lista de favoritos de la vista
				if (cursor.getCount() != mListUnconference.size()) {

					if (cursor.moveToFirst()) {
						do {
							favoritos.add(cursor.getString(1));
						} while (cursor.moveToNext());
					}

				}
			}
			dbAdapter.close();
			return favoritos;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);
			if (!result.isEmpty()) {
				new ConsultarUnconferencesFavoritasTask().execute(result);
			}
		}

	}

	// consultar unconferences favoritas
	private class ConsultarUnconferencesFavoritasTask extends
			AsyncTask<ArrayList<String>, Void, Void> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Cargando datos...");
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			BDAdapter dbAdapter = new BDAdapter(getActivity());
			dbAdapter.openDataBase();
			ArrayList<String> favoritos = params[0];
			if (!favoritos.isEmpty()) {
				// Consultar todos los places
				Cursor places = dbAdapter.consultar(
						AppsConstants.Database.NAME_TABLE_PLACE, null, null,
						new String[] {}, null);
				HashMap<Long, String> placesMap = new HashMap<Long, String>();
				if (places != null) {
					if (places.moveToFirst()) {
						do {
							placesMap.put(places.getLong(0),
									places.getString(1));
						} while (places.moveToNext());
					}
					places.close();
				}
				// limpia la lista actual de unconferences favoritas
				mListUnconference = new ArrayList<Unconference>();
				for (int i = 0; i < favoritos.size(); i++) {
					Cursor unconference = dbAdapter.consultar(
							AppsConstants.Database.NAME_TABLE_UNCONFERENCE,
							null, "_id = ?", new String[] { favoritos.get(i) },
							null);
					if (unconference != null) {
						if (unconference.moveToFirst()) {
							Unconference u = new Unconference();
							u.setIdentifier(unconference.getLong(0));
							u.setName(unconference.getString(1));
							u.setDescription(unconference.getString(2));
							u.setPlace(unconference.getLong(3));
							u.setKeywords(unconference.getString(4));
							u.setSpeakers(unconference.getString(5));
							u.setStartTime(unconference.getString(6));
							u.setEndTime(unconference.getString(7));
							u.setScheduleId(unconference.getLong(8));
							u.setSchedule(unconference.getString(9));
							// busca en el mapa el nombre del place
							u.setNamePlace(placesMap.get(u.getPlace()));
							mListUnconference.add(u);
						}
						unconference.close();
					}
				}
			}
			dbAdapter.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			mListAdapter.notifyDataSetChanged();
		}
	}
}
