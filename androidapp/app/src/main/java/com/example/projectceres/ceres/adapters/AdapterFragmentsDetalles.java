package com.example.projectceres.ceres.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 4/10/15.
 */
public class AdapterFragmentsDetalles extends FragmentPagerAdapter {

    //region Variables
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    //endregion

    //region Funciones
    //region Constructores
    public AdapterFragmentsDetalles(FragmentManager manager) {
        super(manager);
    }
    //endregion

    //region Funciones del adapter
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    //endregion

    //region Funciones auxiliares
    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    //endregion
    //endregion
}