
package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter{

	List<Fragment> fragments ;
	public PagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}
	public void addFragment(SherlockFragment fragment){
		fragments.add(fragment);
	}
	@Override
	public SherlockFragment getItem(int position) {
		return (SherlockFragment) fragments.get(position);
	}
		
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return "Favoritas";	
		case 1:
			return "Programacion";	
		case 2:
			return "Twitter";
		case 3:
			return "Fotos";
		default:
			return "";
		}
	}
	
	
	@Override
	public int getCount() {
		return fragments.size();
	}
	
	
	
}
