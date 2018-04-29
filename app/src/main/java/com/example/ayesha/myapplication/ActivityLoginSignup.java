package com.example.ayesha.myapplication;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by  on 4/15/2018.
 */
public class ActivityLoginSignup extends AppCompatActivity {

    private static final int numberOfTabs = 3;
    ViewPager viewPager;
    FragmentPagerAdapterClass viewPagerAdapter;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ActivityLoginSignup.this,R.color.grayShade2));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
       setContentView(R.layout.activity_loginsignup);
        SetStatusBar();
        SetViewPager();
    }
    private void SetViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(numberOfTabs); // Keep the data of the fragments loaded
        viewPagerAdapter = new FragmentPagerAdapterClass(getSupportFragmentManager(),getApplicationContext(), ActivityLoginSignup.this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setIndicatorColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setIndicatorHeight(HelperFunctions.DpToPx(2,getApplicationContext()));
        tabsStrip.setBackgroundColor(Color.parseColor("#4c4d4d"));
        tabsStrip.setTextColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setDividerColor(Color.TRANSPARENT);
    }

}
