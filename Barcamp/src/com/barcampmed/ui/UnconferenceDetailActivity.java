package com.barcampmed.ui;

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

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.barcampmed.R;
import com.barcampmed.db.BDAdapter;
import com.barcampmed.util.AppConstants;
import com.barcampmed.util.Utils;
import com.barcampmed.ws.Unconference;

public class UnconferenceDetailActivity extends SherlockActivity {

	private Unconference mUnconference;
	private TextView mLabNameUnconference;
	private TextView mLabSchedule;
	private TextView mLabDescription;
	private TextView mLabSpeakers;
	private TextView mLabKeyWords;
	private boolean esFavorito;
	private MenuItem favoriteMenuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_unconference_screen);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mLabNameUnconference = (TextView) findViewById(R.id.lab_name_unconference);
		mLabSchedule = (TextView) findViewById(R.id.lab_schedule);
		mLabDescription = (TextView) findViewById(R.id.lab_description);
		mLabSpeakers = (TextView) findViewById(R.id.lab_speakers);
		mLabKeyWords = (TextView) findViewById(R.id.lab_keywords);

		mUnconference = new Unconference();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// registra el evento
			esFavorito = extras.getBoolean("esFavorito");
			mUnconference.setIdentifier(extras.getLong("_id"));
			mUnconference.setName(extras.getString("Name"));
			getSupportActionBar().setTitle(extras.getString("Name"));
			mUnconference.setDescription(extras.getString("Description"));
			mUnconference.setPlace(extras.getLong("Place"));
			mUnconference.setNamePlace(extras.getString("PlaceName"));
			getSupportActionBar().setSubtitle(extras.getString("PlaceName"));
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
		mLabKeyWords.setText("Palabras clave: "+mUnconference.getKeywords());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Share")
		.setIcon(R.drawable.ic_action_share)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		favoriteMenuItem = menu.add("Favorite");
		favoriteMenuItem.setIcon(esFavorito ? R.drawable.ic_favorite : R.drawable.ic_unfavorite)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		if("Share".equals(item.getTitle())){
			Utils.share(UnconferenceDetailActivity.this,
					AppConstants.SHARE_SUBJECT, AppConstants.SHARE_MSJ + " "
							+ AppConstants.LINK_PLAY_STORE);
			return true;
		}
		else if("Favorite".equals(item.getTitle())){
			if (esFavorito) {
				UnconferenceDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (borrarFavorito(mUnconference.getIdentifier())) {
							esFavorito = false;
							favoriteMenuItem.setIcon(R.drawable.ic_unfavorite);
						}
					}
				});
			} else {
				UnconferenceDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if ((insertarFavorito(mUnconference.getIdentifier()) != -1)) {
							esFavorito = true;
							favoriteMenuItem.setIcon(R.drawable.ic_favorite);
						}
					}
				});
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
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
