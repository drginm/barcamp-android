
package com.barcampmed;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter{

	List<Fragment> fragments ;
	public PagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}
	public void addFragment(Fragment fragment){
		fragments.add(fragment);
	}
	@Override
	public Fragment getItem(int position) {
		return (Fragment) fragments.get(position);
	}
		
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return "Programación";
		case 1:
			return "Favoritas";
		case 2:
			return "Acerca de";
		default:
			return "";
		}
	}
	
	
	@Override
	public int getCount() {
		return fragments.size();
	}
	
	
	
}
