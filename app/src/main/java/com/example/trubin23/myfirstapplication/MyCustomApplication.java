package com.example.trubin23.myfirstapplication;

import android.app.Application;

import com.example.trubin23.database.NoteDaoImpl;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

	private NoteDaoImpl mNoteDaoImpl;

	@Override
	public void onCreate() {
		super.onCreate();

		mNoteDaoImpl = new NoteDaoImpl(this);
	}

	NoteDaoImpl getDBNotes(){
		return mNoteDaoImpl;
	}
}
