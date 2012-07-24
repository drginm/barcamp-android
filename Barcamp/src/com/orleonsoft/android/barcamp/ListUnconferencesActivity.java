package com.orleonsoft.android.barcamp;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.unconference_row, null);
				holder = new ViewHolder();
				holder.nameUnconference = (TextView) convertView
						.findViewById(R.id.lab_name_unconference);
				holder.speakers = (TextView) convertView
						.findViewById(R.id.lab_speakers);
				holder.image = (ImageView) findViewById(R.id.img_favorite);
				holder.image.setTag(false);
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
										}
									});
						}
					}
				});
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
}
