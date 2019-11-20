package com.example.pollutionctrl.myfragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Air","Water","Plastic","Other"};

    public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new AirPollution();
        }else if(position == 1){
            return new WaterPollution();
        }else if(position == 2){
            return new PlasticPollution();
        }else {
            return new OtherPollution();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
