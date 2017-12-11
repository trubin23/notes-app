package com.example.trubin23.myfirstapplication;

import android.app.Application;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

	private DatabaseConnector mDatabaseConnector;

	@Override
	public void onCreate() {
		super.onCreate();

		mDatabaseConnector = new DatabaseConnector(this);
	}

	DatabaseConnector getDBNotes(){
		return mDatabaseConnector;
	}
}
