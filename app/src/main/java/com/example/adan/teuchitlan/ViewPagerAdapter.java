package com.example.adan.teuchitlan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 5/16/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String>   listaTitulos  = new ArrayList<>();
    private final List<Fragment> listaFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listaFragments.get(position);
    }

    @Override
    public int getCount() {
        return listaTitulos.size();
    }

    public CharSequence getPageTitle(int position){
        return listaTitulos.get(position);
    }

    public void AddFragment(Fragment fragment, String titulo){
        listaFragments.add(fragment);
        listaTitulos.add(titulo);
    }

}
