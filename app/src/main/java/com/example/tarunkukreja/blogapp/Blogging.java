package com.example.tarunkukreja.blogapp;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tarunkukreja on 13/04/17.
 */

public class Blogging extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
