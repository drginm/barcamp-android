
package com.orleonsoft.android.barcamp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orleonsoft.android.barcampmed.R;

public class PlainFragment extends Fragment {

	
	View view = null;
	
	public PlainFragment(){
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.acercade_screen, null); 
		view.setBackgroundColor(Color.BLACK);
		return view; 
	}

		
}
