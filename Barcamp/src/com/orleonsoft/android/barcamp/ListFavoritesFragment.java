package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
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

public class ListFavoritesFragment extends Fragment {

	private LayoutInflater mInflater;
	private FavoritesEfficientAdapter mListAdapter;
	private ArrayList<Unconference> mListUnconference;
	private ListView mListViewSalas;

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
		mListViewSalas = (ListView) view.findViewById(android.R.id.list);
		mListViewSalas.setAdapter(mListAdapter);
		new ConsultarFavoritosTask().execute();
		return view;
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
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {

		}
	}

	// consultar los favoritos
	private class ConsultarFavoritosTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
			BDAdapter dbAdapter = new BDAdapter(getActivity());
			dbAdapter.openDataBase();
			Cursor favoritos = dbAdapter.consultar(
					AppsConstants.Database.NAME_TABLE_FAVORITE, null, null,
					new String[] {}, null);
			dbAdapter.close();
			return favoritos;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			if (result != null && result.getCount() > 0) {
				// verifica si la lista de favoritos ha cambiado con respecto a
				// la lista de favoritos actual
				if (result.getCount() != mListUnconference.size()) {
					new ConsultarUnconferencesFavoritasTask().execute(result);
				}
			}
		}

	}

	// consultar unconferences favoritas
	private class ConsultarUnconferencesFavoritasTask extends
			AsyncTask<Cursor, Void, Void> {

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
		protected Void doInBackground(Cursor... params) {
			BDAdapter dbAdapter = new BDAdapter(getActivity());
			dbAdapter.openDataBase();
			Cursor favoritos = params[0];
			if (favoritos != null) {
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
				if (favoritos.moveToFirst()) {
					do {
						String id_unconference = favoritos.getString(1);
						Cursor unconference = dbAdapter.consultar(
								AppsConstants.Database.NAME_TABLE_UNCONFERENCE,
								null, "_id = ?",
								new String[] { id_unconference }, null);
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
					} while (favoritos.moveToNext());
				}
				favoritos.close();
				dbAdapter.close();
			}

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
