package com.orleonsoft.android.barcamp;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.Unconference;

public class UnconferenceDetailActivity extends Activity implements
		OnClickListener {

	private Unconference mUnconference;
	private TextView mLabNameUnconference;
	private TextView mLabSchedule;
	private TextView mLabDescription;
	private TextView mLabSpeakers;
	private TextView mLabKeyWords;
	private ImageView mImgFavorite;
	private boolean esFavorito;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_unconference_screen);

		mLabNameUnconference = (TextView) findViewById(R.id.lab_name_unconference);
		mLabSchedule = (TextView) findViewById(R.id.lab_schedule);
		mLabDescription = (TextView) findViewById(R.id.lab_description);
		mLabSpeakers = (TextView) findViewById(R.id.lab_speakers);
		mLabKeyWords = (TextView) findViewById(R.id.lab_keywords);
		mImgFavorite = (ImageView) findViewById(R.id.img_favorite);

		mUnconference = new Unconference();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// registra el evento
			mImgFavorite.setOnClickListener(this);
			esFavorito = extras.getBoolean("esFavorito");
			mUnconference.setIdentifier(extras.getLong("_id"));
			mUnconference.setName(extras.getString("Name"));
			mUnconference.setDescription(extras.getString("Description"));
			mUnconference.setPlace(extras.getLong("Place"));
			mUnconference.setKeywords(extras.getString("Keywords"));
			mUnconference.setSpeakers(extras.getString("Speakers"));
			mUnconference.setStartTime(extras.getString("StartTime"));
			mUnconference.setEndTime(extras.getString("EndTime"));
			mUnconference.setScheduleId(extras.getLong("ScheduleId"));
			mUnconference.setSchedule(extras.getString("Schedule"));
			// consulta el nombre de la sala
			BDAdapter dbAdapter = new BDAdapter(this);
			dbAdapter.openDataBase();
			Cursor cursor = dbAdapter.consultar(
					AppsConstants.Database.NAME_TABLE_PLACE,
					new String[] { "Name" }, "_id = ?",
					new String[] { mUnconference.getPlace() + "" }, null);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					mUnconference.setNamePlace(cursor.getString(0));
				}
			}

			dbAdapter.close();

		}

		// set data
		mLabNameUnconference.setText(mUnconference.getName());
		mLabSchedule.setText(mUnconference.getSchedule());
		mLabDescription.setText(mUnconference.getDescription());
		mLabSpeakers.setText(mUnconference.getSpeakers());
		mLabKeyWords.setText(mUnconference.getKeywords());
		if (esFavorito) {
			mImgFavorite.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_favorite));
		} else {
			mImgFavorite.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_unfavorite));
		}
	}

	@Override
	public void onClick(View v) {

		if (esFavorito) {
			UnconferenceDetailActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (borrarFavorito(mUnconference.getIdentifier())) {
						esFavorito = false;
						mImgFavorite.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.ic_unfavorite));
					}
				}
			});
		} else {
			UnconferenceDetailActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if ((insertarFavorito(mUnconference.getIdentifier()) != -1)) {
						esFavorito = true;
						mImgFavorite.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.ic_favorite));
					}
				}
			});
		}
	}

	public long insertarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(UnconferenceDetailActivity.this);
		dbAdapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("id_unconference", idUnconference);
		long result = dbAdapter.insert(
				AppsConstants.Database.NAME_TABLE_FAVORITE, values);
		dbAdapter.close();
		return result;
	}

	public boolean borrarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(UnconferenceDetailActivity.this);
		dbAdapter.openDataBase();
		long result = dbAdapter.delete(
				AppsConstants.Database.NAME_TABLE_FAVORITE,
				"id_unconference = ?", new String[] { "" + idUnconference });
		dbAdapter.close();
		return result != 0;
	}
}
