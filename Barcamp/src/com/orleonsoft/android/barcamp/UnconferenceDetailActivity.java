package com.orleonsoft.android.barcamp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orleonsoft.android.barcamp.database.BDAdapter;
import com.orleonsoft.android.barcamp.network.Unconference;
import com.orleonsoft.android.barcampmed.R;

public class UnconferenceDetailActivity extends Activity  implements OnClickListener{

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
		
		butActionAbout=(ImageView)findViewById(R.id.but_action_about);
		butActionShare=(ImageView)findViewById(R.id.but_action_share);
		butHome=(RelativeLayout)findViewById(R.id.back);
		imgBack=(ImageView)findViewById(R.id.ic_previous);
		imgBack.setVisibility(View.VISIBLE);
		
		butActionAbout.setOnClickListener(this);
		butHome.setOnClickListener(this);
		butActionShare.setOnClickListener(this);

		mUnconference = new Unconference();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
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
		switch (v.getId()) {
		case R.id.but_action_about:
			startActivity(new Intent(UnconferenceDetailActivity.this, AcercaDeActivity.class));
			break;
			
		case R.id.but_action_share:
			Utils.share(UnconferenceDetailActivity.this, AppsConstants.SHARE_SUBJECT, AppsConstants.SHARE_MSJ+" "+AppsConstants.LINK_PLAY_STORE);
			break;
			
		case R.id.back:
			Intent intent = new Intent(this,HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		       startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}
