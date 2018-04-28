package co.romero.mandegar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;


public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }






    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }



    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }



}