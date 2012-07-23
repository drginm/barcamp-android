package com.orleonsoft.android.barcamp;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.Place;

public class ListSalasFragment extends ListFragment {

	private LayoutInflater mInflater;
	private SalasEfficientAdapter mListAdapter;
	private ArrayList<Place> mListSalas;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.salas_panel, container, false);

		SharedPreferences settings = getActivity().getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		if (settings.getBoolean("hayDatosDescargados", false)) {
			new ConsultarSalasTask().execute();
		}

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	// carga los datos de las salas desde la db
	public void cargarDatosSalas() {
		new ConsultarSalasTask().execute();
	}

	// view holder
	static class ViewHolder {
		TextView nameSale;
		TextView nextUnCoference;
	}

	// adapter list
	private class SalasEfficientAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListSalas.size();
		}

		@Override
		public Object getItem(int position) {
			return mListSalas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mListSalas.get(position).getIdentifier();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.programacion_row, null);

				holder = new ViewHolder();
				holder.nameSale = (TextView) convertView
						.findViewById(R.id.lab_sala);
				holder.nextUnCoference = (TextView) convertView
						.findViewById(R.id.lab_proxima);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();

			holder.nameSale.setText(mListSalas.get(position).getName());
			holder.nextUnCoference.setText(mListSalas.get(position)
					.getNextUnconference());

			return convertView;
		}
	}

	private class ConsultarSalasTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("Cargando datos...");
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			BDAdapter dbAdapter = new BDAdapter(getActivity());
			dbAdapter.openDataBase();
			Cursor cursor = dbAdapter.consultar(
					AppsConstants.Database.NAME_TABLE_PLACE, null, null,
					new String[] {}, null);
			mListSalas = new ArrayList<Place>();

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Place p = new Place();
						p.setIdentifier(cursor.getLong(0));
						p.setName(cursor.getString(1));
						p.setDescription(cursor.getString(2));
						p.setImage(cursor.getString(3));
						mListSalas.add(p);
					} while (cursor.moveToNext());
				}
			}
			dbAdapter.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			mListAdapter = new SalasEfficientAdapter();
			setListAdapter(mListAdapter);
		}

	}
}
