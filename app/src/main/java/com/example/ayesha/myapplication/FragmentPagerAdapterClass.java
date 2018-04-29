package com.example.ayesha.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by on 4/15/2018.
 */
public class FragmentPagerAdapterClass extends FragmentStatePagerAdapter 
{
    final int PAGE_COUNT = 2;
    Context context;
    Activity activity;
    private String tabTitles[] = new String[]{"Log In","Sign Up"};
    public FragmentPagerAdapterClass(FragmentManager fm, Context context, Activity activity) 
	{
        super(fm);
        this.context = context;
        this.activity = activity;
    }
    @Override
    public int getCount() 
	{
        return PAGE_COUNT;
    }
    @Override
    public Fragment getItem(int position) 
	{
        if(position == 0)
            return new FragmentLogin();
        else if(position == 1)
            return new FragmentSignup();
        else
            return null;

    }
    @Override
    public CharSequence getPageTitle(int position) 
	{
        // Generate title based on item position
        return tabTitles[position];
    }
}