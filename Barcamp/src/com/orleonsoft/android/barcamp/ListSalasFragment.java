package com.orleonsoft.android.barcamp;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.orleonsoft.android.barcamp.network.JSONParser;

public class ListSalasFragment extends ListFragment {

	private JSONArray mDataSalas;
	private LayoutInflater mInflater;
	private SalasEfficientAdapter mListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Error cargando Tuits, no hay conexion a internet",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (mDataSalas == null) {
			try {
				mDataSalas = JSONParser
						.getJSONArrayFromURL(AppsConstants.URL_GET_SALAS);

			} catch (IOException e) {
				mDataSalas = new JSONArray();
				e.printStackTrace();
			}
		}

		mListAdapter = new SalasEfficientAdapter();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.salas_panel, container, false);
		setListAdapter(mListAdapter);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	// view holder
	static class ViewHolder {
		TextView nameSale;
		TextView nameCoferencia;
	}

	// adapter list
	private class SalasEfficientAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataSalas.length();
		}

		@Override
		public Object getItem(int position) {
			try {
				return mDataSalas.getJSONObject(position);
			} catch (JSONException e) {
				e.printStackTrace();
				return new JSONObject();
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
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
				holder.nameCoferencia = (TextView) convertView
						.findViewById(R.id.lab_proxima);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();

			try {
				JSONObject sala = mDataSalas.getJSONObject(position);
				holder.nameSale.setText(sala.getString("Name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

}
