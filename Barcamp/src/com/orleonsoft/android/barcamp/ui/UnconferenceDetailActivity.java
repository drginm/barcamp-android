package com.orleonsoft.android.barcamp.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orleonsoft.android.barcamp.db.BDAdapter;
import com.orleonsoft.android.barcamp.util.AppConstants;
import com.orleonsoft.android.barcamp.util.Utils;
import com.orleonsoft.android.barcamp.ws.Unconference;
import com.orleonsoft.android.barcampmed.R;

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

	ImageView butActionShare;
	ImageView butActionAbout;
	ImageView imgBack;
	RelativeLayout butHome;

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

		butActionAbout = (ImageView) findViewById(R.id.but_action_about);
		butActionShare = (ImageView) findViewById(R.id.but_action_share);
		butHome = (RelativeLayout) findViewById(R.id.back);
		imgBack = (ImageView) findViewById(R.id.ic_previous);
		imgBack.setVisibility(View.VISIBLE);

		butActionAbout.setOnClickListener(this);
		butHome.setOnClickListener(this);
		butActionShare.setOnClickListener(this);

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
					AppConstants.Database.NAME_TABLE_PLACE,
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
		mLabSpeakers.setText("Ponente : "+mUnconference.getSpeakers());
		mLabKeyWords.setText("keyWords: "+mUnconference.getKeywords());
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

		switch (v.getId()) {
		case R.id.but_action_about:
			startActivity(new Intent(UnconferenceDetailActivity.this,
					AcercaDeActivity.class));
			break;

		case R.id.but_action_share:
			Utils.share(UnconferenceDetailActivity.this,
					AppConstants.SHARE_SUBJECT, AppConstants.SHARE_MSJ + " "
							+ AppConstants.LINK_PLAY_STORE);
			break;

		case R.id.back:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.img_favorite:
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
			break;
		default:
			break;
		}
	}

	public long insertarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(UnconferenceDetailActivity.this);
		dbAdapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("id_unconference", idUnconference);
		long result = dbAdapter.insert(
				AppConstants.Database.NAME_TABLE_FAVORITE, values);
		dbAdapter.close();
		return result;
	}

	public boolean borrarFavorito(long idUnconference) {
		BDAdapter dbAdapter = new BDAdapter(UnconferenceDetailActivity.this);
		dbAdapter.openDataBase();
		long result = dbAdapter.delete(
				AppConstants.Database.NAME_TABLE_FAVORITE,
				"id_unconference = ?", new String[] { "" + idUnconference });
		dbAdapter.close();
		return result != 0;
	}
}
