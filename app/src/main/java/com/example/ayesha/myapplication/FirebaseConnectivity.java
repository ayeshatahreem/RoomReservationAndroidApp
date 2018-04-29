package com.example.ayesha.myapplication;

import android.support.multidex.MultiDexApplication;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseConnectivity extends MultiDexApplication 
{
    @Override
    public void onCreate() 
	{
        super.onCreate();
        Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

}