package com.orleonsoft.android.barcamp.network;

/**
 *Archivo: AdapterListCategorias.java
 *@autor:Yesid Lazaro
 *Fecha:2/03/2012
 */

import java.io.InputStream;
import java.util.ArrayList;

import com.orleonsoft.android.barcamp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Esta Clase define el adaptador de la lista de categorias
 * 
 */

public class AdapterListPhotos extends ArrayAdapter<PhotoBarcamp> {

	Context context;
	ArrayList<PhotoBarcamp> photos;

	public AdapterListPhotos(Context context, int resource,
			ArrayList<PhotoBarcamp> elements) {

		super(context, resource, elements);
		this.context = context;
		this.photos = elements;
		resource = R.layout.tweet_row;

	}

	@Override
	public int getCount() {

		return photos.size();
	}

	@Override
	public PhotoBarcamp getItem(int position) {
		return photos.get(position);
	}

	@Override
	public int getPosition(PhotoBarcamp item) {
		return photos.indexOf(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.photo_row, null);

		TextView labTitle = (TextView) view
				.findViewById(R.id.lab_title);
		
		labTitle.setText(photos.get(position).getPhotoTitle());
		
		ImageView imgPhoto=(ImageView)view.findViewById(R.id.img_photo);
		
		
		
		

		return view;
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mBitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mBitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mBitmap;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}


}
