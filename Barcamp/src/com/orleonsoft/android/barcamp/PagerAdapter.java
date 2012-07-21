
package com.orleonsoft.android.barcamp;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments ;
	public PagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
	}
	public void addFragment(Fragment fragment){
		fragments.add(fragment);
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return "Favoritas";	
		case 1:
			return "Home";	
		case 2:
			return "Twitter";
		case 3:
			return "Photos";
		default:
			return "";
		}
	}
	
	
	
	
	@Override
	public int getCount() {
		return fragments.size();
	}
	
	
	
}
